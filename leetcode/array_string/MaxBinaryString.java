import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1702: https://leetcode.com/problems/maximum-binary-string-after-change/
//
// You are given a binary string binary consisting of only 0's or 1's. You can apply each of the
// following operations any number of times:
// Operation 1: If the number contains the substring "00", you can replace it with "10".
// For example, "00010" -> "10010"
// Operation 2: If the number contains the substring "10", you can replace it with "01".
// For example, "00010" -> "00001"
// Return the maximum binary string you can obtain after any number of operations. Binary string x
// is greater than binary string y if x's decimal representation is greater than y's.
//
// Constraints:
// 1 <= binary.length <= 10^5
// binary consist of '0' and '1'.
public class MaxBinaryString {
    // Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 58 ms(100.00%), 105.3 MB(100.00%) for 74 tests
    public String maximumBinaryString(String binary) {
        char[] s = binary.toCharArray();
        outer:
        for (int i = 0, n = s.length, first0After01 = -1; i < n; i++) {
            if (s[i] == '1') { continue; }
            if (i == n - 1) { break; }
            if (s[i + 1] == '0') {
                s[i] = '1';
                continue;
            }
            // now we have "01"
            if (first0After01 < 0) {
                first0After01 = i + 2;
            }
            for (; ; first0After01++) {
                if (first0After01 == n) { break outer; }
                if (s[first0After01] == '0') { break; }
            }
            s[i] = '1';
            s[i + 1] = '0';
            s[first0After01] = '1';
        }
        return String.valueOf(s);
    }

    // Greedy
    // time complexity: O(N), space complexity: O(N)
    // 48 ms(100.00%), 105.3 MB(100.00%) for 74 tests
    public String maximumBinaryString2(String binary) {
        int leadingOnes = 0;
        int zeros = 0;
        int n = binary.length();
        for (int i = 0; i < n; i++) { // make all 0's continuous
            if (binary.charAt(i) == '0') {
                zeros++;
            } else if (zeros == 0) {
                leadingOnes++;
            }
        }
        String allOnes = "1".repeat(n);
        if (leadingOnes == n) { return allOnes; }

        StringBuilder res = new StringBuilder(allOnes);
        res.setCharAt(leadingOnes + zeros - 1, '0');
        return res.toString();
    }

    private void test(String binary, String expected) {
        assertEquals(expected, maximumBinaryString(binary));
        assertEquals(expected, maximumBinaryString2(binary));
    }

    @Test public void test() {
        test("000110", "111011");
        test("01", "01");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
