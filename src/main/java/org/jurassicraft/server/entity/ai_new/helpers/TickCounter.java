package org.jurassicraft.server.entity.ai_new.helpers;

import java.util.Random;

public class TickCounter {

    private long age = 0;
    private long lastTick = -1;
    //private long lastTickDif = -1;

    private Random random;
    private int randomSpread = 3;
    private int wait = 50;
    private boolean randomize = true;

    //public long getLastTickDif()
//    {
//        return lastTickDif;
//    }
    public int getWaitTicks() { return wait; }

    public TickCounter(Random random, boolean randomize)
    {
        this.random = random;
        this.randomize = randomize;
    }

    public int getTicks(long currentTick)
    {
        if (lastTick < 0)
            lastTick = currentTick;

        long dif = currentTick - lastTick;

//        if (Math.abs(dif) > wait + randomSpread)
//            dif = wait + randomSpread;

        //lastTickDif = dif;

        int tickCount = 0;
        long tLastTick = lastTick;
        while (dif >= wait) {
            if (dif >= wait) {
                if (randomize)
                    tLastTick = tLastTick + wait + (random.nextInt(randomSpread * 2) - randomSpread);
                else
                    tLastTick = tLastTick + wait;

                tickCount++;
            }

            dif = currentTick - tLastTick;
        }

        if (tickCount == 0)
            return 0;

        long timeElapsed = tickCount * wait;

        age += timeElapsed;
        lastTick += timeElapsed;

        return tickCount;
    }
}
