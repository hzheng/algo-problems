import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1585: https://leetcode.com/problems/check-if-string-is-transformable-with-substring-sort-operations/
//
// Given two strings s and t, you want to transform string s into string t using the following
// operation any number of times:
// Choose a non-empty substring in s and sort it in-place so the characters are in ascending order.
// Return true if it is possible to transform string s into string t. Otherwise, return false.
// A substring is a contiguous sequence of characters within a string.
// Constraints:
// s.length == t.length
// 1 <= s.length <= 10^5
// s and t only contain digits from '0' to '9'.
public class TransformableWithSort {
    // Time Limit Exceeded
    // time complexity: O(N^2), space complexity: O(N)
    public boolean isTransformable0(String s, String t) {
        char[] c1 = s.toCharArray();
        char[] c2 = t.toCharArray();
        int n = c1.length;
        for (int i = n - 1, j = n - 1; i >= 0 && j >= 0; j--) {
            if (c1[i] == c2[j]) {
                i--;
                continue;
            }
            if (c1[i] == 0) {
                i--;
                j++;
                continue;
            }
            if (c2[j] == 0) {
                continue;
            }
            if (c1[i] > c2[j]) { return false; }

            boolean found = false;
            for (int k = i - 1; k >= 0; k--) {
                if (c1[k] > c2[j]) { return false; }

                if (c1[k] == c2[j]) {
                    c1[k] = 0;
                    found = true;
                    break;
                }
            }
            if (!found) { return false; }
        }
        return true;
    }

    // List
    // time complexity: O(N), space complexity: O(N)
    // 32 ms(61.53%), 41 MB(5.45%) for 137 tests
    public boolean isTransformable(String s, String t) {
        @SuppressWarnings("unchecked")
        List<Integer>[] pos = new List[10]; // store each digit's positions
        for (int i = 0; i < pos.length; i++) {
            pos[i] = new ArrayList<>();
        }
        for (int i = 0, n = s.length(); i < n; i++) {
            pos[s.charAt(i) - '0'].add(i);
        }
        int[] cur = new int[pos.length]; // store each digit's running index
        for (char c : t.toCharArray()) {
            int digit = c - '0';
            if (cur[digit] >= pos[digit].size()) { return false; }

            for (int smaller = 0; smaller < digit; smaller++) {
                if (cur[smaller] < pos[smaller].size()
                    && pos[smaller].get(cur[smaller]) < pos[digit].get(cur[digit])) {
                    return false;
                }
            }
            cur[digit]++;
        }
        return true;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 35 ms(56.08%), 46.2 MB(5.45%) for 137 tests
    public boolean isTransformable2(String s, String t) {
        @SuppressWarnings("unchecked")
        Stack<Integer>[] pos = new Stack[10];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = new Stack<>();
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            pos[s.charAt(i) - '0'].push(i);
        }
        for (char c : t.toCharArray()) {
            int digit = c - '0';
            if (pos[digit].empty()) { return false; }

            Integer index = pos[digit].pop();
            for (int smaller = 0; smaller < digit; smaller++) {
                if (!pos[smaller].empty() && pos[smaller].peek() < index) { return false; }
            }
        }
        return true;
    }

    private void test(String s, String t, boolean expected) {
        assertEquals(expected, isTransformable0(s, t));
        assertEquals(expected, isTransformable(s, t));
        assertEquals(expected, isTransformable2(s, t));
    }

    @Test public void test() {
        test("84532", "34852", true);
        test("34521", "23415", true);
        test("12345", "12435", false);
        test("1", "2", false);
        test("3142", "3412", false);
        test("9831274388097554408489573478237928974325", "8931274388059754403848957478223739897425",
             true);
        test("9831274388097554408489573478237928974325", "8931274388059754403848957478232739897425",
             true);
        test("9831274388097554408489573478237928974325", "8931274388059754403848957478232739897254",
             false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
