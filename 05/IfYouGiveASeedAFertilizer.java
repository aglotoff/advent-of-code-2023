import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Scanner;

public class IfYouGiveASeedAFertilizer {
  private static class Range {
    private long start;
    private long length;
    private long end;

    public Range(long start, long length) {
      this.start = start;
      this.length = length;
      this.end = start + length - 1;
    }

    private Range getIntersection(Range other) {
      if (end < other.start || start > other.end) {
        return null;
      }

      long intersectionStart = Math.max(start, other.start);
      long intersectionEnd = Math.min(end, other.end);

      return new Range(
        intersectionStart,
        intersectionEnd - intersectionStart + 1
      );
    }
  }

  private static long getDestination(TreeMap<Long, Range> map, long source) {
    for (long sourceStart: map.keySet()) {
      Range entry = map.get(sourceStart);
      if (source >= sourceStart && source < (sourceStart + entry.length)) {
        return entry.start + source - sourceStart;
      }
    }

    return source;
  }

  private static void getDestinationRanges(
    ArrayList<Range> destinations,
    TreeMap<Long, Range> map,
    Range source
  ) {
    for (long start: map.keySet()) {
      Range destination = map.get(start);

      Range intersection = source.getIntersection(
        new Range(start, destination.length)
      );

      if (intersection != null) {
        if (intersection.start > source.start) {
          destinations.add(new Range(
            source.start, intersection.start - source.start
          ));
        }

        source = new Range(intersection.end + 1, source.end - intersection.end);

        destinations.add(new Range(
          destination.start + intersection.start - start,
          intersection.length
        ));
      }
    }

    if (source.length != 0) {
      destinations.add(source);
    }
  }

  private static TreeMap<Long, Range> parseMap(Scanner scanner) {
    TreeMap<Long, Range> map = new TreeMap<>();

    scanner.nextLine();
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.length() == 0) {
        break;
      }

      String[] parts = line.split(" ");
      long destination = Long.parseLong(parts[0]);
      long source = Long.parseLong(parts[1]);
      long length = Long.parseLong(parts[2]);

      map.put(source, new Range(destination, length));
    }

    return map;
  }

  public static void main(String[] args) {
    ArrayList<Long> seeds = new ArrayList<>();
    ArrayList<TreeMap<Long, Range>> maps = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    String line = scanner.nextLine();
    String[] parts = line.split(" ");
    for (int i = 1; i < parts.length; i++) {
      seeds.add(Long.parseLong(parts[i]));
    }
    scanner.nextLine();

    while (scanner.hasNextLine()) {
      maps.add(parseMap(scanner));
    }

    scanner.close();

    long minLocationV1 = Long.MAX_VALUE;

    for (long seed: seeds) {
      long result = seed;

      for (int i = 0; i < maps.size(); i++) {
        result = getDestination(maps.get(i), result);
      }

      minLocationV1 = Math.min(minLocationV1, result);
    }

    System.out.println(minLocationV1);

    ArrayList<Range> seedRanges = new ArrayList<>();
    for (int i = 0; i < seeds.size(); i += 2) {
      seedRanges.add(new Range(seeds.get(i), seeds.get(i + 1)));
    }

    ArrayList<Range> resultRanges = new ArrayList<>();
    for (Range r: seedRanges) {
      resultRanges.add(r);
    }

    for (int i = 0; i < maps.size(); i++) {
      ArrayList<Range> newRanges = new ArrayList<>();

      for (Range r: resultRanges) {
        getDestinationRanges(newRanges, maps.get(i), r);
      }

      resultRanges = newRanges;
    }

    long minLocationV2 = Long.MAX_VALUE;
    for (Range r: resultRanges) {
      minLocationV2 = Math.min(minLocationV2, r.start);
    }
    System.out.println(minLocationV2);
  }
}
