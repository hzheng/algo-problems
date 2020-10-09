import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1583: https://leetcode.com/problems/count-unhappy-friends/
//
// You are given a list of preferences for n friends, where n is always even.
// For each person i, preferences[i] contains a list of friends sorted in the order of preference.
// In other words, a friend earlier in the list is more preferred than a friend later in the list.
// Friends in each list are denoted by integers from 0 to n-1.
// All the friends are divided into pairs. The pairings are given in a list pairs, where
// pairs[i] = [xi, yi] denotes xi is paired with yi and yi is paired with xi.
// However, this pairing may cause some of the friends to be unhappy. A friend x is unhappy if x is
// paired with y and there exists a friend u who is paired with v but:
// x prefers u over y, and
// u prefers x over v.
// Return the number of unhappy friends.
// Constraints:
// 2 <= n <= 500
// n is even.
// preferences.length == n
// preferences[i].length == n - 1
// 0 <= preferences[i][j] <= n - 1
// preferences[i] does not contain i.
// All values in preferences[i] are unique.
// pairs.length == n/2
// pairs[i].length == 2
// xi != yi
// 0 <= xi, yi <= n - 1
// Each person is contained in exactly one pair.
public class UnhappyFriends {
    // Hash Table
    // time complexity: O(N^2), space complexity: O(N^2)
    // 27 ms(32.37%), 60.7 MB(5.40%) for 99 tests
    public int unhappyFriends(int n, int[][] preferences, int[][] pairs) {
        Map<Integer, Map<Integer, Integer>> orders = new HashMap<>();
        for (int i = 0; i < n; i++) {
            orders.put(i, new HashMap<>());
        }
        for (int i = 0; i < n; i++) {
            int[] pref = preferences[i];
            for (int j = 0; j < n - 1; j++) {
                orders.get(pref[j]).put(i, j);
            }
        }
        int res = 0;
        for (int[] pair : pairs) {
            int a = pair[0];
            int b = pair[1];
            if (unhappy(a, b, orders, pairs)) {
                res++;
            }
            if (unhappy(b, a, orders, pairs)) {
                res++;
            }
        }
        return res;
    }

    private boolean unhappy(int x, int y, Map<Integer, Map<Integer, Integer>> orders,
                            int[][] pairs) {
        for (int[] pair : pairs) {
            int u = pair[0];
            int v = pair[1];
            if (x == pair[0] || x == pair[1]) { continue; }
            if (prefer(x, u, y, orders) && prefer(u, x, v, orders)) {
                return true;
            }
            if (prefer(x, v, y, orders) && prefer(v, x, u, orders)) {
                return true;
            }
        }
        return false;
    }

    private boolean prefer(int a, int b1, int b2, Map<Integer, Map<Integer, Integer>> orders) {
        return orders.get(b1).get(a) < orders.get(b2).get(a);
    }

    // Hash Table
    // time complexity: O(N^2), space complexity: O(N^2)
    // 19 ms(41.15%), 60.7 MB(5.40%) for 99 tests
    public int unhappyFriends2(int n, int[][] preferences, int[][] pairs) {
        int[] paired = new int[n];
        for (int[] pair : pairs) {
            paired[pair[0]] = pair[1];
            paired[pair[1]] = pair[0];
        }
        int res = 0;
        Map<Integer, Integer>[] orders = new Map[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (orders[i] == null) {
                    orders[i] = new HashMap<>();
                }
                orders[i].put(preferences[i][j], j);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j : preferences[i]) {
                if (orders[j].get(i) < orders[j].get(paired[j]) && orders[i].get(j) < orders[i]
                        .get(paired[i])) {
                    res++;
                    break;
                }
            }
        }
        return res;
    }

    // Hash Table + Set
    // time complexity: O(N^2), space complexity: O(N^2)
    // 8 ms(57.07%), 63.6 MB(5.40%) for 99 tests
    public int unhappyFriends3(int n, int[][] preferences, int[][] pairs) {
        Map<Integer, Set<Integer>> preferMap = new HashMap<>();
        for (int[] pair : pairs) {
            int a = pair[0];
            int b = pair[1];
            preferMap.put(a, preferSet(preferences, a, b));
            preferMap.put(b, preferSet(preferences, b, a));
        }
        int res = 0;
        for (int a = 0; a < n; a++) {
            for (int b : preferMap.get(a)) {
                if (preferMap.get(b).contains(a)) {
                    res++;
                    break;
                }
            }
        }
        return res;
    }

    private Set<Integer> preferSet(int[][] preferences, int a, int b) {
        Set<Integer> res = new HashSet<>();
        for (int c : preferences[a]) {
            if (c == b) { break; }
            res.add(c);
        }
        return res;
    }

    private void test(int n, int[][] preferences, int[][] pairs, int expected) {
        assertEquals(expected, unhappyFriends(n, preferences, pairs));
        assertEquals(expected, unhappyFriends2(n, preferences, pairs));
        assertEquals(expected, unhappyFriends3(n, preferences, pairs));
    }

    @Test public void test() {
        test(4, new int[][] {{1, 2, 3}, {3, 2, 0}, {3, 1, 0}, {1, 2, 0}},
             new int[][] {{0, 1}, {2, 3}}, 2);
        test(2, new int[][] {{1}, {0}}, new int[][] {{1, 0}}, 0);
        test(4, new int[][] {{1, 3, 2}, {2, 3, 0}, {1, 3, 0}, {0, 2, 1}},
             new int[][] {{1, 3}, {0, 2}}, 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
