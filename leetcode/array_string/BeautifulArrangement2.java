import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC667: https://leetcode.com/problems/beautiful-arrangement-ii/
//
// Given two integers n and k, you need to construct a list which contains n 
// different positive integers ranging from 1 to n and obeys the following
// requirement: Suppose this list is [a1, a2, a3, ... , an], then the list
// [|a1 - a2|, |a2 - a3|, |a3 - a4|, ... , |an-1 - an|] has exactly k distinct
// integers. If there are multiple answers, print any of them.
public class BeautifulArrangement2 {
    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 98.38%(4 ms for 68 tests)
    public int[] constructArray(int n, int k) {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = i + 1;
        }
        construct(res, k, 1);
        return res;
    }
    
    private void construct(int[] res, int k, int base) {
        if (k < 1) return;

        res[k - 1] = base;
        res[k] = base + k;
        if (k < 2) return;

        res[k - 2] = base + k - 1;
        construct(res, k - 2, base + 1);
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 20.47%(5 ms for 68 tests)
    public int[] constructArray2(int n, int k) {
        int[] res = new int[n];
        for (int i = k, base = 1; i > 0; i -= 2, base++) {
            res[i - 1] = base;
            res[i] = base + i;
            if (i >= 2) {
                res[i - 2] = base + i - 1;
            }
        }
        for (int i = k + 1; i < n; i++) {
            res[i] = i + 1;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 98.38%(4 ms for 68 tests)
    public int[] constructArray3(int n, int k) {
        int[] res = new int[n];
        int index = 0;
        for (int i = 1; i < n - k; i++) {
            res[index++] = i;
        }
        // When k = n-1, a construction is [1, n, 2, n-1, 3, n-2, ....]
        for (int i = 0; i <= k; i++) {
            res[index++] = (i % 2 == 0) ? (n - k + i / 2) : (n - i / 2);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 8.62%(6 ms for 68 tests)
    public int[] constructArray4(int n, int k) {
        int[] res = new int[n];
        for (int i = 0, l = 1, r = n; l <= r; i++, k--) {
            res[i] = (k > 1) ? ((k % 2 != 0) ? l++ : r--) : l++;
        }
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(int n, int k) {
        BeautifulArrangement2 b = new BeautifulArrangement2();
        test(n, k, b::constructArray);
        test(n, k, b::constructArray2);
        test(n, k, b::constructArray3);
        test(n, k, b::constructArray4);
    }
    
    void test(int n, int k, Function<Integer, Integer, int[]> construct) {
        int[] res = construct.apply(n, k);
        Set<Integer> set = new HashSet<>();
        for (int i = 1; i < n; i++) {
            set.add(Math.abs(res[i] - res[i - 1]));
        }
        assertEquals(k, set.size());
    }

    @Test
    public void test() {
        test(3, 1);
        test(3, 2);
        test(6, 4);
        test(60, 40);
        test(900, 453);
        test(9000, 4853);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
