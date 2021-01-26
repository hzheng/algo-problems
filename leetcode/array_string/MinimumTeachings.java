import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1733: https://leetcode.com/problems/minimum-number-of-people-to-teach/
//
// On a social network consisting of m users and some friendships between users, two users can
// communicate with each other if they know a common language.
// You are given an integer n, an array languages, and an array friendships where:
// There are n languages numbered 1 through n,
// languages[i] is the set of languages the ith user knows, and
// friendships[i] = [ui, vi] denotes a friendship between the users ui and vi.
// You can choose one language and teach it to some users so that all friends can communicate with
// each other. Return the minimum number of users you need to teach.
// Note that friendships are not transitive, meaning if x is a friend of y and y is a friend of z,
// this doesn't guarantee that x is a friend of z.
//
// Constraints:
// 2 <= n <= 500
// languages.length == m
// 1 <= m <= 500
// 1 <= languages[i].length <= n
// 1 <= languages[i][j] <= n
// 1 <= ui < vi <= languages.length
// 1 <= friendships.length <= 500
// All tuples (ui, vi) are unique
// languages[i] contains only unique values
public class MinimumTeachings {
    // Hash Table + Set
    // time complexity: O(N*(M+F)), space complexity: O(F+M*N)
    // 93 ms(60.44%), 57.5 MB(55.58%) for 99 tests
    public int minimumTeachings(int n, int[][] languages, int[][] friendships) {
        int m = languages.length;
        Map<Integer, Set<Integer>> langMap = new HashMap<>();
        for (int user = 0; user < m; user++) {
            for (int lang : languages[user]) {
                langMap.computeIfAbsent(user + 1, x -> new HashSet<>()).add(lang);
            }
        }
        boolean[] ignoredFriendships = new boolean[friendships.length];
        int i = 0;
        for (int[] f : friendships) {
            for (int lang = 1; lang <= n; lang++) {
                if (langMap.get(f[0]).contains(lang) && langMap.get(f[1]).contains(lang)) {
                    ignoredFriendships[i] = true;
                    break;
                }
            }
            i++;
        }
        int res = Integer.MAX_VALUE;
        for (int lang = 1; lang <= n; lang++) {
            Set<Integer> teachSet = new HashSet<>();
            i = 0;
            for (int[] f : friendships) {
                if (ignoredFriendships[i++]) { continue; }

                if (!langMap.get(f[0]).contains(lang)) {
                    teachSet.add(f[0]);
                }
                if (!langMap.get(f[1]).contains(lang)) {
                    teachSet.add(f[1]);
                }
            }
            res = Math.min(teachSet.size(), res);
        }
        return res;
    }

    // Set
    // time complexity: O(N*(M+F)), space complexity: O(M*N)
    // 70 ms(69.18%), 53.8 MB(81.07%) for 99 tests
    public int minimumTeachings2(int n, int[][] languages, int[][] friendships) {
        List<Set<Integer>> langList = new ArrayList<>();
        for (int[] lang : languages) {
            langList.add(Arrays.stream(lang).boxed().collect(Collectors.toSet()));
        }
        Set<Integer> needTeach = new HashSet<>();
        for (int[] f : friendships) {
            int u = f[0] - 1;
            int v = f[1] - 1;
            if (!intersect(langList.get(u), langList.get(v))) {
                needTeach.add(u);
                needTeach.add(v);
            }
        }
        int[] langCount = new int[n + 1];
        for (int user : needTeach) {
            for (int lang : langList.get(user)) {
                langCount[lang]++;
            }
        }
        return needTeach.size() - Arrays.stream(langCount).max().getAsInt();
    }

    private boolean intersect(Set<Integer> a, Set<Integer> b) {
        for (int x : a) {
            if (b.contains(x)) { return true; }
        }
        for (int x : b) {
            if (a.contains(x)) { return true; }
        }
        return false;
    }

    private void test(int n, int[][] lauguages, int[][] friendships, int expected) {
        assertEquals(expected, minimumTeachings(n, lauguages, friendships));
        assertEquals(expected, minimumTeachings2(n, lauguages, friendships));
    }

    @Test public void test() {
        test(2, new int[][] {{1}, {2}, {1, 2}}, new int[][] {{1, 2}, {1, 3}, {2, 3}}, 1);
        test(3, new int[][] {{2}, {1, 3}, {1, 2}, {3}},
             new int[][] {{1, 4}, {1, 2}, {3, 4}, {2, 3}}, 2);
        test(11,
             new int[][] {{3, 11, 5, 10, 1, 4, 9, 7, 2, 8, 6}, {5, 10, 6, 4, 8, 7}, {6, 11, 7, 9},
                          {11, 10, 4}, {6, 2, 8, 4, 3}, {9, 2, 8, 4, 6, 1, 5, 7, 3, 10},
                          {7, 5, 11, 1, 3, 4}, {3, 4, 11, 10, 6, 2, 1, 7, 5, 8, 9},
                          {8, 6, 10, 2, 3, 1, 11, 5}, {5, 11, 6, 4, 2}},
             new int[][] {{7, 9}, {3, 7}, {3, 4}, {2, 9}, {1, 8}, {5, 9}, {8, 9}, {6, 9}, {3, 5},
                          {4, 5}, {4, 9}, {3, 6}, {1, 7}, {1, 3}, {2, 8}, {2, 6}, {5, 7}, {4, 6},
                          {5, 8}, {5, 6}, {2, 7}, {4, 8}, {3, 8}, {6, 8}, {2, 5}, {1, 4}, {1, 9},
                          {1, 6}, {6, 7}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
