import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/counting-bits/
//
// Given a non negative integer number num. For every numbers i in the range
// 0 ≤ i ≤ num calculate the number of 1's in their binary representation and
// return them as an array.
//
// Can you do it in linear time O(n) /possibly in a single pass?
// Space complexity should be O(n).
// Can you do it like a boss? Do it without using any builtin function like
// __builtin_popcount in c++ or in any other language.
public class CountingBits {
    // beats 19.09%(5 ms)
    public int[] countBits(int num) {
        int[] counts = new int[num + 1];
        for (int i = 1; i <= num; i++) {
            counts[i] = bits(i);
        }
        return counts;
    }

    private int bits(int n) {
        int count = 1;
        while ((n &= (n - 1)) != 0) {
            count++;
        }
        return count;
    }

    // Dynamic Programming
    // beats 35.18%(3 ms)
    public int[] countBits2(int num) {
        int[] counts = new int[num + 1];
        int mask = 0;
        for (int i = 1; i <= num; i++) {
            counts[i] = counts[i & mask] + 1;
            if ((i & i + 1) == 0) {
                mask <<= 1;
                mask |= 1;
            }
        }
        return counts;
    }

    // beats 68.35%(2 ms)
    public int[] countBits3(int num) {
        int[] counts = new int[num + 1];
        for (int i = 1; i <= num; i++) {
            counts[i] = counts[i & (i - 1)] + 1;
        }
        return counts;
    }

    // https://discuss.leetcode.com/topic/40162/three-line-java-solution
    // beats 68.35%(2 ms)
    public int[] countBits4(int num) {
        int[] counts = new int[num + 1];
        for (int i = 1; i <= num; i++) {
            counts[i] = counts[i >> 1] + (i & 1);
        }
        return counts;
    }

    void test(int num, int... expected) {
        assertArrayEquals(expected, countBits(num));
        assertArrayEquals(expected, countBits2(num));
        assertArrayEquals(expected, countBits3(num));
        assertArrayEquals(expected, countBits4(num));
    }

    @Test
    public void test1() {
        test(11, 0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountingBits");
    }
}
