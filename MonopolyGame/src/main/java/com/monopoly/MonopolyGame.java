package com.monopoly;

public class MonopolyGame {

	public static void main(String[] args) {
		System.out.println("Welcome to the Monopoly Game.");
		Controller co = new Controller();
		try {
			co.gameOpt();
		}catch (Exception e){
			System.out.println("Error in starting game.");
		}
	}
}