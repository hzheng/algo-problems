import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC868: https://leetcode.com/problems/binary-gap/
//
// Given a positive integer N, find and return the longest distance between two 
// consecutive 1's in the binary representation of N.
// If there aren't two consecutive 1's, return 0.
public class BinaryGap {
    // beats 95.11%(10 ms for 260 tests)
    public int binaryGap(int N) {
        String s = Integer.toBinaryString(N);
        int res = 0;
        for (int i = 1, prev = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                res = Math.max(res, i - prev);
                prev = i;
            }
        }
        return res;
    }

    // beats 52.75%(11 ms for 260 tests)
    public int binaryGap2(int N) {
        int res = 0;
        for (int i = 0, prev = -1; i < 32; i++) {
            if (((N >> i) & 1) != 0) { // or: if (N << ~i < 0) {
                if (prev >= 0) {
                    res = Math.max(res, i - prev);
                }
                prev = i;
            }
        }
        return res;
    }

    // beats 52.75%(11 ms for 260 tests)
    public int binaryGap3(int N) {
        int res = 0;
        for (int dist = -32, x = N; x > 0; x >>= 1, dist++) {
            if ((x & 1) == 1) {
                res = Math.max(res, dist);
                dist = 0;
            }
        }
        return res;
    }

    void test(int N, int expected) {
        assertEquals(expected, binaryGap(N));
        assertEquals(expected, binaryGap2(N));
        assertEquals(expected, binaryGap3(N));
    }

    @Test
    public void test() {
        test(22, 2);
        test(5, 2);
        test(6, 1);
        test(8, 0);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
