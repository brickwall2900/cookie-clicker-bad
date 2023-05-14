
package com.github.brickwall2900.cookie;

import java.io.Serial;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class SettingsWindow extends javax.swing.JDialog {
    public SettingsWindow() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new JLabel();
        separator = new JSeparator();
        fpsLimitLabel = new JLabel();
        fpsLimitField = new JTextField();
        graphicsQualityLabel = new JLabel();
        autoSaveNotifications = new JCheckBox();
        autoSaveTimerLabel = new JLabel();
        autoSaveTimerField = new JTextField();
        debugCounter = new JCheckBox();
        themeLabel = new JLabel();
        themeBox = new JComboBox<>();
        cancelButton = new JButton();
        applyButton = new JButton();
        okButton = new JButton();
        progressBar = new JProgressBar();
        graphicsSlider = new JSlider();
        graphicsDescription = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Settings");

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setIcon(new ImageIcon(getClass().getResource("/settings.png"))); // NOI18N
        title.setText("Settings");

        fpsLimitLabel.setText("FPS Limit:");

        graphicsQualityLabel.setText("Graphics Quality:");

        autoSaveNotifications.setText("Auto Save Notifications");

        autoSaveTimerLabel.setText("Auto Save Timer:");

        autoSaveTimerField.setToolTipText("");

        debugCounter.setText("Enable Debug Counter (MIGHT DESTROY PC!!)");

        themeLabel.setText("Theme:");

        themeBox.setModel(new DefaultComboBoxModel<>());

        cancelButton.setText("Cancel");

        applyButton.setText("Apply");

        okButton.setText("OK");

        graphicsSlider.setMaximum(10);
        graphicsSlider.setSnapToTicks(true);

        graphicsDescription.setText("0 (???)");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(title, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(separator)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applyButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(fpsLimitLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fpsLimitField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addComponent(autoSaveNotifications)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(autoSaveTimerLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(autoSaveTimerField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addComponent(debugCounter)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(themeLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(themeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(graphicsQualityLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(graphicsSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(graphicsDescription, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(fpsLimitLabel)
                    .addComponent(fpsLimitField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(graphicsQualityLabel)
                    .addComponent(graphicsSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(graphicsDescription))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autoSaveNotifications)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(autoSaveTimerLabel)
                    .addComponent(autoSaveTimerField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(debugCounter)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(themeLabel)
                    .addComponent(themeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(applyButton)
                        .addComponent(okButton)
                        .addComponent(cancelButton))
                    .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JButton applyButton;
    public JCheckBox autoSaveNotifications;
    public JTextField autoSaveTimerField;
    public JLabel autoSaveTimerLabel;
    public JButton cancelButton;
    public JCheckBox debugCounter;
    public JTextField fpsLimitField;
    public JLabel fpsLimitLabel;
    public JLabel graphicsDescription;
    public JLabel graphicsQualityLabel;
    public JSlider graphicsSlider;
    public JButton okButton;
    public JProgressBar progressBar;
    public JSeparator separator;
    public JComboBox<String> themeBox;
    public JLabel themeLabel;
    public JLabel title;
    // End of variables declaration//GEN-END:variables

}
