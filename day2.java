import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class day2 {
    public static void main(String[] args) throws IOException {
        String content = Files.readString(Paths.get("2_input.txt")).trim();
        long part1 = 0;
        long part2 = 0;
        for (String range : content.split(",")) {
            var bounds = range.split("-");
            long lower = Long.parseLong(bounds[0]);
            long upper = Long.parseLong(bounds[1]);
            for (var i = lower; i <= upper; i++) {
                String numStr = Long.toString(i);
                var halfStr = numStr.substring(0, numStr.length() / 2);
                if (numStr.equals(halfStr + halfStr)) {
                    part1 += i;
                }
                for (var parts = 2; parts <= numStr.length(); ++parts) {
                    if (numStr.length() % parts != 0) {
                        continue;
                    }
                    var part = numStr.substring(0, numStr.length() / parts);
                    var candidate = "";
                    for (var p = 0; p < parts; ++p) {
                        candidate += part;
                    }
                    if (candidate.equals(numStr)) {
                        part2 += i;
                        break;
                    }
                }
            }
        }
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
}
