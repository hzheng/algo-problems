import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC898: https://leetcode.com/problems/bitwise-ors-of-subarrays/
//
// We have an array A of non-negative integers. For every (contiguous) subarray
// B = [A[i], A[i+1], ..., A[j]] (with i <= j), we take the bitwise OR of all
// the elements in B, obtaining a result A[i] | A[i+1] | ... | A[j].
// Return the number of possible results. 
public class SubarrayBitwiseORs {
    // Set (Frontier Set)
    // time complexity: O(N * log(Max(A)))
    // beats %(409 ms for 83 tests)
    public int subarrayBitwiseORs(int[] A) {
        Set<Integer> res = new HashSet<>();
        Set<Integer> prev = new HashSet<>();
        for (int a : A) {
            Set<Integer> cur = new HashSet<>();
            for (int p : prev) {
                cur.add(p | a);
            }
            cur.add(a);
            res.addAll(cur);
            prev = cur;
        }
        return res.size();
    }

    void test(int[] A, int expected) {
        assertEquals(expected, subarrayBitwiseORs(A));
    }

    @Test
    public void test() {
        test(new int[]{0}, 1);
        test(new int[]{1, 1, 2}, 3);
        test(new int[]{1, 2, 4}, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
