package com.moqifei.bdd.jupiter.generate.util;

import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;


public class RandomUtil {

    private RandomUtil() {
    }

    public static final String randomStr(int len) {
        String tmpStr = UUID.randomUUID().toString().replace("-", "");
        if (len > tmpStr.length()) {
            return tmpStr.substring(0, tmpStr.length());
        }
        return tmpStr.substring(0, len);
    }

    public static final long randomLong() {
        return RandomUtils.nextLong(0, Long.MAX_VALUE);
    }

    /**
     * @param max must >= 0
     * @return
     */
    public static final long randomLong(long max) {
        return RandomUtils.nextLong(0, max);
    }

    public static final int randomInt() {
        return RandomUtils.nextInt(0, Integer.MAX_VALUE);
    }

    /**
     * @param max must >= 0
     * @return
     */
    public static final int randomInt(int max) {
        return RandomUtils.nextInt(0, max);
    }

    public static final double randomDouble() {
        return RandomUtils.nextDouble(0d, Double.MAX_VALUE);
    }

    /**
     * @param max must >= 0
     * @return
     */
    public static final double randomDouble(double max) {
        return RandomUtils.nextDouble(0d, max);
    }
}
