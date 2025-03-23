package pl.edu.pwr.student.damian_fryc.lab2;

public class Game {
	static int playingGames = 0;
	static int gamesOnTables = 0;

	int id;
	int minPlayer;
	int maxPlayer;

	Player[] assignedPlayers;

	int assignedToTable = 0;
	int assignedPlayerCount = 0;
	boolean playing = false;

	Game(int id, int minPlayer, int maxPlayer) {
		this.id = id;
		this.minPlayer = minPlayer;
		this.maxPlayer = maxPlayer;
		assignedPlayers = new Player[maxPlayer];
	}

	// Copy constructor
	public Game(Game original) {
		this.id = original.id;
		this.minPlayer = original.minPlayer;
		this.maxPlayer = original.maxPlayer;
		this.assignedToTable = original.assignedToTable;
		this.assignedPlayerCount = original.assignedPlayerCount;
		this.playing = original.playing;

		this.assignedPlayers = new Player[assignedPlayerCount];
		for (int i = 0; i < assignedPlayerCount; i++) {
			this.assignedPlayers[i] = new Player(original.assignedPlayers[i]);
		}
	}
	boolean assignPlayer(Player player, int preferenceNumber){
		if(assignedPlayerCount + 1 > maxPlayer) return false;

		assignedPlayers[assignedPlayerCount] = player;
		assignedPlayerCount++;

		if(assignedPlayerCount >= minPlayer) {
			playing = true;
			playingGames++;
		}

		player.satisfaction = (float) 1 / (preferenceNumber+1);
		return true;
	}

	void removePlayer(Player player){

		assignedPlayerCount--;
		assignedPlayers[assignedPlayerCount] = null;
		if(assignedPlayerCount < minPlayer) {
			playing = false;
			playingGames--;
		}
	}

	@Override
	public String toString() {
		StringBuilder playerString = new StringBuilder();
		for (int i = 0; i < assignedPlayers.length; i++) {
			if(assignedPlayers[i] == null) continue;
			playerString.append(assignedPlayers[i].id).append("(").append(String.format("%.2f", assignedPlayers[i].satisfaction)).append("), ");
		}

		return playerString.substring(0, playerString.length() - 2);
	}
}
