package com.monopoly.propertyType;

import com.monopoly.IOManipulate;
import org.junit.jupiter.api.Test;

import com.monopoly.Board;
import com.monopoly.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

public class GoToJailTest {
	GoToJail goToJail;
	Player player;

	@BeforeEach
	void setUp() {
		goToJail = new GoToJail(1);
		player = new Player("Player1");
	}

	@Test
	// Test the constructor GoToJail(int position)
	void constructor() {
		assertEquals(1, goToJail.getPosition());
		assertEquals("Go To Jail", goToJail.getName());
	}

	@Test
	// Test the action method when the jail is not found
	void actionCantFoundJail() throws IOException {
		Board.getProperties().removeIf(property -> property instanceof InJailORJustVisiting);
		String expected = "Jail not found.";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		goToJail.action(player);
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertTrue(output.contains(expected));
	}

	@Test
	// Test the action method when the jail is found
	void actionFoundJail()  {
		IOManipulate io = new IOManipulate();
		io.loadGame(IOManipulate.getDefaultPathName());
		goToJail.action(player);
		assertTrue(player.isInJail());
		assertTrue(player.getCurrentSquare() instanceof InJailORJustVisiting);
	}

	@Test
	// It should doing nothing
	void editProperty() {
		goToJail.editProperty();
	}
}