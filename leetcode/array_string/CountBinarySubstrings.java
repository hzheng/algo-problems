import org.junit.Test;
import static org.junit.Assert.*;

// LC696: https://leetcode.com/problems/count-binary-substrings/description/
//
// Give a string s, count the number of non-empty (contiguous) substrings that
// have the same number of 0's and 1's, and all the 0's and all the 1's in these
// substrings are grouped consecutively. Substrings that occur multiple times
// are counted the number of times they occur.
public class CountBinarySubstrings {
    // time complexity: O(N), space complexity: O(1)
    // beats N/A%(31 ms for 90 tests)
    public int countBinarySubstrings(String s) {
        for (int i = s.length() - 1, cur = 1, prev = 0, res = 0;; i--) {
            if (i == 0) return res + Math.min(cur, prev);

            if (s.charAt(i) == s.charAt(i - 1)) {
                cur++;
            } else {
                res += Math.min(cur, prev);
                prev = cur;
                cur = 1;
            }
        }
    }

    // time complexity: O(N), space complexity: O(1)
    // beats N/A%(33 ms for 90 tests)
    public int countBinarySubstrings2(String s) {
        int res = 0;
        for (int i = s.length() - 1, cur = 1, prev = 0; i > 0; i--) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                cur++;
            } else {
                prev = cur;
                cur = 1;
            }
            if (cur <= prev) {
                res++;
            }
        }
        return res;
    }

    void test(String s, int expected) {
        assertEquals(expected, countBinarySubstrings(s));
        assertEquals(expected, countBinarySubstrings2(s));
    }

    @Test
    public void test() {
        test("0", 0);
        test("00110011", 6);
        test("10101", 4);
        test("1001101110", 6);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
