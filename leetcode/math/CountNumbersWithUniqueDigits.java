import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC357: https://leetcode.com/problems/count-numbers-with-unique-digits/
//
// Given a non-negative integer n, count all numbers with unique digits, x,
// where 0 ≤ x < 10 ^ n.
public class CountNumberswithUniqueDigits {
    // Solution of Choice
    // Combinatorics + Dynamic Programming
    // beats 13.76%(0 ms for 9 tests)
    public int countNumbersWithUniqueDigits(int n) {
        int count = (n == 0) ? 1 : 10;
        for (int i = 9, min = 10 - Math.min(n, 10), lastPerm = 9; i > min; i--) {
            count += (lastPerm *= i);
        }
        return count;
    }

    // Combinatorics + Recursion
    // beats 13.76%(0 ms for 9 tests)
    public int countNumbersWithUniqueDigits2(int n) {
        int count = 1;
        for (int i = Math.min(n, 10) - 1; i >= 0; i--) {
            count += 9 * permutation(9, i);
        }
        return count;
    }

    private int permutation(int n, int r) {
        return r == 0 ? 1 : n * permutation(n - 1, r - 1);
    }

    // Recursion + DFS + Backtracking (Can be used to print all numbers)
    // beats 6.55%(85 ms for 9 tests)
    public int countNumbersWithUniqueDigits3(int n) {
        int[] count = {1};
        countUnique3(Math.min(10, n), new ArrayList<>(), 0, count);
        return count[0];
    }

    private void countUnique3(int left, List<Integer> buf, int visited, int[] count) {
        if (left >= 0 && buf.size() > 0 && buf.get(0) != 0) {
            count[0]++;
        }
        if (left == 0) return;

        for (int i = 0; i < 10; i++) {
            int mask = 1 << i;
            if ((visited & mask) != 0) continue;

            buf.add(i);
            countUnique3(left - 1, buf, visited | mask, count);
            buf.remove(buf.size() - 1);
        }
    }

    // Recursion + DFS + Backtracking
    // beats 8.05%(50 ms for 9 tests)
    public int countNumbersWithUniqueDigits4(int n) {
        int[] count = {1};
        countUnique4(Math.min(10, n), false, 0, count);
        return count[0];
    }

    private void countUnique4(int left, boolean first, int visited, int[] count) {
        if (left >= 0 && first) {
            count[0]++;
        }
        if (left == 0) return;

        for (int i = 9, mask = 1; i >= 0; i--, mask <<= 1) {
            if ((visited & mask) == 0) {
                countUnique4(left - 1, (i != 0), visited | mask, count);
            }
        }
    }

    void test(int n, int expected) {
        assertEquals(expected, countNumbersWithUniqueDigits(n));
        assertEquals(expected, countNumbersWithUniqueDigits2(n));
        assertEquals(expected, countNumbersWithUniqueDigits3(n));
        assertEquals(expected, countNumbersWithUniqueDigits4(n));
    }

    @Test
    public void test1() {
        test(0, 1);
        test(1, 10);
        test(2, 91);
        test(3, 739);
        test(4, 5275);
        test(5, 32491);
        test(6, 168571);
        test(7, 712891);
        test(8, 2345851);
        test(9, 5611771);
        test(10, 8877691);
        test(11, 8877691);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountNumberswithUniqueDigits");
    }
}
