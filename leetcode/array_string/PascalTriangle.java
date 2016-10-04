import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC118: https://leetcode.com/problems/pascals-triangle/
//
// Generate the first numRows of Pascal's triangle.
public class PascalTriangle {
    // beats 3.29%(2 ms)
    public List<List<Integer> > generate(int numRows) {
        List<List<Integer> > res = new ArrayList<>();
        if (numRows == 0) return res;

        List<Integer> prev = new ArrayList<>(Arrays.asList(1));
        res.add(prev);
        for (int i = 2; i <= numRows; i++) {
            List<Integer> cur = new ArrayList<>(Arrays.asList(1));
            for (int j = 0; j < i - 2; j++) {
                cur.add(prev.get(j) + prev.get(j + 1));
            }
            cur.add(1);
            res.add(cur);
            prev = cur;
        }
        return res;
    }

    // Solution of Choice
    // beats 3.29%(2 ms)
    public List<List<Integer> > generate2(int numRows) {
        List<List<Integer> > res = new ArrayList<>();
        List<Integer> row = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            row.add(0, 1);
            for (int j = 1; j < row.size() - 1; j++) {
                row.set(j, row.get(j) + row.get(j + 1));
            }
            res.add(new ArrayList<>(row));
        }
        return res;
    }

    void test(Function<Integer, List<List<Integer>>> generate,
              int n, int[][] expected) {
        List<List<Integer> > res = generate.apply(n);
        assertArrayEquals(expected, res.stream().map(
                              a -> a.toArray(
                                  new Integer[0])).toArray(Integer[][]::new));
    }

    void test(int n, int[][] expected) {
        PascalTriangle p = new PascalTriangle();
        test(p::generate, n, expected);
        test(p::generate2, n, expected);
    }

    @Test
    public void test1() {
        test(5, new int[][] {{1}, {1, 1}, {1, 2, 1}, {1, 3, 3, 1},
                             {1, 4, 6, 4, 1}});
        test(6, new int[][] {{1}, {1, 1}, {1, 2, 1}, {1, 3, 3, 1},
                             {1, 4, 6, 4, 1}, {1, 5, 10, 10, 5, 1}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PascalTriangle");
    }
}
