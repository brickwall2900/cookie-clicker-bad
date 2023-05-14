package com.github.brickwall2900.cookie;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

public class SoundSystem extends Thread {
    private final Properties properties = new Properties();
    private final HashMap<String, AudioInformation> textIdCached = new HashMap<>();

    public SoundSystem() {
        setName("Sound System Thread");
        setDaemon(true);
    }

    public void loadAudioMap(ClassResource resource) throws IOException {
        properties.load(resource.getStream());
    }

    private boolean running = true;

    public enum SSCommand {
        PlayInstancedAudio, LoopBackgroundMusic, EmptyCommand;
    }

    private final int maxCommands = 256; // hard coded in

    private record Command(SSCommand command, Object[] parameters) {
        @Override
        public String toString() {
            return "Command{" +
                    "command=" + command +
                    ", parameters=" + Arrays.toString(parameters) +
                    '}';
        }
    }
    private final BlockingQueue<Command> queue = new ArrayBlockingQueue<>(maxCommands);
    private final List<Long> commandsTimestamp = new ArrayList<>();
    private final List<Clip> audioClips = new ArrayList<>();
    private final List<Sequencer> sequencers = new ArrayList<>();
    private final Map<String, Soundbank> soundFontNames = new HashMap<>();

    public void run() {
        try {
            while (running) {
                final Command command = queue.poll(1, TimeUnit.SECONDS);
                if (command != null) {
//                    System.out.println("[!] COMMAND FOUND!! (" + command + ")");
                    switch (command.command) {
                        case PlayInstancedAudio ->
                            playInstancedAudioI((String) command.parameters[0]);
                        case LoopBackgroundMusic ->
                            loopBackgroundMusicI((String) command.parameters[0], (Integer) command.parameters[1]);
                    }
                    commandsTimestamp.add(System.currentTimeMillis());
                }
                soundSystemUpdate();
            }
        } catch (Exception ex) {
            System.err.println("Sound System shut down on exception!");
            ex.printStackTrace();
        }
        queue.clear();
    }

    public void soundSystemUpdate() {
        Iterator<Sequencer> sequencerIterator = sequencers.iterator();
        while (sequencerIterator.hasNext()) {
            Sequencer sequencer = sequencerIterator.next();
            if (!sequencer.isRunning()) {
                sequencer.close();
                sequencerIterator.remove();
            }
        }
    }

    public int updateDebug() {
        try {
            commandsTimestamp.removeIf(t -> (System.currentTimeMillis() - t) >= 1000);
        } catch (ConcurrentModificationException ignored) {}
        return commandsTimestamp.size();
    }

    private AudioInformation getAudioInformation(String textId) {
        AudioInformation info;
        if (!textIdCached.containsKey(textId)) {
            String[] textInfo = properties.getProperty(textId, "").split(",");
            int minimumNumber = 2;
            if (textInfo.length < minimumNumber) {
                throw new IllegalArgumentException("Audio Information is incomplete or incorrectly interpreted!");
            }
            String name = textInfo[0];
            DataType type = DataType.valueOf(textInfo[1].toUpperCase(Locale.ROOT));
//            System.out.println(Arrays.toString(textInfo));
            if (!type.equals(DataType.MIDI)) {
                minimumNumber = 7;
                if (textInfo.length < minimumNumber) {
                    throw new IllegalArgumentException("Audio Information is incomplete or incorrectly interpreted!");
                }
                AudioFormat format = new AudioFormat(
                        stringToInt(textInfo[2]), // sample rate
                        stringToInt(textInfo[3]), // sample size
                        stringToInt(textInfo[4]), // channels
                        stringToBool(textInfo[5]), // signed
                        stringToBool(textInfo[6])); // big endian
                info = new AudioInformation(name, format, type);
            } else {
                info = new AudioInformation(name, type, properties.getProperty(textInfo[2], "game.defaultSoundfont"));
            }
            textIdCached.put(textId, info);
        } else {
            info = textIdCached.get(textId);
        }
        return info;
    }

    private int stringToInt(String str) {
        return Integer.parseInt(str);
    }

    private boolean stringToBool(String str) {
        return Boolean.parseBoolean(str);
    }

    private void addCommand(Command command) {
        queue.add(command);
    }

    public void playInstancedAudio(String textId)  {
        addCommand(new Command(SSCommand.PlayInstancedAudio, new Object[] {textId}));
    }

    private void playInstancedAudioI(String textId) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InvalidMidiDataException, MidiUnavailableException {
        AudioInformation info = getAudioInformation(textId);
        if (!info.dataType.equals(DataType.MIDI)) {
            Clip clip = createNewClip(info.format);
            ClassResource resource = new ClassResource(info.name);
            switch (info.dataType) {
                case RAW -> openClipRaw(clip, info.format, resource.getStream());
                case COMPRESSED -> {
                    try (GZIPInputStream gzipInputStream = new GZIPInputStream(resource.getStream())) {
                        openClipRaw(clip, info.format, gzipInputStream);
                    }
                }
                default -> openClipFormatted(clip, info.format, resource.getStream());
            }
            autoCloseClip(clip);
            audioClips.add(clip);
            clip.start();
        } else {
            ClassResource resource = new ClassResource(info.name);
            Synthesizer synthesizer = getSynthesizer();
            if (info.soundfont != null) {
                setSoundfont(info.soundfont, synthesizer);
            }
            Sequence sequence = getSequence(resource.getStream());
            Sequencer sequencer = getSequencer(synthesizer);
            playMidi(sequencer, sequence);
            sequencers.add(sequencer);
            resource.close();
        }
    }

    private final Random random = new Random();

    public String getRandomAudioName(String name) {
        Map<String, String> audioNames = getPropertiesStartingWith(properties, name);
        return name + '.' + random.nextInt(audioNames.size());
    }

    public static Map<String, String> getPropertiesStartingWith(Properties properties, String prefix) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String name = (String) entry.getKey();
            if (name.startsWith(prefix)) {
                String value = (String) entry.getValue();
                map.put(name, value);
            }
        }
        return map;
    }

    public void loopBackgroundMusic(String textId, int loops) {
//        StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
//        System.out.println("loopBackgroundMusic caller " + stackWalker.getCallerClass());
        addCommand(new Command(SSCommand.LoopBackgroundMusic, new Object[] {textId, loops}));
    }

    private AudioInformation backgroundMusicInfo;
    private Clip backgroundMusic;
    private boolean backgroundMusicEnded;
    private Sequencer backgroundMidiMusic;
    private Synthesizer backgroundMidiSynthesizer;
    private final AudioFormat dummyFormat = new AudioFormat(44100, 8, 1, true, false);

    public boolean isBackgroundMusicEnded() {
//        System.out.println("1 " + (backgroundMusic != null && backgroundMidiMusic != null));
//        System.out.println("2 " + backgroundMusicEnded);
//        if (backgroundMidiMusic != null) {
//            System.out.println("3 " + !backgroundMidiMusic.isRunning());
//            if (backgroundMusicInfo != null) {
//                System.out.println("4 " + (backgroundMusicInfo.dataType != DataType.MIDI ? backgroundMusicEnded : !backgroundMidiMusic.isRunning()));
//            }
//        }
//        System.out.println("5 " + ((backgroundMusic != null && backgroundMidiMusic != null) && (backgroundMusicInfo.dataType != DataType.MIDI ? backgroundMusicEnded : !backgroundMidiMusic.isRunning())));
//        if (((backgroundMusic != null && backgroundMidiMusic != null) && (backgroundMusicInfo.dataType != DataType.MIDI ? backgroundMusicEnded : !backgroundMidiMusic.isRunning()))) {
//            System.out.println("ENDED --- ENDED");
//        }
//        System.out.println("00-000-0-0--000--0-");
        return (backgroundMusic != null && backgroundMidiMusic != null) && (backgroundMusicInfo.dataType != DataType.MIDI ? backgroundMusicEnded : !backgroundMidiMusic.isRunning());
    }

    public AudioInformation getBackgroundMusicInfo() {
        return backgroundMusicInfo;
    }

    private void loopBackgroundMusicI(String textId, int loops) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InvalidMidiDataException, MidiUnavailableException {
        if (backgroundMusic == null) {
            backgroundMusic = createNewClip(dummyFormat);
            backgroundMusic.addLineListener(line -> backgroundMusicEnded = line.getType().equals(LineEvent.Type.STOP));
        }
        backgroundMusic.stop();
        backgroundMusic.close();

        backgroundMusicEnded = false;

        if (backgroundMidiMusic == null) {
            backgroundMidiSynthesizer = getSynthesizer();
            backgroundMidiMusic = getSequencer(backgroundMidiSynthesizer);
        }
        if (backgroundMidiMusic.isOpen()) {
            backgroundMidiMusic.stop();
            backgroundMidiMusic.close();
        }

//        System.out.println("-----------------------------------------");
////        StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
////        System.out.println("caller: " + stackWalker.getCallerClass());
//        System.out.println(backgroundMusic);
//        System.out.println(backgroundMidiMusic);
//        System.out.println((backgroundMusic == null && backgroundMidiMusic == null));
//        System.out.println(backgroundMusicInfo);
//        System.out.println(textId);

        if (textId != null) {
            AudioInformation info = getAudioInformation(textId);
            backgroundMusicInfo = info;
            ClassResource resource = new ClassResource(info.name);
            if (!info.dataType.equals(DataType.MIDI)) {
                switch (info.dataType) {
                    case RAW -> openClipRaw(backgroundMusic, info.format, resource.getStream());
                    case COMPRESSED -> {
                        try (GZIPInputStream gzipInputStream = new GZIPInputStream(resource.getStream())) {
                            openClipRaw(backgroundMusic, info.format, gzipInputStream);
                        }
                    }
                    default -> openClipFormatted(backgroundMusic, info.format, resource.getStream());
                }
                backgroundMusic.loop(loops);
            } else {
                if (info.soundfont != null) {
                    setSoundfont(info.soundfont, backgroundMidiSynthesizer);
                }
                Sequence sequence = getSequence(resource.getStream());
                playMidi(backgroundMidiMusic, sequence);
                backgroundMidiMusic.setLoopCount(loops);
            }
            resource.close();
        }
    }

    private void setSoundfont(String soundfont, Synthesizer synthesizer) throws IOException, InvalidMidiDataException {
        Soundbank soundbank;
        if (!soundFontNames.containsKey(soundfont)) {
            try (ClassResource resource = new ClassResource(soundfont + ".gz")) {
                try (GZIPInputStream inputStream = new GZIPInputStream(resource.getStream())) {
                    soundbank = MidiSystem.getSoundbank(inputStream);
                    soundFontNames.put(soundfont, soundbank);
                }
            }
        } else {
            soundbank = soundFontNames.get(soundfont);
        }
        synthesizer.loadAllInstruments(soundbank);
    }

    public Clip createNewClip(AudioFormat format) throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        return (Clip) AudioSystem.getLine(info);
    }

    public Clip openClip(Clip clip, InputStream inputStream) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        clip.open(AudioSystem.getAudioInputStream(inputStream));
        return clip;
    }

    public Clip openClipRaw(Clip clip, AudioFormat format, InputStream inputStream) throws IOException, LineUnavailableException {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(inputStream.readAllBytes());
        clip.open(new AudioInputStream(byteArray, format, byteArray.available()));
        return clip;
    }

    public Clip openClipFormatted(Clip clip, AudioFormat format, InputStream inputStream) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(format, AudioSystem.getAudioInputStream(new ByteArrayInputStream(inputStream.readAllBytes())));
        clip.open(ais);
        return clip;
    }

    public Sequence getSequence(InputStream inputStream) throws InvalidMidiDataException, IOException {
        return MidiSystem.getSequence(inputStream);
    }

    public Synthesizer getSynthesizer() throws MidiUnavailableException {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        return synthesizer;
    }

    public Sequencer getSequencer(Synthesizer synthesizer) throws MidiUnavailableException {
        Sequencer sequencer = MidiSystem.getSequencer();
        for (Transmitter tm : sequencer.getTransmitters()) {
            tm.close();
        }
        sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
        return sequencer;
    }

    public void playMidi(Sequencer sequencer, Sequence sequence) throws MidiUnavailableException, InvalidMidiDataException {
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.start();
    }

    public void autoCloseClip(Clip clip) {
        LineListener lineListener = event -> {
            if (event.getType().equals(LineEvent.Type.STOP)) {
                audioClips.remove(clip);
                clip.close();
            }
        };
        clip.addLineListener(lineListener);
    }

    public record AudioInformation(String name, AudioFormat format, DataType dataType, String soundfont) {
        public AudioInformation(String name, AudioFormat format, DataType dataType) {
            this(name, format, dataType, null);
        }

        public AudioInformation(String name, DataType dataType, String soundfont) {
            this(name, null, dataType, soundfont);
        }
    }

    public enum DataType {
        RAW, COMPRESSED, FORMATTED, MIDI;

        public void handle(AudioInformation info, ClassResource resource) {
            throw new UnsupportedOperationException();
        }
    }
}
