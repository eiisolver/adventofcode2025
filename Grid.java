public class Grid implements Iterable<Pos> {
    private final int rows;
    private final int cols;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public boolean contains(Pos p) {
        return p.row() >= 0 && p.row() < rows && p.col() >= 0 && p.col() < cols;
    }

    public char get(char[][] cells, Pos p) {
        return cells[p.row()][p.col()];
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    @Override
    public java.util.Iterator<Pos> iterator() {
        return new java.util.Iterator<>() {
            private int currentRow = 0;
            private int currentCol = 0;

            @Override
            public boolean hasNext() {
                return currentRow < rows;
            }

            @Override
            public Pos next() {
                Pos nextPos = new Pos(currentCol, currentRow);
                currentCol++;
                if (currentCol >= cols) {
                    currentCol = 0;
                    currentRow++;
                }
                return nextPos;
            }
        };
    }
}
