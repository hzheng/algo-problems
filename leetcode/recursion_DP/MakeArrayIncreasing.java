import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1187: https://leetcode.com/problems/make-array-strictly-increasing/
//
// Given two integer arrays arr1 and arr2, return the minimum number of operations (possibly zero)
// needed to make arr1 strictly increasing. In one operation, you can choose two indices
// 0 <= i < arr1.length and 0 <= j < arr2.length and do the assignment arr1[i] = arr2[j].
// If there is no way to make arr1 strictly increasing, return -1.
//
// Constraints:
// 1 <= arr1.length, arr2.length <= 2000
// 0 <= arr1[i], arr2[i] <= 10^9
public class MakeArrayIncreasing {
    // 2-D Dynamic Programming(Bottom-Up) + Sort + Binary Search
    // time complexity: O(N1*(N1+M1)*log(N2)), space complexity: O(M1+N1)
    // 232 ms(30.00%), 113.5 MB(14.00%) for 21 tests
    public int makeArrayIncreasing(int[] arr1, int[] arr2) {
        Arrays.sort(arr2);
        Map<Integer, Integer> dp = new HashMap<>();
        dp.put(-1, 0);
        for (int a : arr1) {
            Map<Integer, Integer> ndp = new HashMap<>();
            for (int num : dp.keySet()) {
                int operations = dp.get(num);
                if (a > num) { // keep 'a' (no operation)
                    ndp.put(a, Math.min(ndp.getOrDefault(a, Integer.MAX_VALUE), operations));
                }
                int i = Arrays.binarySearch(arr2, num + 1);
                if (i < 0) {
                    i = -i - 1;
                }
                if (i < arr2.length) { // choose from arr2 (1 operation)
                    ndp.put(arr2[i],
                            Math.min(ndp.getOrDefault(arr2[i], Integer.MAX_VALUE), operations + 1));
                }
            }
            if (ndp.isEmpty()) { return -1; }

            dp = ndp;
        }
        return Collections.min(dp.values());
    }

    // Recursion + Dynamic Programming(Top-Down) + Sort + Binary Search
    // time complexity: O(N1*N2), space complexity: O(N1*N2)
    // 48 ms(86.00%), 58.5 MB(41.00%) for 21 tests
    public int makeArrayIncreasing2(int[] arr1, int[] arr2) {
        int m = arr1.length;
        int n = arr2.length;
        Arrays.sort(arr2);
        int res = dfs(arr1, arr2, 0, 0, -1, new int[m][n + 1]);
        return res > m ? -1 : res;
    }

    private int dfs(int[] a1, int[] a2, int i1, int i2, int prev, int[][] dp) {
        if (i1 >= a1.length) { return 0; }

        i2 = Arrays.binarySearch(a2, i2, a2.length, prev + 1);
        if (i2 < 0) {
            i2 = -i2 - 1;
        }
        if (a1[i1] <= prev && i2 >= a2.length) { return a1.length + 1; }

        if (dp[i1][i2] == 0) {
            dp[i1][i2] = (i2 < a2.length ? dfs(a1, a2, i1 + 1, i2, a2[i2], dp) : a1.length) + 1;
            if (a1[i1] > prev) {
                dp[i1][i2] = Math.min(dp[i1][i2], dfs(a1, a2, i1 + 1, i2, a1[i1], dp));
            }
        }
        return dp[i1][i2];
    }

    // 2-D Dynamic Programming(Bottom-Up) + SortedSet
    // time complexity: O(N1^2*log(N2)), space complexity: O(N1^2)
    // 142 ms(54.00%), 62.4 MB(35.00%) for 21 tests
    public int makeArrayIncreasing3(int[] arr1, int[] arr2) {
        NavigableSet<Integer> set = new TreeSet<>();
        for (int a : arr2) {
            set.add(a);
        }
        int n = arr1.length + 1;
        int[][] dp = new int[n][n]; // min value for the index of i-1 in arr1 given operations
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        dp[0][0] = -1;
        for (int i = 1; i < n; i++) {
            for (int op = 0; op <= i; op++) {
                if (arr1[i - 1] > dp[i - 1][op]) {
                    dp[i][op] = arr1[i - 1];
                }
                if (op > 0) {
                    Integer val = set.higher(dp[i - 1][op - 1]);
                    if (val != null) {
                        dp[i][op] = Math.min(dp[i][op], val);
                    }
                }
                if (i == n - 1 && dp[i][op] < Integer.MAX_VALUE) { return op; }
            }
        }
        return -1;
    }

    private void test(int[] arr1, int[] arr2, int expected) {
        assertEquals(expected, makeArrayIncreasing(arr1, arr2));
        assertEquals(expected, makeArrayIncreasing2(arr1, arr2));
        assertEquals(expected, makeArrayIncreasing3(arr1, arr2));
    }

    @Test public void test() {
        test(new int[] {1, 5, 3, 6, 7}, new int[] {1, 3, 2, 4}, 1);
        test(new int[] {1, 5, 3, 6, 7}, new int[] {4, 3, 1}, 2);
        test(new int[] {1, 5, 3, 6, 7}, new int[] {1, 6, 3, 3}, -1);
        test(new int[] {1}, new int[] {1, 3, 2, 4}, 0);
        test(new int[] {1, 5, 9, 3, 6, 7, 8, 2, 5, 5}, new int[] {1, 6, 3, 3, 5, 7, 8, 9, 2, 4, 10},
             9);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
