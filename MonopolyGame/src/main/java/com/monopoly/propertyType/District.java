package com.monopoly.propertyType;

import java.util.Scanner;

import com.monopoly.Board;
import com.monopoly.Player;

public class District extends Property {
    private int price;
    private int rent;
    private Player owner = null;

    public District(int position, String name, int price, int rent) {
        super(position, name);
        this.price = price;
        this.rent = rent;
    }

    public District(int position) {
        super(position, "null");
    }

    @Override
    public void action(Player player) {
        if (getOwner() != null) {
            if (getOwner() == player) {
                System.out.println("You already own this property.");
            } else {
                System.out.println("You have to pay $" + getRent() + " as rent to "
                        + getOwner().getName());
                player.setMoney("-", getRent());
                getOwner().setMoney("+", getRent());
                System.out.println("You now have $" + player.getMoney() + ".");
            }
        } else {
            System.out.println("This property is cost $" + getPrice() + ".");
            if (player.getMoney() < getPrice()) {
                System.out.println("You don't have enough money to buy this property.");
                return;
            }
            System.out.println("Do you want to buy this property?");
            while (true) {
                System.out.println("Press 1: To buy.");
                System.out.println("Press 2: Not to buy.");
                Scanner sc = new Scanner(System.in);
                int choice = sc.nextInt();

                if (choice == 1) {
                    player.setMoney("-", getPrice());
                    player.addProperty(this);
                    System.out.println("You bought " + getName() + ".");
                    System.out.println("You now have $" + player.getMoney() + ".");
                    break;
                } else if (choice == 2) {
                    System.out.println("You chose not to buy " + getName() + ".");
                    break;
                }
            }
        }
    }

    public int getPrice() {
        return this.price;
    }

    public int getRent() {
        return this.rent;
    }

    public Player getOwner() {
        return this.owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setPrice(int money) {
        this.price = money;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public District createDistrict() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the name of the district: ");
            this.setName(sc.nextLine());
            System.out.println("Enter the price of the district: ");
            this.setPrice(Integer.parseInt(sc.nextLine()));
            System.out.println("Enter the rent of the district: ");
            this.setRent(Integer.parseInt(sc.nextLine()));
            System.out.println("District created successfully.");

        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a correct value.");
            createDistrict();
            return null;
        }
        return this;
    }

    @Override
    public void editProperty() {
        System.out.println("District: " + this);

            Scanner sc = new Scanner(System.in);
            System.out.println("""
                    Press 1: To edit name.
                    Press 2: To edit position.
                    Press 3: To edit price.
                    Press 4: To edit rent.
                    Press 5: To exit.
                    Enter:""");
            String choice = sc.nextLine();
        try {
            switch (choice) {
                case "1" -> {
                    System.out.println("Enter the new name of the district: ");
                    this.setName(sc.nextLine());
                }
                case "2" -> {
                    System.out.println("""
                            Enter the new position of the district.
                            If you want to place after current position of property 5, enter 5.
                            Enter: """);
                        Board.removeProperty(this.getPosition());
                        int pos = Integer.parseInt(sc.nextLine());
                        if (pos < this.getPosition()) {
                            Board.insertProperty(this, pos);
                        }
                        else {
                            Board.insertProperty(this, pos - 1);
                        }
                }
                case "3" -> {
                    System.out.println("Enter the new price of the district: ");
                    this.setPrice(sc.nextInt());
                }
                case "4" -> {
                    System.out.println("Enter the new rent of the district: ");
                    this.setRent(sc.nextInt());
                }
                case "5" -> {
                    return;
                }
                default -> {
                    System.out.println("Invalid input. Please enter a correct number.");
                    editProperty();
                }
            }
            System.out.println("District updated successfully.");
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a correct number.");
            editProperty();
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("[position= " + super.getPosition() + ", name= " + super.getName() + ", price= " + price
                + ", rent= " + rent);
        if (owner != null)
            sb.append(", owner= " + owner.getName());
        if (super.getHavePlayer().size() > 0) {
            sb.append(", Have player= [");
            for (int i = 0; i < super.getHavePlayer().size(); i++) {
                sb.append(super.getHavePlayer().get(i).getName());
                if (i != super.getHavePlayer().size() - 1)
                    sb.append(", ");
            }
            sb.append("]");
        }
        sb.append("]");
        return sb.toString();
    }

    public String toStringWithoutPosition() {
        StringBuffer sb = new StringBuffer();
        sb.append("[name= " + super.getName() + ", price= " + price + ", rent= " + rent);
        if (owner != null)
            sb.append(", owner= " + owner.getName());
        if (super.getHavePlayer().size() > 0) {
            sb.append(", Have player= [");
            for (int i = 0; i < super.getHavePlayer().size(); i++) {
                sb.append(super.getHavePlayer().get(i).getName());
                if (i != super.getHavePlayer().size() - 1)
                    sb.append(", ");
            }
            sb.append("]");
        }
        sb.append("]");
        return sb.toString();
    }

}