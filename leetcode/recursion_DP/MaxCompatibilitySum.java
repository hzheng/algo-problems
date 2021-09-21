import org.junit.Test;

import static org.junit.Assert.*;

// LC1947: https://leetcode.com/problems/maximum-compatibility-score-sum/
//
// There is a survey that consists of n questions where each question's answer is either 0 (no) or
// 1 (yes). The survey was given to m students numbered from 0 to m - 1 and m mentors numbered from
// 0 to m - 1. The answers of the students are represented by a 2D integer array students where
// students[i] is an integer array that contains the answers of the ith student (0-indexed). The
// answers of the mentors are represented by a 2D integer array mentors where mentors[j] is an
// integer array that contains the answers of the jth mentor (0-indexed).
// Each student will be assigned to one mentor, and each mentor will have one student assigned to
// them. The compatibility score of a student-mentor pair is the number of answers that are the same
// for both the student and the mentor.
// For example, if the student's answers were [1, 0, 1] and the mentor's answers were [0, 0, 1],
// then their compatibility score is 2 because only the second and the third answers are the same.
// You are tasked with finding the optimal student-mentor pairings to maximize the sum of the
// compatibility scores.
// Given students and mentors, return the maximum compatibility score sum that can be achieved.
//
// Constraints:
// m == students.length == mentors.length
// n == students[i].length == mentors[j].length
// 1 <= m, n <= 8
// students[i][k] is either 0 or 1.
// mentors[j][k] is either 0 or 1.
public class MaxCompatibilitySum {
    // time complexity: O(M!*N), space complexity: O(M)
    // 67 ms(32.80%), 37 MB(60.72%) for 86 tests
    public int maxCompatibilitySum(int[][] students, int[][] mentors) {
        int m = mentors.length;
        int[] perm = new int[m];
        for (int i = 0; i < m; i++) {
            perm[i] = i;
        }
        int res = 0;
        do {
            res = Math.max(res, compatibilitySum(students, mentors, perm));
        } while (nextPermutation(perm));
        return res;
    }

    private int compatibilitySum(int[][] students, int[][] mentors, int[] perm) {
        int n = mentors.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            int[] s = students[i];
            int[] m = mentors[perm[i]];
            for (int j = s.length - 1; j >= 0; j--) {
                res += (s[j] == m[j] ? 1 : 0);
            }
        }
        return res;
    }

    private boolean nextPermutation(int[] nums) {
        int len = nums.length;
        int i = len - 1;
        for (; i > 0 && nums[i - 1] >= nums[i]; i--) {}

        if (i > 0) {
            int j = len - 1;
            for (; nums[j] <= nums[i - 1]; j--) {} // may use binary search
            swap(nums, i - 1, j);
        } else {return false;}
        // reverse from i to the end
        for (int j = len - 1; j > i; i++, j--) {
            swap(nums, i, j);
        }
        return true;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // DFS + Recursion
    // time complexity: O(M!*N), space complexity: O(M)
    // 86 ms(28.24%), 36.3 MB(99.49%) for 86 tests
    public int maxCompatibilitySum2(int[][] students, int[][] mentors) {
        int m = mentors.length;
        int[] res = new int[1];
        dfs(students, mentors, new int[m], 0, new boolean[m], res);
        return res[0];
    }

    private void dfs(int[][] students, int[][] mentors, int[] perm, int cur, boolean[] visited,
                     int[] res) {
        int n = perm.length;
        if (cur == n) {
            res[0] = Math.max(res[0], compatibilitySum(students, mentors, perm));
            return;
        }
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                visited[i] = true;
                perm[cur] = i;
                dfs(students, mentors, perm, cur + 1, visited, res);
                visited[i] = false;
            }
        }
    }

    // DFS + Recursion
    // time complexity: O(M!*N), space complexity: O(M)
    // 36 ms(57.87%), 37.1 MB(56.99%) for 86 tests
    public int maxCompatibilitySum3(int[][] students, int[][] mentors) {
        int[] res = new int[1];
        dfs(students, mentors, 0, new boolean[mentors.length], 0, res);
        return res[0];
    }

    private void dfs(int[][] students, int[][] mentors, int cur, boolean[] visited, int score,
                     int[] res) {
        int n = students.length;
        if (cur == n) {
            res[0] = Math.max(res[0], score);
            return;
        }
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                visited[i] = true;
                dfs(students, mentors, cur + 1, visited,
                    score + getScore(students[cur], mentors[i]), res);
                visited[i] = false;
            }
        }
    }

    private int getScore(int[] students, int[] mentors) {
        int res = 0;
        for (int i = mentors.length - 1; i >= 0; i--) {
            res += (students[i] == mentors[i]) ? 1 : 0;
        }
        return res;
    }

    // Dynamic Programming(Bottom-Up) + Bit Manipulation
    // time complexity: O(2^M*M+M^2*N), space complexity: O(M^2)
    // 2 ms(92.73%), 36.8 MB(82.71%) for 86 tests
    public int maxCompatibilitySum4(int[][] students, int[][] mentors) {
        int m = mentors.length;
        int n = mentors[0].length;
        int[][] score = new int[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < n; k++) {
                    score[i][j] += (students[i][k] == mentors[j][k]) ? 1 : 0;
                }
            }
        }
        int[] dp = new int[1 << m];
        for (int mask = 0; mask < dp.length; mask++) { // assigned mentors
            int i = Integer.bitCount(mask);  // i-th student to be assigned
            for (int j = 0; j < m; j++) {
                int choice = 1 << j;
                if ((mask & choice) == 0) {  // assign i-th student to the j th mentor
                    dp[mask | choice] = Math.max(dp[mask | choice], dp[mask] + score[i][j]);
                }
            }
        }
        return dp[dp.length - 1];
    }

    private void test(int[][] students, int[][] mentors, int expected) {
        assertEquals(expected, maxCompatibilitySum(students, mentors));
        assertEquals(expected, maxCompatibilitySum2(students, mentors));
        assertEquals(expected, maxCompatibilitySum3(students, mentors));
        assertEquals(expected, maxCompatibilitySum4(students, mentors));
    }

    @Test public void test() {
        test(new int[][] {{1, 1, 0}, {1, 0, 1}, {0, 0, 1}},
             new int[][] {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}}, 8);
        test(new int[][] {{0, 0}, {0, 0}, {0, 0}}, new int[][] {{1, 1}, {1, 1}, {1, 1}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
