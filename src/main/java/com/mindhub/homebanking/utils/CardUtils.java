package com.mindhub.homebanking.utils;

public class CardUtils {

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String getRandomCardNumber() {
        return String.format("%04d", getRandomNumber(1, 9999))
                                    + "-" + String.format("%04d", getRandomNumber(1, 9999))
                                    + "-" + String.format("%04d", getRandomNumber(1, 9999))
                                    + "-" + String.format("%04d", getRandomNumber(1, 9999));
    }

    public static int getRandomCardCvv() {
       return getRandomNumber(100, 999);
    }

}
