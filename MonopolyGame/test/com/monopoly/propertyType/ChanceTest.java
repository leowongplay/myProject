package com.monopoly.propertyType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.monopoly.Player;

public class ChanceTest {
	Chance chance1;
	Chance chance;
	private int addValue = 30;
	private int subtractValue = 40;

	@BeforeEach
	void setUp() {
		chance = new Chance(1, addValue, subtractValue);
	}

	@Test
	// Test the constructor Chance(int position)
	void constructor1() {
		chance1 = new Chance(1);
		assertEquals(1, chance1.getPosition());
	}

	@Test
	// Test the constructor Chance(int position, int addValue, int subtractValue)
	void constructor2() {
		chance1 = new Chance(1, addValue, subtractValue);
		assertEquals(1, chance1.getPosition());
		assertEquals(addValue, chance1.getAddValue());
		assertEquals(subtractValue, chance1.getSubtractValue());
	}

	@Test
	// Test the if the value of addValue is init correctly
	void getAddValue() {
		assertEquals(addValue, chance.getAddValue());
	}

	@Test
	// Test the if the value of addValue is set correctly
	void setAddValue() {
		int newAddValue = 40;
		chance.setAddValue(newAddValue);
		assertEquals(newAddValue, chance.getAddValue());
	}

	@Test
	// Test the if the value of subtractValue is init correctly
	void getSubtractValue() {
		assertEquals(subtractValue, chance.getSubtractValue());
	}

	@Test
	// Test the if the value of subtractValue is set correctly
	void setSubtractValue() {
		int newSubtractValue = 50;
		chance.setSubtractValue(newSubtractValue);
		assertEquals(newSubtractValue, chance.getSubtractValue());
	}

	@Test
	// Test if the action method of Chance class works correctly by editing the
	// money, run 10 times to reach better coverage
	void action() {
		for (int i = 0; i < 10; i++) {
			Player player = new Player("Player1");
			float value = player.getMoney();
			chance.action(player);
			assertNotEquals(value, player.getMoney());
		}
	}

	@Test
	// Test if the editProperty method works correctly by editing the SubtractValue
	void editPropertyWithSubtractValue() {
		System.setIn(new ByteArrayInputStream("2\n10\n".getBytes()));
		chance.editProperty();
		assertEquals(10, chance.getSubtractValue());
	}

	@Test
	// Test if the editProperty method works correctly by editing the AddValue
	void editPropertyWithAddValue() {
		System.setIn(new ByteArrayInputStream("1\n10\n".getBytes()));
		chance.editProperty();
		assertEquals(10, chance.getAddValue());
	}

	@Test
	// Test if the editProperty method throws an exception when the input is invalid
	void editPropertyWithInvalidInput() {
		try {
			System.setIn(new ByteArrayInputStream("3\n".getBytes()));
			String expected = "Please enter a valid number.";
			ByteArrayOutputStream outContent = new ByteArrayOutputStream();
			System.setOut(new java.io.PrintStream(outContent));
			chance.editProperty();
			String output = outContent.toString().trim();
			assertTrue(output.contains(expected));
		} finally {
			System.setOut(System.out);
		}

		try {
			System.setIn(new ByteArrayInputStream("a\n".getBytes()));
			String expected = "Please enter a valid number.";
			ByteArrayOutputStream outContent = new ByteArrayOutputStream();
			System.setOut(new java.io.PrintStream(outContent));
			chance.editProperty();
			String output = outContent.toString().trim();
			assertTrue(output.contains(expected));
		} finally {
			System.setOut(System.out);
		}
	}
}
