package com.monopoly.propertyType;

import java.util.Scanner;

import com.monopoly.Player;

public class Go extends Property {

    private int goMoney = 1500;
    private final static String name = "Go";

    public Go(int position) {
        super(position, name);
    }

    public Go(int position, int goMoney) {
        super(position, name);
        this.goMoney = goMoney;
    }

    public int getGoMoney() {
        return goMoney;
    }

    public void setGoMoney(int goMoney) {
        this.goMoney = goMoney;
    }

    @Override
    public void action(Player player) {
        System.out.println("You pass " + name + ". You won $" + getGoMoney() + ".");
        player.setMoney("+", getGoMoney());
        System.out.println("You now have $" + player.getMoney() + ".");
    }

    @Override
    public void editProperty() {
        try {
            System.out.println("The current amount of money awarded for passing " + name + " is: " + getGoMoney());
            Scanner sc = new Scanner(System.in);

            System.out.println("Enter the new amount of money awarded for passing Go: ");
            String goMoney = sc.nextLine();
            setGoMoney(Integer.parseInt(goMoney));
            System.out.println("The new amount of money awarded for passing " + name + " is: " + getGoMoney());

        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            editProperty();
        }
    }

}
