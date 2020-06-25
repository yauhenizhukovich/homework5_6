package com.gmail.supersonicleader.util;

import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static int getElement(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static boolean getRandomBooleanValue() {
        switch (RandomUtil.getElement(0, 1)) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new IllegalArgumentException("Wrong value");
        }
    }

}
