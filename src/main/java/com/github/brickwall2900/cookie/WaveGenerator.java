package com.github.brickwall2900.cookie;

import javax.sound.sampled.*;
import java.util.LinkedList;
import java.util.Queue;

public abstract class WaveGenerator {
    protected SourceDataLine line;
    protected Thread thread;
    protected WaveGeneratorThread waveGeneratorManager;

    public void start(AudioFormat format) {
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open();
            line.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        thread = new Thread(waveGeneratorManager = new WaveGeneratorThread(), getClass().getSimpleName() + "@" + hashCode());
        thread.start();
    }

    public void play(float frequency, int amplitude) {
        if (waveGeneratorManager.stopped) {
            thread = new Thread(waveGeneratorManager = new WaveGeneratorThread(), getClass().getName() + "@" + hashCode());
            thread.start();
        }
        waveGeneratorManager.post(new Command(true, frequency, amplitude));
    }

    public void pause() {
        waveGeneratorManager.post(new Command(false, 1, 0));
    }

    public void stop() {
        pause();
        waveGeneratorManager.stopped = true;
    }

    private record Command(boolean play, float frequency, int amplitude) {
        @Override
        public String toString() {
            return "Command{" +
                    "play=" + play +
                    ", frequency=" + frequency +
                    ", amplitude=" + amplitude +
                    '}';
        }
    }

    private class WaveGeneratorThread implements Runnable {
        private final Queue<Command> commandQueue = new LinkedList<>();
        private final Object lock = new Object();
        private boolean stopped = false;
        private long idleTime = 0;
        private float frequency;
        private int amplitude;
        private boolean playing = false;

        @Override
        public void run() {
            try {
                idleTime = System.currentTimeMillis();
                while (!stopped) {
                    Command cmd = commandQueue.poll();
                    if (cmd != null) {
                        playing = cmd.play;
                        frequency = cmd.frequency;
                        amplitude = cmd.amplitude;
//                        System.out.println(cmd);
                        idleTime = System.currentTimeMillis();
                    }

                    byte[] buffer;
                    if (playing) {
                        buffer = generate(frequency, amplitude);
                    } else {
                        buffer = generateSilence();
                    }
                    line.write(buffer, 0, buffer.length);

                    long elapsedTime = System.currentTimeMillis() - idleTime;
                    if (elapsedTime >= 60 * 1000) {
                        stopped = true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        public void post(Command command) {
            commandQueue.add(command);
        }
    }

    public byte[] generate(float frequency, int amplitude) {
        return generate(frequency, amplitude, (int) line.getFormat().getSampleRate());
    }

    /* Generate wave for 1/30th of a second */
    private int index = 0;
    public byte[] generate(float frequency, int amplitude, int sampleRate) {
        byte[] bytes = new byte[(int) (sampleRate * (1/30f))];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = doEquation(index++, amplitude, sampleRate, frequency);
        }
        return bytes;
    }
    public byte[] generateSilence() {
        return generateSilence((int) line.getFormat().getSampleRate());
    }

    public byte[] generateSilence(int sampleRate) {
        return new byte[(int) (sampleRate * (1/15f))];
    }

    protected float calculateSamplingRate(int index, float sampleRate, float frequency) {
        float rate = (float)index / sampleRate;
        return rate * frequency;
    }

    protected abstract byte doEquation(int index, int amplitude, int sampleRate, float frequency);
}
