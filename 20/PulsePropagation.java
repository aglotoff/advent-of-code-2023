import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PulsePropagation {
  private static long gcd(long p, long q) {
    if (q == 0) return p;
    return gcd(q, p % q);
  }

  private static long lcm(long p, long q) {
    return p * (q / gcd(p, q));
  }

  private interface Module {
    public boolean setInputPulse(String from, boolean pulse);
    public boolean getOutputPulse();
  }

  private static class Broadcaster implements Module {
    private boolean pulse;

    public boolean setInputPulse(String from, boolean pulse) {
      this.pulse = pulse;
      return true;
    }

    public boolean getOutputPulse() {
      return pulse;
    }
  }

  private static class FlipFlop implements Module {
    private boolean on;

    public boolean setInputPulse(String from, boolean pulse) {
      if (!pulse) {
        on = !on;
        return true;
      }
      return false;
    }

    public boolean getOutputPulse() {
      return on;
    }
  }

  private static class Conjunction implements Module {
    private HashMap<String, Boolean> pulses;

    public Conjunction(LinkedList<String> inputs) {
      pulses = new HashMap<>();
      for (String inputName: inputs) {
        pulses.put(inputName, false);
      }
    }

    public boolean setInputPulse(String from, boolean pulse) {
      pulses.put(from, pulse);
      return true;
    }

    public boolean getOutputPulse() {
      for (boolean pulse: pulses.values()) {
        if (!pulse) {
          return true;
        }
      }
      return false;
    }
  }

  public static void main(String[] args) {
    HashMap<String, LinkedList<String>> inputs = new HashMap<>();
    HashMap<String, LinkedList<String>> outputs = new HashMap<>();
    HashMap<String, String> types = new HashMap<>();

    Pattern pattern = Pattern.compile("(%|&)?(\\w+) -> (\\w+(?:, \\w+)*)");

    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      Matcher matcher = pattern.matcher(scanner.nextLine());
      if (matcher.find()) {
        String prefix = matcher.group(1);
        String name = matcher.group(2);
        String[] outputNames = matcher.group(3).split(", ");

        if (prefix != null) {
          types.put(name, prefix);
        }

        outputs.put(name, new LinkedList<>(Arrays.asList(outputNames)));

        for (String outputName: outputNames) {
          if (!inputs.containsKey(outputName)) {
            inputs.put(outputName, new LinkedList<>());
          }
          inputs.get(outputName).addLast(name);
        }
      }
    }
    scanner.close();

    HashMap<String, Module> modules = new HashMap<>();

    for (String name: inputs.keySet()) {
      if (!types.containsKey(name)) {
        modules.put(name, new Broadcaster());
      } else if (types.get(name).equals("%")) {
        modules.put(name, new FlipFlop());
      } else {
        modules.put(name, new Conjunction(inputs.get(name)));
      }
    }

    Module button = new Broadcaster();
    modules.put("button", button);
    outputs.put("button", new LinkedList<>());
    outputs.get("button").addLast("broadcaster");

    Module broadcaster = new Broadcaster();
    modules.put("broadcaster", broadcaster);

    long lowCount = 0;
    long highCount = 0;

    HashMap<String, Integer> cycles = new HashMap<>();
    LinkedList<String> shouldBeHigh = inputs.get(inputs.get("rx").get(0));

    for (int i = 1; i <= 1000 || cycles.size() < shouldBeHigh.size(); i++) {
      LinkedList<String> queue = new LinkedList<>();
      queue.add("button");

      while (!queue.isEmpty()) {
        String name = queue.removeFirst();
        Module module = modules.get(name);

        boolean pulse = module.getOutputPulse();

        for (String inputName: shouldBeHigh) {
          if (!cycles.containsKey(inputName) && name.equals(inputName) && pulse) {
            cycles.put(inputName, i);
          }
        }

        if (!outputs.containsKey(name)) {
          continue;
        }
        
        for (String outputName: outputs.get(name)) {
          if (pulse) {
            highCount++;
          } else {
            lowCount++;
          }

          Module outputModule = modules.get(outputName);

          if (outputModule.setInputPulse(name, pulse)) {
            queue.add(outputName);
          }
        }
      }

      if (i == 1000) {
        System.out.println(lowCount * highCount);
      }
    }

    long minPresses = 1L;
    for (int length: cycles.values()) {
      minPresses = lcm(minPresses, length);
    }
    System.out.println(minPresses);
  }
}
