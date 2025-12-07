import module java.base;

public class day1 {

    // Compress dial position: even number if dial is on 0, else odd number,
    // E.g. dial position 0 -> 0, 1-99 -> 1, 100 -> 2, 101-199 -> 3, 200 -> 4, ...
    static int compressedPosition(int dial) {
        int pos = 2 * (dial >= 0 ? dial / 100 : (dial - 99) / 100);
        return dial % 100 == 0 ? pos : pos + 1;
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("input/1.txt"));
        List<Integer> numbers = lines.stream().map((line) -> line.replace("L", "-").replace("R", ""))
                .map(Integer::parseInt).toList();
        int dial = 50;
        int part1 = 0;
        int part2 = 0;
        int prevPos = compressedPosition(dial);
        for (int number : numbers) {
            dial += number;
            int pos = compressedPosition(dial);
            if (dial % 100 == 0) {
                ++part1;
            }
            int posDelta = Math.abs(pos - prevPos);
            part2 += (posDelta) / 2;
            if (pos % 2 == 0 && posDelta % 2 == 1) {
                // from odd to even position: landed on a multiple of 100
                ++part2;
            }
            prevPos = pos;
        }
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
}
