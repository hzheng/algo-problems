import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC526: https://leetcode.com/problems/beautiful-arrangement/
//
// Suppose you have N integers from 1 to N. We define a beautiful arrangement as
// an array that is constructed by these N numbers successfully if one of the
// following is true for the ith position (1 ≤ i ≤ N) in this array:
// The number at the ith position is divisible by i.
// i is divisible by the number at the ith position.
// Now given N, how many beautiful arrangements can you construct?
// Note: N is a positive integer and will not exceed 15.
public class BeautifulArrangement {
    // DFS + Recursion + Bit Manipulation
    // beats 58.26%(100 ms for 15 tests)
    public int countArrangement(int N) {
        int[] res = new int[1];
        dfs(N, 1, 0, res);
        return res[0];
    }

    private void dfs(int n, int cur, int visited, int[] res) {
        if (cur > n) {
            res[0]++;
            return;
        }
        for (int i = 1, mask = 1; i <= n; mask <<= 1, i++) {
            if ((visited & mask) == 0 && (i % cur == 0 || cur % i == 0)) {
                dfs(n, cur + 1, visited | mask, res);
            }
        }
    }

    // DFS + Dynamic Programming(Top-Down) + Bit Manipulation
    // beats 82.24%(33 ms for 15 tests)
    public int countArrangement2(int N) {
        return dfs2(N, 1, 0, new int[N + 1][1 << N]);
    }

    private int dfs2(int n, int cur, int visited, int[][] dp) {
        if (cur > n) return 1;

        int res = dp[cur][visited];
        if (res > 0) return res;

        for (int i = 1, mask = 1; i <= n; mask <<= 1, i++) {
            if ((visited & mask) == 0 && (i % cur == 0 || cur % i == 0)) {
                res += dfs2(n, cur + 1, visited | mask, dp);
            }
        }
        return dp[cur][visited] = res;
    }

    // DFS/Backtracking + Recursion
    // beats 92.83%(8 ms for 15 tests)
    public int countArrangement3(int N) {
        int[] nums = new int[N];
        for (int i = 0; i < N; i++) {
            nums[i] = i + 1;
        }
        return dfs3(N, nums);
    }

    private int dfs3(int n, int[] nums) {
        if (n == 0) return 1;

        int count = 0;
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            if (num % n == 0 || n % num == 0) {
                swap(nums, i, n - 1);
                count += dfs3(n - 1, nums);
                swap(nums, i, n - 1);
            }
        }
        return count;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // Dynamic Programming(Bottom-Up) + Bit Manipulation
    // beats 84.74%(20 ms for 15 tests)
    public int countArrangement4(int N) {
        int size = 1 << N;
        int[][] dp = new int[N + 1][size];
        dp[0][0] = 1;
        for (int i = 1; i <= N; i++) {
            for (int j = 1, mask = 1; j <= N; mask <<= 1, j++) {
                if (i % j != 0 && j % i != 0) continue;

                for (int arrange = 0; arrange < size; arrange++) {
                    if ((arrange & mask) == 0) {
                        dp[i][arrange | mask] += dp[i - 1][arrange];
                    }
                }
            }
        }
        int res = 0;
        for (int i = 0; i < size; i++) {
            res += dp[N][i];
        }
        return res;
    }

    // TODO: Recursion according to combinatorics

    void test(int N, int expected) {
        assertEquals(expected, countArrangement(N));
        assertEquals(expected, countArrangement2(N));
        assertEquals(expected, countArrangement3(N));
        assertEquals(expected, countArrangement4(N));
    }

    @Test
    public void test() {
        test(1, 1);
        test(2, 2);
        test(3, 3);
        test(4, 8);
        test(5, 10);
        test(6, 36);
        test(7, 41);
        test(8, 132);
        test(9, 250);
        test(10, 700);
        test(11, 750);
        test(12, 4010);
        test(13, 4237);
        test(14, 10680);
        test(15, 24679);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BeautifulArrangement");
    }
}
