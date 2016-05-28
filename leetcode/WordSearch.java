import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a 2D board and a word, find if the word exists in the grid.
// The word can be constructed from letters of sequentially adjacent cell,
// where "adjacent" cells are those horizontally or vertically neighboring.
// The same letter cell may not be used more than once.
public class WordSearch {
    // beats 92.54%
    public boolean exist(char[][] board, String word) {
        if (word.isEmpty()) return true;

        int m = board.length;
        if (m == 0) return false;

        int n = board[0].length;
        char c = word.charAt(0);
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == c) {
                    if (exist(board, word, i, j, 1, visited)) return true;
                }
            }
        }
        return false;
    }

    private boolean exist(char[][] board, String word,
                          int row, int col, int start, boolean[][] visited) {
        if (start >= word.length()) return true;

        visited[row][col] = true;
        char c = word.charAt(start);
        if (row > 0 && board[row - 1][col] == c && !visited[row - 1][col]) {
            if (exist(board, word, row - 1, col, start + 1, visited)) {
                return true;
            }
        }
        if (row + 1 < board.length && board[row + 1][col] == c
            && !visited[row + 1][col]) {
            if (exist(board, word, row + 1, col, start + 1, visited)) {
                return true;
            }
        }
        if (col > 0 && board[row][col - 1] == c && !visited[row][col - 1]) {
            if (exist(board, word, row, col - 1, start + 1, visited)) {
                return true;
            }
        }
        if (col + 1 < board[0].length && board[row][col + 1] == c
            && !visited[row][col + 1]) {
            if (exist(board, word, row, col + 1, start + 1, visited)) {
                return true;
            }
        }
        visited[row][col] = false;
        return false;
    }

    // beats 43.58%
    public boolean exist2(char[][] board, String word) {
        if (word.isEmpty()) return true;

        int m = board.length;
        if (m == 0) return false;

        int n = board[0].length;
        char c = word.charAt(0);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == c) {
                    if (exist2(board, word, i, j, 0)) return true;
                }
            }
        }
        return false;
    }

    private boolean exist2(char[][] board, String word,
                           int row, int col, int start) {
        if (start >= word.length()) return true;

        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length
            || board[row][col] != word.charAt(start)) {
            return false;
        }

        board[row][col] = '\0';
        boolean res = exist2(board, word, row - 1, col, start + 1)
                      || exist2(board, word, row + 1, col, start + 1)
                      || exist2(board, word, row, col - 1, start + 1)
                      || exist2(board, word, row, col + 1, start + 1);
        board[row][col] = word.charAt(start);
        return res;
    }

    void test(String[] words, boolean[] expected, String ... boardStr) {
        char[][] board = new char[boardStr.length][];
        for (int i = 0; i < boardStr.length; i++) {
            board[i] = boardStr[i].toCharArray();
        }
        for (int i = 0; i < words.length; i++) {
            assertEquals(expected[i], exist(board, words[i]));
            assertEquals(expected[i], exist2(board, words[i]));
        }
    }

    @Test
    public void test1() {
        test(new String[] {"ABCCED", "SEE", "ABCB"},
             new boolean[] {true, true, false},
             "ABCE", "SFCS", "ADEE");
        test(new String[] {"ABCESEEEFS"}, new boolean[] {true},
             "ABCE", "SFES", "ADEE");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordSearch");
    }
}
