package com.monopoly.propertyType;

import com.monopoly.Player;

public class FreeParking extends Property {

    private final static String name = "Free Parking";

    public FreeParking(int position) {
        super(position, name);
    }

    @Override
    public void action(Player player) {
        System.out.println("You landed on " + name + ".\nYou don't have to do anything.");
    }

    @Override
    public void editProperty() {
    }

}
