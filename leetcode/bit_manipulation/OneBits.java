import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/number-of-1-bits/
//
// Write a function that takes an unsigned integer and returns the number of 1
// bits it has(also known as the Hamming weight).
public class OneBits {
    // beats 13.53%(2 ms)
    public int hammingWeight(int n) {
        int count = 0;
        for (int i = n; i != 0; i >>>= 1) {
            if ((i & 1) != 0) {
                count++;
            }
        }
        return count;
    }

    // beats 13.53%(2 ms)
    public int hammingWeight2(int n) {
        int count = 0;
        for (int i = n; i != 0; i &= (i - 1)) {
            count++;
        }
        return count;
    }

    void test(int n, int expected) {
        assertEquals(expected, hammingWeight(n));
        assertEquals(expected, hammingWeight2(n));
    }

    @Test
    public void test1() {
        test(11, 3);
        test(45, 4);
        test(-1, 32);
        test(Integer.MIN_VALUE, 1);
        test(0, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("OneBits");
    }
}
