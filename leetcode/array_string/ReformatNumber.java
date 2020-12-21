import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1694: https://leetcode.com/problems/reformat-phone-number/
//
// You are given a phone number as a string number. number consists of digits, spaces ' ', and/or
// dashes '-'. You would like to reformat the phone number in a certain manner. Firstly, remove all
// spaces and dashes. Then, group the digits from left to right into blocks of length 3 until there
// are 4 or fewer digits. The final digits are then grouped as follows:
// 2 digits: A single block of length 2.
// 3 digits: A single block of length 3.
// 4 digits: Two blocks of length 2 each.
// The blocks are then joined by dashes. Notice that the reformatting process should never produce
// any blocks of length 1 and produce at most two blocks of length 2.
// Return the phone number after formatting.
//
// Constraints:
// 2 <= number.length <= 100
// number consists of digits and the characters '-' and ' '.
// There are at least two digits in number.
public class ReformatNumber {
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(46.76%), 37.8 MB(78.75%) for 108 tests
    public String reformatNumber(String number) {
        String num = number.replaceAll("-", "").replaceAll(" ", "");
        int n = num.length();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i + 4 < n; i += 3) {
            if (i > 0) {
                sb.append("-");
            }
            sb.append(num, i, i + 3);
        }
        if (n > 4) {
            sb.append("-");
        }
        if (n - i == 4) {
            sb.append(num, i, i += 2).append("-");
        }
        sb.append(num, i, n);
        return sb.toString();
    }

    // Regular Expression
    // time complexity: O(N), space complexity: O(N)
    // 7 ms(36.10%), 37.5 MB(83.74%) for 108 tests
    public String reformatNumber2(String number) {
        return number.replaceAll("\\D", "").replaceAll("...?(?=..)", "$0-");
    }

    private void test(String number, String expected) {
        assertEquals(expected, reformatNumber(number));
        assertEquals(expected, reformatNumber2(number));
    }

    @Test public void test() {
        test("1-23-45 6", "123-456");
        test("123 4-567", "123-45-67");
        test("123 4-5678", "123-456-78");
        test("12", "12");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
