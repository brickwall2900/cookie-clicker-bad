package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;

public class Worker extends Item implements Serializable {
    private transient float work = 0;
    private transient boolean doWork = true;
    private transient float nextClick;

    @Override
    public void update(Game game, float delta, int instances) {
        if (work > 0) {
            work -= delta;

            if (doWork) {
                if (nextClick > 0) {
                    nextClick -= delta;
                } else {
                    nextClick = game.getRandom().nextFloat(0.67f, 1);
                    game.addClicks(instances);
                }
            }
        } else {
            work = game.getRandom().nextFloat(8f);
            doWork = !doWork;
        }
    }
}
