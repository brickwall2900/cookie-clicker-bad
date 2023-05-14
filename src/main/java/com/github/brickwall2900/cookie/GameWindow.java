
package com.github.brickwall2900.cookie;

import com.github.brickwall2900.cookie.items.ItemDescription;

import java.awt.*;
import javax.swing.*;
import java.io.Serial;
import javax.swing.table.DefaultTableModel;

public class GameWindow extends javax.swing.JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    public GameWindow() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new JSplitPane();
        canvasPanel = new JPanel();
        menuPanel = new JPanel();
        shopButton = new JButton();
        saveButton = new JButton();
        settingsButton = new JButton();
        closeButton = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cookie Clicker but bad");

        splitPane.setDividerLocation(725);
        splitPane.setDividerSize(10);
        splitPane.setPreferredSize(new Dimension(854, 480));

        canvasPanel.setBorder(BorderFactory.createTitledBorder("Cookie"));
        canvasPanel.setPreferredSize(new Dimension(668, 480));
        canvasPanel.setLayout(new BorderLayout());
        canvasPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        splitPane.setLeftComponent(canvasPanel);

        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));

        shopButton.setText("Shop");

        saveButton.setText("Save");

        settingsButton.setText("Settings");

        closeButton.setText("Close");

        GroupLayout menuPanelLayout = new GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(menuPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(shopButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveButton, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(settingsButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(closeButton, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        menuPanelLayout.setVerticalGroup(menuPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shopButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 345, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addContainerGap())
        );

        splitPane.setRightComponent(menuPanel);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JPanel canvasPanel;
    public JButton closeButton;
    public JPanel menuPanel;
    public JButton saveButton;
    public JButton settingsButton;
    public JButton shopButton;
    public JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables

}
