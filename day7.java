import module java.base;

public class day7 {

    static void add(Map<Integer, Long> map, int key, long value) {
        map.put(key, map.getOrDefault(key, 0L) + value);
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("input/7.txt"));
        var line0 = lines.get(0);
        int tachyon = line0.indexOf('S');
        Map<Integer, Long> tachyons = new HashMap<>();
        tachyons.put(tachyon, 1L);
        long part1 = 0;
        for (var line : lines) {
            var newTachyons = new HashMap<Integer, Long>();
            for (var entry : tachyons.entrySet()) {
                int t = entry.getKey();
                long count = entry.getValue();
                if (line.charAt(t) == '^') {
                    ++part1;
                    add(newTachyons, t - 1, count);
                    add(newTachyons, t + 1, count);
                } else {
                    add(newTachyons, t, count);
                }
            }
            tachyons = newTachyons;
        }
        System.out.println("Part 1: " + part1);
        long part2 = tachyons.values().stream().mapToLong(Long::longValue).sum();
        System.out.println("Part 2: " + part2);
    }
}
