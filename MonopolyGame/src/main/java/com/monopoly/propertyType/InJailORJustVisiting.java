package com.monopoly.propertyType;

import java.util.Scanner;
import com.monopoly.Dice;
import com.monopoly.Player;

public class InJailORJustVisiting extends Property {
	private int costOfEscape = 150;
	private int turnsNeededToEscape = 3;
	private final static String name = "In Jail/Just Visiting";

	public int getCostOfEscape() {
		return costOfEscape;
	}

	public void setCostOfEscape(int costOfEscape) {
		this.costOfEscape = costOfEscape;
	}

	public int getTurnsNeededToEscape() {
		return turnsNeededToEscape;
	}

	public void setTurnsNeededToEscape(int turnsNeededToEscape) {
		this.turnsNeededToEscape = turnsNeededToEscape;
	}

	public InJailORJustVisiting(int position) {
		super(position, name);
	}

	public InJailORJustVisiting(int position, int costOfEscape, int turnsNeededToEscape) {
		super(position, name);
		this.costOfEscape = costOfEscape;
		this.turnsNeededToEscape = turnsNeededToEscape;
	}

	@Override
	public void editProperty() {
		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("The current cost of escape is: " + getCostOfEscape());
			System.out.println("The current number of turns needed to escape is: " + getTurnsNeededToEscape());
			System.out.println("""
					Press 1: To edit the cost of escape.
					Press 2: To edit the number of turns needed to escape.
					Enter: """);
			String choice = sc.nextLine();
			if (choice.equals("1")) {
				System.out.println("Enter the cost of escape: ");
				String costOfEscape = sc.nextLine();
				setCostOfEscape(Integer.parseInt(costOfEscape));
				System.out.println("The cost of escape is: " + getCostOfEscape());
			} else if (choice.equals("2")) {
				System.out.println("Enter the number of turns needed to escape: ");
				String turnsNeededToEscape = sc.nextLine();
				setTurnsNeededToEscape(Integer.parseInt(turnsNeededToEscape));
				System.out.println("The number of turns needed to escape is: " + getTurnsNeededToEscape());
			} else {
				System.out.println("Invalid input. Please try again.");
				editProperty();
			}

		} catch (Exception e) {
			System.out.println("Invalid input. Please try again.");
			editProperty();
		}
	}

	@Override
	public void action(Player player) {
		if (!player.isInJail()) {
			System.out.println("You are just visiting the jail.");
			return;
		}
		if (player.getJailTurns() == getTurnsNeededToEscape()) {
			System.out.println("You have to pay $" + getCostOfEscape() + " to get out of jail.");
			player.setMoney("-", getCostOfEscape());
			player.setInJail(false);
			player.setJailTurns(0);
			return;
		}
		System.out.println("You are in jail of turn " + player.getJailTurns() + ".");
		System.out.println("You have two options.");
		while (true) {
			System.out.println("Press 1: To pay $" + getCostOfEscape() + " and get out of jail.");
			System.out.println("Press 2: To throw dices of doubles.");
			Scanner sc = new Scanner(System.in);
			int num = sc.nextInt();
			if (num == 1) {
				player.setMoney("-", getCostOfEscape());
				player.setInJail(false);
				player.setJailTurns(0);
				break;
			} else if (num == 2) {
				player.addJailTurns();

				int diceValue1 = Dice.roll();
				int diceValue2 = Dice.roll();
				int sum = diceValue1 + diceValue2;
				if (diceValue1 == diceValue2) {
					System.out.println("You rolled doubles: " + diceValue1 + " + " + diceValue2 + " = " + sum
							+ ".\nYou are out of jail.");
					player.setInJail(false);
					player.setJailTurns(0);
					player.move(sum);
				} else {
					System.out.println("You rolled: " + diceValue1 + " + " + diceValue2 + ".");
					System.out.println("You didn't roll doubles.");
					System.out.println("You are still in jail.");
					break;
				}
				break;
			} else {
				System.out.println("Error input!");
			}
		}
	}
}
