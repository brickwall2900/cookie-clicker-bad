package com.github.brickwall2900.cookie;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
            Game.GAME.start();
        } catch (Throwable t) {
            t.printStackTrace();
            if (t instanceof Exception) {
                JOptionPane.showMessageDialog(null, "Error! " + t, "Error!", JOptionPane.ERROR_MESSAGE, null);
            }
            System.exit(1);
        }
    }
}
