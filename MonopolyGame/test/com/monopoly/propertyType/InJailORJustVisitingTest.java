package com.monopoly.propertyType;

import org.junit.jupiter.api.Test;

import com.monopoly.Board;
import com.monopoly.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

public class InJailORJustVisitingTest {
	InJailORJustVisiting inJailORJustVisiting;
	Player player;

	@BeforeEach
	void setUp() {
		inJailORJustVisiting = new InJailORJustVisiting(1);
		player = new Player("Player1");
	}

	@Test
	// Test the constructor InJailORJustVisiting(int position)
	void constructor() {
		assertEquals(1, inJailORJustVisiting.getPosition());
		assertEquals("In Jail/Just Visiting", inJailORJustVisiting.getName());
		assertEquals(150, inJailORJustVisiting.getCostOfEscape());
		assertEquals(3, inJailORJustVisiting.getTurnsNeededToEscape());
	}

	@Test
	// Test the constructor InJailORJustVisiting(int position, int costOfEscape, int
	// turnsNeededToEscape)
	void constructorWithParameters() {
		InJailORJustVisiting inJailORJustVisiting2 = new InJailORJustVisiting(1, 100, 4);
		assertEquals(1, inJailORJustVisiting2.getPosition());
		assertEquals(100, inJailORJustVisiting2.getCostOfEscape());
		assertEquals(4, inJailORJustVisiting2.getTurnsNeededToEscape());
	}

	@Test
	// Test if the cost of escape is initial correctly
	void getCostOfEscape() {
		assertEquals(150, inJailORJustVisiting.getCostOfEscape());
	}

	@Test
	// Test if the cost of escape is set correctly
	void setCostOfEscape() {
		inJailORJustVisiting.setCostOfEscape(50);
		assertEquals(50, inJailORJustVisiting.getCostOfEscape());
	}

	@Test
	// Test if the turns needed to escape is initial correctly
	void getTurnsNeededToEscape() {
		assertEquals(3, inJailORJustVisiting.getTurnsNeededToEscape());
	}

	@Test
	// Test if the turns needed to escape is set correctly
	void setTurnsNeededToEscape() {
		inJailORJustVisiting.setTurnsNeededToEscape(3);
		assertEquals(3, inJailORJustVisiting.getTurnsNeededToEscape());
	}

	@Test
	// Test if the the cost of escape is edited correctly by the editProperty method
	void editPropertyOfEscapeCost() {
		System.setIn(new ByteArrayInputStream("1\n100\n".getBytes()));
		inJailORJustVisiting.editProperty();
		assertEquals(100, inJailORJustVisiting.getCostOfEscape());

	}

	@Test
	// Test if the the number of turns needed to escape is edited correctly by the
	// editProperty method
	void editPropertyOfTurnsNeededToEscape() {
		System.setIn(new ByteArrayInputStream("2\n4\n".getBytes()));
		inJailORJustVisiting.editProperty();
		assertEquals(4, inJailORJustVisiting.getTurnsNeededToEscape());
	}

	@Test
	// Test if the action method works correctly when the player is not in jail and
	// just passing by
	void actionIfInJail() {
		player.setInJail(false);
		String expected = "You are just visiting the jail.";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		inJailORJustVisiting.action(player);
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertTrue(output.contains(expected));
	}

	@Test
	// Test if the action method works correctly when the player is in jail and
	// reaches the turns needed to escape
	void actionIfReachTurnsNeededToEscape() {
		player.setInJail(true);
		player.setJailTurns(3);
		player.setMoney("=", 1500);
		inJailORJustVisiting.setCostOfEscape(100);
		inJailORJustVisiting.setTurnsNeededToEscape(3);
		inJailORJustVisiting.action(player);
		assertEquals(0, player.getJailTurns());
		assertEquals(1400, player.getMoney());
		assertFalse(player.isInJail());
	}

	@Test
	// Test if the action method works correctly when the player is in jail and
	// chooses to pay to escape
	void actionPayToEscape() {
		System.setIn(new ByteArrayInputStream("1\n".getBytes()));
		player.setInJail(true);
		player.setJailTurns(2);
		player.setMoney("=", 1500);
		inJailORJustVisiting.setCostOfEscape(100);
		inJailORJustVisiting.setTurnsNeededToEscape(3);
		inJailORJustVisiting.action(player);
		assertEquals(0, player.getJailTurns());
		assertEquals(1400, player.getMoney());
		assertFalse(player.isInJail());
	}

	@Test
	// Test if the action method works correctly when the player is in jail and
	// chooses to throw to escape
	// possible scenario 1: player chooses to throw to escape but fails
	// possible scenario 2: player chooses to throw to escape and succeeds
	// Here we test the both scenario by repeating the test 30 times
	// If the player is still in jail, some of the line cant be covered
	void actionThrowToEscape() {
		Board.getProperties().clear();
		for(int i = 0; i < 10; i++){
			Board.addProperty(new InJailORJustVisiting(i+1));
		}
		for(int i = 0; i < 30; i++) {
			player.setCurrentSquare(Board.getProperties().get(0));
			System.setIn(new ByteArrayInputStream("2\n".getBytes()));
			player.setInJail(true);
			player.setJailTurns(1);
			ByteArrayOutputStream outContent = new ByteArrayOutputStream();
			System.setOut(new java.io.PrintStream(outContent));
			inJailORJustVisiting.action(player);
			String output = outContent.toString().trim();
			System.setOut(System.out);
			assertTrue(checkIfPlayerIsEscapeFromJail(player) || checkIfPlayerIsInJail(output));
		}
	}

	boolean checkIfPlayerIsEscapeFromJail(Player player) {
		if (player.getJailTurns() == 0 && !player.isInJail()) {
			return true;
		}
		return false;
	}

	boolean checkIfPlayerIsInJail(String output) {
		String expected = "You are still in jail.";
		if (output.contains(expected)) {
			return true;
		}
		return false;
	}
}