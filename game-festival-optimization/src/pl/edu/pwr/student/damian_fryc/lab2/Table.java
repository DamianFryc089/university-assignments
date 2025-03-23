package pl.edu.pwr.student.damian_fryc.lab2;

import java.util.ArrayList;

public class Table {
	static int usedTables = 0;

	int id;
	int maxPlayersAtTheTable;

	ArrayList<Game> gamesOnTable = new ArrayList<>();
	int playersAtTheTable = 0;

	Table(int id, int maxPlayersAtTheTable) {
		this.id = id;
		this.maxPlayersAtTheTable = maxPlayersAtTheTable;
	}

	// Copy constructor
	Table(Table original) {
		this.id = original.id;
		this.maxPlayersAtTheTable = original.maxPlayersAtTheTable;
		this.playersAtTheTable = original.playersAtTheTable;

		this.gamesOnTable = new ArrayList<>();
		for (Game game : original.gamesOnTable) {
			this.gamesOnTable.add(new Game(game));
		}
	}
	boolean addGame(Game game) {
		if(playersAtTheTable + game.assignedPlayerCount <= maxPlayersAtTheTable)
		{
			if(gamesOnTable.isEmpty())
				usedTables++;
			gamesOnTable.add(game);
			game.assignedToTable = id;
			Game.gamesOnTables++;
			playersAtTheTable += game.assignedPlayerCount;
			return true;
		}
		else
			return false;
	}

	void clearGames() {
		if(gamesOnTable.isEmpty()) return;

		for (Game game : gamesOnTable) {
			game.assignedToTable = 0;
			Game.gamesOnTables--;
		}
		playersAtTheTable = 0;
		usedTables--;
		gamesOnTable.clear();
	}
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Table ").append(id).append(":\n");
		for(Game game : gamesOnTable){
			stringBuilder.append("- Game ").append(game.id).append(": ").append(game).append("\n");
		}
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}
}
