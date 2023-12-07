import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class CamelCards {
  private static final HashMap<Character, Integer> cardStrengths;
  static {
    cardStrengths = new HashMap<>();
    cardStrengths.put('2', 2);
    cardStrengths.put('3', 3);
    cardStrengths.put('4', 4);
    cardStrengths.put('5', 5);
    cardStrengths.put('6', 6);
    cardStrengths.put('7', 7);
    cardStrengths.put('8', 8);
    cardStrengths.put('9', 9);
    cardStrengths.put('T', 10);
    cardStrengths.put('J', 11);
    cardStrengths.put('Q', 12);
    cardStrengths.put('K', 13);
    cardStrengths.put('A', 14);
  }

  private static int getCardStrength(char card, boolean jForJoker) {
    if (jForJoker && card == 'J') {
      return 0;
    }
    return cardStrengths.get(card);
  }

  private static class Hand {
    private static final int HIGH_CARD = 0;
    private static final int ONE_PAIR = 1;
    private static final int TWO_PAIR = 2;
    private static final int THREE_OF_A_KIND = 3;
    private static final int FULL_HOUSE = 4;
    private static final int FOUR_OF_A_KIND = 5;
    private static final int FIVE_OF_A_KIND = 6;

    private final char[] cards;
    private final int bid;

    public Hand(char[] cards, int bid) {
      this.cards = cards;
      this.bid = bid;
    }

    public int getStrength(boolean jForJoker) {
      HashMap<Character, Integer> cardCounts = new HashMap<>();
      for (char c: cards) {
        if (cardCounts.containsKey(c)) {
          cardCounts.put(c, cardCounts.get(c) + 1);
        } else {
          cardCounts.put(c, 1);
        }
      }

      if (cardCounts.size() == 1) {
        return FIVE_OF_A_KIND;
      }

      int jokerCount = jForJoker && cardCounts.containsKey('J')
        ? cardCounts.get('J')
        : 0;

      if (cardCounts.size() == 5) {
        if (jokerCount > 0) {
          return ONE_PAIR;
        }
        return HIGH_CARD;
      }

      if (cardCounts.size() == 4) {
        if (jokerCount > 0) {
          return THREE_OF_A_KIND;
        }
        return ONE_PAIR;
      }

      int pairs = 0;
      
      for (char c: cardCounts.keySet()) {
        if (cardCounts.get(c) == 2)
          pairs++;
      }

      if (cardCounts.size() == 3) {
        if (pairs == 0) {
          if (jokerCount > 0) {
            return FOUR_OF_A_KIND;
          }

          return THREE_OF_A_KIND;
        }

        if (jokerCount == 2) {
          return FOUR_OF_A_KIND;
        }
        if (jokerCount == 1) {
          return FULL_HOUSE;
        }

        return TWO_PAIR;
      }

      if (jokerCount > 0) {
        return FIVE_OF_A_KIND;
      }

      return pairs == 0 ? FOUR_OF_A_KIND : FULL_HOUSE;
    }
  }

  private static class HandComparator implements Comparator<Hand> {
    private boolean jForJoker;

    public HandComparator(boolean jForJoker) {
      this.jForJoker = jForJoker;
    }

    public int compare(Hand h1, Hand h2) {
      int thisStrength = h1.getStrength(jForJoker);
      int thatStrength = h2.getStrength(jForJoker);

      if (thisStrength != thatStrength) {
        return Integer.compare(thisStrength, thatStrength);
      }

      for (int i = 0; i < 5; i++) {
        int thisCardStrength = getCardStrength(h1.cards[i], jForJoker);
        int thatCardStrength = getCardStrength(h2.cards[i], jForJoker);

        if (thisCardStrength != thatCardStrength) {
          return Integer.compare(thisCardStrength, thatCardStrength);
        }
      }

      return 0;
    }
  }

  private static long getTotalWinnings(
    ArrayList<Hand> hands,
    HandComparator order
  ) {
    Collections.sort(hands, order);

    long total = 0;

    for (int i = 0; i < hands.size(); i++) {
      total += (i + 1) * hands.get(i).bid;
    }

    return total;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    ArrayList<Hand> hands = new ArrayList<>();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] parts = line.split(" ");

      hands.add(new Hand(parts[0].toCharArray(), Integer.parseInt(parts[1])));
    }

    scanner.close();

    System.out.println(getTotalWinnings(hands, new HandComparator(false)));
    System.out.println(getTotalWinnings(hands, new HandComparator(true)));
  }
}
