import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HauntedWasteland {
  private static long gcd(long p, long q) {
    if (q == 0) return p;
    return gcd(q, p % q);
  }

  private static long lcm(long p, long q) {
    return p * (q / gcd(p, q));
  }

  private static long walkOne(
    String node,
    String instructions,
    HashMap<String, String> left,
    HashMap<String, String> right,
    boolean v2
  ) {
    int steps;

    for (steps = 0; v2 ? !node.endsWith("Z") : !node.equals("ZZZ"); steps++) {
      if (instructions.charAt(steps % instructions.length()) == 'L') {
        node = left.get(node);
      } else {
        node = right.get(node);
      }
    }

    return steps;
  }

  private static ArrayList<Long> walkAll(
    ArrayList<String> currentNodes,
    String instructions,
    HashMap<String, String> left,
    HashMap<String, String> right
  ) {
    ArrayList<Long> steps = new ArrayList<>();

    for (int i = 0; i < currentNodes.size(); i++) {
      steps.add(walkOne(currentNodes.get(i), instructions, left, right, true));
    }

    return steps;
  }

  public static void main(String[] args) {
    Pattern pattern = Pattern.compile("([\\w]+) = \\(([\\w]+), ([\\w]+)\\)");

    HashMap<String, String> left = new HashMap<>();
    HashMap<String, String> right = new HashMap<>();

    Scanner scanner = new Scanner(System.in);

    String instructions = scanner.nextLine();
    scanner.nextLine();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        left.put(matcher.group(1), matcher.group(2));
        right.put(matcher.group(1), matcher.group(3));
      }
    }

    scanner.close();

    System.out.println(walkOne("AAA", instructions, left, right, false));

    ArrayList<String> initialV2 = new ArrayList<>();
    for (String node: left.keySet()) {
      if (node.endsWith("A")) {
        initialV2.add(node);
      }
    }

    ArrayList<Long> steps = walkAll(initialV2, instructions, left, right);
    
    long n = steps.get(0);
    for (int i = 1; i < steps.size(); i++) {
      n = lcm(n, steps.get(i));
    }
    System.out.println(n);
  }
}
