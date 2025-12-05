import module java.base;

public class day5 {
    static class Range {
        long start;
        long end;

        public Range(long start, long end) {
            this.start = start;
            this.end = end;
        }

        public boolean contains(long value) {
            return value >= start && value <= end;
        }

        public boolean overlaps(Range other) {
            return this.start <= other.end && other.start <= this.end;
        }

        public void merge(Range other) {
            this.start = Math.min(this.start, other.start);
            this.end = Math.max(this.end, other.end);
        }

        @Override
        public String toString() {
            return "Range(" + start + "-" + end + ")";
        }

        public static Range fromString(String s) {
            var parts = s.split("-");
            return new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
        }
    }

    public static void main(String[] args) throws Exception {
        var lines = Files.readAllLines(Paths.get("5_input.txt"));
        var ranges = lines.stream().takeWhile((s) -> !s.isEmpty()).map(Range::fromString).toList();
        List<Long> numbers = lines.stream().skip(ranges.size() + 1).map(Long::parseLong).toList();
        Predicate<Long> inAnyRange = (num) -> ranges.stream().anyMatch(r -> r.contains(num));
        long part1 = numbers.stream().filter(inAnyRange).count();
        System.out.println("Part 1: " + part1);
        // Merge all overlapping ranges to list of non-overlapping ranges
        var nonOverlappingRanges = new ArrayList<Range>(ranges);
        for (var stop = false; !stop;) {
            stop = true;
            var tempRanges = new ArrayList<Range>();
            for (var r : nonOverlappingRanges) {
                boolean merged = false;
                for (var r2 : tempRanges) {
                    if (r.overlaps(r2)) {
                        r2.merge(r);
                        merged = true;
                        stop = false;
                    }
                }
                if (!merged) {
                    tempRanges.add(new Range(r.start, r.end));
                }
            }
            nonOverlappingRanges = tempRanges;
        }
        long part2 = nonOverlappingRanges.stream().mapToLong(r -> r.end - r.start + 1).sum();
        System.out.println("Part 2: " + part2);
    }
}
