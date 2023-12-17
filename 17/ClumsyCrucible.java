import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ClumsyCrucible {
  private static enum Direction { UP, RIGHT, DOWN, LEFT };

  private int[][] map;

  public ClumsyCrucible(ArrayList<String> inputLines) {
    map = new int[inputLines.size()][];
    for (int i = 0; i < inputLines.size(); i++) {
      String line = inputLines.get(i);
      map[i] = new int[line.length()];
      for (int j = 0; j < line.length(); j++) {
        map[i][j] = line.charAt(j) - '0';
      }
    }
  }

  private class CrucibleState {
    int row;
    int col;
    int steps;
    Direction dir;

    public CrucibleState(int row, int col, int steps, Direction dir) {
      this.row = row;
      this.col = col;
      this.steps = steps;
      this.dir = dir;
    }

    public CrucibleState moveForward() {
      switch (dir) {
        case UP:
          return new CrucibleState(row - 1, col, steps + 1, dir);
        case RIGHT:
          return new CrucibleState(row, col + 1, steps + 1, dir);
        case DOWN:
          return new CrucibleState(row + 1, col, steps + 1, dir);
        case LEFT:
          return new CrucibleState(row, col - 1, steps + 1, dir);
        default:
          return this;
      }
    }

    public CrucibleState turnLeft() {
      switch (dir) {
        case UP:
          return new CrucibleState(row, col, 0, Direction.LEFT);
        case RIGHT:
          return new CrucibleState(row, col, 0, Direction.UP);
        case DOWN:
          return new CrucibleState(row, col, 0, Direction.RIGHT);
        case LEFT:
          return new CrucibleState(row, col, 0, Direction.DOWN);
        default:
          return this;
      }
    }

    public CrucibleState turnRight() {
      switch (dir) {
        case UP:
          return new CrucibleState(row, col, 0, Direction.RIGHT);
        case RIGHT:
          return new CrucibleState(row, col, 0, Direction.DOWN);
        case DOWN:
          return new CrucibleState(row, col, 0, Direction.LEFT);
        case LEFT:
          return new CrucibleState(row, col, 0, Direction.UP);
        default:
          return this;
      }
    }

    @Override
    public int hashCode() {
      return 31 * (31 * (31 * (31 * row + col) + steps)) + dir.hashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      if (this.getClass() != o.getClass()) return false;

      CrucibleState state = (CrucibleState) o;
      return row == state.row &&
        col == state.col &&
        steps == state.steps &&
        dir == state.dir;
    }

    public boolean isValid() {
      if (row < 0 || row >= map.length) {
        return false;
      }
      if (col < 0 || col >= map[row].length) {
        return false;
      }
      return true;
    }

    public boolean isGoal() {
      return row == map.length - 1 && col == map[row].length - 1;
    }
  }

  private class Node implements Comparable<Node> {
    CrucibleState state;
    int cost;

    public Node(CrucibleState state, int cost) {
      this.state = state;
      this.cost = cost;
    }

    @Override
    public int compareTo(Node n) {
      return cost - n.cost;
    }
  }

  public int findMinHeatLoss(int minStepsForward, int maxStepsForward) {
    PriorityQueue<Node> queue = new PriorityQueue<Node>();
    HashSet<CrucibleState> settled = new HashSet<>();
    HashMap<CrucibleState, Integer> dist = new HashMap<>();

    CrucibleState start = new CrucibleState(0, 0, 0, Direction.RIGHT);
    queue.add(new Node(start, 0));
    dist.put(start, 0);

    int minLoss = Integer.MAX_VALUE;

    while (!queue.isEmpty()) {
      Node node = queue.poll();
      CrucibleState current = node.state;

      if (settled.contains(current)) {
        continue;
      }

      if (current.isGoal()) {
        minLoss = Math.min(minLoss, node.cost);
      }

      settled.add(current);

      ArrayList<CrucibleState> adjacentStates = new ArrayList<>();

      if (current.steps < maxStepsForward) {
        adjacentStates.add(current.moveForward());
      }

      if (current.steps >= minStepsForward) {
        adjacentStates.add(current.turnLeft().moveForward());
        adjacentStates.add(current.turnRight().moveForward());
      }

      for (CrucibleState nextState: adjacentStates) {
        if (nextState.isValid() && !settled.contains(nextState)) {
          int cost = dist.get(current) + map[nextState.row][nextState.col];

          if (!dist.containsKey(nextState) || cost < dist.get(nextState)) {
            dist.put(nextState, cost);
          }
          
          queue.add(new Node(nextState, cost));
        }
      }
    }
    
    return minLoss;
  }

  public static void main(String[] args) {
    ArrayList<String> inputLines = new ArrayList<>();
    
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      inputLines.add(scanner.nextLine());
    }
    scanner.close();

    ClumsyCrucible puzzle = new ClumsyCrucible(inputLines);
    System.out.println(puzzle.findMinHeatLoss(1, 3));
    System.out.println(puzzle.findMinHeatLoss(4, 10));
  }
}
