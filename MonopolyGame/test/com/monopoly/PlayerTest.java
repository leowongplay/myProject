package com.monopoly;

import org.junit.jupiter.api.Test;

import com.monopoly.propertyType.Chance;
import com.monopoly.propertyType.District;
import com.monopoly.propertyType.Go;
import com.monopoly.propertyType.Property;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

public class PlayerTest {
	private Player player;
	private IOManipulate io = new IOManipulate();

	@BeforeEach
	void setUpAll(){
		io.loadGame(IOManipulate.getDefaultPathName());
		player = new Player("Player1");
		player.setCurrentSquare(Board.getProperties().get(0));
		player.addProperty((District) Board.getProperties().get(1));
	}

	@Test
	// Test the constructor Player(String name)
	void constructor() {
		Player player2 = new Player("Player1");
		assertEquals("Player1", player2.getName());
		assertEquals(1500, player2.getMoney());
	}

	@Test
	// Test if the initial name is correct
	void getName() {
		assertEquals("Player1", player.getName());
	}

	@Test
	// Test if the name is set correctly
	void setName() {
		player.setName("New Name");
		assertEquals("New Name", player.getName());
	}

	@Test
	// Test if the initial money is correct
	void getMoney() {
		assertEquals(1500, player.getMoney());
	}

	@Test
	// Test if the money is set correctly
	void setMoney() {
		player.setMoney("=", 100);
		assertEquals(100, player.getMoney());
		player.setMoney("+", 50);
		assertEquals(150, player.getMoney());
		player.setMoney("-", 50);
		assertEquals(100, player.getMoney());
		player.setMoney("*", 2);
		assertEquals(200, player.getMoney());
		player.setMoney("/", 2);
		assertEquals(100, player.getMoney());
	}

	@Test
	// Test if the set money with wrong operation
	void setMoneyWithWrongOperation() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		player.setMoney("wrong", 100);
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertTrue(output.contains("Invalid operation"));
	}

	@Test
	// Test if the initial property is correct
	void getProperty() {
		assertEquals(player.getName(), player.getProperty().get(0).getOwner().getName());
	}

	@Test
	// Test if the property is added correctly to the player and the owner is set
	void addProperty() {
		player.addProperty((District) Board.getProperties().get(2));
		assertEquals(player.getName(), player.getProperty().get(0).getOwner().getName());
	}

	@Test
	// Test if can get the current square where the player is located
	void getCurrSquare() {
		assertEquals(Board.getProperties().get(0), player.getCurrentSquare());
	}

	@Test
	// Test if the current square is set correctly to the player
	void setCurrSquare() {
		player.setCurrentSquare((Property) Board.getProperties().get(1));
		assertEquals(Board.getProperties().get(1), player.getCurrentSquare());
	}

	@Test
	// Test if the player is not in jail
	void isInJail() {
		assertFalse(player.isInJail());
	}

	@Test
	// Test if able to set the player into the jail
	void setInJail() {
		player.setInJail(true);
		assertTrue(player.isInJail());
	}

	@Test
	// Test if the player in jail turns is 0
	void getJailTurns() {
		assertEquals(0, player.getJailTurns());
	}

	@Test
	// Test if the player in jail turns is added correctly
	void addJailTurns() {
		assertEquals(0, player.getJailTurns());
		player.addJailTurns();
		assertEquals(1, player.getJailTurns());
	}

	@Test
	// Test if the player in jail turns is set correctly
	void setJailTurns() {
		player.setJailTurns(2);
		assertEquals(2, player.getJailTurns());
	}

	@Test
	// Test if the player is bankrupt, the player's properties are removed
	void bankrupt() {
		player.bankrupt();
		assertEquals(0, player.getProperty().size());
	}

	@Test
	// Test if the player has properties, print the properties
	void printProperties() {
		int i = 0;
		for (Property property : Board.getProperties()) {
			if (property instanceof District) {
				player.addProperty((District) Board.getProperties().get(i));
				break;
			}
			i++;
		}
		StringBuffer sb = new StringBuffer("Properties of Player1:\n" + player.getProperty().get(0).getName());
		String expected = sb.toString();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		player.printProperties();
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertTrue(output.contains(expected));
	}

	@Test
	// Test if the player has properties, print the properties with the owner
	void printStatus() {
		StringBuffer sb = new StringBuffer(
				"Name: " + player.getName() + "\n" + "Current Square: " + player.getCurrentSquare().getName() + "\n");
		String expected = sb.toString();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		player.printStatus();
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertTrue(output.contains(expected));
	}

	@Test
	// Test if the player has properties, print the properties with the owner and
	// money
	void printStatusWithMoney() {
		StringBuffer sb = new StringBuffer("Name: " + player.getName() + "\n" + "Money: " + player.getMoney() + "\n"
				+ "Current Square: " + player.getCurrentSquare().getName() + "\n");
		String expected = sb.toString();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		player.printStatusWithMoney();
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertTrue(output.contains(expected));

	}

	@Test
	// Test if the player is moved to the next square
	void move() {
		io.loadGame("");
		int lastSquare = Board.getProperties().size();
		player.setCurrentSquare(Board.getProperties().get(0));
		int base = player.getCurrentSquare().getPosition();
		int chanceSquarePosition = 1;
		for (Property property : Board.getProperties()) {
			if (property instanceof Chance) {
				chanceSquarePosition = property.getPosition();
				break;
			}
		}
		int moveSquare = chanceSquarePosition - 1;
		player.move(moveSquare);
		for (int i = 0; i < Board.getProperties().size(); i++) {
			if ((Board.getProperties().get(i).getPosition()) == ((base + moveSquare) % lastSquare)) {
				Property target = Board.getProperties().get(i);
				assertEquals(target, player.getCurrentSquare());
				break;
			}
		}
	}

	@Test
	// Test if the player is moved to the next square with passing GO
	// when the player is at the last square to/pass the first square
	void moveWithPassingGO(){
		io.loadGame("");
		int lastSquare = Board.getProperties().size();
		player.setCurrentSquare(Board.getProperties().get(lastSquare - 1));
		int base = player.getCurrentSquare().getPosition();
		int chanceSquarePosition = 1;
		for (Property property : Board.getProperties()) {
			if (property instanceof Chance) {
				chanceSquarePosition = property.getPosition();
				break;
			}
		}
		int moveSquare = chanceSquarePosition;
		player.move(moveSquare);
		for (int i = 0; i < Board.getProperties().size(); i++) {
			if ((Board.getProperties().get(i).getPosition()) == ((base + moveSquare) % lastSquare)) {
				Property target = Board.getProperties().get(i);
				assertEquals(target, player.getCurrentSquare());
				break;
			}
		}
	}

}