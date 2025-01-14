package com.monopoly;

import com.monopoly.propertyType.District;
import com.monopoly.propertyType.Property;
import java.util.ArrayList;

public class Player {
    private String name;
    private float money = 1500;
    private ArrayList<District> districts = new ArrayList<>();
    private Property currentSquare = null;
    private boolean inJail = false;
    private int jailTurns = 0;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(String op, float money) {
        if (op.equals("+")) {
            this.money += money;
        } else if (op.equals("-")) {
            this.money -= money;
        } else if (op.equals("*")) {
            this.money *= money;
        } else if (op.equals("/")) {
            this.money /= money;
        } else if (op.equals("=")) {
            this.money = money;
        } else {
            System.out.println("Invalid operation");
        }
    }

    public ArrayList<District> getProperty() {
        return districts;
    }

    public void addProperty(District property) {
        this.districts.add(property);
        property.setOwner(this);
    }

    public Property getCurrentSquare() {
        return currentSquare;
    }

    public void setCurrentSquare(Property newSquare) {
        if (this.currentSquare != null)
            this.currentSquare.removeHavePlayer(this);
        this.currentSquare = newSquare;
        this.currentSquare.addHavePlayer(this);
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public int getJailTurns() {
        return jailTurns;
    }

    public void setJailTurns(int jailTurns) {
        this.jailTurns = jailTurns;
    }

    public void addJailTurns() {
        this.jailTurns += 1;
    }

    public void bankrupt() {
        for (District district : districts) {
            district.setOwner(null);
        }
        districts.clear();
    }

    public void printProperties() {
        System.out.println("Properties of " + getName() + ":");
        for (District district : districts) {
            System.out.println(district.getName());
        }
    }

    public void printStatus() {
        System.out.println("Name: " + getName());
        System.out.println("Current Square: " + getCurrentSquare().getName());
        printProperties();
    }

    public void printStatusWithMoney() {
        System.out.println("Name: " + getName());
        System.out.println("Money: " + getMoney());
        System.out.println("Current Square: " + getCurrentSquare().getName());
        printProperties();
    }

    public void move(int num) {
        if (num > 0) {
            int idxOfNextSquare = this.currentSquare.getPosition() + num;
            if (idxOfNextSquare > Board.getProperties().size()) {
                idxOfNextSquare = idxOfNextSquare % Board.getProperties().size();
            }
            for (int i = 0; i < Board.getProperties().size(); i++) {
                if (Board.getProperties().get(i).getPosition() == idxOfNextSquare) {
                    Property nextSquare = Board.getProperties().get(i);
                    setCurrentSquare(nextSquare);
                    break;
                }
            }
            System.out.println(getName() + " moved to " + getCurrentSquare().getName());
        }
        getCurrentSquare().action(this);
    }
}
