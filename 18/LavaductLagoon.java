import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LavaductLagoon {
  private static class Coordinates {
    long row;
    long col;

    public Coordinates(long row, long col) {
      this.row = row;
      this.col = col;
    }

    public Coordinates move(char direction, int steps) {
      switch (direction) {
        case 'U':
        case '3':
          return new Coordinates(row - steps, col);
        case 'D':
        case '1':
          return new Coordinates(row + steps, col);
        case 'L':
        case '2':
          return new Coordinates(row, col - steps);
        case 'R':
        case '0':
          return new Coordinates(row, col + steps);
      }
      return this;
    }
  }

  private static long getPerimeter(ArrayList<Coordinates> points) {
    long perimeter = 0;

    for (int i = 0; i < points.size(); i++) {
      Coordinates a = points.get(i);
      Coordinates b = points.get((i + 1) % points.size());
      perimeter += Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }
    return perimeter;
  }

  private static long getEnclosedArea(ArrayList<Coordinates> points) {
    long left = 0;
    long right = 0;

    for (int i = 1; i < points.size(); i++) {
      left += points.get(i - 1).col * points.get(i).row;
      right += points.get(i).col * points.get(i - 1).row;
    }

    return Math.abs(left - right) / 2;
  }

  private static long getArea(ArrayList<Coordinates> points) {
    return getEnclosedArea(points) + getPerimeter(points) / 2 + 1;
  }

  public static void main(String[] args) {
    ArrayList<Coordinates> pointsV1 = new ArrayList<>();
    ArrayList<Coordinates> pointsV2 = new ArrayList<>();

    Coordinates currentV1 = new Coordinates(0, 0);
    Coordinates currentV2 = new Coordinates(0, 0);

    Pattern pattern = Pattern.compile("([UDLR]) (\\d+) \\(#([0-9a-f]+)\\)");

    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      Matcher matcher = pattern.matcher(scanner.nextLine());
      if (matcher.find()) {
        char dirV1 = matcher.group(1).charAt(0);
        int stepsV1 = Integer.parseInt(matcher.group(2));

        String color = matcher.group(3);

        currentV1 = currentV1.move(dirV1, stepsV1);
        pointsV1.add(currentV1);

        char dirV2 = color.charAt(5);
        int stepsV2 = Integer.parseInt(color.substring(0, 5), 16);

        currentV2 = currentV2.move(dirV2, stepsV2);
        pointsV2.add(currentV2);
      }
    }
    scanner.close();

    System.out.println(getArea(pointsV1));
    System.out.println(getArea(pointsV2));
  }
}
