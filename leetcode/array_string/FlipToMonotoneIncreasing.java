import org.junit.Test;
import static org.junit.Assert.*;

// LC926: https://leetcode.com/problems/flip-string-to-monotone-increasing/
//
// We are given a string S of '0's and '1's, and we may flip any '0' to a '1' or
// a '1' to a '0'. Return the minimum flips to make S monotone increasing.
public class FlipToMonotoneIncreasing {
    // time complexity: O(N), space complexity: O(N)
    // beats 100.00%(6 ms for 81 tests)
    public int minFlipsMonoIncr(String S) {
        int n = S.length();
        int[] ones = new int[n + 1];
        for (int i = 0; i < n; i++) {
            ones[i + 1] = ones[i] + (S.charAt(i) - '0');
        }
        int res = n;
        for (int i = 0; i <= n; i++) {
            res = Math.min(res, ones[i] + (n - i) - (ones[n] - ones[i]));
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 92.45%(8 ms for 81 tests)
    public int minFlipsMonoIncr2(String S) {
        int flips = 0;
        for (int i = 0, ones = 0, n = S.length(); i < n; i++) {
            if (S.charAt(i) == '1') {
                ones++;
            } else if (ones > 0 && ++flips > ones) {
                flips = ones;
            }
        }
        return flips;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 57.76%(11 ms for 81 tests)
    public int minFlipsMonoIncr3(String S) {
        int minFlip = 0;
        for (int i = 0, ones = 0; i < S.length(); ++i) {
            ones += S.charAt(i) - '0';
            minFlip = Math.min(ones, minFlip + 1 - (S.charAt(i) - '0'));
        }
        return minFlip;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 92.45%(8 ms for 81 tests)
    public int minFlipsMonoIncr4(String S) {
        int endWith0 = 0;
        int endWith1 = 0;
        for (int i = 0, n = S.length(); i < n; i++) {
            int digit = S.charAt(i) - '0';
            endWith1 = Math.min(endWith0, endWith1) + 1 - digit;
            endWith0 += digit;
        }
        return Math.min(endWith0, endWith1);
    }

    void test(String S, int expected) {
        assertEquals(expected, minFlipsMonoIncr(S));
        assertEquals(expected, minFlipsMonoIncr2(S));
        assertEquals(expected, minFlipsMonoIncr3(S));
        assertEquals(expected, minFlipsMonoIncr4(S));
    }

    @Test
    public void test() {
        test("00110", 1);
        test("010110", 2);
        test("00011000", 2);
        test("0101100011", 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
