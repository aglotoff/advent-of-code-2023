import java.util.ArrayList;
import java.util.Scanner;

public class WaitForIt {
  private static long joinIntegers(ArrayList<Integer> list) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int num: list) {
      stringBuilder.append(Integer.toString(num));
    }
    return Long.parseLong(stringBuilder.toString());
  }

  public static void main(String[] args) {
    ArrayList<Integer> times = new ArrayList<>();
    ArrayList<Integer> distances = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    String[] parts = scanner.nextLine().split("\\s+");
    for (int i = 1; i < parts.length; i++) {
      times.add(Integer.parseInt(parts[i]));
    }

    parts = scanner.nextLine().split("\\s+");
    for (int i = 1; i < parts.length; i++) {
      distances.add(Integer.parseInt(parts[i]));
    }

    scanner.close();

    long totalV1 = 1;

    for (int i = 0; i < times.size(); i++) {
      int count = 0;

      for (int speed = 0; speed < times.get(i); speed++) {
        int distance = speed * (times.get(i) - speed);
        if (distance > distances.get(i)) {
          count++;
        }
      }

      totalV1 *= count;
    }

    System.out.println(totalV1);

    long totalTime = joinIntegers(times);
    long totalDistance = joinIntegers(distances);

    long totalV2 = 0;

    for (long speed = 0; speed < totalTime; speed++) {
      long distance = speed * (totalTime - speed);
      if (distance > totalDistance) {
        totalV2++;
      }
    }
      
    System.out.println(totalV2);
  }
}
