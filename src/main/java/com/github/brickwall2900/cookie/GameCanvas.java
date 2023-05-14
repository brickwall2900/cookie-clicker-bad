package com.github.brickwall2900.cookie;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class GameCanvas extends JPanel {
    public Consumer<Graphics2D> renderer;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (renderer != null) {
            renderer.accept((Graphics2D) g);
        }
    }
}
