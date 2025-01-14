package com.monopoly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiceTest {

	@Test
	// Test that the roll method returns a value between 1 and 4
	void roll() {
		int result = Dice.roll();
		assertTrue(result >= 1 && result <= 4);
	}
}