import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trebuchet {
  private static int parseCalibrationValueV1(String s) {
    Pattern pattern = Pattern.compile("[0-9]");
    Matcher matcher = pattern.matcher(s);

    LinkedList<String> allDigits = new LinkedList<>();

    while (matcher.find()) {
      allDigits.add(matcher.group());
    }

    int firstValue = Integer.parseInt(allDigits.getFirst());
    int lastValue = Integer.parseInt(allDigits.getLast());

    return firstValue * 10 + lastValue;
  }

  private static int parseCalibrationValueV2(String s) {
    HashMap<String, Integer> spelledDigits = new HashMap<>();
    spelledDigits.put("one", 1);
    spelledDigits.put("two", 2);
    spelledDigits.put("three", 3);
    spelledDigits.put("four", 4);
    spelledDigits.put("five", 5);
    spelledDigits.put("six", 6);
    spelledDigits.put("seven", 7);
    spelledDigits.put("eight", 8);
    spelledDigits.put("nine", 9);

    StringBuilder regex = new StringBuilder("(?=([0-9]");
    for (String key: spelledDigits.keySet()) {
      regex.append("|");
      regex.append(key);
    }
    regex.append(")).");

    Pattern pattern = Pattern.compile(regex.toString());
    Matcher matcher = pattern.matcher(s);

    LinkedList<String> allDigits = new LinkedList<>();

    while (matcher.find()) {
      allDigits.add(matcher.group(1));
    }

    String first = allDigits.getFirst();
    String last = allDigits.getLast();

    int firstValue = spelledDigits.containsKey(first) 
      ? spelledDigits.get(first)
      : Integer.parseInt(first);

    int lastValue = spelledDigits.containsKey(last) 
      ? spelledDigits.get(last)
      : Integer.parseInt(last);

    return firstValue * 10 + lastValue;
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    int sumV1 = 0;
    int sumV2 = 0;

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      sumV1 += parseCalibrationValueV1(line);
      sumV2 += parseCalibrationValueV2(line);
    }

    scanner.close();

    System.out.println(sumV1);
    System.out.println(sumV2);
  }
}
