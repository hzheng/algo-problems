import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC419: https://leetcode.com/problems/battleships-in-a-board/
//
// Given an 2D board, count how many different battleships are in it. The
// battleships are represented with 'X's, empty slots are represented with '.'s.
// You may assume the following rules:
// You receive a valid board, made of only battleships or empty slots.
// Battleships can only be placed horizontally or vertically. In other words,
// they can only be made of the shape 1xN (1 row, N columns) or Nx1 (N rows,
// 1 column), where N can be of any size.
// At least one horizontal or vertical cell separates between two battleships -
// there are no adjacent battleships.
public class CountBattleships {
    // beats N/A(5 ms for 28 tests)
    public int countBattleships(char[][] board) {
        int m = board.length;
        int n = board[0].length;
        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'X' && (i == 0 || board[i - 1][j] != 'X')
                    && (i == m - 1 || board[i + 1][j] != 'X')) {
                    count++;
                    while (++j < n && board[i][j] == 'X') {}
                }
            }
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m - 1; i++) {
                if (board[i][j] == 'X' && board[i + 1][j] == 'X') {
                    count++;
                    while (++i < m && board[i][j] == 'X') {}
                }
            }
        }
        return count;
    }

    // beats N/A(4 ms for 28 tests)
    public int countBattleships2(char[][] board) {
        int m = board.length;
        int n = board[0].length;
        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'X' && (i == 0 || board[i - 1][j] != 'X')
                    && (j == 0 || board[i][j - 1] != 'X')) {
                    count++;
                }
            }
        }
        return count;
    }

    // DFS + Recursion(Flood Fill algorithm)
    // beats N/A(6 ms for 28 tests)
    public int countBattleships3(char[][] board) {
        int m = board.length;
        int n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        int count = 0;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (board[i][j] == 'X' && !visited[i][j]) {
                    count++;
                    dfs(board, m, n, i, j, visited);
                }
            }
        }
        return count;
    }

    private void dfs(char[][] board, int m, int n, int i, int j, boolean[][] visited) {
        if (i < 0 || i >= m || j < 0 || j >= n || board[i][j] != 'X' || visited[i][j]) return;

        visited[i][j] = true;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : dirs) {
            dfs(board, m, n, i + dir[0], j + dir[1], visited);
        }
    }

    // BFS + Queue
    // beats N/A(6 ms for 28 tests)
    public int countBattleships4(char[][] board) {
        int m = board.length;
        int n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        int count = 0;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (board[i][j] != 'X' || visited[i][j]) continue;

                count++;
                queue.offer(new int[] {i, j});
                while (!queue.isEmpty()) {
                    int[] pos = queue.poll();
                    visited[pos[0]][pos[1]] = true;
                    for (int[] dir : dirs) {
                        int x = pos[0] + dir[0];
                        int y = pos[1] + dir[1];
                        if (x >= 0 && x < m && y >= 0 && y < n
                            && board[x][y] == 'X' && !visited[x][y]) {
                            queue.offer(new int[] {x, y});
                        }
                    }
                }
            }
        }
        return count;
    }

    void test(int expected, String ... boardStr) {
        char[][] board = new char[boardStr.length][];
        for (int i = 0; i < boardStr.length; i++) {
            board[i] = boardStr[i].toCharArray();
        }
        assertEquals(expected, countBattleships(board));
        assertEquals(expected, countBattleships2(board));
        assertEquals(expected, countBattleships3(board));
        assertEquals(expected, countBattleships4(board));
    }

    @Test
    public void test1() {
        test(1, "XXX");
        test(2, "X..X", "...X", "...X");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountBattleships");
    }
}
