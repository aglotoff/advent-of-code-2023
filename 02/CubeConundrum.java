import java.util.LinkedList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CubeConundrum {
  private static boolean isGamePossible(
    LinkedList<HashMap<String, Integer>> game
  ) {
    for (HashMap<String, Integer> set: game) {
      if (set.get("red") > 12)
        return false;
      if (set.get("green") > 13)
        return false;
      if (set.get("blue") > 14)
        return false;
    }
    return true;
  }

  private static int getGamePower(LinkedList<HashMap<String, Integer>> game) {
    int minRed = 0;
    int minGreen = 0;
    int minBlue = 0;

    for (HashMap<String, Integer> set: game) {
      minRed = Math.max(minRed, set.get("red"));
      minGreen = Math.max(minGreen, set.get("green"));
      minBlue = Math.max(minBlue, set.get("blue"));
    }
  
    return minRed * minGreen * minBlue;
  }


  public static void main(String[] args) {
    int possibleIdSum = 0;
    int powerSum = 0;

    Scanner scanner = new Scanner(System.in);
    Pattern pattern = Pattern.compile("(\\d+ \\w+)");
    
    for (int i = 1; scanner.hasNextLine(); i++) {
      String line = scanner.nextLine();

      LinkedList<HashMap<String, Integer>> sets = new LinkedList<>();

      for (String setString: line.split(";")) {
        HashMap<String, Integer> set = new HashMap<>();
        set.put("red", 0);
        set.put("green", 0);
        set.put("blue", 0);

        Matcher matcher = pattern.matcher(setString);
        while (matcher.find()) {
          String[] parts = matcher.group(0).split(" ");
          set.put(parts[1], Integer.parseInt(parts[0]));
        }

        sets.add(set);
      }

      if (isGamePossible((sets))) {
        possibleIdSum += i;
      }

      powerSum += getGamePower(sets);
    }

    scanner.close();

    System.out.println(possibleIdSum);
    System.out.println(powerSum);
  }
}
