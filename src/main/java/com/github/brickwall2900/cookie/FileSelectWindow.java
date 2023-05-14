
package com.github.brickwall2900.cookie;

import java.awt.Color;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class FileSelectWindow extends javax.swing.JFrame {
    public FileSelectWindow() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new JLabel();
        instruction = new JLabel();
        listScroll = new JScrollPane();
        fileList = new JList<>();
        openButton = new JButton();
        createButton = new JButton();
        deleteButton = new JButton();
        credit = new JLabel();
        exitButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("File Select");

        title.setIcon(new ImageIcon(getClass().getResource("/wizard.png"))); // NOI18N
        title.setText("Welcome toCookie Clliker!");
        title.setToolTipText("");

        instruction.setText(" Please select a file on the lsit or crate a new file to start your cookie click adventure");

        fileList.setModel(new DefaultListModel<>());
        listScroll.setViewportView(fileList);

        openButton.setText("Open");

        createButton.setText("Create");

        deleteButton.setForeground(new Color(51, 255, 51));
        deleteButton.setText("Delete");

        credit.setText("(C) (TM) (R) brickwall2900 2023");

        String html = """
                <html>
                    <body>
                        <!-- We are using rudimentary HTML here to render our tooltips. -->
                        <p>Version 1.0</p>
                        <p>This is a <b>debug</b> build.</p>
                        <br>
                        <p>This is a satire game. Don't take seriously.</p>
                        <p>Made by <strong>brickwall2900</strong></p>
                        <p>How did this take me 3 weeks???</p>
                        <br>
                        <p>Thanks for <b>https://www.khinsider.com/midi/n64/super-mario-64</b> and <b>https://themushroomkingdom.net/media/sm64/mid</b></p>
                        <p>for the Super Mario 64 MIDIs!</p>
                        <p>Thank you YouTube for some music.</p>
                    </body>
                </html>
                """;

        credit.setToolTipText(html);

        exitButton.setText("> Exit <");
        exitButton.setToolTipText("Please leave");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(listScroll)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(openButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(credit))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(instruction)
                            .addComponent(title))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addComponent(exitButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(title, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(instruction))
                    .addComponent(exitButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listScroll)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(openButton)
                    .addComponent(createButton)
                    .addComponent(deleteButton)
                    .addComponent(credit))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JButton createButton;
    public JLabel credit;
    public JButton deleteButton;
    public JButton exitButton;
    public JList<String> fileList;
    public JLabel instruction;
    public JScrollPane listScroll;
    public JButton openButton;
    public JLabel title;
    // End of variables declaration//GEN-END:variables

}
