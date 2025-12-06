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
                System.out.println("Adding " + v + " from column " + i);
                result += v;
            } else if (ops[i].equals("*")) {
                var v = stream.reduce(1, (a, b) -> a * b);
                System.out.println("Multiplying " + v + " from column " + i);
                result += v;
            }
        }
        System.out.println("Part 1: " + result);
    }

    static void part2(List<String> lines) {
        long part2 = 0;
        char op = '+';
        long result = 0;
        char[] ops = lines.get(lines.size() - 1).toCharArray();
        var nrs = lines.subList(0, lines.size() - 1);
        int len = nrs.stream().mapToInt(String::length).max().orElse(0);
        Function<Integer, Integer> getNumberAt = (i) -> {
            String nrAsString = nrs.stream()
                    .filter(row -> i < row.length())
                    .map(row -> String.valueOf(row.charAt(i)))
                    .collect(Collectors.joining()).trim();
            return nrAsString.isEmpty() ? 0 : Integer.parseInt(nrAsString);
        };
        List<Integer> mirroredNrs = IntStream.range(0, len).mapToObj(i -> getNumberAt.apply(i)).toList();
        for (int i = 0; i < len; ++i) {
            if (i < ops.length && ops[i] != ' ') {
                part2 += result;
                op = ops[i];
                result = op == '+' ? 0 : 1;
            }
            int number = mirroredNrs.get(i);
            if (op == '+') {
                result += number;
            } else if (op == '*' && number != 0) {
                result *= number;
            }
        }
        part2 += result;
        System.out.println("Part 2: " + part2);
        /*
         * for (int i = 0; i < ops.length; ++i) {
         * if (ops[i] != ' ') {
         * part2 += result;
         * op = ops[i];
         * result = op == '+' ? 0 : 1;
         * }
         * long number = 0;
         * for (var row : nrs) {
         * if (i >= row.length()) {
         * continue;
         * }
         * var c = row.charAt(i);
         * if (c != ' ') {
         * number = 10 * number + (c - '0');
         * }
         * }
         * if (op == '+') {
         * result += number;
         * } else if (op == '*' && number != 0) {
         * result *= number;
         * }
         */
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("6_input.txt"));
        part1(lines);
        part2(lines);
    }
}
