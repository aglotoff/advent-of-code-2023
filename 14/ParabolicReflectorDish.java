import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ParabolicReflectorDish {
  private static class Platform {
    private char[][] map;

    public Platform(Platform p) {
      this.map = new char[p.map.length][p.map[0].length];
      for (int i = 0; i < p.map.length; i++) {
        for (int j = 0; j < p.map[i].length; j++) {
          this.map[i][j] = p.map[i][j];
        }
      }
    }

    public Platform(ArrayList<String> input) {
      map = new char[input.size()][];
      for (int i = 0; i < input.size(); i++) {
        map[i] = input.get(i).toCharArray();
      }
    }

    public void slideNorth() {
      for (int i = 1; i < map.length; i++) {
        for (int j = 0; j < map[i].length; j++) {
          if (map[i][j] != 'O') {
            continue;
          }

          for (int k = i - 1; k >= 0 && map[k][j] == '.'; k--) {
            map[k][j] = 'O';
            map[k + 1][j] = '.';
          }
        }
      }
    }

    public void slideWest() {
      for (int j = 1; j < map.length; j++) {
        for (int i = 0; i < map[j].length; i++) {
          if (map[i][j] != 'O') {
            continue;
          }

          for (int k = j - 1; k >= 0 && map[i][k] == '.'; k--) {
            map[i][k] = 'O';
            map[i][k + 1] = '.';
          }
        }
      }
    }

    public void slideSouth() {
      for (int i = map.length - 2; i >= 0; i--) {
        for (int j = 0; j < map[i].length; j++) {
          if (map[i][j] != 'O') {
            continue;
          }

          for (int k = i + 1; k < map.length && map[k][j] == '.'; k++) {
            map[k][j] = 'O';
            map[k - 1][j] = '.';
          }
        }
      }
    }

    public void slideEast() {
      for (int j = map[0].length - 2; j >= 0; j--) {
        for (int i = 0; i < map[j].length; i++) {
          if (map[i][j] != 'O') {
            continue;
          }

          for (int k = j + 1; k < map[i].length && map[i][k] == '.'; k++) {
            map[i][k] = 'O';
            map[i][k - 1] = '.';
          }
        }
      }
    }

    public int getLoad() {
      int total = 0;

      for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[0].length; j++) {
          if (map[i][j] == 'O') {
            total += map.length - i;
          }
        }
      }
      return total;
    }

    public Platform nextCycle() {
      Platform p = new Platform(this);
      p.slideNorth();
      p.slideWest();
      p.slideSouth();
      p.slideEast();
      return p;
    }

    @Override
    public int hashCode() {
      int code = 0;
      for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[i].length; j++) {
          if (map[i][j] == 'O') {
            code = 31 * ((31 * code) + i) + j;
          }
        }
      }
      return code;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      if (o.getClass() != this.getClass()) return false;

      Platform p = (Platform) o;
      for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[i].length; j++) {
          if (map[i][j] != p.map[i][j]) {
            return false;
          }
        }
      }
      return true;
    }
  }

  public static void main(String[] args) {
    ArrayList<String> lines = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      lines.add(scanner.nextLine());
    }
    scanner.close();

    Platform initial = new Platform(lines);
    Platform p = new Platform(initial);

    initial.slideNorth();
    System.out.println(initial.getLoad());

    ArrayList<Platform> platforms = new ArrayList<>();
    HashMap<Platform, Long> platformIndex = new HashMap<>();

    for (long i = 0; i < 1000000000; i++) {
      if (platformIndex.containsKey(p)) {
        long loopStart = platformIndex.get(p);
        long loopLength = i - loopStart;
        long endOffset = (1000000000 - loopStart) % loopLength;
        Platform endPlatform = platforms.get((int) (loopStart + endOffset));
        System.out.println(endPlatform.getLoad());

        break;
      }

      platforms.add(p);
      platformIndex.put(p, i);
      p = p.nextCycle();
    }
  }
}
