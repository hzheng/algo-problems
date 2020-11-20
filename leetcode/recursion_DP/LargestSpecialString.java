import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC761: https://leetcode.com/problems/special-binary-string/
//
// Special binary strings are binary strings with the following two properties:
// The number of 0's is equal to the number of 1's.
// Every prefix of the binary string has at least as many 1's as 0's.
// Given a special string S, a move consists of choosing two consecutive, non-empty, special
// substrings of S, and swapping them.
// At the end of any number of moves, what is the lexicographically largest resulting string?
//
// Note:
// S has length at most 50.
// S is guaranteed to be a special binary string as defined above.
public class LargestSpecialString {
    // Recursion + Sort
    // 3 ms(48.29%), 37.6 MB(39.67%) for 86 tests
    public String makeLargestSpecial(String S) {
        List<String> balancedList = new ArrayList<>();
        for (int n = S.length(), i = 0, j = 0, count = 0; j < n; j++) {
            count += (S.charAt(j) == '1') ? 1 : -1;
            if (count == 0) {
                balancedList.add("1" + makeLargestSpecial(S.substring(i + 1, j)) + "0");
                i = j + 1;
            }
        }
        balancedList.sort(Collections.reverseOrder());
        return String.join("", balancedList);
    }

    // Recursion + Sort
    // 1 ms(100.00%), 37.6 MB(39.67%) for 86 tests
    public String makeLargestSpecial2(String S) {
        return largestSpecial(S.toCharArray(), new int[1]);
    }

    private String largestSpecial(char[] s, int[] index) {
        StringBuilder res = new StringBuilder();
        List<String> balancedList = new ArrayList<>();
        while (index[0] < s.length) {
            if (s[index[0]++] == '1') {
                balancedList.add(largestSpecial(s, index));
            } else {
                res.append("1");
                break;
            }
        }
        boolean needWrap = res.length() > 0;
        Collections.sort(balancedList);
        for (int i = balancedList.size() - 1; i >= 0; i--) {
            res.append(balancedList.get(i));
        }
        if (needWrap) {
            res.append('0');
        }
        return res.toString();
    }

    private void test(String S, String expected) {
        assertEquals(expected, makeLargestSpecial(S));
        assertEquals(expected, makeLargestSpecial2(S));
    }

    @Test public void test() {
        test("11011000", "11100100");
        test("101100111000", "111000110010");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
