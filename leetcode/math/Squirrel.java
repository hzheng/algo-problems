import org.junit.Test;
import static org.junit.Assert.*;

// LC573: https://leetcode.com/problems/squirrel-simulation/
//
// There's a tree, a squirrel, and several nuts. Positions are represented by the cells
// in a 2D grid. Your goal is to find the minimal distance for the squirrel to collect
// all the nuts and put them under the tree one by one. The squirrel can only take at
// most one nut at one time and can move in four directions - up, down, left and right,
// to the adjacent cell. The distance is represented by the number of moves.
// Note:
// All given positions won't overlap.
// Height and width are positive integers. 3 <= height * width <= 10,000.
// The given positions contain at least one nut, only one tree and one squirrel.
public class Squirrel {
    // time complexity: O(N), space complexity: O(1)
    // beats 96.17%(13 ms for 122 tests)
    public int minDistance(int height, int width, int[] tree, int[] squirrel, int[][] nuts) {
        int steps = 0;
        int max = Integer.MIN_VALUE;
        for (int[] nut : nuts) {
            int d = distance(tree, nut);
            steps += d;
            max = Math.max(max, d - distance(squirrel, nut));
        }
        return steps * 2 - max;
    }

    private int distance(int[] p, int[] q) {
        return Math.abs(p[0] - q[0]) + Math.abs(p[1] - q[1]);
    }

    void test(int height, int width, int[] tree, int[] squirrel, int[][] nuts, int expected) {
        assertEquals(expected, minDistance(height, width, tree, squirrel, nuts));
    }

    @Test
    public void test() {
        test(5, 7, new int[] {2, 2}, new int[] {4, 4}, new int[][] {{3, 0}, {2, 5}}, 12);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
