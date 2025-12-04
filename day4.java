import java.nio.file.Files;
import java.nio.file.Paths;

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
        long part1 = 0;
        for (Pos p : g) {
            if (canMoveTo(p)) {
                part1++;
            }
        }
        System.out.println("Part 1: " + part1);
        long part2 = 0;
        boolean stop = false;
        while (!stop) {
            stop = true;
            for (Pos p : g) {
                if (canMoveTo(p)) {
                    cells[p.row()][p.col()] = '.';
                    part2++;
                    stop = false;
                }
            }
        }
        System.out.println("Part 2: " + part2);
    }
}
