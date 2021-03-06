import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1291: https://leetcode.com/problems/sequential-digits/
//
// An integer has sequential digits if and only if each digit in the number is one more than the
// previous digit. Return a sorted list of all the integers in the range [low, high] inclusive that
// have sequential digits.
//
// Constraints:
// 10 <= low <= high <= 10^9
public class SequentialDigits {
    // Sort
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 0 ms(100.00%), 36.9 MB(22.30%) for 23 tests
    public List<Integer> sequentialDigits(int low, int high) {
        List<Integer> res = new ArrayList<>();
        for (int firstDigit = 1; firstDigit < 10; firstDigit++) {
            for (int num = 0, base = firstDigit; base < 10; base++) {
                num = num * 10 + base;
                if (num < low) { continue; }
                if (num > high) { break; }

                res.add(num);
            }
        }
        Collections.sort(res);
        return res;
    }

    // DFS + Recursion + Sort
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 0 ms(100.00%), 36.4 MB(78.42%) for 23 tests
    public List<Integer> sequentialDigits2(int low, int high) {
        List<Integer> res = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            dfs(res, i, low, high);
        }
        Collections.sort(res);
        return res;
    }

    private void dfs(List<Integer> res, int val, int low, int high) {
        if (val > high) { return; }

        if (val >= low) {
            res.add(val);
        }
        int lastDigit = val % 10 + 1;
        if (lastDigit < 10) {
            dfs(res, val * 10 + lastDigit, low, high);
        }
    }

    // BFS + Queue
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 0 ms(100.00%), 36.1 MB(96.40%) for 23 tests
    public List<Integer> sequentialDigits3(int low, int high) {
        final int base = 10;
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i < base; i++) {
            queue.offer(i);
        }
        List<Integer> res = new ArrayList<>();
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            if (cur > high) { break; }

            if (cur >= low) {
                res.add(cur);
            }
            int num = cur % base + 1;
            if (num < base) {
                queue.offer(cur * base + num);
            }
        }
        return res;
    }

    private void test(int low, int high, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, sequentialDigits(low, high));
        assertEquals(expectedList, sequentialDigits2(low, high));
        assertEquals(expectedList, sequentialDigits3(low, high));
    }

    @Test public void test() {
        test(100, 300, new Integer[] {123, 234});
        test(1000, 13000, new Integer[] {1234, 2345, 3456, 4567, 5678, 6789, 12345});
        test(10, 1000000000,
             new Integer[] {12, 23, 34, 45, 56, 67, 78, 89, 123, 234, 345, 456, 567, 678, 789, 1234,
                            2345, 3456, 4567, 5678, 6789, 12345, 23456, 34567, 45678, 56789, 123456,
                            234567, 345678, 456789, 1234567, 2345678, 3456789, 12345678, 23456789,
                            123456789});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
