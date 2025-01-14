package com.monopoly.propertyType;

import org.junit.jupiter.api.Test;

import com.monopoly.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.BeforeEach;

public class GoTest {
	Player player;
	Go go;

	@BeforeEach
	void setUp() {
		player = new Player("Player1");
		player.setMoney("=", 1000);
		go = new Go(1);
	}

	@Test
	// Test the constructor Go(int position)
	void constructor() {
		assertEquals(1, go.getPosition());
		assertEquals("Go", go.getName());
	}

	@Test
	// Test the constructor Go(int position, int goMoney)
	void constructor2() {
		go = new Go(1, 2000);
		assertEquals(1, go.getPosition());
		assertEquals("Go", go.getName());
		assertEquals(2000, go.getGoMoney());
	}

	@Test
	// Test the getGoMoney method
	void getGoMoney() {
		assertEquals(1500, go.getGoMoney());
	}

	@Test
	// Test the setGoMoney method
	void setGoMoney() {
		go.setGoMoney(2000);
		assertEquals(2000, go.getGoMoney());
	}

	@Test
	// Test the action method, when the player passes Go, the player will get
	// certain amount of money
	void action() {
		go.action(player);
		assertEquals(2500, player.getMoney());
	}

	@Test
	// Test the editProperty method, this method will allow the user to edit the
	// money obtained when passing Go
	void editProperty() {
		String input = "2000";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		go.editProperty();
		assertEquals(2000, go.getGoMoney());
	}

}