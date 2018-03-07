import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC782: https://leetcode.com/problems/transform-to-chessboard/
//
// An N x N board contains only 0s and 1s. In each move, you can swap any 2 rows
// with each other, or any 2 columns with each other. What is the minimum number
// of moves to transform the board into a "chessboard" - a board where no 0s and
// no 1s are 4-directionally adjacent? If the task is impossible, return -1.
// Note:
// board has the same number of rows and columns, a number in the range [2, 30].
public class TransformToChessboard {
    // Dimension Independence + Bit Manipulation
    // beats %(11 ms for 124 tests)
    public int movesToChessboard(int[][] board) {
        int count1 = moves(board[0], 1);
        if (count1 < 0) return -1;

        int n = board.length;
        int[] rows = new int[n];
        int i = 0;
        for (int[] row : board) {
            int code = 0;
            for (int bit : row) {
                code = 2 * code + bit;
            }
            rows[i++] = code;
        }
        int count2 = moves(rows, n);
        return (count2 < 0) ? -1 : (count1 + count2);
    }

    private int moves(int[] nums, int bits) {
        int num1 = nums[0];
        int num2 = num1 ^ ((1 << bits) - 1); // flip all bits
        int count1 = 0;
        int count2 = 0;
        for (int num : nums) {
            if (num == num1) {
                count1++;
            } else if (num == num2) {
                count2++;
            } else return -1;
        }
        if (Math.abs(count1 - count2) > 1) return -1;

        if (count1 > count2) return moves(nums, new int[] {num1, num2});

        if (count1 < count2) return moves(nums, new int[] {num2, num1});

        return Math.min(moves(nums, new int[] {num1, num2}),
                        moves(nums, new int[] {num2, num1}));
    }

    private int moves(int[] nums, int[] flip) {
        int res = 0;
        for (int i = 0, j = nums.length - 1; i < j; i++) {
            if (nums[i] != flip[i & 1]) {
                for (; nums[j] == flip[j & 1]; j--) {}
                j--;
                res++;
            }
        }
        return res;
    }

    // Dimension Independence + Bit Manipulation + Hash Table
    // beats %(18 ms for 124 tests)
    public int movesToChessboard2(int[][] board) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int[] row : board) {
            int code = 0;
            for (int bit : row) {
                code = 2 * code + bit;
            }
            count.put(code, count.getOrDefault(code, 0) + 1);
        }
        int n = board.length;
        int k1 = moves(count, n);
        if (k1 < 0) return -1;

        count = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int code = 0;
            for (int j = 0; j < n; j++) {
                code = 2 * code + board[j][i];
            }
            count.put(code, count.getOrDefault(code, 0) + 1);
        }
        int k2 = moves(count, n);
        return (k2 < 0) ? -1 : (k1 + k2);
    }

    private int moves(Map<Integer, Integer> count, int bits) {
        if (count.size() != 2) return -1;

        Iterator<Integer> itr = count.keySet().iterator();
        int k1 = itr.next();
        int k2 = itr.next();
        if ((k1 ^ k2) != (1 << bits) - 1) return -1;
        if (Math.abs(count.get(k1) - count.get(k2)) > 1) return -1;

        int ones = Integer.bitCount(k1);
        int res = Integer.MAX_VALUE;
        if ((bits & 1) == 0 || ones * 2 < bits) { // zero-ending
            int mask = 0xAAAAAAAA & ((1 << bits) - 1);
            res = Math.min(res, Integer.bitCount(k1 ^ mask) / 2);
        }
        if ((bits & 1) == 0 || ones * 2 > bits) { // ones-ending
            int mask = 0x55555555 & ((1 << bits) - 1);
            res = Math.min(res, Integer.bitCount(k1 ^ mask) / 2);
        }
        return res;
    }

    // Dimension Independence + Bit Manipulation
    // beats %(11 ms for 124 tests)
    public int movesToChessboard3(int[][] board) {
        int count1 = moves(board[0]);
        if (count1 < 0) return -1;

        int n = board.length;
        int[] bits = new int[n];
        for (int i = 1; i < n; i++) {
            bits[i] = board[i][0] ^ board[0][0];
            for (int j = 1; j < n; j++) {
                if ((board[i][j] ^ board[0][j]) != bits[i]) return -1;
            }
        }
        int count2 = moves(bits);
        return (count2 < 0) ? -1 : (count1 + count2);
    }

    private int moves(int[] bits) {
        int code = 0;
        for (int bit : bits) {
            code = 2 * code + bit;
        }
        int ones = Integer.bitCount(code);
        int zeros = bits.length - ones;
        if (Math.abs(zeros - ones) > 1) return -1;

        int res = Integer.MAX_VALUE;
        if (zeros >= ones) { // zero-ending
            int mask = 0xAAAAAAAA & ((1 << bits.length) - 1);
            res = Math.min(res, Integer.bitCount(code ^ mask) / 2);
        }
        if (zeros <= ones) { // one-ending
            int mask = 0x55555555 & ((1 << bits.length) - 1);
            res = Math.min(res, Integer.bitCount(code ^ mask) / 2);
        }
        return res;
    }

    // Dimension Independence + Bit Manipulation
    // beats %(11 ms for 124 tests)
    public int movesToChessboard4(int[][] board) {
        int n = board.length;
        for (int i = 1; i < n; i++) {
            int bit = board[i][0] ^ board[0][0];
            for (int j = 1; j < n; j++) {
                if ((board[0][j] ^ board[i][j]) != bit) return -1;
            }
        }
        int row1 = 0; // total 1's in row
        int col1 = 0; // total 1's in column
        int rowSwap = 0;
        int colSwap = 0;
        for (int i = 0; i < n; i++) {
            row1 += board[0][i];
            col1 += board[i][0];
            rowSwap += board[i][0] ^ (i & 1); 
            colSwap += board[0][i] ^ (i & 1); 
        }
        if (Math.abs(2 * row1 - n) > 1 || Math.abs(2 * col1 - n) > 1) return -1;

        if ((n & 1) == 1) {
            if ((colSwap & 1) == 1) {
                colSwap = n - colSwap;
            }
            if ((rowSwap & 1) == 1) {
                rowSwap = n - rowSwap;
            }
        } else {
            colSwap = Math.min(n - colSwap, colSwap);
            rowSwap = Math.min(n - rowSwap, rowSwap);
        }
        return (colSwap + rowSwap) >> 1;
    }

    void test(int[][] board, int expected) {
        assertEquals(expected, movesToChessboard(board));
        assertEquals(expected, movesToChessboard2(board));
        assertEquals(expected, movesToChessboard3(board));
        assertEquals(expected, movesToChessboard4(board));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 1, 1, 0}, {0, 1, 1, 0},
                          {1, 0, 0, 1}, {1, 0, 0, 1}}, 2);
        test(new int[][] {{0, 1}, {1, 0}}, 0);
        test(new int[][] {{1, 0}, {1, 0}}, -1);
        test(new int[][] {{1, 1, 0}, {0, 0, 1}, {0, 0, 1}}, 2);
        test(new int[][] {{1, 0, 0, 1, 1}, {0, 1, 1, 0, 0}, {1, 0, 0, 1, 1},
                          {0, 1, 1, 0, 0}, {0, 1, 1, 0, 0}}, 3);
        test(new int[][] {{0, 0, 1, 0, 1, 1}, {1, 1, 0, 1, 0, 0},
                          {1, 1, 0, 1, 0, 0}, {0, 0, 1, 0, 1, 1},
                          {1, 1, 0, 1, 0, 0}, {0, 0, 1, 0, 1, 1}}, 2);
        test(new int[][] {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}}, 0);
        test(new int[][] {{1, 0, 1}, {0, 1, 0}, {1, 0, 0}}, -1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
