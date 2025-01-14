package com.monopoly;

public class Dice {

    public static int roll() {
        return (int) (Math.random() * 4) + 1;
    }

}
