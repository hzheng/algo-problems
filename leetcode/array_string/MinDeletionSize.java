import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC944: https://leetcode.com/problems/delete-columns-to-make-sorted/
//
// We are given an array A of N lowercase letter strings, all of the same length.
// Now, we may choose any set of deletion indices, and for each string, we
// delete all the characters in those indices.
// Return the minimum possible value of D.length.
public class MinDeletionSize {
    // beats %(42 ms for 84 tests)
    public int minDeletionSize(String[] A) {
        int n = A.length;
        int len = A[0].length();
        int res = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 1; j < n; j++) {
                if (A[j].charAt(i) < A[j - 1].charAt(i)) {
                    res++;
                    break;
                }
            }
        }
        return res;
    }

    void test(String[] A, int expected) {
        assertEquals(expected, minDeletionSize(A));
    }

    @Test
    public void test() {
        test(new String[] {"cba", "daf", "ghi"}, 1);
        test(new String[] {"a", "b"}, 0);
        test(new String[] {"zyx", "wvu", "tsr"}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
