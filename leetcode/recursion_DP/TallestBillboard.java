import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC956: https://leetcode.com/problems/tallest-billboard/
//
// You are installing a billboard and want it to have the largest height.  The billboard will have
// two steel supports, one on each side.  Each steel support must be an equal height.
// You have a collection of rods which can be welded together. For example, if you have rods of
// lengths 1, 2, and 3, you can weld them together to make a support of length 6.
// Return the largest possible height of your billboard installation. If you cannot support the
// billboard, return 0.
//
// Note:
// 0 <= rods.length <= 20
// 1 <= rods[i] <= 1000
// The sum of rods is at most 5000.
public class TallestBillboard {
    // Bit Manipulation
    // time complexity: O(2^N), space complexity: O(2^N)
    // Time Limit Exceeded
    //  ms(91.72%), 38.8 MB(32.41%) for 74 tests
    public int tallestBillboard0(int[] rods) {
        int n = rods.length;
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int mask = (1 << n) - 1; mask > 0; mask--) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) != 0) {
                    sum += rods[i];
                }
            }
            map.computeIfAbsent(sum, x -> new ArrayList<>()).add(mask);
        }
        int res = 0;
        outer:
        for (int sum : map.keySet()) {
            List<Integer> masks = map.get(sum);
            int m = masks.size();
            for (int i = 0; i < m; i++) {
                for (int j = i + 1; j < m; j++) {
                    if ((masks.get(i) & masks.get(j)) == 0) {
                        res = Math.max(res, sum);
                        continue outer;
                    }
                }
            }
        }
        return res;
    }

    // Meet in the Middle + DFS + Recursion + Hash Table
    // time complexity: O(3^(N/2)), space complexity: O(3^(N/2))
    // 398 ms(5.15%), 39.3 MB(52.94%) for 74 tests
    public int tallestBillboard(int[] rods) {
        int n = rods.length;
        Map<Integer, Integer> map1 = new HashMap<>();
        countSum(rods, 0, 0, 0, n / 2, map1);
        Map<Integer, Integer> map2 = new HashMap<>();
        countSum(rods, 0, 0, n / 2, n, map2);
        int res = 0;
        for (int sum1 : map1.keySet()) {
            Integer maxSum2 = map2.get(-sum1);
            if (maxSum2 != null) {
                res = Math.max(res, map1.get(sum1) + maxSum2);
            }
        }
        return res;
    }

    private int positiveSum(int[] rods, int choice, int start, int end) {
        int sum = 0;
        for (int i = end - 1; i >= start; i--, choice /= 3) {
            if (choice % 3 == 2) { // chose +1 (instead of 0 or -1)
                sum += rods[i];
            }
        }
        return sum;
    }

    private void countSum(int[] rods, int choice, int sum, int cur, int end,
                          Map<Integer, Integer> map) {
        if (cur == end) {
            int n = rods.length;
            int maxSum = positiveSum(rods, choice, end == n ? n / 2 : 0, end);
            map.put(sum, Math.max(map.getOrDefault(sum, 0), maxSum));
            return;
        }

        choice *= 3; // ternary system
        for (int i = -1; i <= 1; i++) {
            countSum(rods, choice + i + 1, sum + rods[cur] * i, cur + 1, end, map);
        }
    }

    // Meet in the Middle + Hash Table
    // time complexity: O(3^(N/2)), space complexity: O(3^(N/2))
    // 218 ms(10.29%), 47.6 MB(5.15%) for 74 tests
    public int tallestBillboard2(int[] rods) {
        int n = rods.length;
        final int max = (int)Math.pow(3, (n + 1) / 2);
        Map<Integer, Integer> sum1 = sumMap(Arrays.copyOfRange(rods, 0, n / 2), max);
        Map<Integer, Integer> sum2 = sumMap(Arrays.copyOfRange(rods, n / 2, n), max);
        int res = 0;
        for (int s : sum1.keySet()) {
            if (sum2.containsKey(-s)) {
                res = Math.max(res, sum1.get(s) + sum2.get(-s));
            }
        }
        return res;
    }

    public Map<Integer, Integer> sumMap(int[] rods, int max) {
        int[][] sum = new int[max][2];
        int ptr = 0;
        sum[ptr++] = new int[2];
        for (int rod : rods) {
            for (int i = 0, len = ptr; i < len; i++) {
                int[] s = sum[i];
                sum[ptr++] = new int[] {s[0] + rod, s[1]};
                sum[ptr++] = new int[] {s[0], s[1] + rod};
            }
        }
        Map<Integer, Integer> res = new HashMap<>();
        for (int i = 0; i < ptr; i++) {
            int positive = sum[i][0];
            int negative = sum[i][1];
            res.put(positive - negative,
                    Math.max(res.getOrDefault(positive - negative, 0), positive));
        }
        return res;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N*SUM)), space complexity: O(N*SUM)
    // 29 ms(52.94%), 42.6 MB(23.53%) for 74 tests
    public int tallestBillboard3(int[] rods) {
        int sum = Arrays.stream(rods).sum();
        return dfs(rods, 0, sum, sum, new Integer[rods.length][sum * 2 + 1]);
    }

    private int dfs(int[] rods, int cur, int sum, int total, Integer[][] dp) {
        if (cur == rods.length) { // or: early return if (sum > total * 1.5)
            return sum == total ? 0 : Integer.MIN_VALUE / 3;
        }
        if (dp[cur][sum] != null) { return dp[cur][sum]; }

        int res = dfs(rods, cur + 1, sum, total, dp);
        res = Math.max(res, dfs(rods, cur + 1, sum - rods[cur], total, dp));
        res = Math.max(res, rods[cur] + dfs(rods, cur + 1, sum + rods[cur], total, dp));
        return dp[cur][sum] = res;
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N*SUM)), space complexity: O(N*SUM)
    // 68 ms(26.47%), 40.8 MB(38.97%) for 74 tests
    public int tallestBillboard4(int[] rods) {
        int n = rods.length;
        int sum = Arrays.stream(rods).sum();
        int maxSum = sum * 2 + 1; // shift by sum
        boolean[][] welded = new boolean[n + 1][maxSum]; // can first i rods be welded to j - sum?
        int[][] dp = new int[n + 1][maxSum]; // the max sum of positive
        welded[0][sum] = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < maxSum; j++) {
                for (int k = -1; k <= 1; k++) {
                    int s = j + rods[i] * k;
                    if (s >= 0 && s < maxSum && welded[i][s]) {
                        welded[i + 1][j] = true;
                        dp[i + 1][j] = Math.max(dp[i + 1][j], dp[i][s] + (k < 0 ? rods[i] : 0));
                    }
                }
            }
        }
        return dp[n][sum];
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N*SUM)), space complexity: O(N*SUM)
    // 13 ms(78.68%), 38.5 MB(77.21%) for 74 tests
    public int tallestBillboard5(int[] rods) {
        int sum = Arrays.stream(rods).sum();
        int[] dp = new int[sum + 1]; // smaller one of max pair of numbers whose difference is index
        Arrays.fill(dp, Integer.MIN_VALUE / 3);
        dp[0] = 0;
        for (int r : rods) {
            int[] cur = dp.clone();
            for (int d = 0; d + r < dp.length; d++) {
                int newDiff = d + r;
                dp[newDiff] = Math.max(dp[newDiff], cur[d]);
                newDiff = Math.abs(d - r);
                dp[newDiff] = Math.max(dp[newDiff], cur[d] + Math.min(d, r));
            }
        }
        return dp[0];
    }

    // Dynamic Programming(Bottom-Up) + Hash Table
    // time complexity: O(N*SUM)), space complexity: O(N*SUM)
    // 102 ms(22.06%), 39.7 MB(46.32%) for 74 tests
    public int tallestBillboard6(int[] rods) {
        Map<Integer, Integer> dp = new HashMap<>();
        dp.put(0, 0);
        for (int r : rods) {
            Map<Integer, Integer> cur = new HashMap<>(dp);
            for (int d : cur.keySet()) {
                int newDiff = d + r;
                dp.put(newDiff, Math.max(cur.get(d), dp.getOrDefault(newDiff, 0)));
                newDiff = Math.abs(d - r);
                dp.put(newDiff, Math.max(cur.get(d) + Math.min(d, r), dp.getOrDefault(newDiff, 0)));
            }
        }
        return dp.get(0);
    }

    private void test(int[] rods, int expected) {
        assertEquals(expected, tallestBillboard0(rods));
        assertEquals(expected, tallestBillboard(rods));
        assertEquals(expected, tallestBillboard2(rods));
        assertEquals(expected, tallestBillboard3(rods));
        assertEquals(expected, tallestBillboard4(rods));
        assertEquals(expected, tallestBillboard5(rods));
        assertEquals(expected, tallestBillboard6(rods));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 6}, 6);
        test(new int[] {1, 2, 3, 4, 5, 6}, 10);
        test(new int[] {1, 2}, 0);
        test(new int[] {34, 28, 39, 23, 32, 26, 23, 24, 26, 24, 26, 29, 27, 34, 30, 38, 34, 37, 36},
             285);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
