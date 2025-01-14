package com.monopoly.propertyType;

import java.util.Scanner;

import com.monopoly.Player;

public class IncomeTax extends Property {
	private float incomeTaxPercentage = 10;
	private final static String name = "Income Tax";

	public float getIncomeTaxPercentage() {
		return incomeTaxPercentage;
	}

	public void setIncomeTaxPercentage(int incomeTaxPercentage) {
		this.incomeTaxPercentage = incomeTaxPercentage;
	}

	public IncomeTax(int position) {
		super(position, name);
	}

	public IncomeTax(int position, float incomeTaxPercentage) {
		super(position, name);
		this.incomeTaxPercentage = incomeTaxPercentage;
	}

	@Override
	public void action(Player player) {
		System.out.println("You have to pay " + getIncomeTaxPercentage() + "% of your money as " + name + ".");
		float amount = (player.getMoney() * (incomeTaxPercentage / 100));
		amount = (amount / 10) * 10;
		player.setMoney("-", amount);
		System.out.println("You paid $" + amount + " as " + name + ".");
		System.out.println("You now have $" + player.getMoney() + ".");
	}

	@Override
	public void editProperty() {
		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter the new amount of the " + name + " percentage: ");
			String goMoney = sc.nextLine();
			setIncomeTaxPercentage(Integer.parseInt(goMoney));
			System.out.println("The new amount of the " + name + " percentage is: " + getIncomeTaxPercentage());

		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please try again.");
			editProperty();
		}
	}
}
