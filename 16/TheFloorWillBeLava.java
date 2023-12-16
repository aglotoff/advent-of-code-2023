import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class TheFloorWillBeLava {
  private static enum Direction { UP, RIGHT, DOWN, LEFT };

  private static class Beam {
    private final int row;
    private final int col;
    private final Direction dir;

    public Beam(int row, int col, Direction dir) {
      this.row = row;
      this.col = col;
      this.dir = dir;
    }

    public Beam move() {
      switch (dir) {
        case UP:
          return new Beam(row - 1, col, dir);
        case RIGHT:
          return new Beam(row, col + 1, dir);
        case DOWN:
          return new Beam(row + 1, col, dir);
        case LEFT:
          return new Beam(row, col - 1, dir);
        default:
          return this;
      }
    }

    public Beam turnLeft() {
      switch (dir) {
        case UP:
          return new Beam(row, col, Direction.LEFT);
        case RIGHT:
          return new Beam(row, col, Direction.UP);
        case DOWN:
          return new Beam(row, col, Direction.RIGHT);
        case LEFT:
          return new Beam(row, col, Direction.DOWN);
        default:
          return this;
      }
    }

    public Beam turnRight() {
      switch (dir) {
        case UP:
          return new Beam(row, col, Direction.RIGHT);
        case RIGHT:
          return new Beam(row, col, Direction.DOWN);
        case DOWN:
          return new Beam(row, col, Direction.LEFT);
        case LEFT:
          return new Beam(row, col, Direction.UP);
        default:
          return this;
      }
    }

    @Override
    public int hashCode() {
      return 31 * (31 * (31 * row + col) + dir.hashCode());
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      if (this.getClass() != o.getClass()) return false;

      Beam beam = (Beam) o;
      return row == beam.row && col == beam.col && dir == beam.dir;
    }
  }

  private static int energize(char[][] map, Beam start) {
    boolean[][] energized = new boolean[map.length][map[0].length];
    int count = 0;

    LinkedList<Beam> activeBeams = new LinkedList<>();
    activeBeams.addLast(start);
    
    HashSet<Beam> memo = new HashSet<>();
    memo.add(start);

    while (!activeBeams.isEmpty()) {
      Beam beam = activeBeams.removeFirst();

      for (;;) {
        beam = beam.move();

        if (beam.row < 0 || beam.row >= map.length) {
          break;
        }
        if (beam.col < 0 || beam.col >= map[beam.row].length) {
          break;
        }
        if (memo.contains(beam)) {
          break;
        }

        if (!energized[beam.row][beam.col]) {
          count++;
        }
        energized[beam.row][beam.col] = true;
        memo.add(beam);

        switch (map[beam.row][beam.col]) {
          case '|':
            if (beam.dir == Direction.LEFT || beam.dir == Direction.RIGHT) {
              activeBeams.addLast(beam.turnLeft());
              beam = beam.turnRight();
            }
            break;
          case '-':
            if (beam.dir == Direction.UP || beam.dir == Direction.DOWN) {
              activeBeams.addLast(beam.turnLeft());
              beam = beam.turnRight();
            }
            break;
          case '\\':
            if (beam.dir == Direction.LEFT || beam.dir == Direction.RIGHT) {
              beam = beam.turnRight();
            } else {
              beam = beam.turnLeft();
            }
            break;
          case '/':
            if (beam.dir == Direction.LEFT || beam.dir == Direction.RIGHT) {
              beam = beam.turnLeft();
            } else {
              beam = beam.turnRight();
            }
            break;
        }
      }
    }

    return count;
  }

  public static void main(String[] args) {
    ArrayList<char[]> inputLines = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      inputLines.add(scanner.nextLine().toCharArray());
    }
    scanner.close();

    char[][] map = new char[inputLines.size()][];
    for (int i = 0; i < map.length; i++) {
      map[i] = inputLines.get(i);
    }

    int maxEnergy = energize(map, new Beam(0, -1, Direction.RIGHT));
    System.out.println(maxEnergy);

    ArrayList<Beam> allPossibleStarts = new ArrayList<>();
    for (int i = 0; i < map.length; i++) {
      allPossibleStarts.add(new Beam(i, -1, Direction.RIGHT));
      allPossibleStarts.add(new Beam(i, map[i].length, Direction.LEFT));
    }
    for (int j = 0; j < map[0].length; j++) {
      allPossibleStarts.add(new Beam(-1, j, Direction.DOWN));
      allPossibleStarts.add(new Beam(map.length, j, Direction.UP));
    }

    for (Beam start: allPossibleStarts) {
      int energy = energize(map, start);
      if (energy > maxEnergy) {
        maxEnergy = energy;
      }
    }
    System.out.println(maxEnergy);
  }
}
