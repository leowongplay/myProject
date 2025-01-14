package com.monopoly;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.monopoly.propertyType.*;
import java.io.*;

public class IOManipulate {
    private static final String defaultPathName = "gameData/monopoly.json";
    private StringBuffer pathName = new StringBuffer(defaultPathName);

    public static String getDefaultPathName() {
        return defaultPathName;
    }

    public boolean setPathNameIfExists(String newPathName) {
        try {
            if (newPathName.equals("")) {
                pathName = new StringBuffer().append(defaultPathName);
            } else {
                pathName = new StringBuffer()
                        .append(newPathName.startsWith("gameData/") ? "" : "gameData/")
                        .append(newPathName)
                        .append(newPathName.endsWith(".json") ? "" : ".json");
            }
            BufferedReader reader = new BufferedReader(new FileReader(pathName.toString()));
            reader.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error: File not found!");
            return false;
        }
    }

    public void setPathName(String newPathName) {
        this.pathName = new StringBuffer()
                .append(newPathName.startsWith("gameData/") ? "" : "gameData/")
                .append(newPathName)
                .append(newPathName.endsWith(".json") ? "" : ".json");
    }

    public String getPathName() {
        return this.pathName.toString();
    }

    public int loadGame(String pathName) {
        if (!setPathNameIfExists(pathName)) {
            setPathName(defaultPathName);
            return -1;
        }
        return loadGame();
    }

    private int loadGame() {
        Board.getProperties().clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(getPathName()))) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray propertyArray = jsonObject.getAsJsonArray("properties");
            for (JsonElement element : propertyArray) {
                String name = element.getAsJsonObject().get("name").getAsString();
                int position = element.getAsJsonObject().get("position").getAsInt();

                if (element.getAsJsonObject().has("price")) {
                    // If 'price' exists, it's a District
                    int price = element.getAsJsonObject().get("price").getAsInt();
                    int rent = element.getAsJsonObject().get("rent").getAsInt();
                    Board.addProperty(new District(position, name, price, rent));
                } else {
                    // Otherwise, if name others, it belongs to certain PropertyType
                    switch (name) {
                        case "Go" -> {
                            if (element.getAsJsonObject().has("goMoney")) {
                                Board.addProperty(
                                        new Go(position, element.getAsJsonObject().get("goMoney").getAsInt()));
                            } else {
                                Board.addProperty(new Go(position));
                            }
                        }
                        case "Chance" -> {
                            if (element.getAsJsonObject().has("addValue")) {
                                Board.addProperty(
                                        new Chance(position, element.getAsJsonObject().get("addValue").getAsInt(),
                                                element.getAsJsonObject().get("subtractValue").getAsInt()));
                            } else {
                                Board.addProperty(new Chance(position));
                            }
                        }
                        case "Income Tax" -> {
                            if (element.getAsJsonObject().has("incomeTaxPercentage")) {
                                Board.addProperty(new IncomeTax(position,
                                        element.getAsJsonObject().get("incomeTaxPercentage").getAsFloat()));
                            } else {
                                Board.addProperty(new IncomeTax(position));
                            }
                        }
                        case "Free Parking" -> Board.addProperty(new FreeParking(position));
                        case "Go To Jail" -> Board.addProperty(new GoToJail(position));
                        case "In Jail/Just Visiting" -> {
                            if (element.getAsJsonObject().has("costOfEscape")) {
                                Board.addProperty(new InJailORJustVisiting(position,
                                        element.getAsJsonObject().get("costOfEscape").getAsInt(),
                                        element.getAsJsonObject().get("turnsNeededToEscape").getAsInt()));
                            } else {
                                Board.addProperty(new InJailORJustVisiting(position));
                            }
                        }
                    }
                }
            }
            if (jsonObject.has("players")) {
                // Read Players
                JsonArray playerArray = jsonObject.getAsJsonArray("players");
                for (JsonElement element : playerArray) {
                    JsonObject playerObject = element.getAsJsonObject();
                    Player player = new Player(playerObject.get("name").getAsString());
                    player.setMoney("=", playerObject.get("money").getAsFloat());
                    player.setInJail(playerObject.get("inJail").getAsBoolean());
                    player.setJailTurns(playerObject.get("jailTurns").getAsInt());

                    String currSquareName = playerObject.get("currSquare").getAsString();
                    for (Property property : Board.getProperties()) {
                        if (property.getName().equals(currSquareName)) {
                            player.setCurrentSquare(property);
                            break;
                        }
                    }

                    JsonArray playerPropertiesArray = playerObject.getAsJsonArray("properties");
                    for (JsonElement propertyElement : playerPropertiesArray) {
                        String propertyName = propertyElement.getAsString();
                        for (Property property : Board.getProperties()) {
                            if (property.getName().equals(propertyName)) {
                                player.addProperty((District) property);
                                break;
                            }
                        }
                    }
                    Controller.players.add(player);
                }
                // Read round
                Controller.round = jsonObject.get("round").getAsInt();
            }
            reader.close();
            setPathName(defaultPathName);
            return 0;
        } catch (IOException e) {
            System.out.println("Error: File not found!");
            return -1;
        }
    }

    public int saveGame(String pathName) {
        if (pathName.equals("")) {
            setPathName(defaultPathName);
        } else {
            setPathName(pathName);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();

        // Create properties JSON array
        JsonArray propertyArray = new JsonArray();

        for (Property property : Board.getProperties()) {
            JsonObject propertyObject = new JsonObject();
            propertyObject.addProperty("name", property.getName());
            propertyObject.addProperty("position", property.getPosition());
            if (property instanceof District) {
                propertyObject.addProperty("price", ((District) property).getPrice());
                propertyObject.addProperty("rent", ((District) property).getRent());
            } else if (property instanceof Go) {
                propertyObject.addProperty("goMoney", ((Go) property).getGoMoney());
            } else if (property instanceof Chance) {
                propertyObject.addProperty("addValue", ((Chance) property).getAddValue());
                propertyObject.addProperty("subtractValue", ((Chance) property).getSubtractValue());
            } else if (property instanceof IncomeTax) {
                propertyObject.addProperty("incomeTaxPercentage", ((IncomeTax) property).getIncomeTaxPercentage());
            } else if (property instanceof InJailORJustVisiting) {
                propertyObject.addProperty("costOfEscape", ((InJailORJustVisiting) property).getCostOfEscape());
                propertyObject.addProperty("turnsNeededToEscape",
                        ((InJailORJustVisiting) property).getTurnsNeededToEscape());
            }
            propertyArray.add(propertyObject);
        }
        jsonObject.add("properties", propertyArray);

        if (!Controller.players.isEmpty()) {
            // Create players JSON array
            JsonArray playerArray = new JsonArray();
            for (Player player : Controller.players) {
                JsonObject playerObject = new JsonObject();
                playerObject.addProperty("name", player.getName());
                playerObject.addProperty("money", player.getMoney());
                playerObject.addProperty("currSquare", player.getCurrentSquare().getName());
                playerObject.addProperty("inJail", player.isInJail());
                playerObject.addProperty("jailTurns", player.getJailTurns());
                JsonArray playerPropertiesArray = new JsonArray();
                for (District property : player.getProperty()) {
                    playerPropertiesArray.add(property.getName());
                }
                playerObject.add("properties", playerPropertiesArray);
                playerArray.add(playerObject);
            }
            jsonObject.add("players", playerArray);

            // Add round
            jsonObject.addProperty("round", Controller.round);
        }
        // Write to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(this.pathName)))) {
            gson.toJson(jsonObject, writer);
            writer.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
