package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class ItemDescription implements Serializable {
    private final Class<? extends Item> type;
    private final int id;
    private final String textId;
    private final boolean increasesDemand;
    private long buyCost;
    private long sellCost;
    private int maxInstances;

    public ItemDescription(int id, long buyCost, long sellCost, int maxInstances, String textId, Class<? extends Item> type) {
        this(id, buyCost, sellCost, maxInstances, textId, type, true);
    }

    public ItemDescription(int id, long buyCost, long sellCost, int maxInstances, String textId, Class<? extends Item> type, boolean increasesDemand) {
        this.id = id;
        this.buyCost = buyCost;
        this.sellCost = sellCost;
        this.maxInstances = maxInstances;
        this.textId = textId;
        this.type = type;
        this.increasesDemand = increasesDemand;
    }

    public long getBuyCost() {
        return buyCost;
    }

    public void setBuyCost(long buyCost) {
        this.buyCost = buyCost;
    }

    public long getSellCost() {
        return sellCost;
    }

    public void setSellCost(long sellCost) {
        this.sellCost = sellCost;
    }

    public int getMaxInstances() {
        return maxInstances;
    }

    public void setMaxInstances(int maxInstances) {
        this.maxInstances = maxInstances;
    }

    public String getName() {
        return Game.GAME.getText(textId);
    }

    public String getDescription() {
        return Game.GAME.getText(textId.concat(".desc"));
    }

    public int getId() {
        return id;
    }

    public Class<? extends Item> getType() {
        return type;
    }

    public boolean increasesDemand() {
        return increasesDemand;
    }

    public Item create() {
        try {
            return type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException ex) {
            throw new RuntimeException("error cloning an item!", ex);
        }
    }

    @Override
    public String toString() {
        return "ItemDescription{" +
                "type=" + type +
                ", id=" + id +
                ", textId='" + textId + '\'' +
                ", buyCost=" + buyCost +
                ", sellCost=" + sellCost +
                ", maxInstances=" + maxInstances +
                '}';
    }
}
