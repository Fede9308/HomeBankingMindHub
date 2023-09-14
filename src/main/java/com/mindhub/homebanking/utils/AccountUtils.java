package com.mindhub.homebanking.utils;

public class AccountUtils {

    public static int getRandomNumber(int min, int max) {

        return (int) ((Math.random() * (max - min)) + min);

    }

    public static int getRandomAccountNumber() {
        return getRandomNumber(1, 9999);
    }

}
