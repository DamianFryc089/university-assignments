package pl.edu.pwr.student.damian_fryc.lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataLoader {
	public static ArrayList<Table>  loadTables(String tablesFileName){
		ArrayList<Table> tables = new ArrayList<>();

		try (Scanner scannerT = new Scanner(new File(tablesFileName))){
			while (scannerT.hasNextLine()) {
				String[] row = scannerT.nextLine().split("; ");
				tables.add(new Table(Integer.parseInt(row[0]), Integer.parseInt(row[1])));
			}
		} catch (FileNotFoundException e) {
			System.out.println("File \""+tablesFileName+"\" not found");
			tables = new ArrayList<>();
		} catch (NumberFormatException e) {
			System.out.println("Invalid file content in "+tablesFileName+" - " + e.getMessage());
			tables = new ArrayList<>();
		}

		return tables;
	}

	public static ArrayList<Game>  loadGames(String gamesFileName){
		ArrayList<Game> games = new ArrayList<>();
		try (Scanner scannerG = new Scanner(new File(gamesFileName))){
			while (scannerG.hasNextLine()) {
				String[] row = scannerG.nextLine().split("; ");
				for (int i = 0; i < Integer.parseInt(row[1]); i++) {
					if(Integer.parseInt(row[2]) > Integer.parseInt(row[3])) throw new NumberFormatException("Minimum player value cannot be greater than maximum: "
							+ Integer.parseInt(row[2]) + " > " + Integer.parseInt(row[3]));
					games.add(new Game(Integer.parseInt(row[0]), Integer.parseInt(row[2]), Integer.parseInt(row[3])));
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File \""+gamesFileName+"\" not found");
			games = new ArrayList<>();
		} catch (NumberFormatException e) {
			System.out.println("Invalid file content in "+gamesFileName+" - " + e.getMessage());
			games = new ArrayList<>();
		}
		return games;
	}

	public static ArrayList<Player>  loadPlayers(String playersFileName){
		ArrayList<Player> players = new ArrayList<>();
		try (Scanner scannerP = new Scanner(new File(playersFileName))){
			while (scannerP.hasNextLine()) {
				// spliting into id and preferences
				String[] row = scannerP.nextLine().split("; ", 2);
				if(row.length <= 1) throw new NumberFormatException("Player preferences cannot be empty: " + row[0]);
				String[] preferenceStrings = row[1].split(", ");
				int[] preferences = new int[preferenceStrings.length];
				for (int i = 0; i < preferenceStrings.length; i++)
					preferences[i] = Integer.parseInt(preferenceStrings[i]);
				players.add(new Player(Integer.parseInt(row[0]), preferences));
			}
		} catch (FileNotFoundException e) {
			System.out.println("File \""+playersFileName+"\" not found");
			players = new ArrayList<>();
		} catch (NumberFormatException e) {
			System.out.println("Invalid file content in "+playersFileName+" - " + e.getMessage());
			players = new ArrayList<>();
		}
		return players;
	}

	public static float[] loadWeights(String weightsFileName){
		float[] weights = new float[3];
		try (Scanner scannerW = new Scanner(new File(weightsFileName))){
			int i = 0;
			while (scannerW.hasNextFloat() && i < 3) {
				weights[i] = scannerW.nextFloat();
				i++;
			}
			if(i == 0) throw new NumberFormatException("Weight preferences cannot be empty: " + weightsFileName);
		} catch (FileNotFoundException e) {
			System.out.println("File \""+weightsFileName+"\" not found");
			return null;
		} catch (NumberFormatException e) {
			System.out.println("Invalid file content in "+weightsFileName+" - " + e.getMessage());
			return null;
		}
		return weights;
	}

	public static float setTime(Scanner scanner){
		System.out.println("Input minimum finding time in seconds (>=1)");
		while (!scanner.hasNextFloat()) {
			scanner.nextLine();
		}
		float time = scanner.nextFloat();
		if(time < 1) time = 1;
		return time;
	}

	public static float[] setWeights(Scanner scanner, String[] args, String weightsFileName){
		float[] weights = new float[3];

		if (args.length == 3) {
			try {
				weights[0] = Float.parseFloat(args[0]);
				weights[1] = Float.parseFloat(args[1]);
				weights[2] = Float.parseFloat(args[2]);
				return weights;
			} catch (NumberFormatException e) {
				System.out.println("Arguments are not valid: " + e.getMessage());
			}
		}
		if(new File(weightsFileName).exists()) {
			weights = loadWeights(weightsFileName);
			if(weights != null) return weights;
			else weights = new float[3];
		}

		System.out.println("Input W1 - player count weight");
		while (!scanner.hasNextFloat()) scanner.nextLine();
		weights[0] = scanner.nextFloat();
		scanner.nextLine();

		System.out.println("Input W2 - satisfaction weight");
		while (!scanner.hasNextFloat()) scanner.nextLine();
		weights[1] = scanner.nextFloat();
		scanner.nextLine();

		System.out.println("Input W3 - table penalty weight");
		while (!scanner.hasNextFloat()) scanner.nextLine();
		weights[2] = scanner.nextFloat();
		scanner.nextLine();
		return weights;
	}

}

