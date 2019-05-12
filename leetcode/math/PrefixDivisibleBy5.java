import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1018: https://leetcode.com/problems/binary-prefix-divisible-by-5/
//
// Given an array A of 0s and 1s, consider N_i: the i-th subarray from A[0] to A[i] interpreted as a
// binary number (from most-significant-bit to least-significant-bit.)
// Return a list of booleans answer, where answer[i] is true if and only if N_i is divisible by 5.
public class PrefixDivisibleBy5 {
    // 3 ms(97.46%),  30.1 MB(100%) for 24 tests
    public List<Boolean> prefixesDivBy5(int[] A) {
        List<Boolean> res = new ArrayList<>();
        int cur = 0;
        for (int a : A) {
            cur <<= 1;
            cur += a;
            res.add((cur %= 5) == 0);
        }
        return res;
    }

    void test(int[] A, Boolean[] expected) {
        assertArrayEquals(expected, prefixesDivBy5(A).toArray());
    }

    @Test
    public void test() {
        test(new int[]{0, 1, 1}, new Boolean[]{true, false, false});
        test(new int[]{1, 1, 1}, new Boolean[]{false, false, false});
        test(new int[]{0, 1, 1, 1, 1, 1}, new Boolean[]{true, false, false, false, true, false});
        test(new int[]{1, 1, 1, 0, 1}, new Boolean[]{false, false, false, false, false});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
