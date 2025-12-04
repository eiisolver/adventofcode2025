import module java.base;

public class day4 {
    static char[][] cells;
    static Grid g;

    public static boolean canMoveTo(Pos p) {
        if (g.get(cells, p) != '@') {
            return false;
        }
        var count = java.util.Arrays.stream(Pos.NESW8)
                .map(p::add)
                .filter(neighbor -> g.contains(neighbor) && g.get(cells, neighbor) == '@')
                .count();
        return count < 4;
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("4_input.txt"));
        cells = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            cells[i] = lines.get(i).toCharArray();
        }
        g = new Grid(cells.length, cells[0].length);
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
