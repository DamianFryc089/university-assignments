package pl.edu.pwr.student.damian_fryc.lab2;

import java.util.ArrayList;

public class OptimizationResult {
	public float findingTime;
	public ArrayList<Table> tables = new ArrayList<>();
	public float score;
	public int tablePenalty;
	public int playingPlayers;
	public int allPlayers;
	public float sumOfSatisfactions;
	public int unusedSeats;
	public float[] weights;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Table table : tables)
			sb.append(table);
		sb.append("\nPlaying player: ").append(playingPlayers).append(", out of: ").append(allPlayers);
		sb.append("\nUnused seats: ").append(unusedSeats);
		sb.append("\nSatisfaction: ").append(sumOfSatisfactions);
		sb.append("\nTable penalty: ").append(tablePenalty);
		sb.append("\n\nW1 * Number of players + W2 * Total satisfaction of all players - W3 * Table penalty");
		sb.append("\n").append(weights[0]).append(" * ").append(playingPlayers).append(" + ").append(weights[1]).append(" * ").append(sumOfSatisfactions).append(" - ").append(weights[2]).append(" * ").append(tablePenalty);
		sb.append("\n\nOverall score: ").append(score);
		sb.append("\nTime: ").append(findingTime).append('\n');

		return sb.toString();
	}
}
