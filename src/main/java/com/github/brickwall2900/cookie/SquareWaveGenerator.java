package com.github.brickwall2900.cookie;

public class SquareWaveGenerator extends WaveGenerator {
    public static final float TWO_PI = (float) (2f * Math.PI);

    @Override
    protected byte doEquation(int index, int amplitude, int sampleRate, float frequency) {
        float rate = calculateSamplingRate(index, sampleRate, frequency);
        float calc = Integer.signum((int) (Math.sin(TWO_PI * rate) * amplitude)) * amplitude;
        return (byte) calc;
    }
}
