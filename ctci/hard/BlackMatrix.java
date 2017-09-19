import org.junit.Test;

/**
 * Cracking the Coding Interview(5ed) Problem 18.11:
 * A square matrix, where each cell is either black or white. Find the maximum
 * subsquare such that all four borders are filled with black pixels.
 */
public class BlackMatrix {
    public static class Square {
        int row;
        int column;
        int size;

        public Square(int r, int c, int sz) {
            row = r;
            column = c;
            size = sz;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + column + ", " + size + ")";
        }
    }

    private static class Cell {
        int rightBlacks;
        int belowBlacks;

        public Cell(int right, int below) {
            rightBlacks = right;
            belowBlacks = below;
        }
    }

    private static Square findSquareWithSize(Cell[][] processed, int size) {
        int count = processed.length - size + 1;
        for (int row = 0; row < count; row++) {
            for (int col = 0; col < count; col++) {
                if (isSquare(processed, row, col, size)) {
                    return new Square(row, col, size);
                }
            }
        }
        return null;
    }

    public static Square findSquare(int[][] matrix){
        Cell[][] processed = processSquare(matrix);
        for (int i = matrix.length; i > 0; i--) {
            Square square = findSquareWithSize(processed, i);
            if (square != null) return square;
        }
        return null;
    }

    private static boolean isSquare(Cell[][] matrix, int row, int col, int sz) {
        Cell topLeft = matrix[row][col];
        if (topLeft.rightBlacks < sz) return false;
        if (topLeft.belowBlacks < sz) return false;

        Cell topRight = matrix[row][col + sz - 1];
        if (topRight.belowBlacks < sz) return false;

        Cell bottomLeft = matrix[row + sz - 1][col];
        return (bottomLeft.rightBlacks >= sz);
    }

    public static Cell[][] processSquare(int[][] matrix) {
        Cell[][] processed = new Cell[matrix.length][matrix.length];

        for (int r = matrix.length - 1; r >= 0; r--) {
            for (int c = matrix.length - 1; c >= 0; c--) {
                int rightBlacks = 0;
                int belowBlacks = 0;
                if (matrix[r][c] == 0) {
                    rightBlacks++;
                    belowBlacks++;
                    if (c + 1 < matrix.length) {
                        Cell previous = processed[r][c + 1];
                        rightBlacks += previous.rightBlacks;
                    }
                    if (r + 1 < matrix.length) {
                        Cell previous = processed[r + 1][c];
                        belowBlacks += previous.belowBlacks;
                    }
                }
                processed[r][c] = new Cell(rightBlacks, belowBlacks);
            }
        }
        return processed;
    }

    private void test() {
    }

    @Test
    public void test1() {
        test();
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BlackMatrix");
    }
}
