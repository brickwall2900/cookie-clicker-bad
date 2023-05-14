package com.github.brickwall2900.cookie;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Notes {
    public static final HashMap<Integer, Note> NOTE_FREQ_NAME_MAP = new HashMap<>();

    static { // ;)
        int nn = 89;
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C#0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D#0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("E0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F#0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G#0", nn++));
        nn = 1;
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A#0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("B0", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C#1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D#1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("E1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F#1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G#1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A#1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("B1", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C#2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D#2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("E2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F#2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G#2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A#2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("B2", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C#3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D#3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("E3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F#3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G#3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A#3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("B3", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C#4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D#4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("E4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F#4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G#4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A#4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("B4", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C#5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D#5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("E5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F#5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G#5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A#5", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("B6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C#6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D#6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("E6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F#6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G#6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A#6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("B6", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C#7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D#7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("E7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F#7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G#7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A#7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("B7", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C8", nn++));
        nn += 9;
        NOTE_FREQ_NAME_MAP.put(nn, new Note("C#8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("D#8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("E8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("F#8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("G#8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("A#8", nn++));
        NOTE_FREQ_NAME_MAP.put(nn, new Note("B8", nn++));
    }

    public static final float CONCERT_PITCH = 440;

    // <editor-fold defaultstate="collapsed" desc="Midi Note to Key Map">
    public static int[] MIDI_NOTE_TO_KEY = new int[] {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            89,
            90,
            91,
            92,
            93,
            94,
            95,
            96,
            97,
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            10,
            11,
            12,
            13,
            14,
            15,
            16,
            17,
            18,
            19,
            20,
            21,
            22,
            23,
            24,
            25,
            26,
            27,
            28,
            29,
            30,
            31,
            32,
            33,
            34,
            35,
            36,
            37,
            38,
            39,
            40,
            41,
            42,
            43,
            44,
            45,
            46,
            47,
            48,
            49,
            50,
            51,
            52,
            53,
            54,
            55,
            56,
            57,
            58,
            59,
            60,
            61,
            62,
            63,
            64,
            65,
            66,
            67,
            68,
            69,
            70,
            71,
            72,
            73,
            74,
            75,
            76,
            77,
            78,
            79,
            80,
            81,
            82,
            83,
            84,
            85,
            86,
            87,
            88,
            98,
            99,
            100,
            101,
            102,
            103,
            104,
            105,
            106,
            107,
            108,
    };
    // </editor-fold>

    public static float getFrequencyOfNote(int key) {
        return getFrequencyOfNote(key, CONCERT_PITCH);
    }

    public static float getFrequencyOfNote(int key, float tune) {
        return (float) (Math.pow(nthRootOf(2, 12), key - 49) * tune);
    }

    public static float nthRootOf(float base, float index) {
        return (float) Math.pow(base, 1f / index);
    }

    private static int getNoteNumber(String noteName, int octave) {
        char[] chars = noteName.toCharArray();
        char note = 0;
        char currentChar;
        boolean sharp;
        for (int i = 0; i < chars.length; i++) {
            currentChar = chars[i];
            if (Pattern.matches("[A-G]", String.valueOf(currentChar))) {
                note = currentChar;
            } else if (Pattern.matches("[#b]", String.valueOf(currentChar))) {
                sharp = true;
            }
        }
        return -0;
    }

    public static class Note {
        private String name;
        private int number;

        public Note(String name, int number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        public float getFrequency() {
            return getFrequencyOfNote(number);
        }
    }
}
