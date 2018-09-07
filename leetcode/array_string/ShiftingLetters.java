import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC848: https://leetcode.com/problems/shifting-letters/
//
// We have a string S of lowercase letters, and an integer array shifts.
// Call the shift of a letter, the next letter in the alphabet, (wrapping around
// so that 'z' becomes 'a'). Now for each shifts[i] = x, we want to shift the
// first i+1 letters of S, x times.
// Return the final string after all such shifts to S are applied.
public class ShiftingLetters {
    // Prefix Sum
    // time complexity: O(N), space complexity: O(N)
    // beats 37.50%(10 ms for 46 tests)
    public String shiftingLetters(String S, int[] shifts) {
        int n = S.length();
        char[] res = new char[n];
        long[] s = new long[n];
        s[n - 1] = shifts[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            s[i] = s[i + 1] + shifts[i];
        }
        for (int i = 0; i < n; i++) {
            res[i] = (char) ((S.charAt(i) - 'a' + s[i]) % 26 + 'a');
        }
        return String.valueOf(res);
    }

    // Prefix Sum
    // time complexity: O(N), space complexity: O(1)
    // beats 20.38%(15 ms for 46 tests)
    public String shiftingLetters2(String S, int[] shifts) {
        StringBuilder res = new StringBuilder();
        long totalShift = 0;
        for (int shift : shifts) {
            totalShift += shift;
        }
        for (int i = 0, s = (int)(totalShift % 26); i < S.length(); i++) {
            res.append((char)((S.charAt(i) - 'a' + s) % 26 + 'a'));
            s = Math.floorMod(s - shifts[i], 26);
        }
        return res.toString();
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 90.24%(6 ms for 46 tests)
    public String shiftingLetters3(String S, int[] shifts) {
        char[] cs = S.toCharArray();
        for (int i = cs.length - 1, shift = 0; i >= 0; i--) {
            shift = (shift + shifts[i]) % 26;
            cs[i] = (char)((cs[i] - 'a' + shift) % 26 + 'a');
        }
        return String.valueOf(cs);
    }

    void test(String S, int[] shifts, String expected) {
        assertEquals(expected, shiftingLetters(S, shifts));
        assertEquals(expected, shiftingLetters2(S, shifts));
        assertEquals(expected, shiftingLetters3(S, shifts));
    }

    @Test
    public void test() {
        test("abc", new int[] { 3, 5, 9 }, "rpl");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
