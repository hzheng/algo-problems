import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1871: https://leetcode.com/problems/jump-game-vii/
//
// You are given a 0-indexed binary string s and two integers minJump and maxJump. In the beginning,
// you are standing at index 0, which is equal to '0'. You can move from index i to index j if the
// following conditions are fulfilled:
// i + minJump <= j <= min(i + maxJump, s.length - 1), and
// s[j] == '0'.
// Return true if you can reach index s.length - 1 in s, or false otherwise.
//
// Constraints:
// 2 <= s.length <= 10^5
// s[i] is either '0' or '1'.
// s[0] == '0'
// 1 <= minJump <= maxJump < s.length
public class JumpGameVII {
    // DFS + Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N*(M-m)), space complexity: O(N)
    // Time Limit Exceeded
    public boolean canReach(String s, int minJump, int maxJump) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        if (cs[n - 1] != '0') { return false; }

        return canReach(cs, minJump, maxJump, 0, new Boolean[n]);
    }

    boolean canReach(char[] s, int min, int max, int cur, Boolean[] dp) {
        if (dp[cur] != null) { return dp[cur]; }

        int n = s.length;
        if (cur == n - 1) { return dp[cur] = true; }

        for (int i = Math.min(cur + max, n - 1); i >= cur + min; i--) {
            if (s[i] == '0' && canReach(s, min, max, i, dp)) { return true; }
        }
        return dp[cur] = false;
    }

    // Dynamic Programming
    // time complexity: O(N*(M-m)), space complexity: O(N)
    // Time Limit Exceeded
    public boolean canReach2(String s, int minJump, int maxJump) {
        int n = s.length();
        if (s.charAt(n - 1) != '0') { return false; }

        boolean[] dp = new boolean[n];
        dp[0] = true;
        for (int i = minJump; i < n; i++) {
            if (s.charAt(i) != '0') { continue; }

            for (int j = i - minJump; !dp[i] && j >= Math.max(0, i - maxJump); j--) {
                dp[i] = dp[j];
            }
        }
        return dp[n - 1];
    }

    // Dynamic Programming + Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 7 ms(70.00%), 39.9 MB(40.00%) for 123 tests
    public boolean canReach3(String s, int minJump, int maxJump) {
        int n = s.length();
        if (s.charAt(n - 1) != '0') { return false; }

        boolean[] dp = new boolean[n];
        dp[0] = true;
        for (int i = minJump, reached = 0; i < n; i++) {
            reached += dp[i - minJump] ? 1 : 0;
            dp[i] = reached > 0 && s.charAt(i) == '0';
            reached -= (i >= maxJump && dp[i - maxJump]) ? 1 : 0;
        }
        return dp[n - 1];
    }

    // SortedSet
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 156 ms(10.00%), 49.3 MB(10.00%) for 123 tests
    public boolean canReach4(String s, int minJump, int maxJump) {
        int n = s.length();
        if (s.charAt(n - 1) != '0') { return false; }

        TreeSet<Integer> reachable = new TreeSet<>();
        reachable.add(0);
        for (int i = minJump; i < n; i++) {
            if (s.charAt(i) == '0' && reachable.floor(i - minJump) >= i - maxJump) {
                if (i == n - 1) { return true; }
                reachable.add(i);
            }
        }
        return false;
    }

    // Queue + BFS
    // time complexity: O(N), space complexity: O(N)
    // 13 ms(40.00%), 47.6 MB(10.00%) for 123 tests
    public boolean canReach5(String s, int minJump, int maxJump) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        for (int farthest = 0, n = s.length(), cur; !queue.isEmpty(); farthest = cur + maxJump) {
            cur = queue.poll();
            for (int i = Math.max(cur + minJump, farthest + 1);
                 i < Math.min(n, cur + maxJump + 1); i++) {
                if (s.charAt(i) == '0') {
                    if (i == n - 1) { return true; }

                    queue.offer(i);
                }
            }
        }
        return false;
    }

    private void test(String s, int minJump, int maxJump, boolean expected) {
        assertEquals(expected, canReach(s, minJump, maxJump));
        assertEquals(expected, canReach2(s, minJump, maxJump));
        assertEquals(expected, canReach3(s, minJump, maxJump));
        assertEquals(expected, canReach4(s, minJump, maxJump));
        assertEquals(expected, canReach5(s, minJump, maxJump));
    }

    @Test public void test1() {
        test("011010", 2, 3, true);
        test("01101110", 2, 3, false);
        test("01101110111000011110", 3, 5, true);
        test("01101110111101011110", 3, 5, false);
    }

    @FunctionalInterface interface Function<A, B, C, D> {
        D apply(A a, B b, C c);
    }

    private void test(Function<String, Integer, Integer, Boolean> canReach, String name) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            long t1 = System.nanoTime();
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                int minJump = Integer.parseInt(scanner.nextLine());
                int maxJump = Integer.parseInt(scanner.nextLine());
                boolean res = canReach.apply(s, minJump, maxJump);
                boolean expected = Boolean.parseBoolean(scanner.nextLine());
                assertEquals(expected, res);
            }
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void test2() {
        JumpGameVII j = new JumpGameVII();
        test(j::canReach, "canReach");
        test(j::canReach2, "canReach2");
        test(j::canReach3, "canReach3");
        test(j::canReach4, "canReach4");
        test(j::canReach5, "canReach5");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
