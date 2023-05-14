package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;

public class BlackHole extends Item implements Serializable {
    private transient float timeToClick;

    @Override
    public void update(Game game, float delta, int instances) {
        timeToClick += delta;
        int clicks = (int) (timeToClick * 250);
        timeToClick -= clicks / 250f;

        game.addClicks(clicks * instances);
    }
}
