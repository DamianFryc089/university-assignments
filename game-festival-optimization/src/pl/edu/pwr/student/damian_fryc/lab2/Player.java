package pl.edu.pwr.student.damian_fryc.lab2;

import java.util.Arrays;

public class Player {
	int id;
	int[] preferences;

	float satisfaction = 0;

	Player(int id, int[] preferences) {
		this.id = id;
		this.preferences = Arrays.copyOf(preferences, preferences.length);
	}

	// Copy constructor
	public Player(Player original) {
		this.id = original.id;
		this.satisfaction = original.satisfaction;

		// Deep copy of the preferences array
		this.preferences = new int[original.preferences.length];
		System.arraycopy(original.preferences, 0, this.preferences, 0, original.preferences.length);
	}
}
