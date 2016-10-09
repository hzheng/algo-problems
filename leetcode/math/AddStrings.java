import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC415: https://leetcode.com/problems/add-strings
//
// Given two non-negative numbers num1 and num2 represented as string, return
// the sum of num1 and num2.
public class AddStrings {
    // beats N/A(29 ms for 315 tests)
    public String addStrings(String num1, String num2) {
        int len1 = num1.length();
        int len2 = num2.length();
        int carry = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = len1 - 1, j = len2 - 1; i >= 0 || j >= 0; i--, j--) {
            int d1 = (i >= 0) ? num1.charAt(i) - '0' : 0;
            int d2 = (j >= 0) ? num2.charAt(j) - '0' : 0;
            int sum = d1 + d2 + carry;
            sb.append((char)('0' + sum % 10));
            carry = sum / 10;
        }
        if (carry > 0) {
            sb.append((char)('0' + carry));
        }
        return sb.reverse().toString();
    }

    void test(String num1, String num2, String expected) {
        assertEquals(expected, addStrings(num1, num2));
    }

    @Test
    public void test() {
        test("1", "9", "10");
        test("12", "34", "46");
        test("12", "734", "746");
        test("92", "734", "826");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AddStrings");
    }
}
