package com.github.brickwall2900.cookie;

import com.github.brickwall2900.cookie.items.ItemDescription;

import javax.swing.*;

public class ItemList<T> extends JList<ItemDescription> {
    public ItemList() {
        super(new DefaultListModel<>());
    }
}
