package pl.edu.pwr.student.damian_fryc.lab1;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Encryptor encryptor = new Encryptor(new char[][]
				{
//					{'a'}

//					{'a', 'b'},
//					{'c', 'd'},

//					{'a', 'b', 'c'},
//					{'d', 'e', 'f'},
//					{'g', 'h', 'i'},

					{'a', 'b', 'c', 'd', 'e'},
					{'f', 'g', 'h', 'i', 'j'},
					{'k', 'l', 'm', 'n', 'o'},
					{'p', 'q', 'r', 's', 't'},
					{'u', 'v', 'w', 'x', ' '}
				}, false);
		encryptor.printKey();

		while (true) {
			System.out.print("""
                    
                    Select mode:
                    0 - change key
                    1 - encrypt text
                    2 - decrypt text
                    3 - test
                    4 - exit
                    """);

			int mode = -1;
			if (scanner.hasNextInt())
				mode = scanner.nextInt();
			scanner.nextLine();

			switch (mode)
			{
				case 0:
					System.out.print("New key: ");
					encryptor.setKey(scanner.nextLine());
					encryptor.printKey();
					break;
				case 1:
					System.out.print("To encrypt: ");
					String encrypted = encryptor.encrypt(scanner.nextLine());
					System.out.println(" Encrypted: " + encrypted);
					break;
				case 2:
					System.out.print("To decrypt: ");
					String decrypted = encryptor.decrypt(scanner.nextLine());
					System.out.println(" Decrypted: " + decrypted);
					break;
				case 3:
					System.out.print("To encrypt: ");
					String toEncrypt = scanner.nextLine();

					String encryptedTest = encryptor.encrypt(toEncrypt);
					System.out.println(" Encrypted: " + encryptedTest + " (" + encryptedTest.length() + ")");

					String decryptedTest = encryptor.decrypt(encryptedTest);
					System.out.println(" Decrypted: " + decryptedTest + " (" + decryptedTest.length() + ")");

					System.out.println("Are the same - " + toEncrypt.toLowerCase().equals(decryptedTest));
					break;
				case 4:
					System.exit(0);
					break;
				default:
					break;
			}

		}
	}
}
