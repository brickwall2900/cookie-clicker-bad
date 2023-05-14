package com.github.brickwall2900.cookie;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class SettingsController implements ActionListener, ChangeListener {
    private final SettingsWindow window;

    public SettingsController(SettingsWindow window) {
        this.window = window;

        window.okButton.addActionListener(this);
        window.cancelButton.addActionListener(this);
        window.applyButton.addActionListener(this);

        window.graphicsSlider.addChangeListener(this);

        window.debugCounter.addActionListener(this);
        window.autoSaveNotifications.addActionListener(this);
        window.fpsLimitField.addActionListener(this);
        window.autoSaveTimerField.addActionListener(this);
        window.themeBox.addActionListener(this);
    }

    public void initText() {
        window.title.setText(Game.GAME.getText("game.settings.title"));

        window.okButton.setText(Game.GAME.getText("game.ok"));
        window.cancelButton.setText(Game.GAME.getText("game.cancel"));
        window.applyButton.setText(Game.GAME.getText("game.apply"));

        window.fpsLimitLabel.setText(Game.GAME.getText("game.settings.renderTime"));

        window.graphicsQualityLabel.setText(Game.GAME.getText("game.settings.renderQuality"));

        window.autoSaveNotifications.setText(Game.GAME.getText("game.settings.autoSaveNotifications"));
        window.autoSaveTimerLabel.setText(Game.GAME.getText("game.settings.autoSaveTimer"));
        window.debugCounter.setText(Game.GAME.getText("game.settings.debugCounter"));

        window.themeLabel.setText(Game.GAME.getText("game.settings.theme"));
        ((DefaultComboBoxModel<String>)(window.themeBox.getModel())).addElement(Game.GAME.getText("game.settings.theme.dark"));
        ((DefaultComboBoxModel<String>)(window.themeBox.getModel())).addElement(Game.GAME.getText("game.settings.theme.light"));
    }

    public void loadSettings(SaveFile.SavedSettings settings) {
        window.fpsLimitField.setText(String.valueOf(settings.renderTime()));
        window.graphicsSlider.setValue(settings.renderQuality());
        window.autoSaveNotifications.setSelected(settings.enableAutoSaveNotifications());
        window.autoSaveTimerField.setText(String.valueOf(settings.autoSaveTimer()));
        window.debugCounter.setSelected(settings.enableDebugCounter());
        window.themeBox.setSelectedItem(settings.darkMode() ? Game.GAME.getText("game.settings.theme.dark") : Game.GAME.getText("game.settings.theme.light"));
        updateSlider();
    }

    public void onSave() {
        float frameLimit = Float.parseFloat(window.fpsLimitField.getText());
        int graphicsQuality = window.graphicsSlider.getValue();
        boolean autoSaveNotif = window.autoSaveNotifications.isSelected();
        float autoSaveTime = Math.max(1, Float.parseFloat(window.autoSaveTimerField.getText()));
        boolean enableDebug = window.debugCounter.isSelected();
        boolean dark = Objects.equals(window.themeBox.getSelectedItem(), Game.GAME.getText("game.settings.theme.dark"));
        SaveFile.SavedSettings newSettings = new SaveFile.SavedSettings(frameLimit, graphicsQuality, autoSaveNotif, autoSaveTime, enableDebug, dark);
        Game.GAME.loadSettings(newSettings);
    }

    public void onClose() {
        window.applyButton.setEnabled(false);
        window.dispose();
    }

    public void updateSlider() {
        int slider = window.graphicsSlider.getValue();
        int tValue = slider;
        String desc = "game.";
        while (tValue >= 0 && desc.startsWith("game.")) {
            desc = Game.GAME.getText("game.settings.renderQuality." + tValue);
            tValue--;
        }
        window.graphicsDescription.setText(Game.GAME.getText("game.settings.renderQuality.desc", slider, desc));
        window.applyButton.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(window.cancelButton)) {
            onClose();
        } else if (e.getSource().equals(window.applyButton)) {
            onSave();
        } else if (e.getSource().equals(window.okButton)) {
            onSave();
            onClose();
        } else {
            window.applyButton.setEnabled(true);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(window.graphicsSlider)) {
            updateSlider();
        }
    }
}
