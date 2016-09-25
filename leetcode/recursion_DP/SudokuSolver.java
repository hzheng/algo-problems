import java.util.*;
import java.util.stream.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC037: https://leetcode.com/problems/sudoku-solver/
// 
// Solve a Sudoku puzzle by filling the empty cells
public class SudokuSolver {
    private static final int SIZE = 9;
    private static final char BLANK = '.';

    // beats 18.38%(83 ms)
    public void solveSudoku(char[][] board) {
        List<BlockList> blocks = new ArrayList<>(SIZE * 3);
        for (int i = 0; i < SIZE; i++) {
            blocks.add(new RowList(board, i));
            blocks.add(new ColList(board, i));
            blocks.add(new SquareList(board, i));
        }
        solve(blocks);
    }

    private boolean solve(List<BlockList> blocks) {
        PriorityQueue<BlockList> queue = new PriorityQueue<>(SIZE * 3);
        for (BlockList block : blocks) {
            if (!block.isValid()) return false;

            if (block.getBlanks() > 0) {
                queue.add(block);
            }
        }

        if (queue.isEmpty()) return true;

        BlockList head = queue.poll();
        while (head.fill()) {
            if (solve(blocks)) return true;
        }
        head.unfill();
        return false;
    }

    static abstract class BlockList extends AbstractList<Character>
        implements Comparable<BlockList> {
        int order;
        int blanks = -1;
        char[][] board;
        int[] filledIndices;
        DigitPermuation digitPerm;

        BlockList(char[][] board, int order) {
            this.board = board;
            this.order = order;
        }

        @Override
        public int size() {
            return SIZE;
        }

        @Override
        public int compareTo(BlockList other) {
            return blanks - other.blanks;
        }

        int getBlanks() {
            if (blanks == 0) return 0;

            blanks = 0;
            for (char c : this) {
                if (c == BLANK) {
                    blanks++;
                }
            }
            return blanks;
        }

        boolean isValid() {
            int bits = 0;
            for (char c : this) {
                if (c != BLANK) {
                    int mask = 1 << (c - '1');
                    if ((bits & mask) > 0) return false;

                    bits |= mask;
                }
            }
            return true;
        }

        static class DigitPermuation {
            Character[] digits;
            Character[] lastPerm;

            DigitPermuation(Character[] digits) {
                this.digits = digits;
            }

            public Character[] next() {
                int len = digits.length;
                if (lastPerm == null) {
                    lastPerm = new Character[len];
                    for (int i = 0; i < len; i++) {
                        lastPerm[i] = digits[i];
                    }
                    return lastPerm;
                }

                int i = digits.length - 1;
                for (; i > 0 && lastPerm[i - 1] >= lastPerm[i]; i--);
                if (i == 0) return null;

                int j = len - 1;
                for (; j >= 0 && lastPerm[j] <= lastPerm[i - 1]; j--);
                swap(i - 1, j);

                for (j = len - 1; i < j; i++, j--) {
                    swap(i, j);
                }
                return lastPerm;
            }

            private void swap(int i, int j) {
                char tmp = lastPerm[i];
                lastPerm[i] = lastPerm[j];
                lastPerm[j] = tmp;
            }
        } // DigitPermuation

        private void prepareFill() {
            Set<Character> digits = new HashSet<>();
            for (int i = 1; i < 10; i++) {
                digits.add((char)('0' + i));
            }
            filledIndices = new int[blanks];
            int i = 0;
            int j = 0;
            for (char c : this) {
                if (c == BLANK) {
                    filledIndices[j++] = i;
                } else {
                    digits.remove(c);
                }
                i++;
            }
            digitPerm = new DigitPermuation(digits.toArray(new Character[0]));
        }

        boolean fill() {
            if (digitPerm == null) {
                prepareFill();
            }

            Character[] digits = digitPerm.next();
            if (digits == null) return false;

            for (int i = 0; i < digits.length; i++) {
                set(filledIndices[i], digits[i]);
            }
            blanks = 0;
            return true;
        }

        void unfill() {
            for (int i : filledIndices) {
                set(i, BLANK);
            }
            digitPerm = null;
            // blanks = -1;
            blanks = filledIndices.length;
        }
    } // BlockList

    static class RowList extends BlockList {
        RowList(char[][] board, int order) {
            super(board, order);
        }

        @Override
        public Character get(int index) {
            return board[order][index];
        }

        @Override
        public Character set(int index, Character c) {
            return board[order][index] = c;
        }
    } // RowList

    static class ColList extends BlockList {
        ColList(char[][] board, int order) {
            super(board, order);
        }

        @Override
        public Character get(int index) {
            return board[index][order];
        }

        @Override
        public Character set(int index, Character c) {
            return board[index][order] = c;
        }
    } // ColList

    static class SquareList extends BlockList {
        int groupRow;
        int groupCol;
        int curRow;
        int curCol;

        SquareList(char[][] board, int order) {
            super(board, order);
            groupRow = (order / 3) * 3;
            groupCol = (order % 3) * 3;
        }

        @Override
        public Character get(int index) {
            return board[groupRow + index / 3][groupCol + index % 3];
        }

        @Override
        public Character set(int index, Character c) {
            return board[groupRow + index / 3][groupCol + index % 3] = c;
        }
    } // SquareList

    // Backtracking
    // http://www.jiuzhang.com/solutions/sudoku-solver/
    // beats 33.73%(33 ms)
    public void solveSudoku2(char[][] board) {
        solve(board);
    }

    private boolean solve(char[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != BLANK) continue;

                for (char c = '1'; c <= '9'; c++) {
                    board[i][j] = c;
                    if (isValid(board, i, j) && solve(board)) return true;

                    board[i][j] = BLANK;
                }
                return false;
            }
        }
        return true;
    }

    private boolean isValid(char[][] board, int row, int col) {
        int bits = 0;
        for (int i = 0; i < 9; i++) {
            char c = board[row][i];
            if (c == BLANK) continue;

            int mask = 1 << (c - '1');
            if ((bits & mask) > 0) return false;

            bits |= mask;
        }

        bits = 0;
        for (int i = 0; i < 9; i++) {
            char c = board[i][col];
            if (c == BLANK) continue;

            int mask = 1 << (c - '1');
            if ((bits & mask) > 0) return false;

            bits |= mask;
        }

        bits = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char c = board[row / 3 * 3 + i][col / 3 * 3 + j];
                if (c == BLANK) continue;

                int mask = 1 << (c - '1');
                if ((bits & mask) > 0) return false;

                bits |= mask;
            }
        }
        return true;
    }

    // Backtracking
    // beats 72.51%(19 ms)
    public void solveSudoku3(char[][] board) {
        List<Integer> blanks = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == BLANK) {
                    blanks.add(i * SIZE + j);
                }
            }
        }
        // solve(blanks.stream().mapToInt(i->i).toArray(), board, 0);
        int[] blankArray = new int[blanks.size()];
        for (int i = 0; i < blankArray.length; i++) {
            blankArray[i] = blanks.get(i);
        }
        solve(blankArray, board, 0);
    }

    private boolean solve(int[] blanks, char[][] board, int cur) {
        if (cur == blanks.length) return true;

        int index = blanks[cur];
        int row = index / SIZE;
        int col = index % SIZE;
        for (char c = '1'; c <= '9'; c++) {
            board[row][col] = c;
            if (isValid(board, row, col) && solve(blanks, board, cur + 1)) {
                return true;
            }
            board[row][col] = BLANK;
        }
        return false;
    }

    // Solution of Choice
    // Backtracking
    // beats 54.97%(25 ms)
    public void solveSudoku4(char[][] board) {
        solve4(board);
    }

    private boolean solve4(char[][] board){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != BLANK) continue;

                for (char c = '1'; c <= '9'; c++) {
                    if (!isValid(board, i, j, c)) continue;

                    board[i][j] = c;
                    if (solve4(board)) return true;

                    board[i][j] = BLANK;
                }
                return false;
            }
        }
        return true;
    }

    public boolean isValid(char[][] board, int i, int j, char c) {
        for (int row = 0; row < 9; row++) {
            if (board[row][j] == c) return false;
        }
        for (int col = 0; col < 9; col++) {
            if (board[i][col] == c) return false;
        }
        for (int row = i / 3 * 3; row < i / 3 * 3 + 3; row++) {
            for (int col = j / 3 * 3; col < j / 3 * 3 + 3; col++) {
                if (board[row][col] == c) return false;
            }
        }
        return true;
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<char[][]> solve, String[] boardStr, String[] expected) {
        int size = boardStr.length;
        char[][] board = new char[size][];
        for (int i = 0; i < size; i++) {
            board[i] = boardStr[i].toCharArray();
        }
        char[][] expectedBoard = new char[size][];
        for (int i = 0; i < size; i++) {
            expectedBoard[i] = expected[i].toCharArray();
        }
        solve.apply(board);

        System.out.println("******result*****");
        print(board);
        assertArrayEquals(expectedBoard, board);
    }

    void test(String[] boardStr, String[] expected) {
        SudokuSolver solver = new SudokuSolver();
        test(solver::solveSudoku, boardStr, expected);
        test(solver::solveSudoku2, boardStr, expected);
        test(solver::solveSudoku3, boardStr, expected);
        test(solver::solveSudoku4, boardStr, expected);
    }

    void print(char[][] board) {
        for (char[] row : board) {
            System.out.println(Arrays.toString(row));
        }
    }

    @Test
    public void test1() {
        test(new String[] {
            "534.7....", "672195...", "198.4..6.",
            "8...6...3", "4..853..1", "7...2...6",
            "96..3.28.", "2..419..5", "3...8..79"
        },
             new String[] {
            "534678912", "672195348", "198342567",
            "859761423", "426853791", "713924856",
            "961537284", "287419635", "345286179"
        });
        test(new String[] {
            "53..7....", "6..195...", ".98....6.",
            "8...6...3", "4..8.3..1", "7...2...6",
            ".6....28.", "...419..5", "....8..79"
        },
             new String[] {
            "534678912", "672195348", "198342567",
            "859761423", "426853791", "713924856",
            "961537284", "287419635", "345286179"
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SudokuSolver");
    }
}
