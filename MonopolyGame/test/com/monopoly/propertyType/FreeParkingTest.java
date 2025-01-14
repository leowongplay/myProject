package com.monopoly.propertyType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;

public class FreeParkingTest {
	FreeParking freeParking;

	@BeforeEach
	void setUp() {
		freeParking = new FreeParking(1);
	}

	@Test
	// Test the constructor FreeParking(int position)
	void constructor1() {
		assertEquals(1, freeParking.getPosition());
	}

	@Test
	// Test the if the name is init correctly, and the print message is correct
	void action() {
		String expected = "You landed on Free Parking.\nYou don't have to do anything.";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		freeParking.action(null);
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertTrue(output.contains(expected));
	}

	@Test
	// Test the editProperty method
	void editProperty() {
		freeParking.editProperty();
	}
}