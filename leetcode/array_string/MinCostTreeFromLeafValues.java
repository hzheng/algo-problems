import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1130: https://leetcode.com/problems/minimum-cost-tree-from-leaf-values/
//
// Given an array arr of positive integers, consider all binary trees such that:
// Each node has either 0 or 2 children;
// The values of arr correspond to the values of each leaf in an in-order traversal of the tree.
// The value of each non-leaf node is equal to the product of the largest leaf value in its left and
// right subtree respectively.
// Among all possible binary trees considered, return the smallest possible sum of the values of
// each non-leaf node.  It is guaranteed this sum fits into a 32-bit integer.
//
// Constraints:
// 2 <= arr.length <= 40
// 1 <= arr[i] <= 15
// It is guaranteed that the answer fits into a 32-bit signed integer (ie. it is less than 2^31).
public class MinCostTreeFromLeafValues {
    // DFS + Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^3), space complexity: O(N^2)
    // 2 ms(60.46%), 38.4 MB(22.13%) for 103 tests
    public int mctFromLeafValues(int[] arr) {
        int n = arr.length;
        return dfs(arr, 0, n - 1, new int[n][n][])[0];
    }

    private int[] dfs(int[] arr, int start, int end, int[][][] dp) {
        if (end == start) { return new int[] {0, arr[start]}; }
        if (dp[start][end] != null) { return dp[start][end]; }

        int sum = Integer.MAX_VALUE;
        int max = 0;
        for (int i = start; i < end; i++) {
            int[] left = dfs(arr, start, i, dp);
            int[] right = dfs(arr, i + 1, end, dp);
            int newSum = left[0] + right[0] + left[1] * right[1];
            if (newSum < sum) {
                sum = newSum;
                max = Math.max(left[1], right[1]);
            }
        }
        return dp[start][end] = new int[] {sum, max};
    }

    // DFS + Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^4), space complexity: O(N^2)
    // 10 ms(10.28%), 38.7 MB(10.35%) for 103 tests
    public int mctFromLeafValues2(int[] arr) {
        int n = arr.length;
        return dfs(arr, 0, n - 1, new int[n][n]);
    }

    public int dfs(int[] arr, int start, int end, int[][] dp) {
        if (start == end) { return 0; }
        if (dp[start][end] > 0) { return dp[start][end]; }

        int res = Integer.MAX_VALUE;
        for (int i = start; i < end; i++) {
            int left = dfs(arr, start, i, dp);
            int right = dfs(arr, i + 1, end, dp);
            int maxLeft = 0;
            for (int j = start; j <= i; j++) {
                maxLeft = Math.max(maxLeft, arr[j]);
            }
            int maxRight = 0;
            for (int j = i + 1; j <= end; j++) {
                maxRight = Math.max(maxRight, arr[j]);
            }
            res = Math.min(res, left + right + maxLeft * maxRight);
        }
        return dp[start][end] = res;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^3), space complexity: O(N^2)
    // 7 ms(18.42%), 38.7 MB(10.35%) for 103 tests
    public int mctFromLeafValues3(int[] arr) {
        int n = arr.length;
        int[][][] dp = new int[n][n][];
        for (int i = 0; i < n; i++) {
            dp[i][i] = new int[] {0, arr[i]};
        }
        for (int len = 1; len < n; len++) {
            for (int start = 0; start + len < n; start++) {
                int sum = Integer.MAX_VALUE;
                int max = 0;
                for (int i = start, end = start + len; i < end; i++) {
                    int[] left = dp[start][i];
                    int[] right = dp[i + 1][end];
                    int newSum = left[0] + right[0] + left[1] * right[1];
                    if (newSum < sum) {
                        sum = newSum;
                        max = Math.max(left[1], right[1]);
                    }
                    dp[start][end] = new int[] {sum, max};
                }
            }
        }
        return dp[0][n - 1][0];
    }

    // Greedy + SortedSet
    // time complexity: O(N^2*log(N)), space complexity: O(N)
    // 5 ms(34.38%), 36.6 MB(56.18%) for 103 tests
    public int mctFromLeafValues4(int[] arr) {
        int n = arr.length;
        SortedSet<Integer> indices = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            indices.add(i);
        }
        for (int res = 0; ; ) {
            if (indices.size() == 1) { return res; }

            int minProduct = Integer.MAX_VALUE;
            int minFirst = 0;
            int minSecond = 0;
            Iterator<Integer> itr = indices.iterator();
            for (int i = -1, j; ; i = j) {
                i = (i < 0) ? itr.next() : i;
                if (!itr.hasNext()) { break; }

                j = itr.next();
                int product = arr[i] * arr[j];
                if (product < minProduct) {
                    minProduct = product;
                    minFirst = i;
                    minSecond = j;
                }
            }
            indices.remove(arr[minFirst] < arr[minSecond] ? minFirst : minSecond);
            res += minProduct;
        }
    }

    // Greedy
    // time complexity: O(N^2), space complexity: O(N)
    // 1 ms(95.78%), 36.5 MB(64.47%) for 103 tests
    public int mctFromLeafValues5(int[] arr) {
        for (int res = 0, end = arr.length - 1; ; end--) {
            if (end == 0) { return res; }

            int minProduct = Integer.MAX_VALUE;
            int index = 0;
            for (int i = 0; i < end; i++) {
                int product = arr[i] * arr[i + 1];
                if (product < minProduct) {
                    minProduct = product;
                    index = i;
                }
            }
            for (int i = index + (arr[index] < arr[index + 1] ? 0 : 1); i < end; i++) {
                arr[i] = arr[i + 1];
            }
            res += minProduct;
        }
    }

    // Solution of Choice
    // Monotonic Stack
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(95.78%), 36.5 MB(64.47%) for 103 tests
    public int mctFromLeafValues6(int[] arr) {
        Stack<Integer> stack = new Stack<>();
        stack.push(Integer.MAX_VALUE);
        int res = 0;
        for (int a : arr) {
            while (stack.peek() <= a) {
                res += stack.pop() * Math.min(stack.peek(), a);
            }
            stack.push(a);
        }
        while (stack.size() > 2) {
            res += stack.pop() * stack.peek();
        }
        return res;
    }

    private void test(int[] arr, int expected) {
        assertEquals(expected, mctFromLeafValues(arr));
        assertEquals(expected, mctFromLeafValues2(arr));
        assertEquals(expected, mctFromLeafValues3(arr));
        assertEquals(expected, mctFromLeafValues4(arr));
        assertEquals(expected, mctFromLeafValues5(arr.clone()));
        assertEquals(expected, mctFromLeafValues6(arr.clone()));
    }

    @Test public void test() {
        test(new int[] {6, 2, 4}, 32);
        test(new int[] {6, 2, 4, 5}, 58);
        test(new int[] {3, 6, 4, 7, 2, 5}, 129);
        test(new int[] {6, 2, 4, 5, 7, 9, 8}, 235);
        test(new int[] {5, 1, 2, 3, 15, 7, 3, 2, 2, 5, 9, 1, 11, 9, 15, 14, 7, 1, 5}, 1166);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
