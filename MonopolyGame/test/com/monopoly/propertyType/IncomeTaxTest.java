package com.monopoly.propertyType;

import org.junit.jupiter.api.Test;

import com.monopoly.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;

public class IncomeTaxTest {
	IncomeTax incomeTax;

	@BeforeEach
	void setUp() {
		incomeTax = new IncomeTax(1);
	}

	@Test
	// Test the constructor IncomeTax(int position)
	void constructor1() {
		IncomeTax incomeTax1 = new IncomeTax(1);
		assertEquals(1, incomeTax1.getPosition());
	}

	@Test
	// Test the constructor IncomeTax(int position, float incomeTaxPercentage)
	void constructor2() {
		IncomeTax incomeTax1 = new IncomeTax(1, 10);
		assertEquals(1, incomeTax1.getPosition());
		assertEquals(10, incomeTax1.getIncomeTaxPercentage());
	}

	@Test
	// Test the if the value of incomeTaxPercentage is init correctly
	void getIncomeTaxPercentage() {
		assertEquals(10, incomeTax.getIncomeTaxPercentage());
	}

	@Test
	// Test the if the value of incomeTaxPercentage is set correctly
	void setIncomeTaxPercentage() {
		incomeTax.setIncomeTaxPercentage(20);
		assertEquals(20, incomeTax.getIncomeTaxPercentage());
	}

	@Test
	// Test the action method, it is expected to decrease the player's money by 10%
	void action() {
		Player player = new Player("Player1");
		player.setMoney("=", 1000);
		incomeTax.action(player);
		assertEquals(900, player.getMoney());

	}

	@Test
	// Test the editProperty method, it is expected to change the
	// incomeTaxPercentage to 20
	void editProperty() {
		IncomeTax incomeTax1 = new IncomeTax(1);
		ByteArrayInputStream in = new ByteArrayInputStream("20".getBytes());
		System.setIn(in);
		incomeTax1.editProperty();
		assertEquals(20, incomeTax1.getIncomeTaxPercentage());
	}

	@Test
	void editProperty2() {
		try {
			IncomeTax incomeTax1 = new IncomeTax(1);
			System.setIn(new ByteArrayInputStream("abc\n".getBytes()));
			incomeTax1.editProperty();
			assertEquals(20, incomeTax1.getIncomeTaxPercentage());
		} catch (Exception e) {
			// do nothing, this is expected because the input is invalid
		}
	}

}