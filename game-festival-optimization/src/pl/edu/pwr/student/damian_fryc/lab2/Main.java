package pl.edu.pwr.student.damian_fryc.lab2;
import java.util.*;

public class Main {

	static String gamesFileName = "gry.txt";
	static String tablesFileName = "stoliki.txt";
	static String playersFileName = "preferencje.txt";
	static String weightsFileName = "wagi.txt";

	public static void main(String[] args) {
		ArrayList<Game> games;
		ArrayList<Table> tables;
		ArrayList<Player> players;

		Scanner scanner = new Scanner(System.in);

		while((games = DataLoader.loadGames(gamesFileName)).isEmpty()){
			System.out.println("Try to load data again ('x' to exit)");
			if(Objects.equals(scanner.nextLine(), "x"))
				System.exit(1);
		}

		while((tables = DataLoader.loadTables(tablesFileName)).isEmpty()){
			System.out.println("Try to load data again ('x' to exit)");
			if(Objects.equals(scanner.nextLine(), "x"))
				System.exit(1);
		}

		while((players = DataLoader.loadPlayers(playersFileName)).isEmpty()){
			System.out.println("Try to load data again ('x' to exit)");
			if(Objects.equals(scanner.nextLine(), "x"))
				System.exit(1);
		}

		float minimumTime = DataLoader.setTime(scanner);

			// Gets weights in this order: program arguments -> file -> standard input
		float[] weights = DataLoader.setWeights(scanner, args, weightsFileName);

		Solver solver = new Solver(games, players, tables, weights, minimumTime);

		OptimizationResult optimizationResult = solver.findBestCombination();
		System.out.println(optimizationResult);
		
		System.out.println("\nPress anything to exit!");
		scanner.nextLine();
    }
}