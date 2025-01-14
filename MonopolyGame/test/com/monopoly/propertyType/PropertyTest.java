package com.monopoly.propertyType;

import org.junit.jupiter.api.Test;

import com.monopoly.Player;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest {

	@Test
	void testToString1() {
		Property property = new FreeParking(0);
		Player player = new Player("Player1");
		Player player2 = new Player("Player2");
		property.addHavePlayer(player);
		property.addHavePlayer(player2);
		StringBuffer expected = new StringBuffer(
				"[position= " + property.getPosition() + ", name= " + property.getName()
						+ ", Have Player= [" + player.getName() + ", "+ player2.getName() +"]]");
		String output = property.toString();
		assertTrue(output.contains(expected.toString()));
	}
}