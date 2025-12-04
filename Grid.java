import module java.base;

public class Grid<T> implements Iterable<Pos> {
    private final int rows;
    private final int cols;
    private Function<Pos, T> cellProvider;

    /**
     * Constructs a Grid with the given number of rows and columns.
     * 
     * @param rows         the number of rows in the grid
     * @param cols         the number of columns in the grid
     * @param cellProvider a function that provides the cell value for a given
     *                     position
     */
    public Grid(int rows, int cols, Function<Pos, T> cellProvider) {
        this.rows = rows;
        this.cols = cols;
        this.cellProvider = cellProvider;
    }

    public boolean contains(Pos p) {
        return p.row() >= 0 && p.row() < rows && p.col() >= 0 && p.col() < cols;
    }

    public T get(Pos p) {
        return cellProvider.apply(p);
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    @Override
    public Iterator<Pos> iterator() {
        return new java.util.Iterator<>() {
            private int currentRow = 0;
            private int currentCol = 0;

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

    public Stream<Pos> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }
}
