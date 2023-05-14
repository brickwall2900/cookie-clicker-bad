package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;

public class Universe extends Item implements Serializable {
    private transient float timeToClick;

    @Override
    public void update(Game game, float delta, int instances) {
        timeToClick += delta;
        int clicks = (int) (timeToClick * 333);
        timeToClick -= clicks / 333f;

        game.addClicks(clicks * instances);
    }
}
