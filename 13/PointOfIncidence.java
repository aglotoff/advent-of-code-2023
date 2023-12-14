import java.util.ArrayList;
import java.util.Scanner;

public class PointOfIncidence {
  private static class Pattern {
    private char[][] map;

    public Pattern(ArrayList<String> lines) {
      map = new char[lines.size()][];

      for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        map[i] = new char[line.length()];
        for (int j = 0; j < line.length(); j++) {
          map[i][j] = line.charAt(j);
        }
      }
    }

    private boolean isVerticalSplit(int col) {
      for (int row = 0; row < map.length; row++) {
        for (int k = col-1, l = col; k >= 0 && l < map[row].length; k--, l++) {
          if (map[row][k] != map[row][l]) {
            return false;
          }
        } 
      }
      return true;
    }

    private int findVerticalSplit(int minCol, int maxCol) {
      for (int col = minCol; col <= maxCol; col++) {
        if (isVerticalSplit(col)) {
          return col;
        }
      }
      return -1;
    }

    private boolean isHorizontalSplit(int row) {
      for (int col = 0; col < map[row].length; col++) {
        for (int k = row-1, l = row; k >= 0 && l < map.length; k--, l++) {
          if (map[k][col] != map[l][col]) {
            return false;
          }
        } 
      }
      return true;
    }

    private int findHorizontalSplit(int minRow, int maxRow) {
      for (int row = minRow; row <= maxRow; row++) {
        if (isHorizontalSplit(row)) {
          return row;
        }
      }
      return -1;
    }

    public int summarize(int minRow, int minCol, int maxRow, int maxCol) {
      int row = findHorizontalSplit(minRow, maxRow);
      if (row != -1) {
        return row * 100;
      }

      int col = findVerticalSplit(minCol, maxCol);
      if (col != -1) {
        return col;
      }

      return -1;
    }

    public int summarize() {
      return summarize(1, 1, map.length - 1, map[0].length - 1);
    }

    public int summarizeWithSmudge() {
      for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[i].length; j++) {
          char c = map[i][j];

          int minRow = i / 2 + 1;
          int minCol = j / 2 + 1;
          int maxRow = i + (map.length - i) / 2;
          int maxCol = j + (map[0].length - j) / 2;
          
          map[i][j] = c == '.' ? '#' : '.';
          int result = summarize(minRow, minCol, maxRow, maxCol);
          map[i][j] = c;

          if (result != -1) {
            return result;
          }
        }
      }
      return -1;
    }
  }


  public static void main(String[] args) {
    ArrayList<Pattern> patterns = new ArrayList<>();

    ArrayList<String> lines = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.length() == 0) {
        patterns.add(new Pattern(lines));
        lines = new ArrayList<>();
      } else {
        lines.add(line);
      }
    }
    scanner.close();
    patterns.add(new Pattern(lines));

    int sumV1 = 0;
    int sumV2 = 0;

    for (Pattern p: patterns) {
      sumV1 += p.summarize();
      sumV2 += p.summarizeWithSmudge();
    }

    System.out.println(sumV1);
    System.out.println(sumV2);
  }
}
