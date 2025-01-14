package com.monopoly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.monopoly.propertyType.Chance;
import com.monopoly.propertyType.District;
import com.monopoly.propertyType.Property;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BoardTest {

	private IOManipulate io = new IOManipulate();

	@BeforeEach
	void setUp() {
		io.loadGame(IOManipulate.getDefaultPathName());
	}

	@Test
	// Test the getProperties method, it is expected to return a list of properties
	void getProperties() {
		assertEquals(20, Board.getProperties().size());
	}

	@Test
	// Test the addProperty method, it is expected getProperties().size() to
	// increase
	void addProperty() {
		Property property = new District(Board.getProperties().size() + 1, "Test", 100, 40);
		Board.addProperty(property);
		assertEquals(Board.getProperties().get(Board.getProperties().size() - 1), property);
	}

	@Test
	// Test the getPathName method, it is expected to return the path name of the
	// board
	void getPathName() {
		Board.setPathName("Board.json");
		assertEquals("gameData/Board.json", Board.getPathName());
	}

	@Test
	// Test the setPathName method, it is expected to set a new path name of the
	// board
	void setPathName() {
		Board.setPathName("");
		assertEquals("gameData/Board.json", Board.getPathName());
		Board.setPathName("test.json");
		assertEquals("gameData/test.json", Board.getPathName());
	}

	@Test
	// Test the insertProperty method, it is expected to insert the new property at
	// the given position
	void insertProperty() {
		int position = Board.getProperties().size() / 2;
		Property property = new Chance(position);
		Board.insertProperty(property, position);
		assertEquals(Board.getProperties().get(position - 1), property);
		Board.insertProperty(null, position);

		// Test the insertProperty method, it is expected to print "Property not found."
		// when the insert property is null
		String expected = "Property not found.";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		Board.insertProperty(null, position);
		String output = outContent.toString().trim();
		assertEquals(expected, output);
		assertTrue(output.contains(expected));

		// Test the insertProperty method, it is expected to print "Invalid position."
		// when the position is invalid
		expected = "Invalid position.";
		outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		Board.insertProperty(property, Board.getProperties().size() + 10);
		output = outContent.toString().trim();
		System.setOut(System.out);
		assertTrue(output.contains(expected));
	}

	@Test
	// Test the removeProperty method, it is expected to remove the property at the
	// given position
	void removeProperty() {
		int removedPosition = Board.getProperties().size() - 1;
		Board.removeProperty(removedPosition);
		assertEquals(Board.getProperties().size(), removedPosition);

		assertNull(Board.removeProperty(-1));

	}

	@Test
	// Test the createProperty method, it is expected to create a new property of
	// different type
	void createProperty() {
		Property property;
		System.setIn(new ByteArrayInputStream("1\n".getBytes()));
		property = Board.createProperty();
		assertEquals(-1, property.getPosition());
		System.setIn(new ByteArrayInputStream("2\n".getBytes()));
		property = Board.createProperty();
		assertEquals(-1, property.getPosition());
		System.setIn(new ByteArrayInputStream("3\n".getBytes()));
		property = Board.createProperty();
		assertEquals(-1, property.getPosition());
		System.setIn(new ByteArrayInputStream("4\n".getBytes()));
		property = Board.createProperty();
		assertEquals(-1, property.getPosition());
		System.setIn(new ByteArrayInputStream("5\n".getBytes()));
		property = Board.createProperty();
		assertEquals(-1, property.getPosition());
		System.setIn(new ByteArrayInputStream("6\n".getBytes()));
		property = Board.createProperty();
		assertEquals(-1, property.getPosition());

		System.setIn(new ByteArrayInputStream("7\n1\n".getBytes()));
		property = Board.createProperty();

		System.setIn(new ByteArrayInputStream("7\n-1\n7\n1\n".getBytes()));
		property = Board.createProperty();

		System.setIn(new ByteArrayInputStream("8\n".getBytes()));
		System.setIn(new ByteArrayInputStream("a\n".getBytes()));

	}

	@Test
	// Test the createProperty method with invalid input, it is expected to
	// recursive the function
	void createPropertyWithInvalidInput() {
		System.setIn(new ByteArrayInputStream("8\n1\n".getBytes()));
		Property property = Board.createProperty();
		assertEquals(-1, property.getPosition());
	}

	@Test
	// Test the createProperty method with invalid input, it is expected to
	// recursive the function
	void createPropertyWithInvalidInput2() {
		try {
			System.setIn(new ByteArrayInputStream("a\n".getBytes()));
			Property property = Board.createProperty();
			assertEquals(-1, property.getPosition());
		} catch (Exception e) {
			// do nothing, this is expected because the input is invalid
		}
	}

	@Test
	// Test the sortBoard method, it is expected to sort the property in the board
	void sortBoard() {
		Board.getProperties().clear();
		Property property1 = new District(1, "Test1", 100, 40);
		Property property2 = new District(2, "Test2", 100, 40);
		Property property3 = new District(3, "Test3", 100, 40);
		Property property4 = new District(4, "Test4", 100, 40);
		Property property5 = new District(4, "Test5", 100, 40);
		Board.addProperty(property3);
		Board.addProperty(property1);
		Board.addProperty(property4);
		Board.addProperty(property2);
		Board.addProperty(property5);

		Board.sortBoard();
		assertEquals(property1, Board.getProperties().get(0));
		assertEquals(property2, Board.getProperties().get(1));
		assertEquals(property3, Board.getProperties().get(2));
		assertEquals(property4, Board.getProperties().get(3));

	}

	@Test
	// Test the printBoard method, it is expected to print the board
	void printBoard() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		Board.printBoard();
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertTrue(output.contains(Board.getProperties().get(0).toString()));
		assertTrue(output.contains(Board.getProperties().get(1).toString()));
		assertTrue(output.contains(Board.getProperties().get(2).toString()));
		assertTrue(output.contains(Board.getProperties().get(3).toString()));

	}

	@Test
	// Test the setPathNameIfExists method, it is expected to set the new path name
	// of the board
	void setPathNameIfExists() {
		Board.setPathNameIfExists("");
		assertEquals("gameData/Board.json", Board.getPathName());
		Board.setPathNameIfExists("test");
		assertEquals("gameData/test.json", Board.getPathName());

	}
}