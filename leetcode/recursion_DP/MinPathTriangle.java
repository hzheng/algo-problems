import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC120: https://leetcode.com/problems/triangle/
//
// Given a triangle, find the minimum path sum from top to bottom. Each step you
// may move to adjacent numbers on the row below.
// Bonus point if you are able to do this using only O(n) extra space, where n
// is the total number of rows in the triangle.
public class MinPathTriangle {
    // beats 87.33%(3 ms)
    public int minimumTotal(List<List<Integer> > triangle) {
        int size = triangle.size();
        // beat rate drops dramatically to 0.19% if use stream
        // int[] sum = triangle.get(rowIndex).stream().mapToInt(i -> i).toArray();
        int[] sum = new int[size + 1];
        for (int rowIndex = size - 1; rowIndex >= 0; rowIndex--) { // bottom-up
            List<Integer> row = triangle.get(rowIndex);
            for (int i = 0; i <= rowIndex; i++) {
                sum[i] = row.get(i) + Math.min(sum[i], sum[i + 1]);
            }
        }
        return sum[0];
    }

    void test(Function<List<List<Integer> >, Integer> min,
              int expected, Integer[][] triangle) {
        List<List<Integer>> triangleList = new ArrayList<>();
        for (Integer[] row : triangle) {
            triangleList.add(Arrays.asList(row));
        }
        assertEquals(expected, (int)min.apply(triangleList));
    }

    void test(int expected, Integer[][] triangle) {
        MinPathTriangle m = new MinPathTriangle();
        test(m::minimumTotal, expected, triangle);
    }

    @Test
    public void test1() {
        test(11, new Integer[][] {{2}, {3, 4}, {6, 5, 7}, {4, 1, 8, 3}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinPathTriangle");
    }
}
