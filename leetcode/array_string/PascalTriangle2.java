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
    // time complexity: O(k ^ 2), space complexity: O(k)
    // beats 55.56%(2 ms)
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

    // time complexity: O(k ^ 2), space complexity: O(k)
    // beats 55.56%(2 ms)
    public List<Integer> getRow2(int rowIndex) {
        Integer[] row = new Integer[++rowIndex];
        for (int i = 1; i <= rowIndex; i++) {
            for (int j = i - 3; j >= 0; j--) {
                row[j + 1] += row[j];
            }
            row[i - 1] = 1;
        }
        return Arrays.asList(row);
    }

    // Solution of Choice
    // combinatorics (C[k,i] = C[k,i-1]*(k-i+1)/i)
    // time complexity: O(k), space complexity: O(k)
    // beats 55.56%(2 ms)
    public List<Integer> getRow3(int rowIndex) {
        Integer[] row = new Integer[rowIndex + 1];
        row[0] = 1;
        for (int i = 1; i < row.length; i++) {
            row[i] = (int)((long)row[i - 1] * (rowIndex - i + 1) / i);
        }
        return Arrays.asList(row);
    }

    void test(Function<Integer, List<Integer> > getRow, int n, Integer[] expected) {
        List<Integer> res = getRow.apply(n);
        assertArrayEquals(expected, res.toArray(new Integer[0]));
    }

    void test(int n, Integer[] expected) {
        PascalTriangle2 p = new PascalTriangle2();
        test(p::getRow, n, expected);
        test(p::getRow2, n, expected);
        test(p::getRow3, n, expected);
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
