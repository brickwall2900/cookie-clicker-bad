package com.github.brickwall2900.cookie;

import com.github.brickwall2900.cookie.items.ItemDescription;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

public class ShopController implements ActionListener, ListSelectionListener, ChangeListener {
    private final ShopWindow shopWindow;

    public ShopController(ShopWindow shopWindow) {
        this.shopWindow = shopWindow;
        shopWindow.closeButton.addActionListener(this);
        shopWindow.buyButton.addActionListener(this);
        shopWindow.sellButton.addActionListener(this);
        shopWindow.shopList.addListSelectionListener(this);
        shopWindow.quantity.addChangeListener(this);
    }

    public void manageStock(Set<ItemDescription> items) {
        DefaultListModel<String> model = ((DefaultListModel<String>) shopWindow.shopList.getModel());
        List<String> names = items.stream().sorted(Comparator.comparingInt(ItemDescription::getId)).map(ItemDescription::getName).collect(Collectors.toList());
        Set<String> testSet = new HashSet<>();
        for (String name : names)
            if (!testSet.add(name)) System.err.println("WARNING: " + name + " has duplicate entries!");
        testSet.clear();
        model.addAll(names);
    }

    public void onBuy() {
        String itemName = shopWindow.shopList.getSelectedValue();
        if (itemName != null) {
            int quantity = (int) shopWindow.quantity.getValue();
            ItemDescription itemDescription = Game.GAME.getItemDescription(itemName);
            Game.ShopError shopError = Game.GAME.buyItem(itemDescription, quantity);
            if (!shopError.equals(Game.ShopError.SUCCESS)) {
                JOptionPane.showMessageDialog(shopWindow,
                        shopError.getText(),
                        Game.GAME.getText("game.shop.window.title"),
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void onSell() {
        String itemName = shopWindow.shopList.getSelectedValue();
        if (itemName != null) {
            int quantity = (int) shopWindow.quantity.getValue();
            ItemDescription itemDescription = Game.GAME.getItemDescription(itemName);
            Game.ShopError shopError = Game.GAME.sellItem(itemDescription, quantity);
            if (!shopError.equals(Game.ShopError.SUCCESS)) {
                JOptionPane.showMessageDialog(shopWindow,
                        shopError.getText(),
                        Game.GAME.getText("game.shop.window.title"),
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(shopWindow.closeButton)) {
            shopWindow.dispose();
        } else if (e.getSource().equals(shopWindow.buyButton)) {
            onBuy();
        } else if (e.getSource().equals(shopWindow.sellButton)) {
            onSell();
        }
        update();
    }

    public void update() {
        String itemName = shopWindow.shopList.getSelectedValue();
        if (itemName != null) {
            int quantity = (int) shopWindow.quantity.getValue();
            ItemDescription itemDescription = Game.GAME.getItemDescription(itemName);
            String text = Game.GAME.getText("game.buyStatus.costText", itemDescription.getBuyCost() * quantity, itemDescription.getSellCost() * quantity, Game.GAME.getItemInstances(itemDescription));
            shopWindow.description.setText(itemDescription.getDescription());
            shopWindow.buyButton.setEnabled(Game.GAME.canBuyItem(itemDescription, quantity));
            shopWindow.sellButton.setEnabled(Game.GAME.canSellItem(itemDescription, quantity));
            shopWindow.quantity.setEnabled(true);
            shopWindow.buySellAmount.setText(text);
        } else {
            shopWindow.description.setText(Game.GAME.getText("game.item.null.desc"));
            shopWindow.buyButton.setEnabled(false);
            shopWindow.sellButton.setEnabled(false);
            shopWindow.quantity.setEnabled(false);
            shopWindow.buySellAmount.setText("...");
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        update();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        update();
    }

    public void initText() {
        shopWindow.setTitle(Game.GAME.getText("game.shop.window.title"));
        shopWindow.buyButton.setText(Game.GAME.getText("game.shop.button.buy"));
        shopWindow.sellButton.setText(Game.GAME.getText("game.shop.button.sell"));
        shopWindow.quantityLabel.setText(Game.GAME.getText("game.shop.button.quantity"));
        shopWindow.closeButton.setText(Game.GAME.getText("game.shop.button.close"));
        shopWindow.title.setText(Game.GAME.getText("game.shop.title"));
    }
}
