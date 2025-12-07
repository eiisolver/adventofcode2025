import module java.base;

public class day6 {

    static void part1(List<String> lines) {
        List<String[]> cells = lines.stream().map(String::trim).map(line -> line.split("\\s+")).toList();
        String[] ops = cells.get(cells.size() - 1);
        var nrs = cells.subList(0, cells.size() - 1);
        long result = 0;
        for (int i = 0; i < ops.length; ++i) {
            final int idx = i;
            var stream = nrs.stream().mapToLong(row -> Long.parseLong(row[idx]));
            if (ops[i].equals("+")) {
                var v = stream.sum();
                result += v;
            } else if (ops[i].equals("*")) {
                var v = stream.reduce(1, (a, b) -> a * b);
                result += v;
            }
        }
        System.out.println("Part 1: " + result);
    }

    // Gets "vertical" number at index
    static int getNumberAt(List<String> nrs, int index) {
        String nrAsString = nrs.stream()
                .filter(row -> index < row.length())
                .map(row -> String.valueOf(row.charAt(index)))
                .collect(Collectors.joining()).trim();
        return nrAsString.isEmpty() ? 0 : Integer.parseInt(nrAsString);
    }

    static void part2(List<String> lines) {
        char[] ops = lines.get(lines.size() - 1).toCharArray();
        var nrs = lines.subList(0, lines.size() - 1);
        // Length of the longest line
        int len = nrs.stream().mapToInt(String::length).max().orElse(0);
        long part2 = 0;
        char op = '+';
        long result = 0;
        for (int i = 0; i < len; ++i) {
            if (i < ops.length && ops[i] != ' ') {
                part2 += result;
                op = ops[i];
                result = op == '+' ? 0 : 1;
            }
            int number = getNumberAt(nrs, i);
            if (op == '+') {
                result += number;
            } else if (op == '*' && number != 0) {
                result *= number;
            }
        }
        part2 += result;
        System.out.println("Part 2: " + part2);
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("input/6.txt"));
        part1(lines);
        part2(lines);
    }
}
