import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Scratchcards {
  private static int countMatches(
    HashSet<Integer> winningNumbers,
    HashSet<Integer> myNumbers
  ) {
    int count = 0;

    for (int num: myNumbers) {
      if (winningNumbers.contains(num)) {
        count++;
      }
    }

    return count;
  }

  private static int getPointValue(int matchCount) {
    return matchCount == 0 ? 0 : (1 << (matchCount - 1));
  }

  public static void main(String[] args) {
    int totalValue = 0;

    Scanner scanner = new Scanner(System.in);
    ArrayList<Integer> matches = new ArrayList<>();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] parts = line.split("[|:]");

      HashSet<Integer> winningNumbers = new HashSet<>();
      for (String s: parts[1].trim().split("\\s+")) {
        winningNumbers.add(Integer.parseInt(s));
      }

      HashSet<Integer> myNumbers = new HashSet<>();
      for (String s: parts[2].trim().split("\\s+")) {
        myNumbers.add(Integer.parseInt(s));
      }

      int matchCount = countMatches(winningNumbers, myNumbers);
      matches.add(matchCount);

      totalValue += getPointValue(matchCount);
    }

    scanner.close();

    System.out.println(totalValue);

    LinkedList<Integer> stack = new LinkedList<>();
    for (int cardNum = 0; cardNum < matches.size(); cardNum++) {
      stack.addLast(cardNum);
    }

    int totalCards = 0;

    while (!stack.isEmpty()) {
      totalCards++;

      int cardNum = stack.removeFirst();

      for (int i = 1; i <= matches.get(cardNum); i++) {
        stack.addLast(cardNum + i);
      }
    }

    System.out.println(totalCards);
  }
}