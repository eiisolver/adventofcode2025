import module java.base;

public class day4 {
    static Grid<Character> g;

    public static boolean canMoveTo(Pos p) {
        if (g.get(p) != '@') {
            return false;
        }
        var count = Arrays.stream(Pos.NESW8)
                .map(p::add)
                .filter(neighbor -> g.contains(neighbor) && g.get(neighbor) == '@')
                .count();
        return count < 4;
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("input/4.txt"));
        char[][] cells = lines.stream().map(String::toCharArray).toArray(char[][]::new);
        g = new Grid<>(lines.size(), lines.get(0).length(), p -> cells[p.row()][p.col()]);
        long part1 = g.stream().filter(day4::canMoveTo).count();
        System.out.println("Part 1: " + part1);
        long part2 = 0;
        for (var stop = false; !stop;) {
            var toRemove = g.stream().filter(day4::canMoveTo).toList();
            part2 += toRemove.size();
            for (Pos p : toRemove) {
                cells[p.row()][p.col()] = '.';
            }
            stop = toRemove.isEmpty();
        }
        System.out.println("Part 2: " + part2);
    }
}
