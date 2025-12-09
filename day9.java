import module java.base;

public class day9 {
    // Is sometimes a rectangle, sometimes a line segment
    static class Line implements Comparable<Line> {
        int x1, y1, x2, y2;
        Pos a, b;

        public Line(Pos a, Pos b) {
            this.a = a;
            this.b = b;
            // Sort coordinates
            this.x1 = Math.min(a.col(), b.col());
            this.y1 = Math.min(a.row(), b.row());
            this.x2 = Math.max(a.col(), b.col());
            this.y2 = Math.max(a.row(), b.row());
        }

        public boolean isVertical() {
            return x1 == x2;
        }

        public boolean contains(Pos p) {
            return (x1 < p.col() && p.col() < x2)
                    && (y1 < p.row() && p.row() < y2);
        }

        public boolean contains(Line other) {
            if (contains(other.a) || contains(other.b)) {
                return true;
            }
            // Check if other line has both endpoints outside this rectange,
            // but still crosses either of the sides
            if (other.x1 <= x1 && other.x2 >= x2 && other.y1 > y1 && other.y2 < y2) {
                return true;
            }
            if (other.y1 <= y1 && other.y2 >= y2 && other.x1 > x1 && other.x2 < x2) {
                return true;
            }
            return false;
        }

        public long area() {
            return (x2 - x1 + 1) * (y2 - y1 + 1);
        }

        @Override
        public int compareTo(Line other) {
            return Long.compare(other.area(), this.area());
        }

        @Override
        public String toString() {
            return String.format("Line[(%d,%d)-(%d,%d)]", x1, y1, x2, y2);
        }
    }

    static void toSvg(List<Pos> tiles, Pos a, Pos b) throws Exception {
        try (PrintStream out = new PrintStream("9.svg")) {
            int f = 20;
            int f2 = 1;
            int w = 100000 / f;
            var s = """
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 1000" xmlns:xlink="http://www.w3.org/1999/xlink">
                    <rect width="100%" height="100%" fill="white"/>
                    """
                    .replaceAll("1000", Integer.toString(w));
            out.println(s);
            for (int i = 0; i < tiles.size(); ++i) {
                Pos tile = tiles.get(i);
                Pos next = tiles.get((i + 1) % tiles.size());
                int x = f2 * tile.col() / f;
                int y = f2 * tile.row() / f;
                int x2 = f2 * next.col() / f;
                int y2 = f2 * next.row() / f;
                String col = (i % 2) == 0 ? "blue" : "green";
                out.println(String.format("""
                        <line x1="%d" y1="%d" x2="%d" y2="%d" stroke="%s" stroke-width="1"/>
                        """, x, y, x2, y2, col));
            }
            int ax = f2 * a.col() / f;
            int ay = f2 * a.row() / f;
            int bx = f2 * b.col() / f;
            int by = f2 * b.row() / f;
            int rectX = Math.min(ax, bx);
            int rectY = Math.min(ay, by);
            int rectWidth = Math.abs(ax - bx);
            int rectHeight = Math.abs(ay - by);
            out.println(String.format("""
                    <rect x="%d" y="%d" width="%d" height="%d" fill="none" stroke="red" stroke-width="2"/>
                    """, rectX, rectY, rectWidth, rectHeight));
            out.println("</svg>");
        }
    }

    public static void main(String[] args) throws Exception {
        var lineList = Files.readAllLines(Paths.get("input/9.txt"));
        List<Pos> tiles = lineList.stream().map(line -> {
            String[] parts = line.split(",");
            return new Pos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }).toList();
        long part1 = 1;
        for (int i = 0; i < tiles.size(); ++i) {
            Pos p1 = tiles.get(i);
            for (int j = i + 1; j < tiles.size(); ++j) {
                var line = new Line(p1, tiles.get(j));
                part1 = Math.max(part1, line.area());
            }
        }
        System.out.println("Part 1: " + part1);
        var lines = IntStream.range(0, tiles.size()).mapToObj(i -> {
            return new Line(tiles.get(i), tiles.get((i + 1) % tiles.size()));
        }).toList();
        // Sort lines by length descending to speed up checks.
        var sortedLines = new ArrayList<>(lines);
        Collections.sort(sortedLines);
        long part2 = 1;
        Line bestRect = null;
        for (int i = 0; i < tiles.size(); ++i) {
            Pos p1 = tiles.get(i);
            for (int j = i + 1; j < tiles.size(); ++j) {
                var rect = new Line(p1, tiles.get(j));
                var area = rect.area();
                if (area <= part2) {
                    continue;
                }
                boolean crosses = false;
                for (var other : sortedLines) {
                    if (rect.contains(other)) {
                        crosses = true;
                        break;
                    }
                }
                if (!crosses) {
                    part2 = area;
                    bestRect = rect;
                }
            }
        }
        System.out.println("Part 2: " + part2);
        toSvg(tiles, bestRect.a, bestRect.b);
    }
}
