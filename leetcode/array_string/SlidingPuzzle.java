import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC773: https://leetcode.com/problems/sliding-puzzle/
//
// On a 2x3 board, there are 5 tiles represented by the integers 1 through 5, and an empty square
// represented by 0. A move consists of choosing 0 and a 4-directionally adjacent number and
// swapping it. The state of the board is solved if and only if the board is [[1,2,3],[4,5,0]].
// Given a puzzle board, return the least number of moves required so that the state of the board is
// solved. If it is impossible for the state of the board to be solved, return -1.
//
// Note:
// board will be a 2 x 3 array as described above.
// board[i][j] will be a permutation of [0, 1, 2, 3, 4, 5].
public class SlidingPuzzle {
    // BFS + Queue + Set + Bit Manipulation
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(99.20%), 38.6 MB(5.98%) for 32 tests
    public int slidingPuzzle(int[][] board) {
        Board target = new Board(new int[][] {{1, 2, 3}, {4, 5, 0}});
        Queue<Board> queue = new LinkedList<>();
        queue.offer(new Board(board));
        Set<Integer> visited = new HashSet<>();
        for (int res = 0; !queue.isEmpty(); res++) {
            for (int size = queue.size(); size > 0; size--) {
                Board cur = queue.poll();
                if (cur.state == target.state) { return res; }

                if (visited.add(cur.state)) {
                    for (Board next : cur.nextMoves()) {
                        queue.offer(next);
                    }
                }
            }
        }
        return -1;
    }

    private static class Board {
        int state;
        final int maxIndex = 5;
        private int index0;

        public Board(int[][] board) {
            state = getState(board);
        }

        private Board(int state, int index0) {
            this.state = state;
            this.index0 = index0;
        }

        private int getState(int[][] board) {
            int res = 0;
            for (int i = 0, index = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++, index++) {
                    res <<= 3;
                    res += board[i][j];
                    if (board[i][j] == 0) {
                        index0 = index;
                    }
                }
            }
            return res;
        }

        private static final int[][] MOVES = {{1, 3}, {0, 2, 4}, {1, 5}, {0, 4}, {1, 3, 5}, {2, 4}};

        public List<Board> nextMoves() {
            List<Board> res = new ArrayList<>();
            for (int nextIndex0 : MOVES[index0]) { // swap two 3-bit
                int targetNum = (state >> (maxIndex - nextIndex0) * 3) & 7;
                int newState = state | (targetNum << (maxIndex - index0) * 3);
                newState &= ~(7 << (maxIndex - nextIndex0) * 3);
                res.add(new Board(newState, nextIndex0));
            }
            return res;
        }
    }

    // BFS + Queue + Set
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(91.61%), 38.6 MB(5.98%) for 32 tests
    public int slidingPuzzle2(int[][] board) {
        final String target = "123450";
        StringBuilder start = new StringBuilder();
        for (int[] row : board) {
            for (int i = 0; i < board[0].length; i++) {
                start.append(row[i]);
            }
        }
        Set<String> visited = new HashSet<>();
        int[][] dirs = new int[][] {{1, 3}, {0, 2, 4}, {1, 5}, {0, 4}, {1, 3, 5}, {2, 4}};
        Queue<String> queue = new LinkedList<>();
        queue.offer(start.toString());
        for (int res = 0; !queue.isEmpty(); res++) {
            for (int size = queue.size(); size > 0; size--) {
                String cur = queue.poll();
                if (target.equals(cur)) { return res; }
                if (!visited.add(cur)) { continue; }

                int pos0 = cur.indexOf('0');
                for (int dir : dirs[pos0]) {
                    queue.offer(swap(cur, pos0, dir));
                }
            }
        }
        return -1;
    }

    private String swap(String str, int i, int j) {
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(i, str.charAt(j));
        sb.setCharAt(j, str.charAt(i));
        return sb.toString();
    }

    private static final int[][] MOVES = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private static final int TARGET = 123450;

    // DFS + Recursion + Backtracking + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 51 ms(6.43%), 38.9 MB(5.98%) for 32 tests
    public int slidingPuzzle3(int[][] board) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(TARGET, 0);
        int startX = 0;
        int startY = 0;
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[0].length - 1; j >= 0; j--) {
                if (board[i][j] == 0) {
                    startX = i;
                    startY = j;
                    break;
                }
            }
        }
        int[] res = new int[] {Integer.MAX_VALUE};
        dfs(res, board, startX, startY, 0, map);
        return (res[0] == Integer.MAX_VALUE) ? -1 : res[0];
    }

    private void dfs(int[] res, int[][] board, int x, int y, int steps, Map<Integer, Integer> map) {
        if (steps > res[0]) { return; }

        int code = encode(board);
        if (code == TARGET) {
            res[0] = steps;
            return;
        }
        if (steps > map.getOrDefault(code, Integer.MAX_VALUE)) { return; }

        map.put(code, steps);
        for (int[] dir : MOVES) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < board.length && ny >= 0 && ny < board[0].length) {
                swap(board, x, y, nx, ny);
                dfs(res, board, nx, ny, steps + 1, map);
                swap(board, nx, ny, x, y); // backtracking
            }
        }
    }

    private void swap(int[][] board, int i, int j, int ni, int nj) {
        int tmp = board[i][j];
        board[i][j] = board[ni][nj];
        board[ni][nj] = tmp;
    }

    private int encode(int[][] board) {
        int code = 0;
        for (int[] row : board) {
            for (int i = 0; i < board[0].length; i++) {
                code *= 10;
                code += row[i];
            }
        }
        return code;
    }

    private void test(int[][] board, int expected) {
        assertEquals(expected, slidingPuzzle(board));
        assertEquals(expected, slidingPuzzle2(board));
        assertEquals(expected, slidingPuzzle3(board));
    }

    @Test public void test() {
        test(new int[][] {{1, 2, 3}, {4, 0, 5}}, 1);
        test(new int[][] {{1, 2, 3}, {5, 4, 0}}, -1);
        test(new int[][] {{4, 1, 2}, {5, 0, 3}}, 5);
        test(new int[][] {{3, 2, 4}, {1, 5, 0}}, 14);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
