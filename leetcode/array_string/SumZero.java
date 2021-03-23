import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1304: https://leetcode.com/problems/find-n-unique-integers-sum-up-to-zero/
//
// Given an integer n, return any array containing n unique integers such that they add up to 0.
//
// Constraints:
// 1 <= n <= 1000
public class SumZero {
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 37.2 MB(70.35%) for 42 tests
    public int[] sumZero(int n) {
        int[] res = new int[n];
        int sum = 0;
        for (int i = 1; i < n; i++) {
            res[i] = -i;
            sum += i;
        }
        res[0] = sum;
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38.5 MB(19.15%) for 42 tests
    public int[] sumZero2(int n) {
        int[] res = new int[n];
        for (int i = 0, j = n - 1; i < j; i++, j--) {
            res[j] = j;
            res[i] = -j;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 37.3 MB(56.73%) for 42 tests
    public int[] sumZero3(int n) {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = i * 2 - n + 1;
        }
        return res;
    }

    private void test(int n) {
        assertEquals(0, Arrays.stream(sumZero(n)).sum());
        assertEquals(n, Arrays.stream(sumZero(n)).boxed().collect(Collectors.toSet()).size());
        assertEquals(0, Arrays.stream(sumZero2(n)).sum());
        assertEquals(n, Arrays.stream(sumZero2(n)).boxed().collect(Collectors.toSet()).size());
        assertEquals(0, Arrays.stream(sumZero3(n)).sum());
        assertEquals(n, Arrays.stream(sumZero3(n)).boxed().collect(Collectors.toSet()).size());
    }

    @Test public void test() {
        test(5);
        test(3);
        test(1);
        test(2);
        test(4);
        test(10);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
