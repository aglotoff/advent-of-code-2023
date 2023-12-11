import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class CosmicExpansion {
  private static class Coordinates {
    private long i;
    private long j;

    public Coordinates(long i, long j) {
      this.i = i;
      this.j = j;
    }

    public long distanceTo(Coordinates c) {
      return Math.abs(this.i - c.i) + Math.abs(this.j - c.j);
    }
  }

  private static boolean isEmptyRow(ArrayList<String> input, int i) {
    for (int j = 0; j < input.get(i).length(); j++) {
      if (input.get(i).charAt(j) == '#') {
        return false;
      }
    }
    return true;
  }

  private static boolean isEmptyColumn(ArrayList<String> input, int j) {
    for (int i = 0; i < input.size(); i++) {
      if (input.get(i).charAt(j) == '#') {
        return false;
      }
    }
    return true;
  }

  private static ArrayList<Coordinates> getExpandedGalaxies(
    ArrayList<String> map,
    long ageFactor
  ) {
    HashSet<Integer> emptyRows = new HashSet<>();
    for (int i = 0; i < map.size(); i++) {
      if (isEmptyRow(map, i)) {
        emptyRows.add(i);
      }
    }

    HashSet<Integer> emptyColumns = new HashSet<>();
    for (int j = 0; j < map.get(0).length(); j++) {
      if (isEmptyColumn(map, j)) {
        emptyColumns.add(j);
      }
    }

    ArrayList<Coordinates> galaxies = new ArrayList<>();

    long expandedRow = 0;

    for (int i = 0; i < map.size(); i++) {
      if (emptyRows.contains(i)) {
        expandedRow += ageFactor;
      } else {
        long expandedCol = 0;

        for (int j = 0; j < map.get(0).length(); j++) {
          if (emptyColumns.contains(j)) {
            expandedCol += ageFactor;
          } else {
            if (map.get(i).charAt(j) == '#') {
              galaxies.add(new Coordinates(expandedRow, expandedCol));
            }
            expandedCol++;
          }
        }

        expandedRow++;
      }
    }

    return galaxies;
  }

  private static long getDistanceSum(ArrayList<String> map, long ageFactor) {
    long distanceSum = 0;

    ArrayList<Coordinates> galaxies = getExpandedGalaxies(map, ageFactor);

    for (int i = 0; i < galaxies.size(); i++) {
      for (int j = i + 1; j < galaxies.size(); j++) {
        distanceSum += galaxies.get(i).distanceTo(galaxies.get(j));
      }
    }

    return distanceSum;
  }

  public static void main(String[] args) {
    ArrayList<String> input = new ArrayList<>();
    
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      input.add(scanner.nextLine());
    }
    scanner.close();

    System.out.println(getDistanceSum(input, 2));
    System.out.println(getDistanceSum(input, 1000000));
  }
}
