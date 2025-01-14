package com.monopoly.propertyType;

import com.monopoly.Board;
import com.monopoly.Player;

public class GoToJail extends Property {
	private final static String name = "Go To Jail";

	public GoToJail(int position) {
		super(position, name);
	}

	@Override
	public void action(Player player) {
		System.out.println("You have to go to jail.");
		for (Property property : Board.getProperties()) {
			if (property instanceof InJailORJustVisiting) {
				player.setCurrentSquare(property);
				player.setInJail(true);
				player.addJailTurns();
				System.out.println("You are in jail now.");
				return;
			}
		}
		System.out.println("Jail not found.");
	}

	@Override
	public void editProperty() {

	}
}
