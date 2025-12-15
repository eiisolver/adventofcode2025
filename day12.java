import module java.base;

public class day12 {

    public static void main(String[] args) throws Exception {
        // Note: shape sizes have been precalculated by hand for this problem.
        int[] sizes = { 7, 7, 7, 5, 7, 6 };
        var lines = Files.readAllLines(Paths.get("input/12.txt"));
        long part1 = 0;
        for (var line : lines) {
            if (!line.contains("x")) {
                continue;
            }
            var nrsAsStr = line.split("([:]?\\s+)|(x)");
            var nrs = Arrays.stream(nrsAsStr).mapToInt(Integer::parseInt).toArray();
            System.out.println(Arrays.toString(nrs));
            int area = nrs[0] * nrs[1];
            int sizeNeeded = 0;
            for (int i = 0; i < sizes.length; i++) {
                sizeNeeded += sizes[i] * nrs[i + 2];
            }
            if (area >= sizeNeeded) {
                part1++;
            }
        }
        System.out.println("Part 1: " + part1);
    }
}
