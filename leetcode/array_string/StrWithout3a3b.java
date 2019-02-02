import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC984: https://leetcode.com/problems/string-without-aaa-or-bbb/
//
// Given two integers A and B, return any string S such that:
// S contains exactly A 'a' letters, and exactly B 'b' letters;
// The substring 'aaa' does not occur in S;
// The substring 'bbb' does not occur in S.
// Note:
// 0 <= A <= 100
// 0 <= B <= 100
// It is guaranteed such an S exists for the given A and B.
public class StrWithout3a3b {
    // 4 ms(99.79%), 26.3 MB(100.00%) for 103 tests
    public String strWithout3a3b(int A, int B) {
        StringBuilder sb = new StringBuilder(A + B);
        int a = Math.max(A, B);
        int b = Math.min(A, B);
        for (String repeat = (A > B) ? "aab" : "bba"; a > b && a > 0 && b > 0; a -= 2, b--) {
            sb.append(repeat);
        }
        for (String repeat = (A > B) ? "ab" : "ba"; b > 0; a--, b--) {
            sb.append(repeat);
        }
        for (String repeat = (A > B) ? "a" : "b"; a > 0; a--) {
            sb.append(repeat);
        }
        return sb.toString();
    }

    // 4 ms(99.79%), 26.3 MB(100.00%) for 103 tests
    public String strWithout3a3b2(int A, int B) {
        StringBuilder sb = new StringBuilder(A + B);
        for (int a = A, b = B; a > 0 || b > 0;) {
            boolean addA = false;
            int len = sb.length();
            if (len > 1 && sb.charAt(len - 1) == sb.charAt(len - 2)) {
                addA |= (sb.charAt(len - 1) == 'b');
            } else {
                addA |= (a >= b);
            }
            if (addA) {
                a--;
                sb.append('a');
            } else {
                b--;
                sb.append('b');
            }
        }
        return sb.toString();
    }

    // 4 ms(99.79%), 26.4 MB(100.00%) for 103 tests
    public String strWithout3a3b3(int A, int B) {
        StringBuilder sb = new StringBuilder(A + B);
        char a = 'a';
        char b = 'b';
        int i = A;
        int j = B;
        if (B > A) {
            a = 'b';
            b = 'a';
            i = B;
            j = A;
        }
        for (; i > 0; i--, j--) {
            sb.append(a);
            if (i > j + 1) {
                sb.append(a);
                i--;
            }
            if (j > 0) {
                sb.append(b);
            }
        }
        return sb.toString();
    }

    // Recursion
    public String strWithout3a3b4(int A, int B) {
        if (A == 0 || B == 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = A + B; i > 0; i--) {
                sb.append((A == 0) ? 'b' : 'a');
            }
            return sb.toString();
        }
        if (A > B) return "aab" + strWithout3a3b4(A - 2, B - 1);
        if (A < B) return "bba" + strWithout3a3b4(A - 1, B - 2);
        return "ab" + strWithout3a3b4(A - 1, B - 1);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(int A, int B, Function<Integer, Integer, String> f) {
        String res = f.apply(A, B);
        assertEquals(A + B, res.length());
        assertEquals(A, res.chars().filter(c -> c == 'a').count());
        assertEquals(B, res.chars().filter(c -> c == 'b').count());
        assertEquals("contains \"aaa\"", -1, res.indexOf("aaa"));
        assertEquals("contains \"bbb\"", -1, res.indexOf("bbb"));
    }

    void test(int A, int B) {
        StrWithout3a3b s = new StrWithout3a3b();
        test(A, B, s::strWithout3a3b);
        test(A, B, s::strWithout3a3b2);
        test(A, B, s::strWithout3a3b3);
        test(A, B, s::strWithout3a3b4);
    }

    @Test
    public void test() {
        test(1, 3);
        test(3, 5);
        test(1, 1);
        test(1, 2);
        test(4, 1);
        test(4, 4);
        test(4, 10);
        test(6, 2);
        test(10, 13);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
