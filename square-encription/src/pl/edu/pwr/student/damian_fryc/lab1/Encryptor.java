package pl.edu.pwr.student.damian_fryc.lab1;

public class Encryptor {
	private final boolean keyNotFoundAlert;
	private char[][] key;
	public Encryptor(char[][] newKey, boolean keyNotFoundAlert) {
		setKey(newKey);
		this.keyNotFoundAlert = keyNotFoundAlert;
	}

	public void setKey(char[][] key) {

			// check if the key is a square
		for (int i = 0; i < key.length; i++) {
			if(key[i].length != key.length) {
				this.key = null;
				return;
			}
		}
			// key to lower case
		for (int i = 0; i < key.length; i++) {
			for (int j = 0; j < key[i].length; j++) {
				key[i][j] = Character.toLowerCase(key[i][j]);
			}
		}

		this.key = key;
	}

	public void setKey(String key) {
		double squareValue = Math.sqrt(key.length());
		if (squareValue != (int)squareValue)
			System.out.println("Wrong key");
		else{
			char[][] newKey = new char[(int) squareValue][(int) squareValue];
			key = key.toLowerCase();
			for (int i = 0; i < key.length() / (int) squareValue; i++) {
				newKey[i] = key.substring(i * (int) squareValue , (i + 1) * (int) squareValue).toCharArray();
			}
			this.key = newKey;
		}
	}

	public void printKey() {
		for (int i = 0; i < key.length; i++) {
			for (int j = 0; j < key[i].length; j++) {
				System.out.print(key[i][j] + " ");
			}
			System.out.print('\n');
		}
	}

	private char[] getColumn(int col) {
		char[] column = new char[key[0].length];
		for (int i = 0; i < key[0].length; i++) {
			column[i] = key[i][col];
		}
		return column;
	}

	private int[][] locatePair(char firstLetter, char secondLetter) {
		int[][] pairPositions = new int[][]{{-1,-1},{-1,-1}};

		for (int j = 0; j < key.length; j++) {
			for (int k = 0; k < key[j].length; k++) {
				if (key[j][k] == firstLetter)
					pairPositions[0] = new int[]{j, k};
				if (key[j][k] == secondLetter)
					pairPositions[1] = new int[]{j, k};
			}
		}
		return pairPositions;
	}

	public String encrypt(String text) {
		if(key == null) return "Null key";
		text = text.toLowerCase();
		if(text.length() % 2 == 1) text += 'X';

		StringBuilder encrypted = new StringBuilder();
		for (int i = 0; i < text.length()-1; i+=2) {
			int[][] pairPositions = locatePair(text.charAt(i), text.charAt(i+1));

				// string ending with 'X', so the last character isn't encrypted
			if(pairPositions[1][0] == -1 && text.charAt(i + 1) == 'X') {
				encrypted.append(text.charAt(i));
				i++;
				continue;
			}
				// first or second character isn't the key so, both of them aren't encrypted
			if(pairPositions[0][0] == -1 || pairPositions[1][0] == -1) {
				if(pairPositions[0][0] == -1 && keyNotFoundAlert) 	System.out.println(text.charAt(i) + " not found in the key");
				if(pairPositions[1][0] == -1 && keyNotFoundAlert) 	System.out.println(text.charAt(i + 1) + " not found in the key");
				encrypted.append(text.charAt(i)).append(text.charAt(i + 1));
				continue;
			}

				// p and q are the same letter
			if(pairPositions[0][0] == pairPositions[1][0] && pairPositions[0][1] == pairPositions[1][1]){
				encrypted.append(text.charAt(i)).append("X").append(text.charAt(i + 1));
			}
				// p and q are in the same row
			else if(pairPositions[0][0] == pairPositions[1][0]){
				char[] row = key[pairPositions[0][0]];
				encrypted.append(row[(pairPositions[0][1] + 1) % row.length]);
				encrypted.append(row[(pairPositions[1][1] + 1) % row.length]);
			}
				// p and q are in the same column
			else if(pairPositions[0][1] == pairPositions[1][1]){
				char[] column = getColumn(pairPositions[0][1]);
				encrypted.append(column[(pairPositions[0][0] + 1) % column.length]);
				encrypted.append(column[(pairPositions[1][0] + 1) % column.length]);

			}
				// p and q are in the corners
			else{
				encrypted.append(key[pairPositions[1][0]][pairPositions[0][1]]);
				encrypted.append(key[pairPositions[0][0]][pairPositions[1][1]]);
			}

		}
		if(encrypted.length() % 2 == 1) encrypted.append('X');
		return encrypted.toString();
	}

	public String decrypt(String text){
		if(key == null) return "Null key";

			// Count Xs to add extra if not even
		int xCount = 0;
			// text.length() - 1, because the last X shouldn't be counted
		for (int i = 0; i < text.length() - 1; i++)
			if(text.charAt(i) == 'X') xCount++;
		if((text.length() + xCount) % 2 == 1) text += 'X';

		StringBuilder decrypted = new StringBuilder();

		for (int i = 0; i < text.length()-1; i+=2) {
			int[][] pairPositions = locatePair(text.charAt(i), text.charAt(i+1));


				// check if X on second place, which means that is a sequence of the same character  (p and q are the same)
				// or an end of the string
			if(pairPositions[1][0] == -1 && text.charAt(i + 1) == 'X') {
						// odd number of repeated - makes 2 Xs on the end
					if(text.charAt(i) == 'X')
						continue;

					decrypted.append(text.charAt(i));
						// if not end add the character again
					if(i + 2 < text.length())
						decrypted.append(text.charAt(i));
					i++;
					continue;
			}

				// first or second character isn't the key so, both of them aren't decrypted
			if(pairPositions[0][0] == -1 || pairPositions[1][0] == -1) {
				if(pairPositions[0][0] == -1 && keyNotFoundAlert) 	System.out.println(text.charAt(i) + " not found in the key");
				if(pairPositions[1][0] == -1 && keyNotFoundAlert) 	System.out.println(text.charAt(i + 1) + " not found in the key");
				decrypted.append(text.charAt(i)).append(text.charAt(i + 1));
				continue;
			}


				// p and q are in the same row
			if(pairPositions[0][0] == pairPositions[1][0]){
				char[] row = key[pairPositions[0][0]];
				decrypted.append(row[(pairPositions[0][1] + row.length - 1) % row.length]);
				decrypted.append(row[(pairPositions[1][1] + row.length - 1) % row.length]);
			}
				// p and q are in the same column
			else if(pairPositions[0][1] == pairPositions[1][1]){
				char[] column = getColumn(pairPositions[0][1]);
				decrypted.append(column[(pairPositions[0][0] + column.length - 1) % column.length]);
				decrypted.append(column[(pairPositions[1][0] + column.length - 1) % column.length]);

			}
				// p and q are in the corners
			else{
				decrypted.append(key[pairPositions[1][0]][pairPositions[0][1]]);
				decrypted.append(key[pairPositions[0][0]][pairPositions[1][1]]);
			}

		}

		return decrypted.toString();
	}
}
