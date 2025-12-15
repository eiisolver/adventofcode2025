import module java.base;

public class day11 {

    static Map<String, Long> count = new HashMap<>();

    public static long countPaths(Map<String, String[]> graph, String state, String endState, String excludeState) {
        if (count.containsKey(state)) {
            return count.get(state);
        }
        if (state.equals(endState)) {
            return 1;
        }
        if (state.equals(excludeState) || !graph.containsKey(state)) {
            return 0;
        }
        long total = 0;
        for (var next : graph.get(state)) {
            total += countPaths(graph, next, endState, excludeState);
        }
        count.put(state, total);
        return total;
    }

    public static long count(Map<String, String[]> graph, String state, String endState, String excludeState) {
        count.clear();
        return countPaths(graph, state, endState, excludeState);
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("input/11.txt"));
        var graph = new HashMap<String, String[]>();
        for (var line : lines) {
            var nodes = line.split("([:]?\\s+)");
            graph.put(nodes[0], Arrays.copyOfRange(nodes, 1, nodes.length));
        }
        long part1 = count(graph, "you", "out", "<NONE>");
        System.out.println("Part 1: " + part1);
        long svrFft = count(graph, "svr", "fft", "dac");
        long fftDac = count(graph, "fft", "dac", "<NONE>");
        long dacOut = count(graph, "dac", "out", "fft");
        long svrDac = count(graph, "svr", "dac", "fft");
        long dacFft = count(graph, "dac", "fft", "<NONE>");
        long fftOut = count(graph, "fft", "out", "dac");
        System.out.printf("%d * %d * %d + %d * %d * %d%n", svrFft, fftDac, dacOut, svrDac, dacFft, fftOut);

        long p1 = svrFft * fftDac * dacOut;
        long p2 = svrDac * dacFft * fftOut;
        System.out.println(p1 + " + " + p2 + " = " + (p1 + p2));
        long part2 = p1 + p2;
        System.out.println("Part 2: " + part2);
    }
}
