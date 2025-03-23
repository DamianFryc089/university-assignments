package pl.edu.pwr.student.damian_fryc.lab2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Solver {
	private ArrayList<Game> games;
	private ArrayList<Player> players;
	private ArrayList<Table> tables;

	private float bestCombinationNumber = Float.NEGATIVE_INFINITY;
	private long startTime;
	private float minimumTime = 0;

	private float[] weights;

	private OptimizationResult optimizationResult;
	private int chairCount = 0;

	Solver(ArrayList<Game> games, ArrayList<Player> players, ArrayList<Table> tables, float[] weights, float minimumTime) {
		this.games = games;
		this.players = players;
		this.tables = tables;
		this.weights = weights;
		this.minimumTime = minimumTime;
	}

	private void optimisation() {

		// sorting players by preferences count
		players.sort(Comparator.comparingInt(p -> p.preferences.length));
		tables.sort(Comparator.comparingInt(p -> p.maxPlayersAtTheTable));
		games.sort(Comparator.comparingInt(p -> p.maxPlayer));

		int maxTableSize = 0;
		for (Table table : tables)
			if(table.maxPlayersAtTheTable > maxTableSize)
				maxTableSize = table.maxPlayersAtTheTable;

		// finding games that require more players than maximum amount of seats
		Iterator<Game> gIter = games.iterator();
		int minGameSize = 9999;
		while (gIter.hasNext()) {
			Game game = gIter.next();
			if(game.minPlayer < minGameSize)  minGameSize = game.minPlayer;
			if (game.maxPlayer > maxTableSize) {
				game.maxPlayer = maxTableSize;
				if(game.maxPlayer < game.minPlayer)
					gIter.remove();
			}
		}
		// removing too small tables
		Iterator<Table> tIter = tables.iterator();
		while (tIter.hasNext()) {
			Table table = tIter.next();
			if (table.maxPlayersAtTheTable < minGameSize)
				tIter.remove();
		}

	}

	public OptimizationResult findBestCombination() {
		startTime = System.currentTimeMillis();
		optimisation();
		for (Table table : tables)
			chairCount += table.maxPlayersAtTheTable;
		assignPlayer(0);

		optimizationResult.findingTime = (float) ((System.currentTimeMillis() - startTime) / 1000.0);
		return optimizationResult;
	}

	private void assignPlayer(int i) {
			// A solution has been found, and the algorithm has been running for at least 'minimumTime' seconds
		if(bestCombinationNumber != Float.NEGATIVE_INFINITY && (System.currentTimeMillis() - startTime) > 1000 * minimumTime)
			return;

		// The function has iterated through all players
		if(i == players.size()) {
			assignGameToTable(0);
			return;
		}

			// Assign players to games based on their preferences
		Player player = players.get(i);
		for (int j = 0; j < player.preferences.length; j++) {
			for (int k = 0; k < games.size(); k++) {
				if(games.get(k).id == player.preferences[j]) {
					if(!games.get(k).assignPlayer(player, j)) continue; // game has no free slots
					assignPlayer(i+1);
					games.get(k).removePlayer(player);
				}
			}
				// This will proceed by skipping the current player
			assignPlayer(i+1);
		}
	}

	private void assignGameToTable(int i) {
			// If all tables have been processed, check if the current setup is the best
		if(i == tables.size()) {
			checkIfBest();
			return;
		}
			// Iterate through each game to check if it is active and unassigned to a table
		for (int j = 0; j < games.size(); j++) {
			if(games.get(j).playing && games.get(j).assignedToTable == 0) {
					// Attempt to add the game to the current table; skip if the table is too small.
				if(!tables.get(i).addGame(games.get(j))) continue;
				assignGameToTable(i+1);
			}
		}
			// If the number of active games matches the games assigned to tables
		if(Game.playingGames == Game.gamesOnTables) {
			checkIfBest();
			tables.get(i).clearGames();
			return;
		}

		assignGameToTable(i+1);
		tables.get(i).clearGames();
	}

	private void checkIfBest(){
		int tablePenelty = Table.usedTables - Game.gamesOnTables;
		if(tablePenelty > 0) tablePenelty = 0;

			// Accumulate satisfaction scores and count the total number of assigned players
		float playerSatisfactionCount = 0;
		int playingPlayerCount = 0;
		for (Table table : tables){
			for (Game game : table.gamesOnTable){
				for (Player player : game.assignedPlayers){
					if (player == null) continue;
					playerSatisfactionCount += player.satisfaction;
					playingPlayerCount++;
				}
			}
		}

		float combinationNumber = weights[0] * playingPlayerCount + weights[1] * playerSatisfactionCount + weights[2] * tablePenelty;
		if(combinationNumber > bestCombinationNumber){
			bestCombinationNumber = combinationNumber;

				// Set best values in OptimizationResult object
			optimizationResult = new OptimizationResult();
			for (Table table : tables) {
				optimizationResult.tables.add(new Table(table));
			}
			optimizationResult.playingPlayers = playingPlayerCount;
			optimizationResult.allPlayers = players.size();
			optimizationResult.unusedSeats = chairCount - playingPlayerCount;
			optimizationResult.sumOfSatisfactions = playerSatisfactionCount;
			optimizationResult.tablePenalty = -tablePenelty;
			optimizationResult.weights = weights;
			optimizationResult.score = bestCombinationNumber;
		}
	}

}
