import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Aplenty {
  public static class PartRange {
    HashMap<String, Integer> min = new HashMap<>();
    HashMap<String, Integer> max = new HashMap<>();
    String destination = "in";

    public PartRange() {
      min.put("x", 1);
      min.put("m", 1);
      min.put("a", 1);
      min.put("s", 1);

      max.put("x", 4000);
      max.put("m", 4000);
      max.put("a", 4000);
      max.put("s", 4000);
    }

    public PartRange(PartRange range) {
      for (String s: range.min.keySet()) {
        min.put(s, range.min.get(s));
      }
      for (String s: range.max.keySet()) {
        max.put(s, range.max.get(s));
      }
      destination = range.destination;
    }

    public long getCount() {
      long count = 1;
      for (String s: min.keySet()) {
        count *= (max.get(s) - min.get(s) + 1);
      }
      return count;
    }
  }

  public static class Condition {
    private String category;
    private String operator;
    private int value;
    private String destination;

    public Condition(String cat, String op, int val, String dest) {
      category = cat;
      operator = op;
      value = val;
      destination = dest;
    }

    public boolean test(HashMap<String, Integer> part) {
      int categoryValue = part.get(category);

      if (operator.equals("<")) {
        return categoryValue < value;
      } else {
        return categoryValue > value;
      }
    }

    public PartRange getMatchingRange(PartRange range) {
      int minValue = range.min.get(category);
      int maxValue = range.max.get(category);

      if (operator.equals("<")) {
        if (minValue >= value) {
          return null;
        }

        if (maxValue < value) {
          range.destination = destination;
          return range;
        }

        PartRange result = new PartRange(range);
        result.destination = destination;
        result.max.put(category, value - 1);
        return result;
      } else {
        if (minValue > value) {
          range.destination = destination;
          return range;
        }
        
        if (maxValue <= value) {
          return null;
        }

        PartRange result = new PartRange(range);
        result.destination = destination;
        result.min.put(category, value + 1);
        return result;
      }
    }

    public PartRange getNonMatchingRange(PartRange range) {
      int minValue = range.min.get(category);
      int maxValue = range.max.get(category);

      if (operator.equals("<")) {
        if (minValue >= value) {
          return range;
        }

        if (maxValue < value) {
          return null;
        }

        PartRange result = new PartRange(range);
        result.min.put(category, value);
        return result;
      } else {
        if (minValue > value) {
          return null;
        }
        
        if (maxValue <= value) {
          range.destination = destination;
          return range;
        }

        PartRange result = new PartRange(range);
        result.max.put(category, value);
        return result;
      }
    }
  }

  public static class Workflow {
    LinkedList<Condition> conditions;
    String defaultDestination;

    public Workflow(LinkedList<Condition> cond, String defaultDest) {
      conditions = cond;
      defaultDestination = defaultDest;
    }

    public String test(HashMap<String, Integer> part) {
      for (Condition c: conditions) {
        if (c.test(part)) {
          return c.destination;
        }
      }
      return defaultDestination;
    }

    public LinkedList<PartRange> process(PartRange range) {
      LinkedList<PartRange> result = new LinkedList<>();

      for (Condition c: conditions) {
        PartRange matching = c.getMatchingRange(range);
        PartRange nonMatching = c.getNonMatchingRange(range);

        if (matching != null) {
          result.add(matching);
        }

        if (nonMatching == null) {
          return result;
        }

        range = nonMatching;
      }

      range.destination = defaultDestination;
      result.add(range);

      return result;
    }
  }

  private static int getPartRating(HashMap<String, Integer> part) {
    int rating = 0;
    for (String key: part.keySet()) {
      rating += part.get(key);
    }  
    return rating;
  }

  public static void main(String[] args) {
    HashMap<String, Workflow> workflows = new HashMap<>();
    LinkedList<HashMap<String, Integer>> parts = new LinkedList<>();

    Pattern workflowPattern = Pattern.compile("(\\w+)\\{(.*)\\}");
    Pattern conditionPattern = Pattern.compile("(\\w+)(<|>)(\\d+):(\\w+)");
    Pattern partPattern = Pattern.compile("\\{(.*)\\}");
    Pattern categoryPattern = Pattern.compile("(\\w+)=(\\d+)");

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      Matcher workflowMatcher = workflowPattern.matcher(scanner.nextLine());
      if (!workflowMatcher.find()) {
        break;
      }

      String name = workflowMatcher.group(1);
      String[] rules = workflowMatcher.group(2).split(",");

      LinkedList<Condition> conditions = new LinkedList<>();
      String defaultDestination = "";

      for (String rule: rules) {
        Matcher conditionMatcher = conditionPattern.matcher(rule);
        if (conditionMatcher.find()) {
          conditions.add(new Condition(
            conditionMatcher.group(1),
            conditionMatcher.group(2),
            Integer.parseInt(conditionMatcher.group(3)),
            conditionMatcher.group(4)
          ));
        } else {
          defaultDestination = rule;
        }
      }

      workflows.put(name, new Workflow(conditions, defaultDestination));
    }

    while (scanner.hasNextLine()) {
      Matcher partMatcher = partPattern.matcher(scanner.nextLine());
      if (!partMatcher.find()) {
        break;
      }

      HashMap<String, Integer> part = new HashMap<>();
      for (String s: partMatcher.group(1).split(",")) {
        Matcher matcher = categoryPattern.matcher(s);
        if (matcher.find()) {
          part.put(matcher.group(1), Integer.parseInt(matcher.group(2)));
        }
      }

      parts.add(part);
    }

    scanner.close();

    int acceptedRatingSum = 0;

    for (HashMap<String, Integer> part: parts) {
      String destination = "in";
      while (!destination.equals("A") && !destination.equals("R")) {
        destination = workflows.get(destination).test(part);
      }

      if (destination.equals("A")) {
        acceptedRatingSum += getPartRating(part);
      }
    }

    System.out.println(acceptedRatingSum);

    long totalAccepted = 0;

    LinkedList<PartRange> queue = new LinkedList<>();
    queue.add(new PartRange());

    while (!queue.isEmpty()) {
      PartRange curr = queue.removeFirst();

      for (PartRange next: workflows.get(curr.destination).process(curr)) {
        if (next.destination.equals("A")) {
          totalAccepted += next.getCount();
        } else if (!next.destination.equals("R")) {
          queue.addLast(next);
        }
      }
    }

    System.out.println(totalAccepted);
  }
}
