import java.util.*;

import org.junit.Test;

import static org.hamcrest.collection.IsIn.*;

import static org.junit.Assert.*;

// LC753: https://leetcode.com/problems/cracking-the-safe/
//
// There is a box protected by a password. The password is a sequence of n digits where each digit
// can be one of the first k digits 0, 1, ..., k-1. While entering a password, the last n digits
// entered will automatically be matched against the correct password. For example, assuming the
// correct password is "345", if you type "012345", the box will open because the correct password
// matches the suffix of the entered password.
// Return any password of minimum length that is guaranteed to open the box at some point of
// entering it.
//
// Note:
// n will be in the range [1, 4].
// k will be in the range [1, 10].
// k^n will be at most 4096.
public class CrackSafe {
    // https://en.wikipedia.org/wiki/De_Bruijn_sequence
    // Recursive + DFS + Backtracking
    // time complexity: O(N*K^N), space complexity: O(N*K^N)
    // 18 ms(61.05%), 40.7 MB(5.13%) for 38 tests
    public String crackSafe(int n, int k) {
        String start = String.join("", Collections.nCopies(n, "0"));
        Set<String> visited = new HashSet<>();
        visited.add(start);
        StringBuilder sb = new StringBuilder(start);
        dfs(n, k, (int)Math.pow(k, n), sb, visited);
        return sb.toString();
    }

    private boolean dfs(int n, int k, int total, StringBuilder sb, Set<String> visited) {
        if (visited.size() == total) { return true; }

        String endStr = sb.substring(sb.length() - n + 1);
        for (char c = '0'; c < '0' + k; c++) {
            String next = endStr + c;
            if (visited.add(next)) {
                sb.append(c);
                if (dfs(n, k, total, sb, visited)) { return true; }

                visited.remove(next);
                sb.setLength(sb.length() - 1);
            }
        }
        return false;
    }

    // Hierholzer's Algorithm
    // Recursive + DFS
    // time complexity: O(N*K^N), space complexity: O(N*K^N)
    // 15 ms(78.42%), 41.1 MB(5.13%) for 38 tests
    public String crackSafe2(int n, int k) {
        String start = "0".repeat(Math.max(0, n - 1));
        StringBuilder res = new StringBuilder();
        dfs(k, start, new HashSet<>(), res);
        res.append(start);
        return res.toString();
    }

    private void dfs(int k, String node, Set<String> visited, StringBuilder res) {
        for (int c = 0; c < k; c++) {
            String next = node + c;
            if (visited.add(next)) {
                dfs(k, next.substring(1), visited, res);
                res.append(c); // post-order
            }
        }
    }

    // Inverse Burrows-Wheeler Transform
    // https://leetcode.com/problems/cracking-the-safe/solution/
    // time complexity: O(K^N), space complexity: O(K^N)
    // 1 ms(99.74%), 36.5 MB(5.13%) for 38 tests
    public String crackSafe3(int n, int k) {
        int M = (int)Math.pow(k, n - 1);
        int[] P = new int[M * k];
        for (int i = 0; i < k; i++) {
            for (int q = 0; q < M; q++) {
                P[i * M + q] = q * k + i;
            }
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < M * k; i++) {
            for (int j = i, v; P[j] >= 0; j = v) {
                res.append(j / M);
                v = P[j];
                P[j] = -1;
            }
        }
        res.append("0".repeat(Math.max(0, n - 1)));
        return res.toString();
    }

    private void test(int n, int k, String... expected) {
        assertThat(crackSafe(n, k), in(expected));
        assertThat(crackSafe2(n, k), in(expected));
        assertThat(crackSafe3(n, k), in(expected));
    }

    @Test public void test() {
        test(3, 2, "0011101000", "0001011100");
        test(1, 2, "01", "10");
        test(2, 2, "00110", "01100", "10011", "11001");
        test(1, 1, "0");
        test(2, 3, "0010211220", "0221120100");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
