import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Generate the first numRows of Pascal's triangle.
public class PascalTriangle {
    // beats 40.54%
    public List<List<Integer> > generate(int numRows) {
        List<List<Integer> > res = new ArrayList<>();
        if (numRows == 0) return res;

        List<Integer> prevRow = new ArrayList<>(1);
        prevRow.add(1);
        res.add(prevRow);
        for (int i = 2; i <= numRows; i++) {
            List<Integer> row = new ArrayList<>(i);
            row.add(1);
            for (int j = 0; j < i - 2; j++) {
                row.add(prevRow.get(j) + prevRow.get(j + 1));
            }
            row.add(1);
            res.add(row);
            prevRow = row;
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
