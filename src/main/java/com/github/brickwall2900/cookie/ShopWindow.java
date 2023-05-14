
package com.github.brickwall2900.cookie;

import java.awt.Dimension;
import javax.swing.*;
import java.util.Objects;

public class ShopWindow extends javax.swing.JDialog {
    public ShopWindow() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new JLabel();
        topSeparator = new JSeparator();
        listScrollPane = new JScrollPane();
        shopList = new JList<>();
        bottomSeparator = new JSeparator();
        closeButton = new JButton();
        buyButton = new JButton();
        sellButton = new JButton();
        quantity = new JSpinner();
        quantityLabel = new JLabel();
        description = new JLabel();
        buySellAmount = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Wal Shop");
        setMinimumSize(new Dimension(418, 321));

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setIcon(new ImageIcon(getClass().getResource("/coin.png"))); // NOI18N
        title.setText("Which item would you like to buy?");

        shopList.setModel(new DefaultListModel<String>());
        shopList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shopList.setLayoutOrientation(JList.VERTICAL_WRAP);
        listScrollPane.setViewportView(shopList);

        closeButton.setText("Close");

        buyButton.setText("Buy");
        buyButton.setEnabled(false);

        sellButton.setText("Sell");
        sellButton.setToolTipText("");
        sellButton.setEnabled(false);

        quantity.setModel(new SpinnerNumberModel(1, 0, 10000, 1));
        quantity.setEnabled(false);

        quantityLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        quantityLabel.setText("Quantity");
        quantityLabel.setToolTipText("");

        description.setText("Description of an item.");

        buySellAmount.setText("Cost: 0 clicks; Sell: 0 clicks; 0 instances of this item.");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(title, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(topSeparator)
                    .addComponent(listScrollPane)
                    .addComponent(bottomSeparator)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buyButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sellButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quantityLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quantity, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(closeButton))
                    .addComponent(description, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buySellAmount, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(topSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listScrollPane, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(description)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buySellAmount)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(bottomSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(buyButton)
                    .addComponent(sellButton)
                    .addComponent(quantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantityLabel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JSeparator bottomSeparator;
    public JButton buyButton;
    public JLabel buySellAmount;
    public JButton closeButton;
    public JLabel description;
    public JScrollPane listScrollPane;
    public JSpinner quantity;
    public JLabel quantityLabel;
    public JButton sellButton;
    public JList<String> shopList;
    public JLabel title;
    public JSeparator topSeparator;
    // End of variables declaration//GEN-END:variables

}
