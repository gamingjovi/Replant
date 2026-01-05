package org.achymake.replant.handlers;

import java.util.Random;

public class RandomHandler {
    private Random getRandom() {
        return new Random();
    }
    private double getRandomDouble() {
        return nextDouble(0, 1);
    }
    public boolean isTrue(double chance) {
        return chance >= getRandomDouble();
    }
    public double nextDouble(double origin, double bound) {
        return getRandom().nextDouble(origin, bound);
    }
    public int nextInt(int origin, int bound) {
        return getRandom().nextInt(origin, bound);
    }
}