import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC119: https://leetcode.com/problems/pascals-triangle-ii/
//
// Given an index k, return the kth row of the Pascal's triangle.
// Note:
// Could you optimize your algorithm to use only O(k) extra space?
public class PascalTriangle2 {
    // Solution of Choice
    // time complexity: O(k ^ 2), space complexity: O(k)
    // beats 91.86%(1 ms for 34 tests)
    public List<Integer> getRow(int rowIndex) {
        List<Integer> row = new ArrayList<>(rowIndex + 1);
        for (int i = 0; i <= rowIndex; i++) {
            for (int j = i - 2; j >= 0; j--) {
                row.set(j + 1, row.get(j) + row.get(j + 1));
            }
            row.add(1);
        }
        return row;
    }

    // time complexity: O(k ^ 2), space complexity: O(k)
    // beats 100.00%(0 ms for 34 tests)
    public List<Integer> getRow_2(int rowIndex) {
        Integer[] row = new Integer[rowIndex + 1];
        for (int i = 0; i <= rowIndex; i++) {
            for (int j = i - 2; j >= 0; j--) {
                row[j + 1] += row[j];
            }
            row[i] = 1;
        }
        return Arrays.asList(row);
    }

    // combinatorics (C[k,i] = C[k,i-1]*(k-i+1)/i)
    // time complexity: O(k), space complexity: O(k)
    // beats 100.00%(0 ms for 34 tests)
    public List<Integer> getRow2(int rowIndex) {
        Integer[] row = new Integer[rowIndex + 1];
        row[0] = 1;
        for (int i = 1; i < row.length; i++) {
            row[i] = (int)((long)row[i - 1] * (rowIndex - i + 1) / i);
        }
        return Arrays.asList(row);
    }

    // Solution of Choice
    // time complexity: O(k), space complexity: O(k)
    // beats 100.00%(0 ms for 34 tests)
    public List<Integer> getRow2_2(int rowIndex) {
        List<Integer> res = new ArrayList<>(rowIndex + 1);
        res.add(1);
        for (int i = 1; i <= (rowIndex + 1) / 2; i++) {
            res.add((int)((long)res.get(i - 1) * (rowIndex - i + 1) / i));
        }
        for (int i = (rowIndex + 1) / 2 + 1; i <= rowIndex; i++) {
            res.add(res.get(rowIndex - i));
        }
        return res;
    }

    void test(Function<Integer, List<Integer>> getRow, int n, Integer[] expected) {
        List<Integer> res = getRow.apply(n);
        assertArrayEquals(expected, res.toArray(new Integer[0]));
    }

    void test(int n, Integer[] expected) {
        PascalTriangle2 p = new PascalTriangle2();
        test(p::getRow, n, expected);
        test(p::getRow_2, n, expected);
        test(p::getRow2, n, expected);
        test(p::getRow2_2, n, expected);
    }

    @Test
    public void test1() {
        test(3, new Integer[] {1, 3, 3, 1});
        test(4, new Integer[] {1, 4, 6, 4, 1});
        test(5, new Integer[] {1, 5, 10, 10, 5, 1});
        test(30,
             new Integer[] {1, 30, 435, 4060, 27405, 142506, 593775, 2035800, 5852925, 14307150,
                            30045015, 54627300, 86493225, 119759850, 145422675, 155117520,
                            145422675, 119759850, 86493225, 54627300, 30045015, 14307150, 5852925,
                            2035800, 593775, 142506, 27405, 4060, 435, 30, 1});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PascalTriangle2");
    }
}
