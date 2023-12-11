import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class PipeMaze {
  private static class Coordinates {
    private int i;
    private int j;
    private char symbol;

    public Coordinates(int i, int j, char symbol) {
      this.i = i;
      this.j = j;
      this.symbol = symbol;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) return true;
      if (obj == null) return false;
      if (obj.getClass() != this.getClass()) return false;

      Coordinates c = (Coordinates) obj;
      return (c.i == this.i) && (c.j == this.j) && (c.symbol == this.symbol);
    }

    @Override
    public int hashCode() {
      return 31 * (31 * i + j) + symbol;
    }

    public boolean hasConnectionToNorth() {
      return symbol == 'S' || symbol == '|' || symbol == 'L' || symbol == 'J';
    }

    public boolean hasConnectionToSouth() {
      return symbol == 'S' || symbol == '|' || symbol == '7' || symbol == 'F';
    }

    public boolean hasConnectionToEast() {
      return symbol == 'S' || symbol == '-' || symbol == 'L' || symbol == 'F';
    }

    public boolean hasConnectionToWest() {
      return symbol == 'S' || symbol == '-' || symbol == '7' || symbol == 'J';
    }
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

    HashMap<Coordinates, ArrayList<Coordinates>> adjacent = new HashMap<>();
    Coordinates start = null;

    for (int i = 0; i < mazeHeight; i++) {
      for (int j = 0; j < mazeWidth; j++) {
        Coordinates tile = new Coordinates(i, j, maze.get(i).charAt(j));
        adjacent.put(tile, new ArrayList<>());

        if (tile.symbol == 'S') {
          start = tile;
        }
      }
    }

    for (int i = 0; i < mazeHeight; i++) {
      for (int j = 0; j < mazeWidth; j++) {
        Coordinates current = new Coordinates(i, j, maze.get(i).charAt(j));

        if (i > 0) {
          Coordinates top = new Coordinates(i-1, j, maze.get(i-1).charAt(j));
          if (current.hasConnectionToNorth() && top.hasConnectionToSouth()) {
            adjacent.get(current).add(top);
            adjacent.get(top).add(current);
          }
        }

        if (j > 0) {
          Coordinates left = new Coordinates(i, j-1, maze.get(i).charAt(j-1));
          if (current.hasConnectionToWest() && left.hasConnectionToEast()) {
            adjacent.get(current).add(left);
            adjacent.get(left).add(current);
          }
        }
      }
    }

    HashMap<Coordinates, Integer> distanceTo = new HashMap<>();
    distanceTo.put(start, 0);
    int maxDist = 0;

    LinkedList<Coordinates> queue = new LinkedList<>();
    queue.add(start);

    while (!queue.isEmpty()) {
      Coordinates current = queue.removeFirst();
      int distToAdjacent = distanceTo.get(current) + 1;

      for (Coordinates tile: adjacent.get(current)) {
        if (!distanceTo.containsKey(tile)) {
          distanceTo.put(tile, distToAdjacent);
          queue.addLast(tile);

          maxDist = Math.max(maxDist, distToAdjacent);
        }
      }
    }

    System.out.println(maxDist);

    Set<Coordinates> loop = distanceTo.keySet();
    int insideCount = 0;

    for (int i = 0; i < mazeHeight; i++) {
      boolean inside = false;

      for (int j = 0; j < mazeWidth; j++) {
        Coordinates current = new Coordinates(i, j, maze.get(i).charAt(j));

        if (!loop.contains(current)) {
          if (inside) {
            insideCount++;
          }
        } else if (current.symbol == '|') {
          inside = !inside;
        } else if (current.symbol == 'F') {
          char wallEnd;

          do {
            wallEnd = maze.get(i).charAt(++j);
          } while (wallEnd == '-');

          if (wallEnd == 'J') {
            inside = !inside;
          }
        } else if (current.symbol == 'L') {
          char wallEnd;

          do {
            wallEnd = maze.get(i).charAt(++j);
          } while (wallEnd == '-');

          if (wallEnd == '7') {
            inside = !inside;
          }
        }
      }
    }

    System.out.println(insideCount);
  }
}
