import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC909: https://leetcode.com/problems/snakes-and-ladders/
//
// On an N x N board, the numbers from 1 to N*N are written boustrophedonically
// starting from the bottom left, and alternating direction each row.
// You start on square 1 of the board.  Each move, starting from square x,
// consists of the following:
// Choose a destination square S with number x+1, x+2, x+3, x+4, x+5, or x+6,
// provided this number is <= N*N. If S has a snake or ladder, you move to the
// destination of that snake or ladder.  Otherwise, you move to S.
// A board square on row r and column c has a "snake or ladder" if
// board[r][c] != -1.  The destination of that snake or ladder is board[r][c].
// Return the least number of moves required to reach square N*N.  If it is not
// possible, return -1.
public class SnakesAndLadders {
    // BFS + Queue + Set
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 100.00%(26 ms for 211 tests)
    public int snakesAndLadders(int[][] board) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);
        int n = board.length;
        Set<Integer> visited = new HashSet<>();
        for (int depth = 0, dest = n * n; !queue.isEmpty(); depth++) {
            for (int i = queue.size(); i > 0; i--) {
                int cur = queue.poll();
                if (cur == dest) return depth;
                if (!visited.add(cur)) continue;

                for (int k = Math.min(cur + 6, dest); k > cur; k--) {
                    queue.offer(next(k, board));
                }
            }
        }
        return -1;
    }

    // BFS + Queue + Hash Table
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 100.00%(26 ms for 211 tests)
    private int next(int num, int[][] board) {
        int n = board.length;
        int row = n - 1 - (num - 1) / n;
        int col = ((n - row) % 2 != 0) ? ((num - 1) % n) : (n - num % n);
        int res = board[row][col % n];
        return res > 0 ? res : num;
    }

    public int snakesAndLadders2(int[][] board) {
        int n = board.length;
        Map<Integer, Integer> dist = new HashMap<>();
        dist.put(1, 0);
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);
        for (int dest = n * n; !queue.isEmpty(); ) {
            int cur = queue.poll();
            if (cur == dest) return dist.get(cur);

            for (int k = Math.min(cur + 6, dest); k > cur; k--) {
                int x = n - 1 - (k - 1) / n;
                int remainder = (k - 1) % n;
                int y = (x % 2 != n % 2) ? remainder : n - 1 - remainder;
                int next = (board[x][y] == -1) ? k : board[x][y];
                if (!dist.containsKey(next)) {
                    dist.put(next, dist.get(cur) + 1);
                    queue.offer(next);
                }
            }
        }
        return -1;
    }

    void test(int[][] board, int expected) {
        assertEquals(expected, snakesAndLadders(board));
        assertEquals(expected, snakesAndLadders2(board));
    }

    @Test
    public void test() {
        test(new int[][] { { -1, -1, -1, -1, -1, -1 }, 
                           { -1, -1, -1, -1, -1, -1 },
                           { -1, -1, -1, -1, -1, -1 },
                           { -1, 35, -1, -1, 13, -1 },
                           { -1, -1, -1, -1, -1, -1 },
                           { -1, 15, -1, -1, -1, -1 } }, 4);
        test(new int[][] { { -1, -1, 19, 10, -1 }, 
                           { 2, -1, -1, 6, -1 }, 
                           { -1, 17, -1, 19, -1 }, 
                           { 25, -1, 20, -1, -1 },
                           { -1, -1, -1, -1, 15 } }, 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
