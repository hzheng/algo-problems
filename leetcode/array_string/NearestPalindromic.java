import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC564: https://leetcode.com/problems/find-the-closest-palindrome/
//
// Given an integer n, find the closest integer (not including itself), which is
// a palindrome. The 'closest' means absolute difference minimized between two integers.
// Note:
// The input n is a positive integer represented by string, whose length will not exceed 18.
// If there is a tie, return the smaller one as answer.
public class NearestPalindromic {
    // beats 83.33%(20 ms for 212 tests)
    public String nearestPalindromic(String n) {
        char[] s = n.toCharArray();
        int len = s.length;
        for (int i = 0, j = len - 1; i < j; i++, j--) {
            s[j] = s[i];
        }
        String res = String.valueOf(s);
        long val = Long.valueOf(n);
        s[len / 2] = ++s[(len - 1) / 2];
        res = choose(val, n, res, String.valueOf(s));
        s[len / 2] = (s[(len - 1) / 2] -= 2);
        res = choose(val, n, res, String.valueOf(s));
        char first = n.charAt(0);
        if (first == '9') {
            Arrays.fill(s, '0');
            s[len - 1] = '1';
            return choose(val, n, res, "1" + String.valueOf(s));
        }
        if (first == '1' && len > 1) {
            Arrays.fill(s, '9');
            return choose(val, n, res, String.valueOf(s).substring(1));
        }
        return res;
    }

    private String choose(long val, String n, String n1, String n2) {
        long val2 = 0;
        try {
            val2 = Long.valueOf(n2);
        } catch (Exception e) { return n1; }
        if (n1.equals(n)) return n2;

        long val1 = Long.valueOf(n1);
        long diff1 = Math.abs(val1 - val);
        long diff2 = Math.abs(val2 - val);
        if (diff1 < diff2) return n1;
        if (diff1 > diff2) return n2;
        return val1 <= val2 ? n1 : n2;
    }

    // beats 77.78%(22 ms for 212 tests)
    public String nearestPalindromic2(String n) {
        if (n.equals("1")) return "0";

        String cand1 = mirror(n);
        long diff1 = Long.MAX_VALUE;
        diff1 = Math.abs(Long.parseLong(n) - Long.parseLong(cand1));
        if (diff1 == 0) {
            diff1 = Long.MAX_VALUE;
        }

        StringBuilder s = new StringBuilder(n);
        int i = (s.length() - 1) / 2;
        for (; i >= 0 && s.charAt(i) == '0'; i--) {
            s.setCharAt(i, '9');
        }
        if (i == 0 && s.charAt(i) == '1') {
            s.delete(0, 1);
            s.setCharAt((s.length() - 1) / 2, '9');
        } else {
            s.setCharAt(i, (char)(s.charAt(i) - 1));
        }
        String cand2 = mirror(s.toString());
        long diff2 = Math.abs(Long.parseLong(n) - Long.parseLong(cand2));

        s = new StringBuilder(n);
        i = (s.length() - 1) / 2;
        for (; i >= 0 && s.charAt(i) == '9'; i--) {
            s.setCharAt(i, '0');
        }
        if (i < 0) {
            s.insert(0, "1");
        } else {
            s.setCharAt(i, (char)(s.charAt(i) + 1));
        }
        String cand3 = mirror(s.toString());
        long diff3 = Math.abs(Long.parseLong(n) - Long.parseLong(cand3));
        if (diff2 <= diff1 && diff2 <= diff3) return cand2;
        return (diff1 <= diff3 && diff1 <= diff2) ? cand1 : cand3;
    }

    private String mirror(String s) {
        String t = s.substring(0, s.length() / 2);
        return t + (s.length() % 2 == 1 ? s.charAt(s.length() / 2) : "")
               + new StringBuilder(t).reverse().toString();
    }

    void test(String n, String expected) {
        assertEquals(expected, nearestPalindromic(n));
        assertEquals(expected, nearestPalindromic2(n));
    }

    @Test
    public void test() {
        test("9", "8");
        test("1", "0");
        test("10", "9");
        test("99", "101");
        test("88", "77");
        test("11", "9");
        test("121", "111");
        test("123", "121");
        test("1001", "999");
        test("1234", "1221");
        test("12389", "12421");
        test("1283", "1331");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
