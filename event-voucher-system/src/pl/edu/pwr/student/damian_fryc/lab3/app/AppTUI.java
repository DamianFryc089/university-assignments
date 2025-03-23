package pl.edu.pwr.student.damian_fryc.lab3.app;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class AppTUI {
    protected static int getInt(Scanner scanner, int min, int max){
        int input = 0;
        while (true)
        {
            while (!scanner.hasNextInt()) scanner.nextLine();
            input = scanner.nextInt();
            scanner.nextLine();
            if (input >= min  && (input <= max || max == -1)) break;
        }
        return input;
    }
    private static int[] getColumnWidths(ArrayList<ArrayList<String>> data) {
        if (data.isEmpty()) return new int[0];

        int numColumns = data.getFirst().size();
        int[] columnWidths = new int[numColumns];

        for (int i = 0; i < numColumns; i++) {
            columnWidths[i] = data.getFirst().get(i).length();
        }

        for (int row = 1; row < data.size(); row++) {
            for (int col = 0; col < numColumns; col++) {
                int length = data.get(row).get(col).length();
                if (length > columnWidths[col]) {
                    columnWidths[col] = length;
                }
            }
        }

        // extra space
        for (int i = 0; i < columnWidths.length; i++)
            columnWidths[i] += 2;

        return columnWidths;
    }
    protected static void printTableWithBorders(ArrayList<ArrayList<String>> data) {

        for (int i = 1; i < data.size(); i++) {
            ArrayList<String> row = data.get(i);
            row.addFirst(String.valueOf(i-1));
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) == null)
                    row.set(j, " ");
            }
        }

        int[] columnWidths = getColumnWidths(data);



        String horizontalBorder = buildHorizontalBorder(columnWidths);
        System.out.println(horizontalBorder);
        for (int row = 0; row < data.size(); row++) {
            System.out.print("|");
            for (int col = 0; col < columnWidths.length; col++) {
                String cell = (col < data.get(row).size()) ? data.get(row).get(col) : "";
                System.out.printf(" %-"+ (columnWidths[col]-2) +"s |", cell);
            }
            System.out.println();

            if (row == 0) {
                System.out.println(horizontalBorder);
            }
        }
        System.out.println(horizontalBorder);
    }
    private static String buildHorizontalBorder(int[] columnWidths) {
        StringBuilder border = new StringBuilder();
        border.append("+");
        for (int width : columnWidths) {
            for (int i = 0; i < width; i++) {
                border.append("-");
            }
            border.append("+");
        }
        return border.toString();
    }
    protected AppTUI(){}
}
