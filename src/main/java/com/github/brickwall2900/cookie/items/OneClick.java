package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;

public class OneClick extends Item implements Serializable {
    @Override
    public void onBought(Game game) { }

    @Override
    public void update(Game game, float delta, int instances) {
        removeInstance();
    }
}
