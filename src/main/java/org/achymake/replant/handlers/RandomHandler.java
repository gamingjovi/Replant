package org.achymake.replant.handlers;

import java.util.Random;

public class RandomHandler {
    private Random getRandom() {
        return new Random();
    }
    private double getRandomDouble() {
        return 1.0 - getRandom().nextDouble(1.0);
    }
    private double getRandomDouble(double random) {
        return getRandom().nextDouble(0.01, random);
    }
    public boolean isTrue(double chance) {
        return chance >= getRandomDouble();
    }
    public boolean isTrue(double chance, double random) {
        return chance >= getRandomDouble(random);
    }
    public double nextDouble(double origin, double bound) {
        return getRandom().nextDouble(origin, bound);
    }
    public int nextInt(int origin, int bound) {
        return getRandom().nextInt(origin, bound);
    }
}