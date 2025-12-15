import module java.base;

public class day10 {

    static int calcPart1(int lights, List<Integer> buttons) {
        var totalSet = new HashSet<Integer>();
        totalSet.add(0);
        var latestSet = new HashSet<Integer>(totalSet);
        for (int i = 1; true; i++) {
            var newSet = new HashSet<Integer>();
            for (int val : latestSet) {
                for (int b : buttons) {
                    int newVal = val ^ b;
                    if (newVal == lights) {
                        return i;
                    }
                    if (!totalSet.contains(newVal)) {
                        newSet.add(newVal);
                    }
                }
            }
            latestSet = newSet;
            totalSet.addAll(newSet);
        }
    }

    static class Part2 {
        static final int INF = 1000000;
        static final boolean DEBUG = false;
        int[] joltages;
        List<int[]> buttons;
        // index: state, value: all buttons that affect this state
        int[] stateMask;
        // Variables below are maintained during search
        int[] state;
        int presses = 0; // total presses so far
        boolean[] pressedButtons; // which buttons have been pressed
        int pressedButtonsAsMask = 0; // bitmask of pressed buttons
        int bestSolution = INF;
        long nodes = 0;
        // If true, the selected button must be pressed the max possible nr of times
        // (is set by selectButton())
        boolean isForced = false;

        Part2(List<int[]> buttons, int[] joltages) {
            this.buttons = buttons;
            this.joltages = joltages;
            state = new int[joltages.length];
            stateMask = new int[joltages.length];
            for (int i = 0; i < joltages.length; i++) {
                state[i] = joltages[i];
            }
            pressedButtons = new boolean[buttons.size()];
            pressedButtonsAsMask = 0;
            for (int i = 0; i < buttons.size(); i++) {
                for (int pos : buttons.get(i)) {
                    stateMask[pos] |= (1 << i);
                }
            }
        }

        /**
         * Recursive solver
         */
        public int solve(int depth) {
            ++nodes;
            if (DEBUG)
                log(depth, "At depth " + depth + " with presses: " + presses + " and state: " + Arrays.toString(state));
            if (finished()) {
                if (DEBUG) {
                    log(depth, "Finished with presses: " + presses);
                }
                if (presses < bestSolution) {
                    System.out.println("New best solution: " + presses);
                }
                bestSolution = Math.min(bestSolution, presses);
                return bestSolution;
            }
            int minRequiredPresses = Arrays.stream(state).min().orElse(0);
            if (depth > buttons.size()) {
                if (DEBUG)
                    log(depth, "All buttons pressed without solution");
                return INF;
            }
            if (presses + minRequiredPresses >= bestSolution) {
                if (DEBUG)
                    log(depth, "Give up, min required:" + (minRequiredPresses + presses) + ">= best " + bestSolution);
                return INF;
            }
            int selectedButton = selectButton(depth);
            if (selectedButton == -100) {
                if (DEBUG)
                    log(depth,
                            "No solution possible from here at depth " + depth + " with state: "
                                    + Arrays.toString(state));
                return INF;
            }
            int bestMaxPresses = maxPresses(selectedButton);
            if (!isForced) {
                if (DEBUG)
                    log(depth,
                            "Trying button " + selectedButton + "=" + Arrays.toString(buttons.get(selectedButton))
                                    + " with maxPresses: "
                                    + bestMaxPresses);
            } else {
                if (DEBUG)
                    log(depth, "Forced to press button " + selectedButton + "="
                            + Arrays.toString(buttons.get(selectedButton)));
            }
            // Try pressing this button, from min to max times
            int minPresses = isForced ? bestMaxPresses : 0;
            for (int p = minPresses; p <= bestMaxPresses; p++) {
                if (DEBUG)
                    log(depth, "Pressing button " + selectedButton + " " + p + " times");
                // Press the button p times
                updateState(selectedButton, p);
                pressedButtons[selectedButton] = true;
                pressedButtonsAsMask |= (1 << selectedButton);
                // Recurse
                solve(depth + 1);
                // Backtrack
                updateState(selectedButton, -p);
                pressedButtons[selectedButton] = false;
                pressedButtonsAsMask &= ~(1 << selectedButton);
            }

            return bestSolution;
        }

        /**
         * Return the maximum number of times the given button can be pressed
         * without any state going negative
         */
        int maxPresses(int buttonIndex) {
            var button = buttons.get(buttonIndex);
            int maxPresses = 10000;
            for (var pos : button) {
                maxPresses = Math.min(maxPresses, state[pos]);
            }
            return maxPresses;
        }

        void updateState(int buttonIndex, int presses) {
            var button = buttons.get(buttonIndex);
            for (var pos : button) {
                state[pos] -= presses;
                if (state[pos] < 0 || state[pos] > joltages[pos]) {
                    throw new IllegalStateException(
                            "State went out of bounds at position " + pos + " with state: " + Arrays.toString(state));
                }
            }
            this.presses += presses;
        }

        /**
         * Return the index of a button that must be pressed next,
         * or -1 if no such button exists, or -100 if no solution is possible
         */
        int selectButton(int depth) {
            isForced = false;
            int bestIndex = -1;
            long bestCount = Long.MAX_VALUE;
            for (int i = 0; i < state.length; i++) {
                if (state[i] > 0) {
                    int forcedButton = -1;
                    int count = 0;
                    for (int j = 0; j < pressedButtons.length; j++) {
                        if (!pressedButtons[j]) {
                            var button = buttons.get(j);
                            for (var pos : button) {
                                if (pos == i) {
                                    ++count;
                                    forcedButton = j;
                                }
                            }
                        }
                    }
                    long combs = 0;
                    if (count == 0) {
                        if (DEBUG)
                            log(depth, "No button can press state " + i + " with value " + state[i]);
                        return -100; // No solution possible
                    } else if (count == 1) {
                        isForced = true;
                        return forcedButton;
                    }
                    if (count <= 2) {
                        combs = state[i];
                    } else if (count == 3) {
                        combs = state[i] * state[i];
                    } else {
                        combs = state[i] * state[i] * state[i];
                        if (count > 4) {
                            combs *= state[i];
                        }
                    }
                    if (combs < bestCount) {
                        bestCount = combs;
                        bestIndex = i;
                    }
                }
            }
            if (bestIndex < 0) {
                return -100;
            }
            // check if there are 2 states with different value but same buttons
            for (int i = 0; i < state.length; ++i) {
                if (state[i] == 0)
                    continue;
                for (int j = i + 1; j < state.length; ++j) {
                    if (state[j] == 0 || state[i] == state[j])
                        continue;
                    int mask1 = stateMask[i] & ~pressedButtonsAsMask;
                    int mask2 = stateMask[j] & ~pressedButtonsAsMask;
                    if (mask1 == mask2) {
                        if (DEBUG)
                            log(bestIndex, "No solution, states " + i + " and " + j
                                    + " have same button mask but different values");
                        return -100;
                    }
                }
            }

            int bestButton = -100;
            int bestButtonCount = -1;
            for (int j = 0; j < pressedButtons.length; j++) {
                if (!pressedButtons[j]) {
                    var button = buttons.get(j);
                    for (var pos : button) {
                        if (pos == bestIndex) {
                            if (bestButtonCount < button.length) {
                                bestButton = j;
                                bestButtonCount = button.length;
                            }
                        }
                    }
                }
            }
            return bestButton;
        }

        private boolean finished() {
            return Arrays.stream(state).allMatch(v -> v == 0);
        }

        void log(int depth, String msg) {
            System.out.println(depth + " ".repeat(depth) + msg);
        }

    }

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        var lines = Files.readAllLines(Paths.get("input/10.txt"));
        int part1 = 0;
        int part2 = 0;
        long totalNodes = 0;
        int lineNum = 0;
        for (var line : lines) {
            System.out.println(lineNum + ": Line: " + line);
            ++lineNum;
            if (line.startsWith(";")) {
                continue;
            }
            String pattern = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
            int lights = 0;
            for (int i = 0; i < pattern.length(); i++) {
                if (pattern.charAt(i) == '#') {
                    lights |= (1 << i);
                }
            }
            String coordinates = line.substring(line.indexOf('('), line.lastIndexOf(')') + 1);
            Pattern coordPattern = Pattern.compile("\\((.*?)\\)");
            Matcher matcher = coordPattern.matcher(coordinates);
            List<Integer> buttonBitmasks = new ArrayList<>();
            List<int[]> buttons = new ArrayList<>();
            while (matcher.find()) {
                String lightsStr = matcher.group(1);
                int[] buttonArray = Arrays.stream(lightsStr.split(",")).mapToInt(Integer::parseInt).toArray();
                buttons.add(buttonArray);
                int bits = Arrays.stream(buttonArray).reduce(0, (a, b) -> a + (1 << b));
                buttonBitmasks.add(bits);
            }
            String joltageStr = line.substring(line.indexOf('{') + 1, line.indexOf('}'));
            int count = calcPart1(lights, buttonBitmasks);
            System.out.println("Part 1: button presses: " + count);
            part1 += count;
            int[] joltages = Arrays.stream(joltageStr.split(",")).mapToInt(Integer::parseInt).toArray();
            var p2 = new Part2(buttons, joltages);
            int count2 = p2.solve(0);
            System.out.println("Part 2 presses: " + count2 + " nodes: " + p2.nodes);
            part2 += count2;
            totalNodes += p2.nodes;
        }
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
        System.out.println("Total nodes: " + totalNodes);
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
