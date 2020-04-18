import java.util.*;
import java.io.*;

// Round 1A 2020: Problem B - Pascal Walk
//
// Pascal's triangle consists of an infinite number of rows of an increasing number of integers each, arranged in a
// triangular shape. Let us define (r, k) as the k-th position from the left in the r-th row, with both r and k counted
// starting from 1. Then Pascal's triangle is defined by the following rules:
// The r-th row contains r positions (r, 1), (r, 2), ..., (r, r).
// The numbers at positions (r, 1) and (r, r) are 1, for all r.
// The number at position (r, k) is the sum of the numbers at positions (r - 1, k - 1) and (r - 1, k), for all k with
// 2 ≤ k ≤ r - 1.
// In this problem, a Pascal walk is a sequence of s positions (r1, k1), (r2, k2), ..., (rs, ks) in Pascal's triangle
// that satisfy the following criteria:
// r1 = 1 and k1 = 1.
// Each subsequent position must be within the triangle and adjacent (in one of the six possible directions) to the
// previous position. That is, for all i ≥ 1, (ri + 1, ki + 1) must be one of the following that is within the triangle:
// (ri - 1, ki - 1), (ri - 1, ki), (ri, ki - 1), (ri, ki + 1), (ri + 1, ki), (ri + 1, ki + 1).
// No position may be repeated within the sequence. That is, for every i ≠ j, either ri ≠ rj or ki ≠ kj, or both.
// Find any Pascal walk of S ≤ 500 positions such that the sum of the numbers in all of the positions it visits is equal
// to N. It is guaranteed that at least one such walk exists for every N.
// Input
// The first line of the input gives the number of test cases, T. T test cases follow. Each consists of a single line
// containing a single integer N.
// Output
// For each test case, first output a line containing Case #x:, where x is the test case number (starting from 1). Then,
// output your proposed Pascal walk of length S ≤ 500 using S additional lines. The i-th of these lines must be ri ki
// where (ri, ki) is the i-th position in the walk. For example, the first line should be 1 1 since the first position
// for all valid walks is (1, 1). The sum of the numbers at the S positions of proposed Pascal walk must be exactly N.
// Limits
// Time limit: 20 seconds per test set.
// Memory limit: 1GB.
// 1 ≤ T ≤ 100.
// Test set 1 (Visible Verdict)
// 1 ≤ N ≤ 501.
// Test set 2 (Visible Verdict)
// 1 ≤ N ≤ 1000.
// Test set 3 (Hidden Verdict)
// 1 ≤ N ≤ 10 ^ 9.
public class PascalWalk {
    // DFS + Recursion + Heap
    public static int[][] solve(int target) {
        LinkedList<Point> path = new LinkedList<>();
        if (!dfs(new Point(0, 0), new HashSet<>(), target, path)) return null;

        int n = path.size();
        int[][] res = new int[n][2];
        for (int i = n - 1; i >= 0; i--) {
            Point p = path.pollLast();
            res[i] = new int[]{p.x + 1, p.y + 1};
        }
        return res;
    }

    private static final int[][] MOVES = {{0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {1, 0}, {1, 1}};

    private static boolean dfs(Point cur, Set<Point> visited, long remain, LinkedList<Point> path) {
        if (!visited.add(cur)) return false;

        path.offerLast(cur);
        remain -= cur.getValue();
        if (remain == 0) return true;

        // choose larger value first, otherwise TLE
        PriorityQueue<Point> candidates = new PriorityQueue<>();
        int x = cur.x;
        int y = cur.y;
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (ny < 0 || nx < ny) continue;

            Point p = new Point(nx, ny);
            if (p.getValue() <= remain) {
                candidates.offer(p);
            }
        }
        while (!candidates.isEmpty()) {
            if (dfs(candidates.poll(), visited, remain, path)) return true;
        }
//        visited.remove(cur); // don't need revisit
        path.pollLast(); // backtrack
        return false;
    }

    private static class Point implements Comparable<Point> {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int compareTo(Point other) {
            return Long.compare(other.getValue(), getValue());
        }

        long getValue() {
            return getRow(x).get(y);
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Point)) return false;

            Point p = (Point) other;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static Map<Integer, List<Long>> cached = new HashMap<>();

    private static List<Long> getRow(int rowIndex) {
        List<Long> res = cached.get(rowIndex);
        if (res != null) return res;

        res = new ArrayList<>(rowIndex + 1);
        cached.put(rowIndex, res);
        res.add(1L);
        for (int i = 1; i <= (rowIndex + 1) / 2; i++) {
            res.add(res.get(i - 1) * (rowIndex - i + 1) / i);
        }
        for (int i = (rowIndex + 1) / 2 + 1; i <= rowIndex; i++) {
            res.add(res.get(rowIndex - i));
        }
        return res;
    }

    // Math
    public static List<int[]> solve2(int target) {
        final int BASE = 30; // at most 30 rows
        List<int[]> res = new ArrayList<>();
        int remaining = Math.min(target, BASE);
        int row = 1;
        boolean leftToRight = true;
        for (int num = target - BASE; num > 0; row++, num >>= 1) {
            if ((num & 1) == 0) { // 0-bit
                res.add(new int[] {row, leftToRight ? 1 : row});
                remaining--;
            } else { // 1-bit (sum of each row is 2^row)
               for (int i = 1; i <= row; i++) {
                   res.add(new int[] {row, leftToRight ? i : (row - i + 1)});
               }
               leftToRight ^= true; // change direction
            }
        }
        for (int i = row; remaining > 0; remaining--, i++) {
            res.add(new int[] {i, leftToRight ? 1 : i});
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            int N = in.nextInt();
            out.format("Case #%d:", i);
            out.println();
            for (int[] res : solve2(N)) {
                out.println(res[0] + " " + res[1]);
            }
        }
    }
}
