package com.monopoly.propertyType;

import java.util.Scanner;

import com.monopoly.Player;

public class Chance extends Property {

    private int addValue = 20;
    private int subtractValue = 30;
    private final static String name = "Chance";

    public Chance(int position) {
        super(position, name);
    }

    public Chance(int position, int addValue, int subtractValue) {
        super(position, name);
        this.addValue = addValue;
        this.subtractValue = subtractValue;
    }

    public int getAddValue() {
        return addValue;
    }

    public void setAddValue(int addValue) {
        this.addValue = addValue;
    }

    public int getSubtractValue() {
        return subtractValue;
    }

    public void setSubtractValue(int subtractValue) {
        this.subtractValue = subtractValue;
    }

    @Override
    public void action(Player player) {
        int chance = (int) (Math.random() * 2) + 1;
        int value;
        if (chance == 1) {
            value = (int) (Math.random() * addValue) + 1;
            value *= 10;
            System.out.println("You won $" + value + ".");
            player.setMoney("+", value);
        } else {
            value = (int) (Math.random() * subtractValue) + 1;
            value *= 10;
            System.out.println("You lost $" + value + ".");
            player.setMoney("-", value);
        }
        System.out.println("You now have $" + player.getMoney() + ".");
    }

    @Override
    public void editProperty() {
            Scanner sc = new Scanner(System.in);
            System.out.println(this.getName() + " has two values for adding and subtracting money.");
            System.out.println("The current value for adding money is: " + this.getAddValue());
            System.out.println("The current value for subtracting money is: " + this.getSubtractValue());
            System.out.println("""
                    Press 1: To edit the value for adding money.
                    Press 2: To edit the value for subtracting money.""");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Enter the new value for adding money:");
                    String addValue = sc.nextLine();
                    this.addValue = Integer.parseInt(addValue);
                    break;
                case "2":
                    System.out.println("Enter the new value for subtracting money:");
                    String subtractValue = sc.nextLine();
                    this.subtractValue = Integer.parseInt(subtractValue);
                    break;
                default:
                    System.out.println("Please enter a valid number.");
            }
    }
}
