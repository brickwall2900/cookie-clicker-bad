package com.github.brickwall2900.cookie;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileSelectController implements ActionListener {
    private final FileSelectWindow fileSelectWindow;
    private final SaveFile saveFile;

    public FileSelectController(FileSelectWindow fileSelectWindow, SaveFile saveFile) {
        this.fileSelectWindow = fileSelectWindow;
        this.saveFile = saveFile;

        fileSelectWindow.exitButton.addActionListener(this);
        fileSelectWindow.createButton.addActionListener(this);
        fileSelectWindow.openButton.addActionListener(this);
        fileSelectWindow.deleteButton.addActionListener(this);
    }

    public void initText() {
        fileSelectWindow.setTitle(Game.GAME.getText("game.file.title"));
        fileSelectWindow.title.setText(Game.GAME.getText("game.file.title"));
        fileSelectWindow.instruction.setText(Game.GAME.getText("game.file.instruction"));
        fileSelectWindow.exitButton.setText(Game.GAME.getText("game.file.button.exit"));
        fileSelectWindow.openButton.setText(Game.GAME.getText("game.file.button.open"));
        fileSelectWindow.createButton.setText(Game.GAME.getText("game.file.button.create"));
        fileSelectWindow.deleteButton.setText(Game.GAME.getText("game.file.button.delete"));
//        fileSelectWindow.settingsButton.setText(Game.GAME.getText("game.file.button.settings"));

        ((DefaultListModel<String>) fileSelectWindow.fileList.getModel()).removeAllElements();
        for (String name : saveFile.listSaves()) {
            ((DefaultListModel<String>) fileSelectWindow.fileList.getModel()).addElement(name);
        }

        Game.GAME.getSoundSystem().loopBackgroundMusic("game.file.theme", -1);
//        Game.GAME.getSoundSystem().loopBackgroundMusic("game.midi.test", -1);
//        Game.GAME.getSoundSystem().playInstancedAudio("game.midi.test");
    }

    public void onFileCreate() {
        String fileName = JOptionPane.showInputDialog(fileSelectWindow, Game.GAME.getText("game.file.create.title"));
        if (fileName != null) {
            if (saveFile.doesSaveExist(fileName)) {
                JOptionPane.showMessageDialog(fileSelectWindow, Game.GAME.getText("game.file.create.exists"), Game.GAME.getText("game.title"), JOptionPane.ERROR_MESSAGE, null);
            } else {
                ((DefaultListModel<String>) fileSelectWindow.fileList.getModel()).addElement(fileName);
            }
        }
    }

    private void onFileOpen() {
        String fileName = fileSelectWindow.fileList.getSelectedValue();
        if (fileName != null) {
            if (saveFile.doesSaveExist(fileName)) {
                SaveFile.SaveFileRecord record = saveFile.loadSave(fileName);
//                char[] password = saveFile.getLastEnteredPassword();
                Game.GAME.loadSave(record);
            } else {
                Game.GAME.createSave(fileName);
            }
            Game.GAME.getSoundSystem().loopBackgroundMusic(null, 0);
            fileSelectWindow.dispose();
            Game.GAME.startGame();
        }
    }

    private void onFileDelete() {
        String fileName = fileSelectWindow.fileList.getSelectedValue();
        if (fileName != null) {
            if (JOptionPane.showConfirmDialog(fileSelectWindow, Game.GAME.getText("game.file.delete.confirm"), Game.GAME.getText("game.file.delete.confirm.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_NO_OPTION) {
                if (saveFile.doesSaveExist(fileName)) {
                    saveFile.deleteSave(fileName);
                }
                ((DefaultListModel<String>) fileSelectWindow.fileList.getModel()).removeElement(fileName);
                Game.GAME.getSoundSystem().playInstancedAudio("game.file.delete");
            }
        } else {
            JOptionPane.showMessageDialog(fileSelectWindow, Game.GAME.getText("game.file.delete.not-exist"), Game.GAME.getText("game.title"), JOptionPane.ERROR_MESSAGE, null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(fileSelectWindow.exitButton)) {
            System.exit(0);
        } else if (e.getSource().equals(fileSelectWindow.createButton)) {
            onFileCreate();
        } else if (e.getSource().equals(fileSelectWindow.openButton)) {
            onFileOpen();
        } else if (e.getSource().equals(fileSelectWindow.deleteButton)) {
            onFileDelete();
        }
    }
}
