package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;

public class Factory extends Item implements Serializable {
    private transient float timeToClick;

    @Override
    public void update(Game game, float delta, int instances) {
        timeToClick += delta;
        int clicks = (int) (timeToClick * 75);
        timeToClick -= clicks / 75f;

        game.addClicks(clicks * instances);
    }
}
