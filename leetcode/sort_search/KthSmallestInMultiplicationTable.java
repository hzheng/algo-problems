import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC668: https://leetcode.com/problems/kth-smallest-number-in-multiplication-table/
//
// Given the height m and the length n of a m * n Multiplication Table, and an
// integer k, you need to return the k-th smallest number in this table.
// Note:
// The m and n will be in the range [1, 30000].
// The k will be in the range [1, m * n]
public class KthSmallestInMultiplicationTable {
    // Binary Search
    // time complexity: O(min(m, n) * log(m*n)), space complexity: O(1)
    // beats 86.04%(13 ms for 69 tests)
    public int findKthNumber(int m, int n, int k) {
        int low = 1;
        if (m > n) {
            int tmp = m;
            m = n;
            n = tmp;
        }
        for (int high = m * n; low < high; ) {
            int mid = (low + high) >>> 1;
            int count = 0;
            for (int i = 1; i <= m; i++) {
                count += Math.min(mid / i, n);
            }
            if (count < k) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    void test(int m, int n, int k, int expected) {
        assertEquals(expected, findKthNumber(m, n, k));
    }

    @Test
    public void test() {
        test(3, 3, 5, 3);
        test(2, 3, 6, 6);
        test(38, 40, 955, 437);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
