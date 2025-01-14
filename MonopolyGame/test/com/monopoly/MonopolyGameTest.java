package com.monopoly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;

public class MonopolyGameTest {

	@Test
	void testMain() {

		String expected = "Welcome to the Monopoly Game.";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		MonopolyGame.main(new String[] {});
		String output = outContent.toString().trim();
		assertTrue(output.contains(expected));
	}
}