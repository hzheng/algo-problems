import java.util.*;
import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/00000000000000cb/dashboard/0000000000007a30
// Qualification Round 2018: Problem C - Go, Gopher!
//
// An orchard is a two-dimensional matrix of cells of unprepared soil, with 1000
// rows and 1000 columns. We need there to be at least A prepared cells, which 
// must form a single grid-aligned rectangle. None of the cells outside of that 
// rectangle can be prepared. We have the Go gopher to prepare cells. We can 
// deploy the gopher by giving it the coordinates of a target cell in the matrix
// that is not along any of the borders of the matrix. Gopher will choose a cell 
// uniformly at random from the 3x3 block of nine cells centered on the target
// cell, and then prepare the cell it has chosen. We can only deploy the gopher 
// up to 1000 times. When you deploy the gopher, you will be told which cell the
// gopher actually prepared, and you can take this information into account 
// before deploying it again.
// Input and output
// You will interact with a separate process that both provides information and 
// evaluates your responses. All information comes into your program via 
// standard input; anything that you need to communicate should be sent via 
// standard output. To help you debug, a local testing tool script is provided.
// Initially, your program should read a single line containing a single integer
// T indicating the number of test cases. Then you need to process T test cases.
// For each test case, your program will read a single line containing a single 
// integer A indicating the minimum required prepared rectangular area. Then, 
// your program will process up to 1000 exchanges with our judge.
// For each exchange, your program needs to use standard output to send a single 
// line containing two integers I and J: the row and column number to deploy the 
// gopher to. The two integers must be between 2 and 999. If your output format
// is wrong, your program will fail, and the judge will send you a single line
// with -1 -1 which signals that your test has failed, and it will not send 
// anything to your input stream after that. Otherwise, in response to your 
// deployment, the judge will print a single line containing two integers I' and
// J' to your input stream, which your program must read through standard input.
// If the last deployment caused the set of prepared cells to be a rectangle of
// area at least A, you will get I'=J'=0, signaling the end of the test case. 
// Otherwise, I' and J' are the row and column numbers of the cell that was
// actually prepared by the gopher, with abs(I'-I) ≤ 1 and abs(J'-J) ≤ 1. Then,
// you can start another exchange. If the test case is solved within 1000
// deployments, you will receive the I' = J' = 0 message from the judge, and 
// then continue to solve the next test case. After 1000 exchanges, if the test 
// case is not solved, the judge will send the I' = J' = -1 message, and stop 
// sending output to your input stream after.
// Limits
// 1 ≤ T ≤ 20.
// Memory limit: 1 GB.
// Test set 1 (Visible)
// A = 20.
// Time limit (for the entire test set): 20 seconds.
// Test set 2 (Hidden)
// A = 200.
// Time limit (for the entire test set): 60 seconds.
public class GoGopher {
    private static class Gopher {
        private boolean[][] grid = new boolean[3][3];
        private int unfilledGroups;

        public Gopher(int A) {
            unfilledGroups = (A - 1)/ 9 + 1;
        }

        public int[] request() {
            boolean filled = true;
            outer : for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (!grid[i][j]) {
                        filled = false;
                        break outer;
                    }
                }
            }
            if (filled) {
                unfilledGroups--;
                for (int i = 0; i < 3; i++) {
                    Arrays.fill(grid[i], false);
                }
            }
            return new int[]{2, unfilledGroups * 3 + 2};
        }

        public void exchange(int row, int col) {
            grid[row - 1][col - unfilledGroups * 3 - 1] = true;
        }            
    }
    
    // TreeSet
    // only good for visible test set
    private static class Gopher0 {
        private static final int MAX_ROW = 1000;
        private static final int MAX_COL = 1000;

        private int side;
        private int shiftX;
        private int shiftY;
        private Point[][] cells;
        private NavigableSet<Point> prepareSet = new TreeSet<>();

        public Gopher0(int A) {
            side = (int) Math.ceil(Math.sqrt(A));
        }

        public int[] request() {
            if (shiftX == 0) {
                return new int[] { MAX_ROW / 2, MAX_COL / 2 };
            }
            Point point = prepareSet.first();
            return new int[] { point.x + shiftX, point.y + shiftY };
        }

        public void exchange(int row, int col) {
            if (shiftX == 0) {
                shiftX = row - side / 2;
                shiftY = col - side / 2;
                initialize();
            }
            Point point = cells[row -= shiftX][col -= shiftY];
            if (point.filled) return;

            point.filled = true;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    int r = row + i;
                    int c = col + j;
                    if (r < 0 || r >= side || c < 0 || c >= side) continue;

                    point = cells[r][c];
                    if (--point.hollow == 0) {
                        prepareSet.remove(point);
                    }
                }
            }
        }

        private void initialize() {
            cells = new Point[side][side];
            for (int i = 0; i < side; i++) {
                for (int j = 0; j < side; j++) {
                    Point point = new Point(i, j);
                    cells[i][j] = point;
                    if (i > 0 && i < side - 1 && j > 0 && j < side - 1) {
                        prepareSet.add(point);
                    }
                }
            }
        }

        private static class Point implements Comparable<Point> {
            int x;
            int y;
            boolean filled;
            int hollow = 9;

            Point(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public String toString() {
                return "(" + x + "," + y + "){" + (filled ? '1' : '0') + ";" +
                       hollow + "}";
            }

            @Override
            public boolean equals(Object other) {
                if (!(other instanceof Point)) return false;

                Point pt = (Point) other;
                return x == pt.x && y == pt.y;
            }

            @Override
            public int hashCode() {
                return x * 10000 + y;
            }

            @Override
            public int compareTo(Point other) {
                int diff = hollow - other.hollow;
                if (diff != 0) return diff;

                return hashCode() - other.hashCode();
            }
        }
    }

    public static void main(String[] args) {
        Scanner in =
            new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            Gopher obj = new Gopher(in.nextInt());
            while (true) {
                int[] req = obj.request();

                out.println(req[0] + " " + req[1]);
                int row = in.nextInt();
                int col = in.nextInt();
                if (row < 0 || col < 0) {
                    System.err.println("ERROR");
                    return;
                }
                if (row == 0 && col == 0) break;

                obj.exchange(row, col);
            }
        }
    }
}
