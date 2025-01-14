package com.monopoly;

import com.monopoly.propertyType.Property;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {

    public static ArrayList<Player> players = new ArrayList<>();
    public static int round = 1;
    private final int endGameTurn = 100;
    protected IOManipulate io = new IOManipulate();

    public void gameStart() {
        Board.printBoard();
        System.out.println("Game start!");
        ArrayList<Player> bankruptPlayers = new ArrayList<>();
        while (true) {
            if (round != 1) {
                for (Player player : players) {
                    if (player.getMoney() < 0) {
                        System.out.println(player.getName() + " is bankrupt.");
                        player.bankrupt();
                        bankruptPlayers.add(player);
                    }
                }
                for (Player player : bankruptPlayers) {
                    players.remove(player);
                }
                bankruptPlayers.clear();

                if (round > endGameTurn || players.size() <= 1) {
                    System.out.println("Game over!");
                    foundWinner();
                    quitGame();
                    break;
                }

            }
            System.out.println("*********************************************************");
            System.out.println("Round " + round + " start.");
            for (Player player : players) {
                System.out.println("=========================================================");
                System.out.println(player.getName() + "'s turn.");
                System.out.println("You currently at " + player.getCurrentSquare().getName() + " square.");
                System.out.println("You have $" + player.getMoney() + ".");
                playerOpt(player);
                System.out.println("=========================================================");
            }
            System.out.println("Round " + round + " end.");
            round++;
            if (askSave() == 1) {
                break;
            }
        }

    }

    public int askSave() {
        System.out.println("""
                Do you want to save the game?
                Press Enter : To continue.
                Press 's'   : To save the game.
                Enter: """);
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        if (str.equals("s")) {
            saveGame();
            return 1;
        }
        return 0;
    }

    public void playerOpt(Player player) {
        if (player.isInJail()) {
            player.move(0);
            return;
        }
        System.out.print("""
                Press 1: To throw dice.
                Press 2: To show gameboard.
                Press 3: To show player info.
                Press 4: To query the next player.
                Enter: """);
        Scanner sc = new Scanner(System.in);
        String num = sc.nextLine();
        switch (num) {
            case "1":
                player.move(rollDice());
                break;
            case "2":
                Board.printBoard();
                playerOpt(player);
                break;
            case "3":
                playerInfo(player);
                playerOpt(player);
                break;
            case "4":
                queryNextPlayer(player);
                playerOpt(player);
                break;
            default:
                System.out.println("Error input!");
                playerOpt(player);
        }
    }

    public void queryNextPlayer(Player player) {
        int idx = players.indexOf(player);
        if (idx == players.size() - 1) {
            idx = 0;
        } else {
            idx++;
        }
        System.out.println("The next player is " + players.get(idx).getName() + ".");
    }
    public void foundWinner() {
        ArrayList<Player> winners = new ArrayList<>();
        Player winner = null;
        // find the player with the highest positive money
        for (Player player : players) {
            if ((winner == null || player.getMoney() > winner.getMoney()) && player.getMoney() >= 0) {
                winner = player;
                winners.clear();
                winners.add(player);
            } else if (winner != null && player.getMoney() == winner.getMoney()) {
                winners.add(player);
            }
        }
        if (!winners.isEmpty()) {
            for (Player player : winners) {
                System.out.println("The winner is " + player.getName() + " with $" + player.getMoney() + ".");
            }
        } else {
            System.out.println("Draw! All players have negative amount of money.");
        }
    }

    public void playerInfo(Player player) {
        System.out.println("""
                Which player's info do you want to see?
                Press 1: To see you own info.
                Press 2: To see all players' info.
                Press 3: To see a specific player's info.
                Enter: """);
        Scanner sc = new Scanner(System.in);
        while (true) {
            String num = sc.nextLine();
            if (num.equals("1")) {
                player.printStatusWithMoney();
            }
            if (num.equals("2")) {
                for (Player p : players) {
                    p.printStatus();
                }
            }
            if (num.equals("3")) {
                int idx = 0;
                for (int i = 0; i < players.size(); i++)
                    System.out.println("Press " + (i + 1) + ": To see " + players.get(i).getName() + "'s info.");
                try {
                    idx = Integer.parseInt(sc.nextLine()) - 1;
                    if (idx < 0 || idx >= players.size()) {
                        System.out.println("Error input!");
                        continue;
                    }
                }catch (Exception e){continue;}
                players.get(idx).printStatus();
            }
            break;
        }
    }

    public int rollDice() {
        int diceValue1 = Dice.roll();
        int diceValue2 = Dice.roll();
        int num = diceValue1 + diceValue2;
        System.out.println("You rolled " + diceValue1 + " + " + diceValue2 + " = " + num + ".");
        return num;
    }

    public void gameOpt() throws IOException {
        System.out.print("""
                Press Enter: To start a new game.
                Press 1: To continue previous game.
                Enter: """);
        Scanner sc = new Scanner(System.in);
        String num = sc.nextLine();
        switch (num) {
            case "" -> newGameStart();
            case "1" -> continueGame();
            default -> {
                System.out.println("Error input!");
                gameOpt();
            }
        }
    }

    public static void playerCreate() {
        System.out.println("Enter the number of players between 2 and 6: ");
        Scanner sc = new Scanner(System.in);
        String temp = sc.nextLine();
        int num = 2;
        try {
            num = Integer.parseInt(temp);
            if (num < 2 || num > 6) {
                System.out.println("Number of players should be between 2 and 6.");
                playerCreate();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            playerCreate();
            return;
        }
        System.out.println("""
                Enter the names of players.
                If you want to use default names, press enter and leave it blanks.\n""");
        for (int i = 0; i < num; i++) {
            System.out.println("Enter the name of player " + (i + 1) + ": ");
            String name = sc.nextLine();
            if (name.equals(""))
                name = "Player " + (i + 1);
            Boolean ifSameName = false;
            for (Player player : players) {
                if (player.getName().equals(name)) {
                    System.out.println("Name already exists.");
                    i--;
                    ifSameName = true;
                    break;
                }
            }
            if (!ifSameName) {
                Player newPlayer = new Player(name);
                newPlayer.setCurrentSquare(Board.getProperties().get(0));
                players.add(newPlayer);
            }
        }
    }

    public void ifEditBoard() {
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                Do you want to edit the board?
                Press Enter : To continue using current board.
                Press 1     : To edit the board.
                Enter: """);
        String num = sc.nextLine();
        if (num.equals("1")) {
            editBoard();
        }
    }

    public void editBoard() {
        System.out.println("Editing board...");
        Board.printBoard();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Enter 1: Insert property
                    Enter 2: Remove property
                    Enter 3: Edit property
                    Enter 0: Finish editing
                    Enter: """);
                String choice = sc.nextLine();
                switch (choice) {
                    case "1" -> {
                        insertProperty();
                        editBoard();
                        return;
                    }
                    case "2" -> {
                        removeProperty();
                        editBoard();
                        return;
                    }
                    case "3" -> {
                        editProperty();
                        editBoard();
                        return;
                    }
                    case "0" -> {
                        ifSaveBoard();
                        return;
                    }
                    default -> System.out.println("Invalid input. Please enter a correct number.");
                }
        }
    }

    public void ifSaveBoard() {
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                Do you want to save the board?
                Press s     : To save the board.
                Press Enter : To continue without saving this edited board.
                Enter: """);
        String save = sc.nextLine();
        if (save.equals("s")) {
            System.out.println("Enter the name of the board: ");
            String boardName = sc.nextLine();
            io.saveGame(boardName);
        }
    }

    public static void insertProperty() {
        System.out.println("Inserting property...");
        Property pro = Board.createProperty();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the position of the property you want to insert: ");
        String temp = sc.nextLine();
        int position = Integer.parseInt(temp);
        Board.insertProperty(pro, position);
        System.out.println(pro.getName() + " is inserted successfully to " + pro.getPosition() + " position.");
    }

    public static void removeProperty() {
        System.out.println("Removing property...");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the position of the property you want to remove: ");
        String temp = sc.nextLine();
        int position = Integer.parseInt(temp);
        Property pro = Board.removeProperty(position);
        System.out.println(pro.getName() + " is removed successfully.");
    }

    public void editProperty() {
        System.out.println("Editing property...");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the position of the property you want to edit: ");
        String temp = sc.nextLine();
        int position = Integer.parseInt(temp);
        for (Property pro : Board.getProperties()) {
            if (pro.getPosition() == position) {
                pro.editProperty();
                break;
            }
        }
    }

    public void newGameStart() {
        System.out.println("Starting a new game...");
        chooseBoard();
        io.loadGame(Board.defaultBoardName);
        Board.printBoard();
        ifEditBoard();
        playerCreate();
        gameStart();
    }

    public void chooseBoard() {
        System.out.println("""
                Press Enter : To use default Board (Board.json).
                Press 1     : To use custom Board.
                Enter: """);
        Scanner sc = new Scanner(System.in);
        String num = sc.nextLine();
        String name = "";
        if (num.equals("1")) {
            System.out.println("""
                    Enter the name of the file.
                    Please ensure the file is located under gameBoard folder.
                    If you want to use default file(Board.json), press enter and leave it blanks.
                    Enter: """);
            name = sc.nextLine();
        }
        boolean foundFile = Board.setPathNameIfExists(name);
        if (!foundFile) {
            chooseBoard();
        }
    }

    public void saveGame() {
        System.out.println("Saving game...");
        try {
            System.out.println("""
                    Enter the name of the file.
                    If you want to use default name(monopoly.json), press enter and leave it blanks.
                    Enter: """);
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine();
            io.saveGame(name);
            quitGame();
        } catch (Exception e) {
            System.out.println("Error saving the game: " + e.getMessage());
        }
    }

    public void continueGame() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Continuing previous game...");
        System.out.println("""
                Enter the name of the file.
                If you want to use default name(monopoly.json), press enter and leave it blanks.
                Enter: """);
        String fileName = sc.nextLine();
        if (io.loadGame(fileName) == -1) {
            gameOpt();
            return;
        }
        gameStart();
    }

    public void quitGame() {
        System.out.println("Thank you for playing Monopoly.");
        System.out.println("Quitting game...");
    }
}