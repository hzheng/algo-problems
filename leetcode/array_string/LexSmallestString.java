import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1625: https://leetcode.com/problems/lexicographically-smallest-string-after-applying-operations/
//
// You are given a string s of even length consisting of digits from 0 to 9, and two integers a and
// b. You can apply either of the following 2 operations any number of times and in any order on s:
// Add a to all odd indices of s (0-indexed). Digits post 9 are cycled back to 0.
// Rotate s to the right by b positions. Return the lexicographically smallest string you can obtain
// by applying the above operations any number of times on s.
// A string a is lexicographically smaller than a string b (of the same length) if in the first
// position where a and b differ, string a has a letter that appears earlier in the alphabet than
// the corresponding letter in b.
// Constraints:
// 2 <= s.length <= 100
// s.length is even.
// s consists of digits from 0 to 9 only.
// 1 <= a <= 9
// 1 <= b <= s.length - 1
public class LexSmallestString {
    // Set
    // time complexity: O(N), space complexity: O(N)
    // 72 ms(100%), 42.8 MB(33.33%) for 80 tests
    public String findLexSmallestString(String s, int a, int b) {
        Set<String> addSet = getAdds(s, a, null);
        if (b % 2 == 1) { // even-shifts won't add more candidates
            getAdds(rotate(s, b), a, addSet);
        }
        Set<String> rotateSet = new HashSet<>();
        for (String c : addSet) {
            for (String s2 = c; rotateSet.add(s2); s2 = rotate(s2, b)) {}
        }
        Set<String> candidates = new HashSet<>();
        for (String c : rotateSet) {
            getAdds(c, a, candidates);
        }
        String res = s;
        for (String str : candidates) {
            if (str.compareTo(res) < 0) {
                res = str;
            }
        }
        return res;
    }

    private Set<String> getAdds(String s, int a, Set<String> set) {
        if (set == null) {
            set = new HashSet<>();
        }
        for (String str = s; set.add(str); str = add(str, a)) {}
        return set;
    }

    private String add(String s, int a) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (i % 2 == 1) {
                c[i] = (char)((c[i] - '0' + a) % 10 + '0');
            }
        }
        return String.valueOf(c);
    }

    private String rotate(String s, int b) {
        int n = s.length();
        char[] res = new char[n];
        for (int i = 0; i < n; i++) {
            res[i] = s.charAt((i - b + n) % n);
        }
        return String.valueOf(res);
    }

    // DFS + Recursion + Set
    // time complexity: O(N), space complexity: O(N)
    // 105 ms(100%), 46.1 MB(33.33%) for 80 tests
    public String findLexSmallestString2(String s, int a, int b) {
        return dfs(s, a, b, new HashSet<>(), new String[]{s});
    }

    private String dfs(String s, int a, int b, Set<String> set, String[] res) {
        if (set.add(s)) {
            if (res[0].compareTo(s) > 0) {
                res[0] = s;
            }
            dfs(add(s, a), a, b, set, res);
            dfs(rotate(s, b), a, b, set, res);
        }
        return res[0];
    }

    // BFS + Queue + Set
    // time complexity: O(N), space complexity: O(N)
    // 62 ms(100%), 43.2 MB(33.33%) for 80 tests
    public String findLexSmallestString3(String s, int a, int b) {
        int n = s.length();
        String res = s;
        Queue<String> q = new LinkedList<>();
        Set<String> visited = new HashSet<>(q);
        for (q.offer(s); !q.isEmpty(); ) {
            String cur = q.poll();
            if (res.compareTo(cur) > 0) {
                res = cur;
            }
            char[] cs = cur.toCharArray();
            for (int i = 1; i < n; i += 2) {
                cs[i] = (char)((cs[i] - '0' + a) % 10 + '0');
            }
            String candidate = String.valueOf(cs);
            if (visited.add(candidate)) {
                q.offer(candidate);
            }
            candidate = cur.substring(n - b) + cur.substring(0, n - b);
            if (visited.add(candidate)) {
                q.offer(candidate);
            }
        }
        return res;
    }

    private void test(String s, int a, int b, String expected) {
        assertEquals(expected, findLexSmallestString(s, a, b));
        assertEquals(expected, findLexSmallestString2(s, a, b));
        assertEquals(expected, findLexSmallestString3(s, a, b));
    }

    @Test public void test() {
        test("5525", 9, 2, "2050");
        test("74", 5, 1, "24");
        test("0011", 4, 2, "0011");
        test("43987654", 7, 3, "00553311");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
