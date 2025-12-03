import java.nio.file.Files;
import java.nio.file.Paths;

public class day3 {
    static long maxJoltage(int[] digits, int nrDigits, int startIdx) {
        int digit1 = 0;
        int indexOfDigit1 = 0;
        for (int i = startIdx; i < digits.length - nrDigits + 1; ++i) {
            if (digits[i] > digit1) {
                digit1 = digits[i];
                indexOfDigit1 = i;
            }
        }
        if (nrDigits == 1) {
            return digit1;
        } else {
            return maxJoltage(digits, nrDigits - 1, indexOfDigit1 + 1) +
                    digit1 * (long) Math.pow(10, nrDigits - 1);
        }
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("3_input.txt"));
        long part1 = 0;
        long part2 = 0;
        for (var line : lines) {
            int[] digits = line.chars().map(c -> c - '0').toArray();
            part1 += maxJoltage(digits, 2, 0);

            part2 += maxJoltage(digits, 12, 0);
        }
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
}
