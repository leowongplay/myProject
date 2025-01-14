package com.monopoly;

import com.monopoly.propertyType.District;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOManipulateTest {
	IOManipulate io;

	@BeforeEach
	void setUp() {
		io = new IOManipulate();
	}

	@Test
	// Test if the default path name is initial correctly
	void setPathName() {
		io.setPathName("test");
		assertEquals("gameData/test.json", io.getPathName());
	}

	@Test
	// Test if the property is added correctly edited
	void setPathNameIfExists() {
		io.setPathNameIfExists("");
		assertEquals("gameData/monopoly.json", io.getPathName());
	}

//	@Test
//	// Test if the path name is returned correctly
//	void getPathName() {
//		assertEquals("gameData/monopoly.json", io.getPathName());
//	}

	@Test
	// Test if the the exists game data is loaded correctly by checking the return
	// value
	void loadGame() throws IOException {
		assertEquals(0, io.loadGame("monopoly.json"));
		Files.deleteIfExists(Path.of("gameData/test.json"));
		assertEquals(-1, io.loadGame("test"));
	}

	@Test
	// Test if the the exists game data is saved correctly by checking the return
	// value
	void saveGame() throws IOException {
		io.loadGame(IOManipulate.getDefaultPathName());
		Player player = new Player("Player1");
		Player player2 = new Player("Player2");
		player.setCurrentSquare(Board.getProperties().get(0));
		player2.setCurrentSquare(Board.getProperties().get(1));
		player2.addProperty((District) Board.getProperties().get(1));
		Controller.players.add(player);
		Controller.players.add(player2);

		io.setPathName("myGame");
		String filePath = io.getPathName();
		assertEquals(0, io.saveGame(filePath));
		Files.deleteIfExists(Path.of(filePath));

		io.setPathName(IOManipulate.getDefaultPathName());
	}

}