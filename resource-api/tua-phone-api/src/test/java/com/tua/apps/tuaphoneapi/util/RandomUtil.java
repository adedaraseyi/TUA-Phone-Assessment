package com.tua.apps.tuaphoneapi.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class RandomUtil {

    public static String randomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static int randomInt(int startInclusive, int endInclusive) {
        return RandomUtils.nextInt(startInclusive, endInclusive);
    }

    public static long randomLong(long startInclusive, long endInclusive) {
        return RandomUtils.nextLong(startInclusive, endInclusive);
    }

    public static String randomNumericString(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static <T> T random(List<T> list) {
        return list.get(randomInt(0, list.size() - 1));
    }
}
