package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;

public class Milk extends Item implements Serializable {
    private transient float timeToClick;

    @Override
    public void update(Game game, float delta, int instances) {
        timeToClick += delta;
        int clicks = (int) (timeToClick * 25);
        timeToClick -= clicks / 25f;

        game.addClicks(clicks * instances);
    }
}
