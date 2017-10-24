import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC556: https://leetcode.com/problems/next-greater-element-iii/
//
// Given a positive 32-bit integer n, you need to find the smallest 32-bit integer
// which has exactly the same digits existing in the integer n and is greater in
// value than n. If no such positive 32-bit integer exists, you need to return -1.
public class NextGreaterElement3 {
    // beats 36.52%(4 ms for 34 tests)
    public int nextGreaterElement(int n) {
        char[] s = String.valueOf(n).toCharArray();
        int i = s.length - 2;
        for (; i >= 0 && s[i] >= s[i + 1]; i--) {}
        if (i < 0) return -1;

        int j = s.length - 1;
        for (; s[j] <= s[i]; j--) {}
        swap(s, i, j);
        for (i++, j = s.length - 1; i < j; i++, j--) {
            swap(s, i, j);
        }
        try {
            return Integer.valueOf(String.valueOf(s));
        } catch (Exception e) {
            return -1;
        }
    }

    // Sort
    // beats 79.57%(3 ms for 34 tests)
    public int nextGreaterElement2(int n) {
        char[] s = String.valueOf(n).toCharArray();
        int i = s.length - 2;
        for (; i >= 0 && s[i] >= s[i + 1]; i--) {}
        if (i < 0) return -1;

        int j = s.length - 1;
        for (; s[j] <= s[i]; j--) {}
        swap(s, i, j);
        Arrays.sort(s, i + 1, s.length);
        try {
            return Integer.valueOf(String.valueOf(s));
        } catch (Exception e) {
            return -1;
        }
    }

    private void swap(char[] s, int i, int j) {
        char tmp = s[i];
        s[i] = s[j];
        s[j] = tmp;
    }

    void test(int n, int expected) {
        assertEquals(expected, nextGreaterElement(n));
        assertEquals(expected, nextGreaterElement2(n));
    }

    @Test
    public void test() {
        test(12, 21);
        test(21, -1);
        test(12355421, 12412355);
        test(1999999999, -1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
