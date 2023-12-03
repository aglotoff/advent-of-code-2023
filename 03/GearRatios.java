import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GearRatios {
  private static boolean hasAdjacentSymbol(
    int row,
    int colStart,
    int colEnd,
    ArrayList<HashMap<Integer, Character>> symbols
  ) {
    int minRow = Math.max(row - 1, 0);
    int maxRow = Math.min(row + 1, symbols.size() - 1);

    for (int i = minRow; i <= maxRow; i++) {
      for (int j = colStart - 1; j <= colEnd; j++) {
        if (symbols.get(i).containsKey(j)) {
          return true;
        }
      }
    }

    return false;
  }

  private static long getGearRatio(
    int row,
    int col,
    ArrayList<HashMap<Integer, String>> partNumbers
  ) {
    ArrayList<String> adjacent = new ArrayList<>();

    int minRow = Math.max(row - 1, 0);
    int maxRow = Math.min(row + 1, partNumbers.size() - 1);

    for (int i = minRow; i <= maxRow; i++) {
      for (int j: partNumbers.get(i).keySet()) {
        String num = partNumbers.get(i).get(j);

        if ((col >= (j - 1)) && (col <= (j + num.length()))) {
          adjacent.add(num);
        }
      }
    }

    if (adjacent.size() == 2) {
      return Long.parseLong(adjacent.get(0)) * Long.parseLong(adjacent.get(1));
    }
    return 0;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
  
    ArrayList<HashMap<Integer, String>> numbers = new ArrayList<>();
    ArrayList<HashMap<Integer, Character>> symbols = new ArrayList<>();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      HashMap<Integer, String> rowNumbers = new HashMap<>();
      HashMap<Integer, Character> rowSymbols = new HashMap<>();

      int j = 0;
      while (j < line.length()) {
        char ch = line.charAt(j);

        if (Character.isDigit(ch)) {
          int start = j++;
          
          while ((j < line.length()) && Character.isDigit(line.charAt(j))) {
            j++;
          }

          rowNumbers.put(start, line.substring(start, j));
        } else if (ch != '.') {
          rowSymbols.put(j++, ch);
        } else {
          j++;
        }
      }

      numbers.add(rowNumbers);
      symbols.add(rowSymbols);
    }

    scanner.close();

    int partNumberSum = 0;

    for (int i = 0; i < numbers.size(); i++) {
      for (int j: numbers.get(i).keySet()) {
        String num = numbers.get(i).get(j);

        if (hasAdjacentSymbol(i, j, j + num.length(), symbols)) {
          partNumberSum += Integer.parseInt(num);
        }
      }
    }

    System.out.println(partNumberSum);

    long gearRatioSum = 0;

    for (int i = 0; i < numbers.size(); i++) {
      for (int j: symbols.get(i).keySet()) {
        char sym = symbols.get(i).get(j);
        if (sym == '*') {
          gearRatioSum += getGearRatio(i, j, numbers);
        }
      }
    }

    System.out.println(gearRatioSum);
  }
}
