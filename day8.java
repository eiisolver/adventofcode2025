import module java.base;

public class day8 {

    static class Box {
        long[] pos;
        Circuit circuit;

        public long dist2(Box b) {
            long d2 = 0;
            for (int i = 0; i < 3; ++i) {
                long d = pos[i] - b.pos[i];
                d2 += d * d;
            }
            return d2;
        }
    }

    static record Dist(Box a, Box b, long dist2) implements Comparable<Dist> {

        @Override
        public int compareTo(Dist d) {
            return Long.compare(this.dist2, d.dist2);
        }
    }

    static class Circuit {
        List<Box> boxes = new ArrayList<>();

        void merge(Circuit other) {
            if (this == other) {
                return;
            }
            for (var box : other.boxes) {
                box.circuit = this;
            }
            boxes.addAll(other.boxes);
            other.boxes.clear();
        }
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("input/8.txt"));
        var boxes = new ArrayList<Box>();
        var circuits = new ArrayList<Circuit>();
        for (var line : lines) {
            var box = new Box();
            var circuit = new Circuit();
            var coord = line.split(",");
            box.pos = new long[] { Long.parseLong(coord[0]), Long.parseLong(coord[1]), Long.parseLong(coord[2]) };
            box.circuit = circuit;
            circuit.boxes.add(box);
            boxes.add(box);
            circuits.add(circuit);
        }
        List<Dist> dists = new ArrayList<>();
        for (var i = 0; i < boxes.size(); ++i) {
            var b1 = boxes.get(i);
            for (var j = i + 1; j < boxes.size(); ++j) {
                var b2 = boxes.get(j);
                dists.add(new Dist(b1, b2, b1.dist2(b2)));
            }
        }
        Collections.sort(dists);
        for (int i = 0; true; ++i) {
            if (i == 1000) {
                Integer[] sizes = circuits.stream().map(c -> c.boxes.size()).toArray(Integer[]::new);
                Arrays.sort(sizes, Collections.reverseOrder());
                long part1 = sizes[0] * sizes[1] * sizes[2];
                System.out.println("Part 1: " + part1);
            }
            var dist = dists.get(i);
            var c1 = dist.a().circuit;
            var c2 = dist.b().circuit;
            c1.merge(c2);
            if (c1.boxes.size() == boxes.size()) {
                System.out.println("Part 2: " + dist.a().pos[0] * dist.b().pos[0]);
                break;
            }
        }
    }
}
