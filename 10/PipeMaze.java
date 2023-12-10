import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class PipeMaze {
  private static boolean hasConnectionToNorth(char c) {
    return c == 'S' || c == '|' || c == 'L' || c == 'J';
  }

  private static boolean hasConnectionToSouth(char c) {
    return c == 'S' || c == '|' || c == '7' || c == 'F';
  }

  private static boolean hasConnectionToEast(char c) {
    return c == 'S' || c == '-' || c == 'L' || c == 'F';
  }

  private static boolean hasConnectionToWest(char c) {
    return c == 'S' || c == '-' || c == '7' || c == 'J';
  }

  private static String toStringKey(int i, int j) {
    return i + "," + j;
  }

  public static void main(String[] args) {
    ArrayList<String> maze = new ArrayList<>();
    
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      maze.add(scanner.nextLine());
    }
    scanner.close();

    int mazeWidth = maze.get(0).length();
    int mazeHeight = maze.size();

    HashMap<String, ArrayList<String>> adjacent = new HashMap<>();
    String start = "0,0";

    for (int i = 0; i < mazeHeight; i++) {
      for (int j = 0; j < mazeWidth; j++) {
        adjacent.put(i + "," + j, new ArrayList<>());

        if (maze.get(i).charAt(j) == 'S') {
          start = toStringKey(i, j);
        }
      }
    }

    for (int i = 0; i < mazeHeight; i++) {
      for (int j = 0; j < mazeWidth; j++) {
        char current = maze.get(i).charAt(j);

        if (i > 0) {
          char top = maze.get(i - 1).charAt(j);
          if (hasConnectionToNorth(current) && hasConnectionToSouth(top)) {
            adjacent.get(toStringKey(i, j)).add(toStringKey(i - 1, j));
            adjacent.get(toStringKey(i - 1, j)).add(toStringKey(i, j));
          }
        }

        if (j > 0) {
          char left = maze.get(i).charAt(j - 1);
          if (hasConnectionToWest(current) && hasConnectionToEast(left)) {
            adjacent.get(toStringKey(i, j)).add(toStringKey(i, j - 1));
            adjacent.get(toStringKey(i, j - 1)).add(toStringKey(i, j));
          }
        }
      }
    }

    HashMap<String, Integer> distance = new HashMap<>();
    distance.put(start, 0);
    int maxDist = 0;

    LinkedList<String> queue = new LinkedList<>();
    queue.add(start);

    while (!queue.isEmpty()) {
      String current = queue.removeFirst();
      int distToAdjacent = distance.get(current) + 1;

      for (String tile: adjacent.get(current)) {
        if (!distance.containsKey(tile)) {
          distance.put(tile, distToAdjacent);
          queue.addLast(tile);

          maxDist = Math.max(maxDist, distToAdjacent);
        }
      }
    }

    System.out.println(maxDist);

    int insideCount = 0;

    for (int i = 0; i < mazeHeight; i++) {
      boolean inside = false;
      int wall = 0;

      for (int j = 0; j < mazeWidth; j++) {
        String key = toStringKey(i, j);
        char tile = maze.get(i).charAt(j);

        if (!distance.containsKey(key)) {
          wall = 0;

          if (inside) {
            insideCount++;
          }
        } else {
          if (tile == '|') {
            inside = !inside;
          } else {
            if (wall == 0) {
              wall = tile;
            } else if (tile == 'J') {
              if (wall == 'F') {
                inside = !inside;
              }
              wall = 0;
            } else if (tile == '7') {
              if (wall == 'L') {
                inside = !inside;
              }
              wall = 0;
            }
          }
        }
      }
    }

    System.out.println(insideCount);
  }
}
