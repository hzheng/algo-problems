import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC248: https://leetcode.com/problems/strobogrammatic-number-iii/
//
// A strobogrammatic number is a number that looks the same when rotated 180 degrees.
// Write a function to count the total strobogrammatic numbers that exist in the range of low <= num <= high.
public class StrobogrammaticNumber3 {
    private static final char[] PAIR_MAP = {'0', '1', 0, 0, 0, 0, '9', 0, '8', '6'};
    private static final char[] VALID_CHARS = {1, 1, 0, 0, 0, 0, 1, 0, 1, 1};
    private static final int[] GREATER_COUNTS1 = {3, 2, 1, 1, 1, 1, 1, 1, 1, 0};
    private static final int[] GREATER_COUNTS2 = {4, 3, 3, 3, 3, 3, 2, 2, 1, 0};
    private static final int[][] GREATER_COUNTS = {
        {1, 2, 2, 2, 2, 2, 2, 2, 3, 3},
        {0, 1, 2, 2, 2, 2, 2, 2, 2, 3}
    };

    // Recursion
    // beats 93.63%(0 ms for 16 tests)
    public int strobogrammaticInRange(String low, String high) {
        int m = low.length();
        int n = high.length();
        if (n < m || n == m && low.compareTo(high) > 0) return 0;

        int res = 0;
        for (int i = m + 1; i < n; i++) {
            res += strobogrammaticCount(i, false);
        }
        res += strobogrammaticGreaterCount(low);
        res += strobogrammaticCount(n, false) - strobogrammaticGreaterCount(high) + 1;
        for (int i = 0, j = n - 1; i <= j; i++, j--) {
            if (PAIR_MAP[high.charAt(i) - '0'] != high.charAt(j)) {
                res--;
                break;
            }
        }
        return (m == n) ? res - strobogrammaticCount(m, false) : res;
    }

    private int strobogrammaticGreaterCount(String num) {
        int m = num.length();
        if (m == 0) return 1;
        if (m == 1) return GREATER_COUNTS1[num.charAt(0) - '0'];

        int count = 0;
        int i = 0;
        for (; i < m; i++) {
            int bigs = GREATER_COUNTS2[num.charAt(i) - '0'];
            if (bigs > 0) {
                if (m < (i + 1) * 2) return m == i * 2 ? 1 : 0;

                count += bigs * strobogrammaticCount(m - (i + 1) * 2, true);
                break;
            }
        }
        if (i < m && VALID_CHARS[num.charAt(i) - '0'] != 0) { // same prefix digits
            String middle = num.substring(i + 1, m - (i + 1));
            count += strobogrammaticGreaterCount(middle);
            for (int j = i + 1; j < m - (i + 1); j++) { // compare to the max strobogrammatic
                if (j < m / 2) {
                    if (num.charAt(j) != '9') return count;
                } else if (j > m / 2) {
                    if (num.charAt(j) < '6') return count;
                } else {
                    if (num.charAt(j) < ((m % 2 == 0) ? '6' : '8')) return count;
                }
            }
            for (int j = i; j >= 0; j--) { // continue to compare
                int diff = PAIR_MAP[num.charAt(j) - '0'] - num.charAt(m - j - 1);
                if (diff > 0) return count;
                if (diff < 0) return count - 1; // remove the greatest strobogrammatic
            }
        }
        return count;
    }

    private int strobogrammaticCount(int n, boolean inner) {
        if (n == 0) return 1;
        if (n % 2 == 1) return strobogrammaticCount(n - 1, inner) * 3;

        int count = inner ? 5 : 4;
        for (int i = n / 2 - 1; i > 0; i--) {
            count *= 5;
        }
        return count;
    }

    private static final char[][] PAIRS = {{'0', '0'}, {'1', '1'}, {'6', '9'}, {'8', '8'}, {'9', '6'}};

    // Recursion + DFS
    // beats 79.30%(18 ms for 16 tests)
    public int strobogrammaticInRange2(String low, String high) {
        int[] res = new int[1];
        for (int len = low.length(); len <= high.length(); len++) {
            dfs(low, high, 0, len - 1, new char[len], res);
        }
        return res[0];
    }

    private void dfs(String low, String high, int start, int end, char[] buf, int[] res) {
        if (start > end) {
            String s = new String(buf);
            if ((s.length() != low.length() || s.compareTo(low) >= 0) &&
                (s.length() != high.length() || s.compareTo(high) <= 0)) {
                res[0]++;
            }
            return;
        }
        for (char[] pair : PAIRS) {
            buf[start] = pair[0];
            buf[end] = pair[1];
            if ((buf.length == 1 || buf[0] != '0') &&
                (start != end || pair[0] == pair[1])) {
                dfs(low, high, start + 1, end - 1, buf, res);
            }
        }
    }

    // Recursion + DFS(combine the above 2 methods)
    // beats 89.17%(13 ms for 16 tests)
    public int strobogrammaticInRange3(String low, String high) {
        int m = low.length();
        int n = high.length();
        if (n < m)  return 0;

        int[] res = new int[1];
        if (m == n) {
            dfs3(low, high, 0, m - 1, new char[m], res);
        } else {
            dfs3(low, null, 0, m - 1, new char[m], res);
            dfs3(null, high, 0, n - 1, new char[n], res);
        }
        for (int i = m + 1; i < n; i++) {
            res[0] += strobogrammaticCount(i, false);
        }
        return res[0];
    }

    private void dfs3(String low, String high, int start, int end, char[] buf, int[] res) {
        if (start > end) {
            String s = new String(buf);
            if ((low == null || s.compareTo(low) >= 0) &&
                (high == null || s.compareTo(high) <= 0)) {
                res[0]++;
            }
            return;
        }
        for (char[] pair : PAIRS) {
            buf[start] = pair[0];
            buf[end] = pair[1];
            if ((buf.length == 1 || buf[0] != '0') &&
                (start != end || pair[0] == pair[1])) {
                dfs3(low, high, start + 1, end - 1, buf, res);
            }
        }
    }

    // BFS
    // beats 38.54%(69 ms for 16 tests)
    public int strobogrammaticInRange4(String low, String high) {
        int m = low.length();
        int n = high.length();
        if (n < m || n == m && low.compareTo(high) > 0) return 0;

        int count = 0;
        for (List<String> cur = Arrays.asList("", "0", "1", "8"), next; !cur.isEmpty(); cur = next) {
            next = new ArrayList<>();
            for (String num : cur) {
                int len = num.length();
                if (len == n && num.compareTo(high) > 0) continue;

                if (len > m || (len == m && num.compareTo(low) >= 0)) {
                    if (len == 1 || num.charAt(0) != '0') {
                        count++;
                    }
                }
                if (len <= n - 2) {
                    for (char[] pair : PAIRS) {
                        next.add(pair[0] + num + pair[1]);
                    }
                }
            }
        }
        return count;
    }

    // Recursion + DFS
    // beats 93.63%(0 ms for 16 tests)
    public int strobogrammaticInRange5(String low, String high) {
        int m = low.length();
        int n = high.length();
        return (n < m || n == m && low.compareTo(high) > 0) ?
               0 : allStrobogrammatics(high, true) - allStrobogrammatics(low, false);
    }

    private int allStrobogrammatics(String num, boolean inclusive) {
        int m = num.length();
        if (m == 1) return GREATER_COUNTS[inclusive ? 0 : 1][num.charAt(0) - '0'];

        int count = 0;
        for (int i = 1; i < m; i++) {
            count += strobogrammaticCount(i, false);
        }
        return count + strobogrammaticLowerCount(new char[m], num.toCharArray(), 0, m - 1, inclusive);
    }

    private int strobogrammaticLowerCount(char[] buf, char[] upper, int start, int end, boolean inclusive) {
        if (start > end) {
            int res = compare(upper, buf, buf.length - 1);
            return (res > 0 || res == 0 && inclusive) ? 1 : 0;
        }
        int count = 0;
        for (char[] pair: PAIRS) {
            if ((start > 0 || pair[0] != '0') && (start < end || (pair[0] != '6' && pair[0] != '9'))) {
                buf[start] = pair[0];
                buf[end] = pair[1];
                if (compare(buf, upper, start) > 0) break;

                count += strobogrammaticLowerCount(buf, upper, start + 1, end - 1, inclusive);
            }
        }
        return count;
    }

    private int compare(char[] s1, char[] s2, int end) {
        for (int i = 0; i <= end; i++) {
            if (s1[i] > s2[i]) return 1;
            if (s1[i] < s2[i])  return -1;
        }
        return 0;
    }

    void test(String low, String high, int expected) {
        assertEquals(expected, strobogrammaticInRange(low, high));
        assertEquals(expected, strobogrammaticInRange2(low, high));
        assertEquals(expected, strobogrammaticInRange3(low, high));
        assertEquals(expected, strobogrammaticInRange4(low, high));
        assertEquals(expected, strobogrammaticInRange5(low, high));
    }

    @Test
    public void test() {
        test("10", "0", 0);
        test("0", "0", 1);
        test("1", "0", 0);
        test("1", "1", 1);
        test("1", "9", 2);
        test("11", "69", 2);
        test("10", "69", 2);
        test("50", "100", 3);
        test("888", "999", 4);
        test("888", "10000", 24);
        test("789", "10000", 26);
        test("0", "1000", 19);
        test("0", "10000", 39);
        test("10", "10000", 36);
        test("889", "999", 3);
        test("889", "999000", 182);
        test("1001", "11111", 25);
        test("10000", "11111", 5);
        test("1001", "9999", 20);
        test("1000", "9999", 20);
        test("10000", "99999", 60);
        test("1000", "999000", 179);
        test("100000", "999999", 100);
        test("999000", "999999", 1);
        test("100000", "999000", 99);
        test("100000", "999999", 100);
        test("190010", "999999", 80);
        test("960010", "999999", 15);
        test("96600910", "99999999", 65);
        test("16600118", "99999999", 440);
        test("0", "100000000000000", 124999);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrobogrammaticNumber3");
    }
}
