package com.monopoly.propertyType;

import com.monopoly.IOManipulate;
import org.junit.jupiter.api.Test;

import com.monopoly.Board;
import com.monopoly.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

public class DistrictTest {

	District district;
	Player player;
	private String name = "Kowloon";
	private int price = 100;
	private int rent = 10;

	@BeforeEach
	void setUp() {
		district = new District(1, name, price, rent);
		player = new Player("Player1");
		district.setOwner(player);
	}

	@Test
	// Test the action method when the player is the owner of the district
	void actionOnPayRent() {
		Player player2 = new Player("Player2");
		district.setOwner(player2);
		float initialMoney = 1000;
		player2.setMoney("=", initialMoney);
		player.setMoney("=", initialMoney);
		district.action(player);
		assertEquals(initialMoney - district.getRent(), player.getMoney());
		assertEquals(initialMoney + district.getRent(), player2.getMoney());
	}

	@Test
	// Test the action method when the player is not the owner of the district
	void actionOnOwnProperty() {
		String expected = "You already own this property.";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		district.action(player);
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertEquals(expected, output);
	}

	@Test
	// Test the action method when the player is not the owner of the district and
	// want to buy the district
	void actionOnBuy() {
		district.setOwner(null);
		float initialMoney = 1000;
		player.setMoney("=", initialMoney);
		System.setIn(new ByteArrayInputStream("1\n".getBytes()));
		district.action(player);
		assertEquals(initialMoney - district.getPrice(), player.getMoney());
		assertEquals(player, district.getOwner());
	}

	@Test
	// Test the action method when the player is not the owner of the district and
	// don't want to buy the district
	void actionOnNotBuy() {
		district.setOwner(null);
		float initialMoney = 1000;
		player.setMoney("=", initialMoney);
		System.setIn(new ByteArrayInputStream("2\n".getBytes()));
		district.action(player);
		assertEquals(initialMoney, player.getMoney());
		assertNull(district.getOwner());
	}

	@Test
	// Test the action method when the player is not the owner of the district and
	// don't have enough money to buy the district
	void actionOnNotEnoughMoney() {
		district.setOwner(null);
		float initialMoney = 50;
		player.setMoney("=", initialMoney);
		String expected = "This property is cost $100.\nYou don't have enough money to buy this property.";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		district.action(player);
		String output = outContent.toString().trim();
		System.setOut(System.out);
		assertEquals(expected, output);
	}

	@Test
	// Test the constructor of District(int position)
	void constructor1() {
		District district1 = new District(1);
		assertEquals(1, district1.getPosition());
	}

	@Test
	// Test the constructor of District(int position, String name, int price, int
	// rent)
	void constructor2() {
		District district1 = new District(1, name, price, rent);
		assertEquals(1, district1.getPosition());
		assertEquals(name, district1.getName());
		assertEquals(price, district1.getPrice());
		assertEquals(rent, district1.getRent());
	}

	@Test
	// Test the getPrice method
	void getPrice() {
		assertEquals(price, district.getPrice());
	}

	@Test
	// Test the getRent method
	void getRent() {
		assertEquals(rent, district.getRent());
	}

	@Test
	// Test the getOwner method
	void getOwner() {
		assertEquals(player, district.getOwner());
	}

	@Test
	// Test the setOwner method
	void setOwner() {
		Player player2 = new Player("Player2");
		district.setOwner(player2);
		assertEquals(player2, district.getOwner());
	}

	@Test
	// Test the setPrice method
	void setPrice() {
		int newPrice = 200;
		district.setPrice(newPrice);
		assertEquals(newPrice, district.getPrice());
	}

	@Test
	// Test the setRent method
	void setRent() {
		int newRent = 20;
		district.setRent(newRent);
		assertEquals(newRent, district.getRent());
	}

	@Test
	// Test the createDistrict method
	void createDistrict() {
		System.setIn(new ByteArrayInputStream("Tai O\n500\n50\n".getBytes()));
		District district1 = district.createDistrict();
		assertEquals("Tai O", district1.getName());
		assertEquals(500, district1.getPrice());
		assertEquals(50, district1.getRent());
	}

	@Test
	// Test the edit property name method
	void editPropertyOfName() throws IOException {
		System.setIn(new ByteArrayInputStream("1\nLam Tin\n".getBytes()));
		district.editProperty();
		assertEquals("Lam Tin", district.getName());
	}

	@Test
	// Test the edit property position method
	void editPropertyOfPosition() {
		IOManipulate io = new IOManipulate();
		io.loadGame("");
		System.setIn(new ByteArrayInputStream("2\n5\n".getBytes()));
		district.editProperty();
		assertEquals(5, district.getPosition() + 1);
	}

	@Test
	// Test the edit property position method
	void editPropertyOfPosition2() {
		IOManipulate io = new IOManipulate();
		io.loadGame("");
		System.setIn(new ByteArrayInputStream("2\n1\n".getBytes()));
		district.editProperty();
		assertEquals(1, district.getPosition() + 1);
	}

	@Test
	// Test the edit property price method
	void editPropertyOfPrice() throws IOException {
		System.setIn(new ByteArrayInputStream("3\n200\n".getBytes()));
		district.editProperty();
		assertEquals(200, district.getPrice());
	}

	@Test
	// Test the edit property rent method
	void editPropertyOfRent() throws IOException {
		System.setIn(new ByteArrayInputStream("4\n20\n".getBytes()));
		district.editProperty();
		assertEquals(20, district.getRent());
	}

	@Test
	// Test the edit property method with invalid value
	void editPropertyWithInvalidValue1() {
		try {
			System.setIn(new ByteArrayInputStream("8\n".getBytes()));
			district.editProperty();
		} catch (Exception e) {
			// do nothing, this is expected because the input is invalid and the function is
			// recursive
		}
	}

	@Test
	// Test the edit property rent method
	void editPropertyWithInvalidValue2() throws IOException {
		try {
			System.setIn(new ByteArrayInputStream("4\na\n".getBytes()));
			district.editProperty();
		} catch (Exception e) {
			// do nothing, this is expected because the input is invalid
		}
	}

	@Test
	// Test the toString method
	void testToString() {
		assertEquals("[position= 1, name= Kowloon, price= 100, rent= 10, owner= Player1]", district.toString());
		district.setOwner(null);
		player.setCurrentSquare(district);
		Player player2 = new Player("Player2");
		player2.setCurrentSquare(district);
		assertEquals("[position= 1, name= Kowloon, price= 100, rent= 10, Have player= [Player1, Player2]]",
				district.toString());
	}

	@Test
	// Test the toStringWithoutPosition method
	void toStringWithoutPosition() {
		assertEquals("[name= Kowloon, price= 100, rent= 10, owner= Player1]", district.toStringWithoutPosition());
		district.setOwner(null);
		player.setCurrentSquare(district);
		Player player2 = new Player("Player2");
		player2.setCurrentSquare(district);
		assertEquals("[name= Kowloon, price= 100, rent= 10, Have player= [Player1, Player2]]",
				district.toStringWithoutPosition());
	}
}