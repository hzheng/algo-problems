import org.junit.Test;

import static org.junit.Assert.*;

// LC1234: https://leetcode.com/problems/replace-the-substring-for-balanced-string/
//
// You are given a string containing only 4 kinds of characters 'Q', 'W', 'E' and 'R'.
// A string is said to be balanced if each of its characters appears n/4 times where n is the length
// of the string. Return the minimum length of the substring that can be replaced with any other
// string of the same length to make the original string s balanced.
// Return 0 if the string is already balanced.
//
// Constraints:
// 1 <= s.length <= 10^5
// s.length is a multiple of 4
// s contains only 'Q', 'W', 'E' and 'R'.
public class BalancedString {
    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 7 ms(73.00%), 39.1 MB(74.67%) for 40 tests
    public int balancedString(String s) {
        int[] count = new int[4];
        int[] index = new int['W' + 1];
        index['W'] = 1;
        index['E'] = 2;
        index['R'] = 3;
        final int n = s.length();
        final int limit = s.length() / 4;
        int i = 0;
        for (; i < n; i++) {
            int k = index[s.charAt(i)];
            if (++count[k] > limit) {
                count[k]--;
                break;
            }
        }
        int res = n - (i--);
        for (int j = n - 1; j > i; ) {
            int k = index[s.charAt(j)];
            if (++count[k] <= limit) {
                res = Math.min(res, --j - i);
            } else if (i >= 0) {
                count[k]--;
                count[index[s.charAt(i--)]]--;
            } else { break; }
        }
        return res;
    }

    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 6 ms(93.33%), 38.9 MB(90.00%) for 40 tests
    public int balancedString2(String s) {
        int[] count = new int['W' + 1];
        int n = s.length();
        int res = n;
        for (int j = 0; j < n; j++) {
            count[s.charAt(j)]++;
        }
        for (int i = 0, j = 0, limit = n / 4; j < n; j++) {
            for (count[s.charAt(j)]--;
                 i < n && count['Q'] <= limit && count['W'] <= limit && count['E'] <= limit
                 && count['R'] <= limit; i++) {
                res = Math.min(res, j - i + 1);
                count[s.charAt(i)]++;
            }
        }
        return res;
    }

    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 6 ms(93.33%), 38.8 MB(90.00%) for 40 tests
    public int balancedString3(String s) {
        int[] count = new int['W' + 1];
        int n = s.length();
        int res = n;
        for (int j = 0; j < n; j++) {
            count[s.charAt(j)]++;
        }
        for (int i = 0, j = 0, limit = n / 4; i < n; ) {
            if (count['Q'] <= limit && count['W'] <= limit && count['E'] <= limit
                && count['R'] <= limit) {
                res = Math.min(res, j - i);
                count[s.charAt(i++)]++;
            } else if (j < n) {
                count[s.charAt(j++)]--;
            } else { break; }
        }
        return res;
    }

    private void test(String s, int expected) {
        assertEquals(expected, balancedString(s));
        assertEquals(expected, balancedString2(s));
        assertEquals(expected, balancedString3(s));
    }

    @Test public void test() {
        test("WQWRQQQW", 3);
        test("QWER", 0);
        test("QQWE", 1);
        test("QQQW", 2);
        test("QQQQ", 3);
        test("RQRERREWEEWWQWRRRWQQEQQQ", 2);
        test("WWWEQRQEWWQQQWQQQWEWEEWRRRRRWWQE", 5);
        test("WWEQERQWQWWRWWERQWEQ", 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
