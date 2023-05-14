package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;

public class Farm extends Item implements Serializable {
    private transient float timeToClick;

    @Override
    public void update(Game game, float delta, int instances) {
        timeToClick += delta;
        int clicks = (int) (timeToClick * 90);
        timeToClick -= clicks / 90f;

        game.addClicks(clicks * instances);
    }
}
