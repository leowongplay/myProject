package com.monopoly.propertyType;

import com.monopoly.Player;
import java.util.ArrayList;

public abstract class Property {
	private int position;
	private String name;
	private ArrayList<Player> havePlayer = new ArrayList<>();

	public abstract void action(Player player);

	public abstract void editProperty();

	public Property(int position, String name) {
		this.position = position;
		this.name = name;
	}

	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Player> getHavePlayer() {
		return this.havePlayer;
	}

	public void addHavePlayer(Player player) {
		this.havePlayer.add(player);
	}

	public void removeHavePlayer(Player player) {
		if (this.havePlayer.contains(player))
			this.havePlayer.remove(player);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[position= " + position + ", name= " + name);
		if (havePlayer.size() > 0) {
			sb.append(", Have Player= [");
			for (int i = 0; i < havePlayer.size(); i++) {
				sb.append(havePlayer.get(i).getName());
				if (i != havePlayer.size() - 1)
					sb.append(", ");
			}
			sb.append("]");
		}
		sb.append("]");
		return sb.toString();
	}
}
