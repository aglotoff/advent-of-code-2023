import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LensLibrary {
  private static class Box {
    private LinkedList<String> lenses = new LinkedList<>();
    private HashMap<String, Integer> focalLengths = new HashMap<>();
  }

  private static int computeHash(String s) {
    int value = 0;
    for (int i = 0; i < s.length(); i++) {
      value += s.codePointAt(i);
      value *= 17;
      value %= 256;
    }
    return value;
  }

  public static void main(String[] args) {
    ArrayList<String> initSequence = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      for (String step: scanner.nextLine().split(",")) {
        initSequence.add(step);
      }
    }
    scanner.close();

    int sum = 0;
    for (String step: initSequence) {
      sum += computeHash(step);
    }
    System.out.println(sum);

    Box[] boxes = new Box[256];
    for (int i = 0; i < boxes.length; i++) {
      boxes[i] = new Box();
    }

    Pattern pattern = Pattern.compile("([a-z]+)(-|=)([0-9])?");

    for (String step: initSequence) {
      Matcher matcher = pattern.matcher(step);
      if (matcher.find()) {
        String label = matcher.group(1);
        Box box = boxes[computeHash(label)];

        String operation = matcher.group(2);
        if (operation.equals("=")) {
          int length = Integer.parseInt(matcher.group(3));
          if (!box.focalLengths.containsKey(label)) {
            box.lenses.addLast(label);
          }
          box.focalLengths.put(label, length);
        } else {
          if (box.focalLengths.containsKey(label)) {
            box.focalLengths.remove(label);
            box.lenses.remove(label);
          }
        }
      }
    }

    int totalPower = 0;
    for (int i = 0; i < boxes.length; i++) {
      int slot = 1;
      for (String label: boxes[i].lenses) {
        totalPower += (i + 1) * slot * boxes[i].focalLengths.get(label);
        slot++;
      }
    }

    System.out.println(totalPower);
  }
}