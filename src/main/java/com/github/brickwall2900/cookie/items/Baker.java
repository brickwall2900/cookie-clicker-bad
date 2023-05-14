package com.github.brickwall2900.cookie.items;

import com.github.brickwall2900.cookie.Game;

import java.io.Serializable;

public class Baker extends Item implements Serializable {
    private transient float work = 0f;
    private transient boolean doWork = true;
    private transient float nextClick = 0f;

    @Override
    public void update(Game game, float delta, int instances) {
        if (work > 0) {
            work -= delta;

            if (doWork) {
                if (nextClick > 0) {
                    nextClick -= delta;
                } else {
                    nextClick = game.getRandom().nextFloat(0.125f, 0.5f);
                    game.addClicks(instances);
                }
            }
        } else {
            work = game.getRandom().nextFloat(6f);
            doWork = !doWork;
        }
//        work -= delta * instances;
//        if (work > 0) {
//            if (doWork && nextClick <= 0) {
//                nextClick = game.getRandom().nextFloat(WORK_TIME_MIN, WORK_TIME_MAX);
//                game.click();
//            }
//            nextClick -= delta * instances;
//        } else {
//            work = game.getRandom().nextFloat(6f);
//            doWork = !doWork;
//        }
    }
}
