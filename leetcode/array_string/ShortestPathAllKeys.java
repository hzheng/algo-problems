import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC864: https://leetcode.com/problems/shortest-path-to-get-all-keys/
//
// Given a 2-dimensional grid, "." is an empty cell, "#" is a wall, "@" is the
// starting point, ("a", "b", ...) are keys, and ("A", "B", ...) are locks.
// We start at the starting point, and one move consists of walking one space in
// one of the 4 directions. We cannot walk outside the grid or walk into a wall.
// If we walk over a key, we pick it up. We can't walk over a lock unless we
// have the corresponding key. For some 1 <= K <= 6, there is exactly one
// lowercase and one uppercase letter of the first K letters of the English
// alphabet in the grid. This means that there is exactly one key for each lock,
// and one lock for each key; and also that the letters used to represent the
// keys and locks were chosen in the same order as the English alphabet.
// Return the lowest number of moves to acquire all keys.  If it's impossible,
// return -1.
// Note:
// 1 <= grid.length <= 30, 1 <= grid[0].length <= 30
// grid[i][j] contains only '.', '#', '@', 'a'-'f' and 'A'-'F'
// The number of keys is in [1, 6].  Each key has a different letter and opens
// exactly one lock.
public class ShortestPathAllKeys {
    private static final int[][] MOVES = { { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, -1 } };

    // BFS + Set + Bit Manipulation
    // beats 77.20%(47 ms for 35 tests)
    public int shortestPathAllKeys(String[] grid) {
        int n = grid.length;
        int m = grid[0].length();
        State start = new State(0, 0, 0);
        int allKeys = 0;
        for (int i = 0; i < n; i++) {
            int index = grid[i].indexOf('@');
            if (index >= 0) {
                start.x = i;
                start.y = index;
            }
            for (char c : grid[i].toCharArray()) {
                if (Character.isLowerCase(c)) {
                    allKeys = Math.max(allKeys, (1 << (c - 'a' + 1)) - 1);
                }
            }
        }
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.offer(start);
        for (int level = 1; !queue.isEmpty(); level++) {
            for (int i = queue.size(); i > 0; i--) {
                State cur = queue.poll();
                if (!visited.add(cur)) continue;

                for (int[] move : MOVES) {
                    int x = cur.x + move[0];
                    int y = cur.y + move[1];
                    if (x < 0 || x >= n || y < 0 || y >= m) continue;

                    char c = grid[x].charAt(y);
                    if (c == '#') continue;

                    if (!Character.isUpperCase(c) || cur.unlock(c)) {
                        State next = new State(x, y, cur.keys);
                        if (Character.isLowerCase(c)) {
                            if (next.addKey(c) == allKeys) return level;
                        }
                        queue.offer(next);
                    }
                }
            }
        }
        return -1;
    }

    private static class State {
        int x;
        int y;
        int keys;

        State(int x, int y, int keys) {
            this.x = x;
            this.y = y;
            this.keys = keys;
        }

        public int addKey(char key) {
            return keys |= (1 << (key - 'a'));
        }

        public boolean unlock(char lock) {
            return (keys & (1 << (lock - 'A'))) != 0;
        }

        public int hashCode() {
            return 10000 * x + 100 * y + keys;
        }

        public boolean equals(Object o) {
            return (o instanceof State) && ((State) o).hashCode() == hashCode();
        }
    }

    void test(String[] grid, int expected) {
        assertEquals(expected, shortestPathAllKeys(grid));
    }

    @Test
    public void test() {
        test(new String[] { "@Aa" }, -1);
        test(new String[] { "@.", "Aa" }, 2);
        test(new String[] { "@.a", "##A" }, 2);
        test(new String[] { "@.a.#", "###.#", "b.A.B" }, 8);
        test(new String[] { "@..aA", "..B#.", "....b" }, 6);
        test(new String[] { ".#@..", "#.##.", ".#...", "A...#", ".#.#a" }, -1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
