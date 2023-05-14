package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Item implements Serializable {
    private final AtomicInteger instances = new AtomicInteger();
    private final AtomicInteger sold = new AtomicInteger();

    public void onBought(Game game) {}
    public abstract void update(Game game, float delta, int instances);

    public int getInstances() {
        return instances.get();
    }

    public void setInstances(int instances) {
        this.instances.set(instances);
    }

    public void addInstance() {
        instances.incrementAndGet();
    }

    public void removeInstance() {
        instances.decrementAndGet();
    }

    public int getNumberSold() {
        return sold.get();
    }

    public void setNumberSold(int sold) {
        this.sold.set(sold);
    }

    public void addNumberSold() {
        sold.incrementAndGet();
    }

    public void removeNumberSold() {
        sold.decrementAndGet();
    }
}
