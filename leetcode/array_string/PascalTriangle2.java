import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given an index k, return the kth row of the Pascal's triangle.
public class PascalTriangle2 {
    // beats 22.66%
    public List<Integer> getRow(int rowIndex) {
        List<Integer> row = new ArrayList<>(++rowIndex);
        for (int i = 1; i <= rowIndex; i++) {
            for (int j = i - 3; j >= 0; j--) {
                row.set(j + 1, row.get(j) + row.get(j + 1));
            }
            row.add(1);
        }
        return row;
    }

    void test(Function<Integer, List<Integer>> getRow, int n, Integer[] expected) {
        List<Integer> res = getRow.apply(n);
        assertArrayEquals(expected, res.toArray(new Integer[0]));
    }

    void test(int n, Integer[] expected) {
        PascalTriangle2 p = new PascalTriangle2();
        test(p::getRow, n, expected);
    }

    @Test
    public void test1() {
        test(3, new Integer[] {1, 3, 3, 1});
        test(4, new Integer[] {1, 4, 6, 4, 1});
        test(5, new Integer[] {1, 5, 10, 10, 5, 1});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PascalTriangle2");
    }
}
