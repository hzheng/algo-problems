import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// LC305: https://leetcode.com/problems/number-of-islands-ii/
//
// A 2d grid map of m rows and n columns is initially filled with water. We may
// perform an addLand operation which turns the water at position (row, col) into
// a land. Given a list of positions to operate, count the number of islands after
// each addLand operation. An island is surrounded by water and is formed by connecting
// adjacent lands horizontally or vertically. You may assume all four edges of
// the grid are all surrounded by water.
public class IslandNumber2 {
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // Union Find
    // beats 96.87%(16 ms for 158 tests)
    // time complexity: O(K * log(M * N))
    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        List<Integer> res = new ArrayList<>();
        int[] id = new int[m * n];
        Arrays.fill(id, -1);
        int islands = 0;
        for (int[] position : positions) {
            int x = position[0];
            int y = position[1];
            int index = x * n + y;
            int root = -1;
            for (int[] move : MOVES) {
                int nx = x + move[0];
                int ny = y + move[1];
                int ni = n * nx + ny;
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || id[ni] < 0) continue;

                for (; ni != id[ni]; ni = id[ni] = id[id[ni]]) {}
                if (root >= 0 && root != ni) {
                    id[ni] = root;
                    islands--;
                } else {
                    root = ni;
                }
            }
            if (root < 0) {
                id[index] = index;
                islands++;
            } else {
                id[index] = root;
            }
            res.add(islands);
        }
        return res;
    }

    // Union Find
    // beats 96.87%(16 ms for 158 tests)
    // time complexity: O(K * log(M * N))
    public List<Integer> numIslands2_2(int m, int n, int[][] positions) {
        List<Integer> res = new ArrayList<>();
        int[] id = new int[m * n];
        Arrays.fill(id, -1);
        int islands = 0;
        for (int[] position : positions) {
            int x = position[0];
            int y = position[1];
            int index = x * n + y;
            id[index] = index;
            islands++;
            for (int[] move : MOVES) {
                int nx = x + move[0];
                int ny = y + move[1];
                int ni = n * nx + ny;
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || id[ni] < 0) continue;

                for (; ni != id[ni]; ni = id[ni] = id[id[ni]]) {}
                if (index != ni) {
                    id[index] = ni;
                    index = ni;
                    islands--;
                }
            }
            res.add(islands);
        }
        return res;
    }

    void test(int m, int n, int[][] positions, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, numIslands2(m, n, positions));
        assertEquals(expectedList, numIslands2_2(m, n, positions));
    }

    @Test
    public void test1() {
        test(2, 2, new int[][] {{0, 0}, {1, 1}, {0, 1}}, new Integer[] {1, 2, 1});
        test(3, 3, new int[][] {{0, 0}, {0, 1}, {1, 2}, {2, 1}}, new Integer[] {1, 1, 2, 3});
        test(3, 3, new int[][] {{0, 0}, {0, 1}, {1, 2}, {2, 1}}, new Integer[] {1, 1, 2, 3});
        test(3, 3, new int[][] {{0, 1}, {1, 2}, {2, 1},{1, 0}, {0, 2}, {0, 0}, {1, 1}},
             new Integer[] {1, 2, 3, 4, 3, 2, 1});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IslandNumber2");
    }
}
