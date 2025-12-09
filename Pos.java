public record Pos(int col, int row) {

    public Pos add(Pos other) {
        return new Pos(this.col + other.col, this.row + other.row);
    }

    public int manhattan(Pos other) {
        return Math.abs(this.col - other.col) + Math.abs(this.row - other.row);
    }

    public static final Pos[] NESW8 = {
            new Pos(0, -1), // N
            new Pos(1, -1), // NE
            new Pos(1, 0), // E
            new Pos(1, 1), // SE
            new Pos(0, 1), // S
            new Pos(-1, 1), // SW
            new Pos(-1, 0), // W
            new Pos(-1, -1) // NW
    };
}
