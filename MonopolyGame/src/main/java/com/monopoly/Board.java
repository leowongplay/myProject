package com.monopoly;

import com.monopoly.propertyType.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Board {
    private static ArrayList<Property> properties = new ArrayList<>();
    public static final String defaultBoardName = "gameData/Board.json";
    private static StringBuffer pathName = new StringBuffer(defaultBoardName);

    public static ArrayList<Property> getProperties() {
        return properties;
    }

    public static void addProperty(Property property) {
        properties.add(property);
    }

    public static String getPathName() {
        return pathName.toString();
    }

    public static boolean setPathNameIfExists(String newPathName) {
        try {
            if (newPathName.equals("")) {
                pathName = new StringBuffer().append(defaultBoardName);
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

    public static void setPathName(String newPathName) {
        if (newPathName.equals("")) {
            pathName = new StringBuffer().append(defaultBoardName);
        } else {
            pathName = new StringBuffer()
                    .append(newPathName.startsWith("gameData/") ? "" : "gameData/")
                    .append(newPathName)
                    .append(newPathName.endsWith(".json") ? "" : ".json");
        }
    }

    public static void insertProperty(Property property, int position) {
        if (property == null) {
            System.out.println("Property not found.");
            return;
        }
        if (position < 0 || position >= properties.size()) {
            System.out.println("Invalid position.");
            return;
        }
        property.setPosition(position);
        for (Property pro : properties) {
            if (pro.getPosition() >= property.getPosition()) {
                pro.setPosition(pro.getPosition() + 1);
            }
        }
        properties.add(property);

        sortBoard();
    }

    public static Property removeProperty(int position) {
        if (position < 0 || position >= properties.size()) {
            System.out.println("Property not found.");
            return null;
        }
        Property property = getProperties().get(position - 1);
        properties.remove(property);
        for (Property pro : properties) {
            if (pro.getPosition() > position) {
                pro.setPosition(pro.getPosition() - 1);
            }
        }
        sortBoard();
        return property;
    }

    private static void printAvailableProperties() {
        System.out.println("Here are the properties you can add to the board.");
        System.out.println("Enter 1: Go");
        System.out.println("Enter 2: Chance");
        System.out.println("Enter 3: Income Tax");
        System.out.println("Enter 4: Free Parking");
        System.out.println("Enter 5: Go To Jail");
        System.out.println("Enter 6: In Jail/Just Visiting");
        System.out.println("Enter 7: District");
        System.out.println("Enter value: ");
    }

    public static Property createProperty() {
        Scanner sc = new Scanner(System.in);
        printAvailableProperties();
        ArrayList<District> districts = new ArrayList<>();
        for (Property property : properties) {
            if (property instanceof District) {
                districts.add((District) property);
            }
        }
        while (true) {
            try {
                String propertyNumber = sc.nextLine();
                switch (Integer.parseInt(propertyNumber)) {
                    case 1:
                        return new Go(-1);
                    case 2:
                        return new Chance(-1);
                    case 3:
                        return new IncomeTax(-1);
                    case 4:
                        return new FreeParking(-1);
                    case 5:
                        return new GoToJail(-1);
                    case 6:
                        return new InJailORJustVisiting(-1);
                    case 7:
                        System.out.println("Here are the districts you can add to the board.");
                        int i = 1;
                        System.out.println("Enter 0: To create custom district.");
                        for (District dis : districts) {
                            System.out.println("Enter " + i + ": " + dis.toStringWithoutPosition());
                            i++;
                        }
                        String temp = sc.nextLine();
                        int districtIdx = Integer.parseInt(temp);
                        if (districtIdx == 0) {
                            return new District(-1).createDistrict();
                        } else if (districtIdx < 0 || districtIdx > Board.getProperties().size()) {
                            System.out.println("Invalid input. Please enter a correct number to be added.");
                            continue;
                        }
                        District district = new District(-1);
                        district.setName(districts.get(districtIdx - 1).getName());
                        district.setPrice(districts.get(districtIdx - 1).getPrice());
                        district.setRent(districts.get(districtIdx - 1).getRent());
                        return district;
                    default:
                        System.out.println("Invalid input. Please enter a correct number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a correct number.");
            }
        }

    }

    public static void sortBoard() {
        properties.sort(Comparator.comparingInt(Property::getPosition));
        // To avoid duplicate positions
        for (int i = 0; i < properties.size() - 1; i++) {
            if (properties.get(i).getPosition() == properties.get(i + 1).getPosition()) {
                properties.remove(i + 1);
                break;
            }
        }
    }

    public static void printBoard() {
        System.out.println("Printing Board.....");
        for (Property property : properties) {
            System.out.println(property);
        }
    }
}
