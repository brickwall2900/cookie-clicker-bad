package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;

public class NuclearBomb extends Item implements Serializable {
    private float timeLeft = 30;
    private int lastTick = 30;

    @Override
    public void onBought(Game game) {
        game.getSoundSystem().playInstancedAudio("game.nuke.voice");
    }

    @Override
    public void update(Game game, float delta, int instances) {
        int rounded = Math.round(timeLeft);
        if (rounded < lastTick) {
            lastTick = rounded;
            game.getSoundSystem().playInstancedAudio("game.nuke.beep");
        }
        timeLeft -= delta;
        if (timeLeft <= 0) {
            game.getSoundSystem().playInstancedAudio("game.nuke.explode");
//            for (int i = 0; i < 8000; i++, game.click());
            game.addClicks(8000);
            removeInstance();
            timeLeft = 30;
            lastTick = 30;
        }
    }
}
