import java.util.ArrayList;
import java.util.Scanner;

public class MirageMaintenance {
  private static boolean isAllZeros(ArrayList<Integer> sequence) {
    for (int num: sequence) {
      if (num != 0) {
        return false;
      }
    }
    return true;
  }

  private static int extrapolateRight(ArrayList<Integer> sequence) {
    if (isAllZeros(sequence)) {
      return 0;
    }

    ArrayList<Integer> differences = new ArrayList<>();
    for (int i = 0; i < sequence.size() - 1; i++) {
      differences.add(sequence.get(i + 1) - sequence.get(i));
    }

    return sequence.get(sequence.size() - 1) + extrapolateRight(differences);
  }

  private static int extrapolateLeft(ArrayList<Integer> sequence) {
    if (isAllZeros(sequence)) {
      return 0;
    }

    ArrayList<Integer> differences = new ArrayList<>();
    for (int i = 0; i < sequence.size() - 1; i++) {
      differences.add(sequence.get(i + 1) - sequence.get(i));
    }

    return sequence.get(0) - extrapolateLeft(differences);
  }

  public static void main(String[] args) {
    ArrayList<ArrayList<Integer>> sequences = new ArrayList<>();

    Scanner scanner = new Scanner(System.in);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      ArrayList<Integer> seq = new ArrayList<>();
      for (String s: line.split("\\s+")) {
        seq.add(Integer.parseInt(s));
      }
      sequences.add(seq);
    }

    scanner.close();

    int sumRight = 0;
    int sumLeft = 0;
    for (ArrayList<Integer> seq: sequences) {
      sumRight += extrapolateRight(seq);
      sumLeft += extrapolateLeft(seq);
    }
    System.out.println(sumRight);
    System.out.println(sumLeft);
  }
}
