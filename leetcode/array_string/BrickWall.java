import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC554: https://leetcode.com/problems/brick-wall/
//
// A wall is rectangular and has several rows of bricks. The bricks have the same height
// but different width. You want to draw a vertical line from the top to the bottom and
// cross the least bricks. The brick wall is represented by a list of rows. Each row is
// a list of integers representing the width of each brick in this row from left to right.
// If your line go through the edge of a brick, then the brick is not considered as crossed.
// You need to find out how to draw the line to cross the least bricks and return the number
// of crossed bricks.
public class BrickWall {
    // Hash Table
    // beats 56.99%(40 ms for 85 tests)
    public int leastBricks(List<List<Integer> > wall) {
        Map<Integer, Integer> map = new HashMap<>();
        for (List<Integer> row : wall) {
            for (int i = 0, width = 0, n = row.size() - 1; i < n; i++) {
                width += row.get(i);
                map.put(width, map.getOrDefault(width, 0) + 1);
            }
        }
        int mostFreq = 0;
        for (int count : map.values()) {
            mostFreq = Math.max(mostFreq, count);
        }
        return wall.size() - mostFreq;
    }

    // Hash Table
    // beats 88.24%(34 ms for 85 tests)
    public int leastBricks2(List<List<Integer> > wall) {
        Map<Integer, Integer> map = new HashMap<>();
        int mostFreq = 0;
        for (List<Integer> row : wall) {
            for (int i = 0, width = 0, n = row.size() - 1; i < n; i++) {
                width += row.get(i);
                int freq = map.getOrDefault(width, 0) + 1;
                map.put(width, freq);
                mostFreq = Math.max(mostFreq, freq);
            }
        }
        return wall.size() - mostFreq;
    }

    void test(Integer[][] wall, int expected) {
        assertEquals(expected, leastBricks(Utils.toList(wall)));
        assertEquals(expected, leastBricks2(Utils.toList(wall)));
    }

    @Test
    public void test() {
        test(new Integer[][] {{1, 2, 2, 1}, {3, 1, 2}, {1, 3, 2},
                              {2, 4}, {3, 1, 2}, {1, 3, 1, 1}}, 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
