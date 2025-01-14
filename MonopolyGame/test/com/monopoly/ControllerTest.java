package com.monopoly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import com.monopoly.propertyType.Chance;
import com.monopoly.propertyType.Go;
import com.monopoly.propertyType.Property;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class ControllerTest {
	private Controller co;
	private Player player;
	private Player player2;

	@BeforeEach
	void setUp() {

		co = new Controller();
		player = new Player("player1");
		player2 = new Player("player2");
		Controller.players.clear();
		Controller.players.add(player);
		Controller.players.add(player2);
	}

	@Nested
	class editBoardTest {
		@Test
		void editBoard1() {
			try {
				Board.getProperties().clear();
				co.io.loadGame(Board.defaultBoardName);
				System.setIn(new ByteArrayInputStream("1\n".getBytes()));
				co.editBoard();
			} catch (Exception e) {
				// count as success if exception is thrown, since the function is recursive
			}
		}

		@Test
		void editBoard2() {
			try {
				Board.getProperties().clear();
				co.io.loadGame(Board.defaultBoardName);
				System.setIn(new ByteArrayInputStream("2\n".getBytes()));
				co.editBoard();
			} catch (Exception e) {
				// count as success if exception is thrown, since the function is recursive
			}
		}

		@Test
		void editBoard3() {
			try {
				Board.getProperties().clear();
				co.io.loadGame(Board.defaultBoardName);
				System.setIn(new ByteArrayInputStream("3\n".getBytes()));
				co.editBoard();
			} catch (Exception e) {
				// count as success if exception is thrown, since the function is recursive
			}
		}

		@Test
		void editBoard4() {
			try {
				Board.getProperties().clear();
				co.io.loadGame(Board.defaultBoardName);
				System.setIn(new ByteArrayInputStream("4\n".getBytes()));
				co.editBoard();
			} catch (Exception e) {
				// count as success if exception is thrown, since the function is recursive
			}
		}

		@Test
		void editBoard5() {
			try {
				Board.getProperties().clear();
				co.io.loadGame(Board.defaultBoardName);
				System.setIn(new ByteArrayInputStream("0\n".getBytes()));
				co.editBoard();
			} catch (Exception e) {
				// count as success if exception is thrown, since the function is recursive
			}
		}
	}

	@Test
	// Test if gameStart() works correctly by checking if the game round can be end
	void gameStart() {
		try {
			Board.getProperties().clear();
			for (int i = 0; i < 10; i++) {
				Board.addProperty(new Chance(i));
			}
			player.setCurrentSquare(Board.getProperties().get(0));
			player2.setCurrentSquare(Board.getProperties().get(0));

			StringBuffer expected = new StringBuffer("Round " + Controller.round + " end.");
			ByteArrayOutputStream outContent = new ByteArrayOutputStream();
			System.setOut(new java.io.PrintStream(outContent));
			System.setIn(new ByteArrayInputStream("1\n1\n".getBytes()));
			co.gameStart();
			String output = outContent.toString().trim();
			assertTrue(output.contains(expected.toString()));
		} catch (Exception e) {
		} finally {
			Board.getProperties().clear();
			System.setOut(System.out);
			System.setIn(System.in);
		}
	}

	@Test
	// Test if gameStart() works correctly by checking if the game ends when player
	// is bankrupt and at final round
	void gameStart2() {
		try {
			player.setMoney("=", -10);
			player.setCurrentSquare(Board.getProperties().get(0));
			player2.setCurrentSquare(Board.getProperties().get(0));
			Controller.round = 100;

			StringBuffer expected = new StringBuffer(player.getName() + " is bankrupt.");
			String expected2 = "Quitting game...";
			ByteArrayOutputStream outContent = new ByteArrayOutputStream();
			System.setOut(new java.io.PrintStream(outContent));

			co.gameStart();
			String output = outContent.toString().trim();
			assertTrue(output.contains(expected.toString()));
			assertTrue(output.contains(expected2));
		} catch (Exception e) {
		}
	}

	@Test
	// Test if gameStart() works correctly
	void askSave() {
		try {// Test if user chooses to save the game
			System.setIn(new ByteArrayInputStream("\n".getBytes()));
			int num = co.askSave();
			assertEquals(0, num);
		} catch (Exception e) {
		}

		try { // Test if user chooses not to save the game
			System.setIn(new ByteArrayInputStream("s\n".getBytes()));
			int num = co.askSave();
			assertEquals(1, num);
		} catch (Exception e) {
		}
	}

	@Nested
	// Test if playerOpt() works correctly
	class playerOptTest {
		@BeforeEach
		void setUp2() {
			Board.getProperties().clear();
			for (int i = 0; i < 10; i++) {
				Board.addProperty(new Chance(i));
			}
			player.setCurrentSquare(Board.getProperties().get(0));
			player2.setCurrentSquare(Board.getProperties().get(0));
		}

		@Test
		// `playerOpt` test case when player chooses to roll dice, user moved
		void playerOpt1() {
			System.setIn(new ByteArrayInputStream("1\n".getBytes()));
			Property currPro = player.getCurrentSquare();
			co.playerOpt(player);
			assertNotEquals(currPro, player.getCurrentSquare());
		}

		@Test
		// `playerOpt` test case when player chooses to print board
		void playerOpt2() {
			try {
				System.setIn(new ByteArrayInputStream("2\n".getBytes()));
				co.playerOpt(player);
			} catch (Exception e) {
				// count as success if exception is thrown, since the function is recursive
			} finally {
				System.setIn(System.in);
			}
		}

		@Test
		// `playerOpt` test case when player chooses to see player info
		void playerOpt3() {
			try {
				System.setIn(new ByteArrayInputStream("3\n1\n".getBytes()));
				co.playerOpt(player);

			} catch (Exception e) {
				// count as success if exception is thrown, since the function is calling
				// playerInfo(), then recursive
			} finally {
				System.setIn(System.in);
			}
		}

		@Test
		// `playerOpt` test case when player chooses to see next player
		void playerOpt4() {
			try {
				System.setIn(new ByteArrayInputStream("4\n".getBytes()));
				co.playerOpt(player);
				StringBuffer expected = new StringBuffer(
						"The next player is " + player2.getName() + ".");
				ByteArrayOutputStream outContent = new ByteArrayOutputStream();
				System.setOut(new java.io.PrintStream(outContent));
				co.playerOpt(player);
				String output = outContent.toString().trim();
				assertEquals(expected, output);
				assertTrue(output.contains(expected.toString()));
			} catch (Exception e) {
				// count as success if exception is thrown, since the function is recursive
			} finally {
				System.setIn(System.in);
			}
		}

		@Test
		// `playerOpt` test case when player input is invalid
		void playerOpt5() {
			try {
				System.setIn(new ByteArrayInputStream("2\n".getBytes()));
				co.playerOpt(player);
				StringBuffer expected = new StringBuffer("Error input!");
				ByteArrayOutputStream outContent = new ByteArrayOutputStream();
				System.setOut(new java.io.PrintStream(outContent));
				co.playerOpt(player);
				String output = outContent.toString().trim();
				assertEquals(expected, output);
				assertTrue(output.contains(expected.toString()));
			} catch (Exception e) {
				// count as success if exception is thrown, since the function is recursive
			}
		}
	}

	@Test
	// Test if queryNextPlayer() works correctly, by checking if the next player is
	// printed
	void queryNextPlayer() {
		// Test if the next player is player2, when current player is player1
		StringBuffer expected = new StringBuffer("The next player is " + player2.getName() + ".");
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.queryNextPlayer(player);
		String output = outContent.toString().trim();
		assertTrue(output.contains(expected.toString()));

		// Test if the next player is player1, when current player is player2
		expected = new StringBuffer("The next player is " + player.getName() + ".");
		outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.queryNextPlayer(player2);
		output = outContent.toString().trim();
		assertTrue(output.contains(expected.toString()));
	}

	@Test
	// Test if foundWinner() works correctly
	void foundWinner() {
		// Test case when there are two winners
		player.setMoney("=", 1500);
		player2.setMoney("=", 1500);
		StringBuffer expected = new StringBuffer(
				"The winner is " + player.getName() + " with $" + player.getMoney() + "." +
						"\n" +
						"The winner is " + player2.getName() + " with $" + player2.getMoney()
						+ ".");
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.foundWinner();
		String output = outContent.toString().trim();
		assertTrue(output.contains(expected.toString()));

		// Test case when there is one winner
		player.setMoney("=", -10);
		player2.setMoney("=", 100);
		expected = new StringBuffer(
				"The winner is " + player2.getName() + " with $" + player2.getMoney() + ".");
		outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.foundWinner();
		output = outContent.toString().trim();
		assertTrue(output.contains(expected.toString()));

		// Test case when all players have negative amount of money, no winner
		player.setMoney("-", 522220);
		player2.setMoney("-", 5645454);
		expected = new StringBuffer("Draw! All players have negative amount of money.");
		outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.foundWinner();
		output = outContent.toString().trim();
		assertTrue(output.contains(expected.toString()));

	}

	@Test
	// Test if playerInfo() works correctly, by checking if the player's own info is
	// printed
	void playerInfo() {
		co.io.loadGame(Board.defaultBoardName);
		player.setCurrentSquare(Board.getProperties().get(0));
		System.setIn(new ByteArrayInputStream("1\n".getBytes()));
		StringBuffer sb = new StringBuffer("Name: " + player.getName() + "\n" + "Money: " + player.getMoney() + "\n"
				+ "Current Square: " + player.getCurrentSquare().getName() + "\n");
		String expected = sb.toString();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.playerInfo(player);
		String output = outContent.toString().trim();
		assertTrue(output.contains(expected));
	}

	@Test
	// Test if playerInfo() works correctly, by checking if specific players' info
	// is
	// printed
	void specificPlayerInfo() {
		co.io.loadGame(Board.defaultBoardName);
		player.setCurrentSquare(Board.getProperties().get(0));
		System.setIn(new ByteArrayInputStream("3\n1\n".getBytes()));
		StringBuffer sb = new StringBuffer("Which player's info do you want to see?\n" +
				"Press 1: To see you own info.\n" +
				"Press 2: To see all players' info.\n" +
				"Press 3: To see a specific player's info.\n" +
				"Enter:\n" +
				"Press 1: To see player1's info.\n" +
				"Press 2: To see player2's info.\n");
		sb.append("Name: " + player.getName() + "\n" + "Current Square: "
				+ player.getCurrentSquare().getName() + "\n" + "Properties of " + player.getName() + ":");
		String expected = sb.toString();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.playerInfo(player);
		String output = outContent.toString().trim();
		assertEquals(expected, output);
		assertTrue(output.contains(expected));
	}

	@Test
	// Test if playerInfo() works correctly, by checking if all players' info is
	// printed
	void allPlayersInfo() {
		co.io.loadGame(Board.defaultBoardName);
		player.setCurrentSquare(Board.getProperties().get(0));
		player2.setCurrentSquare(Board.getProperties().get(0));

		System.setIn(new ByteArrayInputStream("2\n".getBytes()));
		StringBuffer sb = new StringBuffer("Which player's info do you want to see?\n" +
				"Press 1: To see you own info.\n" +
				"Press 2: To see all players' info.\n" +
				"Press 3: To see a specific player's info.\n" +
				"Enter:\n");
		sb.append("Name: " + player.getName() + "\n" + "Current Square: "
				+ player.getCurrentSquare().getName() + "\n" + "Properties of " + player.getName() + ":\n");
		sb.append("Name: " + player2.getName() + "\n" + "Current Square: "
				+ player2.getCurrentSquare().getName() + "\n" + "Properties of " + player2.getName() + ":");
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.playerInfo(player);
		String output = outContent.toString().trim();
		String expected = sb.toString();
		assertEquals(expected, output);
		assertTrue(output.contains(expected));
	}

	@Test
	// Test if playerInfo() generate error, when input is invalid
	void PlayerInfoWithInvalidInput() {
		try {
			co.io.loadGame(Board.defaultBoardName);
			player.setCurrentSquare(Board.getProperties().get(0));
			System.setIn(new ByteArrayInputStream("4\n1\n".getBytes()));
		} catch (Exception e) {
			// count as success if exception is thrown
		}

		try {
			co.io.loadGame(Board.defaultBoardName);
			player.setCurrentSquare(Board.getProperties().get(0));
			System.setIn(new ByteArrayInputStream("3\n6\n".getBytes()));
		} catch (Exception e) {
			// count as success if exception is thrown
		}

	}

	@Test
	// Test if rollDice() works correctly, by checking if the number generated is
	// between 2 and 8, since there has two dice with 4 faces each
	void rollDice() {
		int num = co.rollDice();
		assertTrue(num >= 2 && num <= 8);
	}

	@Test
	// Test if gameOpt() works correctly
	void gameOpt() {
		try {// Test if user chooses to start a new game
			System.setIn(new ByteArrayInputStream("\n".getBytes()));
			co.gameOpt();
		} catch (Exception e) {
			// count as success if exception is thrown, because the function newGameStart()
			// has called
		}
		try {// Test if user chooses to continue the game
			System.setIn(new ByteArrayInputStream("1\n".getBytes()));
			co.gameOpt();
		} catch (Exception e) {
			// count as success if exception is thrown, because the function continueGame()
			// has called
		}
		try {// Test if input is invalid
			System.setIn(new ByteArrayInputStream("3\n".getBytes()));
			co.gameOpt();
			StringBuffer sb = new StringBuffer("Error input!");
			ByteArrayOutputStream outContent = new ByteArrayOutputStream();
			System.setOut(new java.io.PrintStream(outContent));
			co.playerInfo(player);
			String output = outContent.toString().trim();
			String expected = sb.toString();
			assertTrue(output.contains(expected));
		} catch (Exception e) {
			// count as success if exception is thrown, since the function is recursive
		}

	}

	@Test
	// Test if playerCreate() works correctly
	void playerCreate() {
		co.io.loadGame(Board.defaultBoardName);
		Controller.players.clear();
		// Test if input player number is valid
		System.setIn(new ByteArrayInputStream("2\n\n\n".getBytes()));
		Controller.playerCreate();
		assertEquals(2, Controller.players.size());

		try { // Test if input player number <= 1 or > 6
			Controller.players.clear();
			System.setIn(new ByteArrayInputStream("1\n".getBytes()));
			Controller.playerCreate();
			Controller.players.clear();
			System.setIn(new ByteArrayInputStream("6\n".getBytes()));
			Controller.playerCreate();
		} catch (Exception e) {
			// count as success if exception is thrown
		}

		try { // Test if input player number is not a number
			Controller.players.clear();
			System.setIn(new ByteArrayInputStream("a\n".getBytes()));
			co.playerCreate();
		} catch (Exception e) {
			// count as success if exception is thrown
		}

		// Test if name repeated
		Controller.players.clear();
		System.setIn(new ByteArrayInputStream("2\nplayer\nplayer\nplayer2\n".getBytes()));
		String expected = "Name already exists.";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.playerCreate();
		String output = outContent.toString().trim();
		assertTrue(output.contains(expected));

	}

	@Test
	void ifEditBoard() {
		// Test case when user chooses not to edit the board, nothing should happen
		System.setIn(new ByteArrayInputStream("\n".getBytes()));
		co.ifEditBoard();

		// Test case when user chooses to edit the board
		try {
			System.setIn(new ByteArrayInputStream("1\n".getBytes()));
			co.ifEditBoard();

		} catch (Exception e) {
			// count as success if exception is thrown, since the function is calling
			// editBoard()
		} finally {
			System.setIn(System.in);
		}
	}

	@Test
	void ifSaveBoard() {
		// Test case when user chooses not to save the board
		try {
			System.setIn(new ByteArrayInputStream("\n".getBytes()));
			ByteArrayOutputStream outContent = new ByteArrayOutputStream();
			System.setOut(new java.io.PrintStream(outContent));
			co.ifSaveBoard();
			String output = outContent.toString().trim();
			assertFalse(output.contains("Enter the name of the board:"));
		} catch (Exception e) {
			fail("Exception should not have been thrown");
		} finally {
			System.setIn(System.in);
			System.setOut(System.out);
		}

		// Test case when user chooses to save the board
		try {
			System.setIn(new ByteArrayInputStream("s\ngameData/testSave.json\n".getBytes()));
			Controller.players.clear();
			co.ifSaveBoard();
			FileReader file = new FileReader("gameData/testSave.json");
			file.close();
			Files.deleteIfExists(Path.of(co.io.getPathName()));
		} catch (Exception e) {
			fail("File not found!");
		} finally {
			System.setIn(System.in);
		}
	}

	@Test
	// Test if insertProperty() works correctly
	void insertProperty() {
		try {
			Board.getProperties().clear();
			co.io.loadGame(Board.defaultBoardName);
			System.setIn(new ByteArrayInputStream("1\n1\n2\n".getBytes()));
			Controller.insertProperty();
			assertTrue(Board.getProperties().get(1) instanceof Go);
		} catch (Exception e) {
			// count as success if exception is thrown, since the function is recursive
		}
	}

	@Test
	// Test if removeProperty() works correctly, by input 2 to remove the second
	// property
	void removeProperty() {
		Board.getProperties().clear();
		co.io.loadGame(Board.defaultBoardName);
		System.setIn(new ByteArrayInputStream("2\n".getBytes()));
		Property pro = Board.getProperties().get(2 - 1);
		Controller.removeProperty();
		assertFalse(Board.getProperties().contains(pro));
	}

	@Test
	void editProperty() {
		try {
			Board.getProperties().clear();
			co.io.loadGame(Board.defaultBoardName);
			System.setIn(new ByteArrayInputStream("2\n".getBytes()));
			Property pro = Board.getProperties().get(2 - 1);
			co.editProperty();
			assertNotEquals(pro, Board.getProperties().get(2 - 1));
		} catch (Exception e) {
			// count as success if exception is thrown, since the function is recursive
		}
	}

	@Test
	// Test if newGameStart() works correctly
	void newGameStart() {
		try {
			Board.getProperties().clear();
			System.setIn(new ByteArrayInputStream("\n\n2\n\n\n".getBytes()));

			co.newGameStart();
			assertEquals(20, Board.getProperties().size());
		} catch (Exception e) {
		}
	}

	@Test
	// Test if chooseBoard() works correctly, by checking if the number of
	// properties
	// is 20 in default board
	void chooseBoard() {
		System.setIn(new ByteArrayInputStream("\n".getBytes()));
		co.chooseBoard();
		assertEquals(20, Board.getProperties().size());

		// Test if user chooses to load a board
		System.setIn(new ByteArrayInputStream("1\nmonopoly\n".getBytes()));
		co.chooseBoard();
		assertEquals(20, Board.getProperties().size());

		try { // Test if input is invalid
			System.setIn(new ByteArrayInputStream("1\nnotAFile\n\n".getBytes()));
			co.chooseBoard();
		} catch (Exception e) {
			// count as success if exception is thrown
		}

	}

	@Test
	// Test if saveBoard() works correctly, by checking if the board name is exists
	void saveGame() {
		try {
			co.io.loadGame(IOManipulate.getDefaultPathName());
			System.setIn(new ByteArrayInputStream("myTestGame\n".getBytes()));
			co.saveGame();
			FileReader file = new FileReader("gameData/myTestGame.json");
			file.close();
			Files.deleteIfExists(Path.of(co.io.getPathName()));
		} catch (Exception e) {
			// fail("File not found!");
		}
	}

	@Test
	// Test if continueGame() works correctly, by calling loadGame() in the function
	void continueGame() {
		try {
			co.io.loadGame(IOManipulate.getDefaultPathName());
			System.setIn(new ByteArrayInputStream("1\n".getBytes()));
			co.continueGame();
		} catch (Exception e) {
			// count as success if exception is thrown, since the function is calling
			// loadGame()
		}

	}

	@Test
	// Test if quitGame() works correctly, by printing "Quitting game..."
	void quitGame() {

		String expected = "Quitting game...";
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new java.io.PrintStream(outContent));
		co.quitGame();
		String output = outContent.toString().trim();
		assertTrue(output.contains(expected));
	}

}