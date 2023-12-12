import java.util.HashMap;
import java.util.Scanner;

public class HotSprings {
  private static boolean checkLength(String record, int length) {
    if (record.length() < length) {
      return false;
    }

    for (int i = 0; i < length; i++) {
      if (record.charAt(i) != '#' && record.charAt(i) != '?') {
        return false;
      }
    }

    return record.length() == length || record.charAt(length) != '#';
  }

  private static long search(String record, HashMap<String, Long> memo) {
    if (memo.containsKey(record)) {
      return memo.get(record);
    }

    long result = 0;

    char firstChar = record.charAt(0);
    String rest = record.substring(1);

    if (firstChar == '#') {
      String[] parts = record.split(" ");
      if (parts.length < 2 || parts[1].length() == 0) {
        result = 0;
      } else {
        String springs = parts[0];

        String[] lengthStrings = parts[1].split(",", 2);
        Integer currentLength = Integer.parseInt(lengthStrings[0]);
        String restLength = lengthStrings.length == 1 ? "" : lengthStrings[1];

        if (!checkLength(springs, currentLength)) {
          result = 0;
        } else if (springs.length() == currentLength) {
          result = search(" " + restLength, memo);
        } else if (springs.charAt(currentLength) == '?') {
          result = search("." + springs.substring(currentLength + 1) + " " + restLength, memo);
        } else if (springs.charAt(currentLength) == '.') {
          result = search(springs.substring(currentLength + 1) + " " + restLength, memo);
        }
      }
    } else if (firstChar == ' ') {
      result = rest.length() == 0 ? 1 : 0;
    } else if (firstChar == '?') {
      result = search("." + rest, memo) + search("#" + rest, memo);
    } else if (firstChar == '.') {
      result = search(rest, memo);
    }

    memo.put(record, result);
    return result;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    long sumV1 = 0;
    long sumV2 = 0;

    while (scanner.hasNextLine()) {
        HashMap<String, Long> memo = new HashMap<>();

        String lineV1 = scanner.nextLine();
        sumV1 += search(lineV1, memo);

        String parts[] = lineV1.split(" ");
        String lineV2 = parts[0];
        for (int i = 0; i < 4; i++) {
          lineV2 += "?" + parts[0];
        }
        lineV2 += " " + parts[1];
        for (int i = 0; i < 4; i++) {
          lineV2 += "," + parts[1];
        }

        sumV2 += search(lineV2, memo);
    }

    scanner.close();

    System.out.println(sumV1);
    System.out.println(sumV2);
  }
}
