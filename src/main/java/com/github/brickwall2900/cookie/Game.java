package com.github.brickwall2900.cookie;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatDesktop;
import com.github.brickwall2900.cookie.items.*;
import com.github.brickwall2900.cookie.items.Cursor;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class Game implements Consumer<Graphics2D>, ActionListener, WindowListener, MouseListener, MouseMotionListener, Runnable {
    public static final Game GAME = new Game();

    private final GameWindow window;
    private final GameCanvas canvas;

    private final Random random;
    private Thread thread;

    private final List<Float> deltaRenderAverage = new ArrayList<>();
    private final List<Float> deltaItemAverage = new ArrayList<>();
    private int soundSystemExecutedCommands;
    private long clickPerSecond = 0;

    private float cpsLastTime = 0;
    private long lastClicks = 0;

    /* settings */
    private SaveFile.SavedSettings savedSettings;
    private float renderTime = 60f;
    private int renderQuality = 5;
    private boolean autoSaveNotifications = false;
    private float autoSaveTimer = 60;
    private boolean enableFpsCounter = false;
    private boolean darkMode = true;

    private final SquareWaveGenerator squareWaveGenerator;
    private BufferedImage cookie;
    private BufferedImage background, backgroundLight;
    private Properties text;
    private Font font, smallFont;
    private SoundSystem soundSystem;

    private final ShopWindow shopWindow;
    private final ShopController shopController;
    private final FileSelectWindow fileSelectWindow;
    private final FileSelectController fileSelectController;
    private final SettingsWindow settingsWindow;
    private final SettingsController settingsController;

    private final SaveFile saveFile;
    private long timePlayed;
    private String saveName;

    private HashMap<ItemDescription, Item> itemMap;

    private final Object itemUpdateLock = new Object();

    public Game() {
        random = new Random();
        window = new GameWindow();
        canvas = new GameCanvas();
        window.canvasPanel.add(canvas, BorderLayout.CENTER);

        window.addWindowListener(this);
        window.shopButton.addActionListener(this);
        window.saveButton.addActionListener(this);
        window.settingsButton.addActionListener(this);
        window.closeButton.addActionListener(this);

        canvas.addMouseListener(this);

        shopWindow = new ShopWindow();
        shopController = new ShopController(shopWindow);

        saveFile = new SaveFile();

        fileSelectWindow = new FileSelectWindow();
        fileSelectController = new FileSelectController(fileSelectWindow, saveFile);

        settingsWindow = new SettingsWindow();
        settingsController = new SettingsController(settingsWindow);

        squareWaveGenerator = new SquareWaveGenerator();

        canvas.renderer = this;
        try {
            resourceLoad();
        } catch (IOException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Error on resource load! " + e, "Error!", JOptionPane.ERROR_MESSAGE, null);
            throw new RuntimeException("Error on resource load!", e);
        }
    }

    public void start() {
        gameLoad();
        fileSelectWindow.setLocationRelativeTo(null);
        fileSelectWindow.setVisible(true);
    }

    public void startGame() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        startGameThread();
        soundSystem.loopBackgroundMusic(soundSystem.getRandomAudioName("game.cookie.tracks"), 0);
    }

    public void startGameThread() {
        thread = new Thread(this);
        itemUpdateThread = new ItemUpdateThread();
        thread.setName("Game Thread");
        itemUpdateThread.setName("Item Update Thread");
        thread.start();
        itemUpdateThread.start();
    }

    private ItemUpdateThread itemUpdateThread;

    private long gameUpdateLastTime = System.nanoTime();
    private float audioUpdateStart = 10f;
    @Override
    public void run() {
        try {
            saveGame(true);
            itemUpdateLastTime = System.nanoTime();
            while (window.isShowing()) {
                synchronized (itemUpdateLock) {
                    itemUpdateLock.notifyAll();
                }
                SwingUtilities.invokeAndWait(this::tick);
                gameUpdate();
                betterSleep(1f / renderTime);
            }
            saveGame(false);
        } catch (Exception ex) {
            System.err.println("Error in game thread!");
            ex.printStackTrace();
        }
    }

    private void gameUpdate() {
        float delta = (System.nanoTime() - gameUpdateLastTime) / NANOSECONDS;
        gameUpdateLastTime = System.nanoTime();

        autoSaveNotificationTimer = Math.max(0, autoSaveNotificationTimer - delta);
        nextAutoSave += delta;
        if (nextAutoSave >= autoSaveTimer) {
            autoSaveNotificationTimer = 3f;
            nextAutoSave = 0;
            saveGame(false);
        }

        cpsLastTime += delta;
        if (cpsLastTime >= 1f) {
            clickPerSecond = clicks.get() - lastClicks;
            lastClicks = clicks.get();
            cpsLastTime = 0;
        }

        if (audioUpdateStart > 0) {
            audioUpdateStart -= delta;
        } else {
            if (soundSystem.isBackgroundMusicEnded()) {
                soundSystem.loopBackgroundMusic(soundSystem.getRandomAudioName("game.cookie.tracks"), 0);
            }
        }
    }

    private class ItemUpdateThread extends Thread {
        @Override
        public void run() {
            while (thread.isAlive()) {
                synchronized (itemUpdateLock) {
                    try {
                        itemUpdateLock.wait(100);
                    } catch (InterruptedException e) { }
                }
                itemUpdate();
            }
        }
    }

    private void resourceLoad() throws IOException {
        ClassResource cookieResource = new ClassResource("cookie.png");
        ClassResource backgroundResource = new ClassResource("background.png");
        ClassResource backgroundLightResource = new ClassResource("background-light.png");
        ClassResource textResource = new ClassResource("text.properties");
        ClassResource audioMapResource = new ClassResource("audio.properties");

        cookie = ImageIO.read(cookieResource.getStream());
        background = ImageIO.read(backgroundResource.getStream());
        backgroundLight = ImageIO.read(backgroundLightResource.getStream());
        text = new Properties();
        text.load(textResource.getStream());

        font = new Font(text.getProperty("game.font"), Font.PLAIN, 24);
        smallFont = new Font(text.getProperty("game.font"), Font.PLAIN, 16);

        soundSystem = new SoundSystem();
        soundSystem.loadAudioMap(audioMapResource);

        cookieResource.close();
        backgroundResource.close();
        backgroundLightResource.close();
        textResource.close();
        audioMapResource.close();
    }

    private void gameLoad() {
        itemMap = new HashMap<>();
        int nextId = 0;
        itemMap.put(new ItemDescription(++nextId, 1, 1, 1, "game.item.oneclick", OneClick.class, false), new OneClick());
        itemMap.put(new ItemDescription(++nextId, 100, 25, Integer.MAX_VALUE, "game.item.cursor", Cursor.class), new Cursor());
        itemMap.put(new ItemDescription(++nextId, 200, 50, Integer.MAX_VALUE, "game.item.worker", Worker.class), new Worker());
        itemMap.put(new ItemDescription(++nextId, 400, 100, Integer.MAX_VALUE, "game.item.baker", Baker.class), new Baker());
        itemMap.put(new ItemDescription(++nextId, 1000, 300, Integer.MAX_VALUE, "game.item.milk", Milk.class), new Milk());
        itemMap.put(new ItemDescription(++nextId, 2000, 700, Integer.MAX_VALUE, "game.item.tractor", Tractor.class), new Tractor());
        itemMap.put(new ItemDescription(++nextId, 5000, 1000, Integer.MAX_VALUE, "game.item.farm", Farm.class), new Farm());
        itemMap.put(new ItemDescription(++nextId, 8000, 2000, Integer.MAX_VALUE, "game.item.factory", Factory.class), new Factory());
        itemMap.put(new ItemDescription(++nextId, 25000, 12000, 1, "game.item.nuclear", NuclearBomb.class), new NuclearBomb());
        itemMap.put(new ItemDescription(++nextId, 50000, 20000, Integer.MAX_VALUE, "game.item.hole", BlackHole.class), new BlackHole());
        itemMap.put(new ItemDescription(++nextId, 100000, 40000, Integer.MAX_VALUE, "game.item.universe", Universe.class), new Universe());

        shopController.manageStock(itemMap.keySet());

        shopController.initText();
        fileSelectController.initText();
        settingsController.initText();

        window.setTitle(getText("game.title"));

        squareWaveGenerator.start(new AudioFormat(44100, 8, 1, true, false));
        squareWaveGenerator.play(60, 1);
        soundSystem.start();
    }

    public void stop() {
        window.dispose();
        audioUpdateStart = 10;
    }

    public String getText(String id, Object... objects) {
        return text.getProperty(id, id).formatted(objects);
    }

    public float clamp(float value, float min, float max) {
        return Math.max(Math.min(max, value), min);
    }

    public void sleep(float seconds) {
        try {
            seconds = clamp(seconds, 0.001f, Float.MAX_VALUE);
            float millis = (seconds * 1000);
            float nanos = (float) ((millis - Math.floor(millis)) * 1_000_000f);
            Thread.sleep((long) millis, (int) nanos);
        } catch (InterruptedException e) {
        }
    }

    private static final long SLEEP_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);
    private static final long SPIN_YIELD_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);

    public void betterSleep(float seconds) {
        try {
            seconds = clamp(seconds, 0.001f, Float.MAX_VALUE);
            long nanoDuration = (long) (seconds * NANOSECONDS);
            final long end = System.nanoTime() + nanoDuration;
            long timeLeft = nanoDuration;
            do {
                if (timeLeft > SLEEP_PRECISION) {
                    Thread.sleep(1);
                } else {
                    if (timeLeft > SPIN_YIELD_PRECISION) {
                        Thread.yield();
                    }
                }
                timeLeft = end - System.nanoTime();
            } while (timeLeft > 0);
        } catch (InterruptedException ex) {}
    }

    public ItemDescription getItemDescription(String name) {
        Iterator<ItemDescription> descriptionIterator = itemMap.keySet().iterator();
        while (descriptionIterator.hasNext()) {
            ItemDescription itemDescription = descriptionIterator.next();
            if (itemDescription.getName().equals(name)) { return itemDescription; }
        }
        return null;
    }

    public int getItemInstances(ItemDescription itemDescription) {
        return itemMap.get(itemDescription).getInstances();
    }

    public void loadSave(SaveFile.SaveFileRecord record) {
        timePlayed = record.timeWasted();
        clicks.set(record.clicks());
        saveName = record.name();

        SaveFile.SavedSettings settings = record.savedSettings();
        loadSettings(settings);

        loadItems(record);

        renderUpdateLastTime = System.nanoTime();
        itemUpdateLastTime = System.nanoTime();
    }

    public void loadSettings(SaveFile.SavedSettings settings) {
        this.savedSettings = settings;
        renderTime = settings.renderTime() > 0 ? settings.renderTime() : Integer.MAX_VALUE;
        renderQuality = settings.renderQuality();
        autoSaveNotifications = settings.enableAutoSaveNotifications();
        autoSaveTimer = settings.autoSaveTimer() > 1 ? settings.autoSaveTimer() : Integer.MAX_VALUE;
        enableFpsCounter = settings.enableDebugCounter();
        boolean changeLaf = darkMode != settings.darkMode();
        darkMode = settings.darkMode();
        if (changeLaf) {
            if (darkMode) changeLookFeel(FlatDarkLaf::setup);
            else changeLookFeel(FlatLightLaf::setup);
        }
    }

    private void loadItems(SaveFile.SaveFileRecord record) {
        for (Item item : record.items()) {
            for (ItemDescription description : itemMap.keySet()) {
                if (description.getType().isInstance(item)) {
                    itemMap.put(description, item);
                    description.setBuyCost(description.getBuyCost() + (item.getNumberSold() * 2L));
                }
            }
        }
    }

    public void createSave(String name) {
        saveName = name;
        savedSettings = new SaveFile.SavedSettings(
                renderTime,
                renderQuality,
                autoSaveNotifications,
                autoSaveTimer,
                enableFpsCounter,
                darkMode
        );
    }

    public void saveGame(boolean explicit) {
        savedSettings = new SaveFile.SavedSettings(
                renderTime,
                renderQuality,
                autoSaveNotifications,
                autoSaveTimer,
                enableFpsCounter,
                darkMode
        );
        SaveFile.SaveFileRecord record = new SaveFile.SaveFileRecord(saveName, clicks.get(), itemMap.values().stream().toList(), timePlayed, savedSettings);
//        if (saveFilePassword == null && explicit) {
//            JPanel panel = new JPanel();
//            JLabel label = new JLabel(getText("game.file.password.new"));
//            JPasswordField pass = new JPasswordField(10);
//            panel.add(label);
//            panel.add(pass);
//            String[] options = new String[] {getText("game.ok"), getText("game.cancel")};
//            int option = JOptionPane.showOptionDialog(window, panel, getText("game.file.password.title"),
//                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
//                    null, options, options[0]);
//            if (option == 0) {
//                saveFilePassword = pass.getPassword();
//            } else {
//                JOptionPane.showMessageDialog(window, getText("game.file.password.not-entered"), getText("game.file.password.title"), JOptionPane.ERROR_MESSAGE);
//            }
//        }
//        if (saveFilePassword != null) {
            saveFile.save(record);
//        }
    }

    private float cookieRotation = 0f;

    private final AtomicLong clicks = new AtomicLong(2_000_000_000_000_000L);

    private AffineTransform cookieTransform;

    /** Renderer function - render(Graphics2D g2d) */
    @Override
    public void accept(Graphics2D g2d) {
        initRender(g2d);
        renderUpdate();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // background
        Color currentColor = g2d.getColor();
        if (renderQuality >= 2) {
            if (darkMode) {
                g2d.drawImage(background, 0, 0, width, height, null);
            } else {
                g2d.drawImage(backgroundLight, 0, 0, width, height, null);
            }
            g2d.setColor(new Color(0, 0, 0, 128));
        } else {
            if (darkMode) {
                g2d.setColor(new Color(31, 0, 127));
            } else {
                g2d.setColor(new Color(200, 255, 255));
            }
        }
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(currentColor);

        // cookie
        float cookieMiddleX = (width / 2f);
        float cookieMiddleY = (height / 2f);

        int cookieWidth = 300;
        int cookieHeight = 300;
        int cookieX = (int) (cookieMiddleX - cookieWidth / 2f);
        int cookieY = (int) (cookieMiddleY - cookieHeight / 2f);

        AffineTransform originalTransform = g2d.getTransform();
        if (renderQuality >= 0) {
            if (cookieTransform == null) {
                cookieTransform = new AffineTransform();
            }
            cookieTransform.setToIdentity();
            cookieTransform.rotate(cookieRotation, cookieMiddleX, cookieMiddleY);

            g2d.setTransform(cookieTransform);
        }

        if (renderQuality >= 1) {
            g2d.drawImage(cookie, cookieX, cookieY, cookieWidth, cookieHeight, null);
        } else {
            Color prevColor = g2d.getColor();
            g2d.setColor(new Color(157, 122, 84));
            g2d.fillRect(cookieX, cookieY, cookieWidth, cookieHeight);
            g2d.setColor(prevColor);
        }
        g2d.setTransform(originalTransform);


        // text
        g2d.setFont(font);
        int textHeight = g2d.getFontMetrics().getHeight();
        g2d.setFont(smallFont);
        int smallTextHeight = g2d.getFontMetrics().getHeight();
        g2d.setFont(font);

        // click count
        String clickCountText = getText("game.clickCount", clicks);
        int clickCountTextWidth = g2d.getFontMetrics().stringWidth(clickCountText);
        int clickCountTextX = (int) (cookieMiddleX - clickCountTextWidth / 2f);
        g2d.drawString(clickCountText, clickCountTextX, 50);

        // buy error notification (now goes unused (probably))
        if (buyErrorNotification > 0f) {
            String lessClicksText = shopError.getText();
            int lessClicksTextWidth = g2d.getFontMetrics().stringWidth(lessClicksText);
            int lessClicksTextX = (int) (cookieMiddleX - lessClicksTextWidth / 2f);
            float percentFade = clamp(buyErrorNotification / 1.5f, 0, 1);
            int alpha = (int) (percentFade * 192);
            int heightAdd = (int) (percentFade * 20);
            if (Objects.requireNonNull(shopError) == ShopError.SUCCESS) {
                g2d.setColor(new Color(0, 255, 0, alpha));
            } else {
                g2d.setColor(new Color(255, 0, 0, alpha));
            }
            g2d.drawString(lessClicksText, lessClicksTextX, height - textHeight - 30 - heightAdd);
        }

        // click per seconds
        g2d.setColor(currentColor);
        String clickPerSecondText = getText("game.clickPerSecond", clickPerSecond);
        int clickPerSecondWidth = g2d.getFontMetrics().stringWidth(clickPerSecondText);
        int clickPerSecondX = (int) (cookieMiddleX - clickPerSecondWidth / 2f);
        g2d.drawString(clickPerSecondText, clickPerSecondX, cookieY + cookieHeight + textHeight);

        // auto save notification
        if (autoSaveNotificationTimer > 0f && autoSaveNotifications) {
            String autoSaveText = getText("game.file.save.autosave");
            int autoSaveTextWidth = g2d.getFontMetrics().stringWidth(autoSaveText);
            int autoSaveTextX = (int) (cookieMiddleX - autoSaveTextWidth / 2f);
            float percentFade = clamp(autoSaveNotificationTimer / 1.5f, 0, 1);
            int alpha = (int) (percentFade * 64);
            int heightAdd = (int) (percentFade * 30);
            g2d.setColor(new Color(0, 255, 0, alpha));
            g2d.drawString(autoSaveText, autoSaveTextX, height - textHeight - heightAdd + 50);
        }

        // debug
        if (enableFpsCounter) {
            int x = 20;
            g2d.setFont(smallFont);
            g2d.setColor(currentColor);
            float renderAverage = 0;
            Iterator<Float> renderIterator = deltaRenderAverage.iterator();
            while (renderIterator.hasNext()) renderAverage += renderIterator.next();
            renderAverage /= 30;

            float itemAverage = 0;
            Iterator<Float> itemIterator = deltaRenderAverage.iterator();
            while (itemIterator.hasNext()) itemAverage += itemIterator.next();
            itemAverage /= 30;

            Runtime runtime = Runtime.getRuntime();
            int mb = 1824 * 1024;
            double totalMemory = runtime.totalMemory();
            double freeMemory = runtime.freeMemory();
            double usedMemory = totalMemory - freeMemory;
            double maxMemory = runtime.maxMemory();

            totalMemory /= mb;
            freeMemory /= mb;
            usedMemory /= mb;
            maxMemory /= mb;

            Color box = new Color(0, 0, 0, 128);
            g2d.setColor(box);
            g2d.fillRect(0, 0, width, smallTextHeight * 8);
            g2d.setColor(currentColor);

            g2d.drawString("Render/s: " + 1f / renderAverage, x, smallTextHeight);
            g2d.drawString("Item/s: " + 1f / itemAverage, x, smallTextHeight * 2);
            g2d.drawString("SSExecutedCommands: " + soundSystemExecutedCommands, x, smallTextHeight * 3);
            g2d.drawString("FreeMemory: " + freeMemory, x, smallTextHeight * 4);
            g2d.drawString("UsedMemory / TotalMemory: " + usedMemory + '/' + totalMemory, x, smallTextHeight * 5);
            g2d.drawString("MaxMemory: " + maxMemory, x, smallTextHeight * 6);
            g2d.drawString("SSBackgroundAudio: " + soundSystem.getBackgroundMusicInfo(), x, smallTextHeight * 7);
            g2d.setFont(font);
        }
    }
    private void initRender(Graphics2D g2d) {
        if (renderQuality >= 7) {
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        }
        if (renderQuality >= 6) {
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        }
        if (renderQuality >= 5) {
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }
        if (renderQuality >= 4) {
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        }
    }

    public static final float NANOSECONDS = 1_000_000_000;

    private long itemUpdateLastTime = System.nanoTime(),
            renderUpdateLastTime = System.nanoTime();

    private long timePlayedDelta;

    private float nextAutoSave;
    private float autoSaveNotificationTimer;

    public void renderUpdate() {
        float delta = (System.nanoTime() - renderUpdateLastTime) / NANOSECONDS;
        renderUpdateLastTime = System.nanoTime();

        deltaRenderAverage.add(delta);
        if (deltaRenderAverage.size() >= 30) deltaRenderAverage.remove(0);

        soundSystemExecutedCommands = soundSystem.updateDebug();

        timePlayed = System.currentTimeMillis() - timePlayedDelta;
        timePlayedDelta = System.currentTimeMillis();

        cookieRotation += delta / 2f;
        buyErrorNotification -= delta;
    }

    public void itemUpdate() {
        float delta = (System.nanoTime() - itemUpdateLastTime) / NANOSECONDS;
        itemUpdateLastTime = System.nanoTime();

        deltaItemAverage.add(delta);
        if (deltaItemAverage.size() >= 30) deltaItemAverage.remove(0);

        if (itemMap == null) throw new IllegalStateException("Game Save isn't loaded!");

        for (Item item : itemMap.values()) {
//            if (item.getInstances() > 0) System.out.println("running " + item.getClass().getSimpleName() + " (" + item.getInstances() + ")");
            if (item.getInstances() > 0) {
                item.update(this, delta, item.getInstances());
            }
        }
    }
    private ShopError shopError;

    private float buyErrorNotification;


    private ItemDescription itemBought;

    /** Timer tick */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(window.shopButton)) { // shop button
            shopWindow.setLocationRelativeTo(window);
            shopWindow.setVisible(true);
        } else if (e.getSource().equals(window.saveButton)) {
            saveGame(true);
            soundSystem.playInstancedAudio("game.file.save");
        } else if (e.getSource().equals(window.settingsButton)) {
            settingsController.loadSettings(savedSettings);
            settingsWindow.setLocationRelativeTo(window);
            settingsWindow.setVisible(true);
        } else if (e.getSource().equals(window.closeButton)) {
            settingsWindow.dispose();
            shopWindow.dispose();
            window.dispose();
            stop();
            System.gc();
            fileSelectController.initText();
            fileSelectWindow.setLocationRelativeTo(null);
            fileSelectWindow.setVisible(true);
        }
    }

    public ShopError buyItem(ItemDescription itemSelected, int quantity) {
        itemBought = itemSelected;
        if (itemSelected != null) {
            long requiredClicks = itemSelected.getBuyCost() * quantity;
            if (clicks.get() >= requiredClicks) {
                while (quantity > 0) {
                    Item item = itemMap.get(itemSelected);
                    if (item.getInstances() < itemSelected.getMaxInstances()) {
                        item.addInstance();
                        item.onBought(this);
                        item.addNumberSold();
                        if (itemSelected.increasesDemand()) {
                            itemSelected.setBuyCost(itemSelected.getBuyCost() + 2);
                        }
                        quantity--;
                    } else {
                        return ShopError.TOO_MANY_INSTANCES;
                    }
                }
                clicks.addAndGet(-requiredClicks);
                return ShopError.SUCCESS;
            } else {
                return ShopError.NOT_ENOUGH_CLICKS;
            }
        } else {
            return ShopError.INVALID_ITEM;
        }
    }

    public ShopError sellItem(ItemDescription itemSelected, int quantity) {
        if (itemSelected != null) {
            long requiredClicks = itemSelected.getSellCost() * quantity;
            Item item = itemMap.get(itemSelected);
            if (item.getInstances() > 0) {
                while (item.getInstances() > 0 && quantity > 0) {
                    item.removeInstance();
                    quantity--;
                    if (itemSelected.increasesDemand()) {
                        itemSelected.setBuyCost(itemSelected.getBuyCost() - 2);
                    }
                }
                clicks.addAndGet(requiredClicks);
                return ShopError.SUCCESS;
            } else {
                return ShopError.NOT_ENOUGH_ITEMS;
            }
        } else {
            return ShopError.INVALID_ITEM;
        }
    }

    public boolean canBuyItem(ItemDescription itemSelected, int quantity) {
        boolean hasItemSelected = itemSelected != null;
        if (hasItemSelected) {
            Item item = itemMap.get(itemSelected);
            long requiredClicks = itemSelected.getBuyCost() * quantity;
            return clicks.get() >= requiredClicks && item.getInstances() < itemSelected.getMaxInstances();
        }
        return false;
    }

    public boolean canSellItem(ItemDescription itemSelected, int quantity) {
        boolean hasItemSelected = itemSelected != null;
        if (hasItemSelected) {
            Item item = itemMap.get(itemSelected);
            return item.getInstances() > 0;
        }
        return false;
    }

    public SoundSystem getSoundSystem() {
        return soundSystem;
    }

    public enum ShopError {
        SUCCESS("game.buyStatus.success"),
        NOT_ENOUGH_CLICKS("game.buyStatus.lessClicks"),
        INVALID_ITEM("game.buyStatus.invalidItem"),
        NOT_ENOUGH_ITEMS("game.buyStatus.noItems"),
        TOO_MANY_INSTANCES("game.buyStatus.tooManyInstances");

        private final String textId;


        ShopError(String textId) {
            this.textId = textId;
        }

        public String getText() {
            return GAME.getText(textId);
        }
    }

    public void tick() {
//        if (renderQuality > 0)
        canvas.repaint();
//        System.out.println(renderTime);
//        sleep(renderTime);
//        betterSleep(renderTime);
//        synchronized (lock) {
//            lock.notifyAll();
//        }
    }

    public void changeLookFeel(Runnable runnable) {
        SwingUtilities.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            runnable.run();
            SwingUtilities.updateComponentTreeUI(window);
            SwingUtilities.updateComponentTreeUI(settingsWindow);
            SwingUtilities.updateComponentTreeUI(shopWindow);
            SwingUtilities.updateComponentTreeUI(fileSelectWindow);
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });

    }

    public void click() {
        clicks.incrementAndGet();
    }

    public void addClicks(int clicks) { this.clicks.addAndGet(clicks); }

    public long getClicks() {
        return clicks.get();
    }

    public Random getRandom() {
        return random;
    }

    @Override public void windowOpened(WindowEvent e) { }
    @Override public void windowClosing(WindowEvent e) {
        saveGame(false);
        stop();
        System.gc();
        fileSelectController.initText();
        fileSelectWindow.setLocationRelativeTo(null);
        fileSelectWindow.setVisible(true);
    }
    @Override public void windowClosed(WindowEvent e) { }
    @Override public void windowIconified(WindowEvent e) { }
    @Override public void windowDeiconified(WindowEvent e) { }
    @Override public void windowActivated(WindowEvent e) { }

    @Override public void windowDeactivated(WindowEvent e) { }
    @Override public void mouseClicked(MouseEvent e) { }
    @Override public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            click();
            soundSystem.playInstancedAudio("game.cookie.click");
        }
    }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
    @Override public void mouseDragged(MouseEvent e) { }
    @Override public void mouseMoved(MouseEvent e) { }
}
