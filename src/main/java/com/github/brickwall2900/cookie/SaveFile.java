package com.github.brickwall2900.cookie;

import com.github.brickwall2900.cookie.items.Item;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.stream.Collectors;

public class SaveFile {
    public static final File SAVE_FILE_DIRECTORY = new File(System.getProperty("user.home"), ".morecookies");

    static {
        SAVE_FILE_DIRECTORY.mkdir();
    }

    public record SaveFileRecord(String name, long clicks, List<Item> items, long timeWasted /* time played */, SavedSettings savedSettings) implements Serializable {}

    public static class SavedSettings implements Serializable {
        private float renderTime;
        private int renderQuality;
        private boolean enableAutoSaveNotifications;
        private float autoSaveTimer;
        private boolean enableDebugCounter;
        private boolean darkMode;

        public SavedSettings(float renderTime, int renderQuality, boolean enableAutoSaveNotifications,
                             float autoSaveTimer, boolean enableDebugCounter, boolean darkMode) {
            this.renderTime = renderTime;
            this.renderQuality = renderQuality;
            this.enableAutoSaveNotifications = enableAutoSaveNotifications;
            this.autoSaveTimer = autoSaveTimer;
            this.enableDebugCounter = enableDebugCounter;
            this.darkMode = darkMode;
        }

        public float renderTime() {
            return renderTime;
        }

        public int renderQuality() {
            return renderQuality;
        }

        public boolean enableAutoSaveNotifications() {
            return enableAutoSaveNotifications;
        }

        public float autoSaveTimer() {
            return autoSaveTimer;
        }

        public boolean enableDebugCounter() {
            return enableDebugCounter;
        }

        public boolean darkMode() {
            return darkMode;
        }

        public void setRenderTime(float renderTime) {
            this.renderTime = renderTime;
        }

        public void setRenderQuality(int renderQuality) {
            this.renderQuality = renderQuality;
        }

        public void setEnableAutoSaveNotifications(boolean enableAutoSaveNotifications) {
            this.enableAutoSaveNotifications = enableAutoSaveNotifications;
        }

        public void setAutoSaveTimer(float autoSaveTimer) {
            this.autoSaveTimer = autoSaveTimer;
        }

        public void setEnableDebugCounter(boolean enableDebugCounter) {
            this.enableDebugCounter = enableDebugCounter;
        }

        public void setDarkMode(boolean darkMode) {
            this.darkMode = darkMode;
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Loading">

    private SaveFileRecord loadStage1(String name) throws Exception {
        File file = getFileOnName(name);
        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 BufferedInputStream inputStream = new BufferedInputStream(fileInputStream)) {
                return loadStage2(name, inputStream);
            }
        } else {
            panicOnLoad(new FileNotFoundException());
            return null;
        }
    }
    private SaveFileRecord loadStage2(String name, InputStream inputStream) throws Exception {
        StringBuilder nameBuilder = new StringBuilder();
        int b;
        while ((b = inputStream.read()) != 0) {
            nameBuilder.append((char) b);
        }
        String nameRead = nameBuilder.toString();
        if (!name.equals(nameRead)) panicOnLoad(null);
        byte[] iv = new byte[16];
        if (inputStream.read(iv) != 16) throw new IOException();
        SaveFileRecord record = null;
//        JPanel panel = new JPanel();
//        JLabel label = new JLabel(Game.GAME.getText("game.file.password"));
//        JPasswordField pass = new JPasswordField(10);
//        panel.add(label);
//        panel.add(pass);
//        String[] options = new String[] {Game.GAME.getText("game.ok"), Game.GAME.getText("game.cancel")};
//        int option = JOptionPane.showOptionDialog(null, panel, Game.GAME.getText("game.file.password.title"),
//                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
//                null, options, options[0]);
//        if (option == 0) {
//            char[] password = pass.getPassword();
        char[] password = bytesToChars(longToBytes(SoftId.getId().getSoftId()));
        try (CipherInputStream cin = new CipherInputStream(inputStream, decrypt(password, iv))) {
            record = loadStage3(name, cin);
        }
//            lastPassword = password;
        Arrays.fill(password, (char) 0);
        Arrays.fill(iv, (byte) 0);
//        } else {
//            Arrays.fill(iv, (byte) 0);
//            panicOnLoad(null);
//        }
        return record;
    }

    private SaveFileRecord loadStage3(String inName, InputStream inputStream) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new DataInputStream(inputStream))) { // yes
            SaveFileRecord record;
            String name = objectInputStream.readUTF();
            if (!inName.equals(name)) panicOnLoad(null);
            byte[] checksum = (byte[]) objectInputStream.readObject();
            byte[] b = (byte[]) objectInputStream.readObject();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] compared = digest.digest(b);
            if (!Arrays.equals(compared, checksum)) {
                panicOnLoad(null);
            }
            record = loadStage4(inName, b);
            Arrays.fill(checksum, (byte) 0);
            Arrays.fill(b, (byte) 0);
            Arrays.fill(compared, (byte) 0);
            return record;
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
            panicOnLoad(e);
        }
        return null;
    }

    private SaveFileRecord loadStage4(String inName, byte[] bytes) {
        Random random = new Random();
        bytes = Base64.getDecoder().decode(bytes);
        for (int i = 0; i < bytes.length; i++) {
            random.setSeed(37L * i + inName.hashCode());
            bytes[i] = (byte)(bytes[i] - random.nextInt(0, 126));
        }
        SaveFileRecord outRecord = null;
        try (ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(new DataInputStream(bin))) {
            Object object = objectInputStream.readObject();
            if (object instanceof SaveFileRecord record) {
                String recordName = record.name;
                if (!recordName.equals(inName)) panicOnLoad(null);
                outRecord = record;
            } else {
                panicOnLoad(null);
            }
        } catch (IOException | ClassNotFoundException e) {
            panicOnLoad(e);
        }
        Arrays.fill(bytes, (byte) 0);
        return outRecord;
    }


    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Saving">

    private void saveStage1(String name, SaveFileRecord saveFileRecord) throws Exception {
        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(new DataOutputStream(bos))) {
            objectOutputStream.writeObject(saveFileRecord);
            bytes = bos.toByteArray();
        }
        Random random = new Random();
        for (int i = 0; i < bytes.length; i++) {
            random.setSeed(37L * i + name.hashCode());
            bytes[i] = (byte)(bytes[i] + random.nextInt(0, 126));
        }
        bytes = Base64.getEncoder().encode(bytes);
        saveStage2(name, bytes);
        Arrays.fill(bytes, (byte) 0);
    }
    private void saveStage2(String name, byte[] bytes) throws Exception {
        byte[] outBytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(new DataOutputStream(bos))) {
            objectOutputStream.writeUTF(name);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            objectOutputStream.writeObject(digest.digest(bytes));
            objectOutputStream.writeObject(bytes);
            outBytes = bos.toByteArray();
        }
        saveStage3(name, outBytes);
        Arrays.fill(outBytes, (byte) 0);
    }

    private void saveStage3(String name, byte[] bytes) throws Exception {
        byte[] iv = generateBIv();
        byte[] outBytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        char[] password = bytesToChars(longToBytes(SoftId.getId().getSoftId()));
        try (CipherOutputStream outputStream = new CipherOutputStream(bos, encrypt(password, iv))) {
            bos.write(name.getBytes()); bos.write('\u0000');
            bos.write(iv);
            outputStream.write(bytes);
        }
        outBytes = bos.toByteArray();
        Arrays.fill(iv, (byte) 0);
        Arrays.fill(password, (char) 0);
        saveStage4(name, outBytes);
        Arrays.fill(outBytes, (byte) 0);
    }

    private void saveStage4(String name, byte[] bytes) throws Exception {
        File file = getFileOnName(name);
        if (!file.exists()) {
            for (int i = 0; i < 10 && !file.createNewFile(); i++);
        }
        byte[] rnd = new byte[nearestPowerOfTwo(bytes.length)];
        byte[] m = new byte[nearestPowerOfTwo(bytes.length) - bytes.length];
        new SecureRandom().nextBytes(m);
        System.arraycopy(bytes, 0, rnd, 0, bytes.length);
        System.arraycopy(m, 0, rnd, bytes.length, m.length);
        for (int i = 0; i < 10; i++) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream)) {
                outputStream.write(rnd);
                return;
            } catch (IOException ex) {
                System.err.println("error saving file!");
                ex.printStackTrace();
                Thread.sleep(500);
            }
        }
        throw new IOException("Error saving file!");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Listing">
    private String getNameStage1(File file) throws Exception {
        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 BufferedInputStream inputStream = new BufferedInputStream(fileInputStream)) {
                return getNameStage2(inputStream);
            }
        } else {
            panicOnLoad(new FileNotFoundException());
            return null;
        }
    }
    private String getNameStage2(InputStream inputStream) throws Exception {
        StringBuilder nameBuilder = new StringBuilder();
        int b;
        while ((b = inputStream.read()) > 0) {
            nameBuilder.append((char) b);
        }
        return nameBuilder.toString();
    }

    // </editor-fold>
    public SaveFileRecord loadSave(String name) {
        try {
            return loadStage1(name);
        } catch (Exception e) {
            e = null;
            JOptionPane.showMessageDialog(null, Game.GAME.getText("game.file.load.fail"), Game.GAME.getText("game.title"), JOptionPane.ERROR_MESSAGE);
            panicOnLoad(null);
            return null;
        }
    }

    public void save(SaveFileRecord record) {
        try {
            saveStage1(record.name, record);
        } catch (Exception e) {
            e = null;
            JOptionPane.showMessageDialog(null, Game.GAME.getText("game.file.save.fail"), Game.GAME.getText("game.title"), JOptionPane.ERROR_MESSAGE);
            System.err.println("Error on save!");
        }
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static int nearestPowerOfTwo(int value) {
        int highestOneBit = Integer.highestOneBit(value);
        if (value == highestOneBit) {
            return value;
        }
        return highestOneBit << 1;
    }

    private static Cipher encrypt(char[] key, byte[] initVector) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key, new byte[] {
                    (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
                    (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17
            }, 65536, 256);
            SecretKeySpec skeySpec = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            return cipher;
        } catch (Exception ex) {
            ex = null;
        }

        return null;
    }

    private static Cipher decrypt(char[] key, byte[] initVector) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(key, new byte[] {
                    (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
                    (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17
            }, 65536, 256);
            SecretKeySpec skeySpec = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            return cipher;
        } catch (Exception ex) {
            ex = null;
        }

        return null;
    }

    private long getSoftId() {
        long hc = 0;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) { throw new RuntimeException(e); }
        return hc;
    }

    public static byte[] longToBytes(long value) {
        byte[] bytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte) (value & 0xff);
            value >>= 8;
        }
        return bytes;
    }

    public static char[] bytesToChars(byte[] bytes) {
        Charset charset = StandardCharsets.UTF_8; // specify character encoding
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes); // wrap the byte array
        CharBuffer charBuffer = charset.decode(byteBuffer); // decode the byte buffer
        char[] charArray = charBuffer.array(); // get the char array from the char buffer
        byteBuffer.clear();
        charBuffer.clear();
        return charArray;
    }

    private byte[] generateBIv() {
        byte[] iv = new byte[16];
//        iv = new byte[]{-98, 74, -52, -84, 69, -69, -80, 70, 48, -51, 53, 99, 85, 20, -42, 73};
        new SecureRandom().nextBytes(iv);
//        System.out.println("iv " + Arrays.toString(iv));
        return iv;
    }

    private File getFileOnName(String name) {
        byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) { throw new RuntimeException(e); }
        String fileName = bytesToHex(digest.digest(nameBytes));
        return new File(SAVE_FILE_DIRECTORY, fileName);
    }

    public boolean deleteSave(String name) {
        return getFileOnName(name).delete();
    }

    private char[] lastPassword;
    public char[] getLastEnteredPassword() {
        char[] pwd = lastPassword;
        Arrays.fill(lastPassword, (char) 0);
        lastPassword = null;
        return pwd;
    }

    private void panicOnLoad(Throwable t) {
        if (t != null)
            throw new RuntimeException("Invalid save file!", t);
        else
            throw new RuntimeException("Invalid save file!");
    }

    public String getNameOnFile(File file) {
        try {
            return getNameStage1(file);
        } catch (Exception e) {
            e = null;
            System.err.println("error listing file!");
//            e.printStackTrace();
            return null;
        }
    }

    public List<String> listSaves() {
        String[] fileList = SAVE_FILE_DIRECTORY.list();
        if (fileList != null) {
            return Arrays.stream(fileList).map(n -> getNameOnFile(new File(SAVE_FILE_DIRECTORY, n))).collect(Collectors.toList());
        }
        return null;
    }

    public boolean doesSaveExist(String name) {
        return getFileOnName(name).exists();
    }
}
