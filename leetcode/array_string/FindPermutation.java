import org.junit.Test;
import static org.junit.Assert.*;

// LC484: https://leetcode.com/problems/find-permutation/
//
// Given a secret signature consisting of character 'D' and 'I'. 'D' represents
// a decreasing relationship between two numbers, 'I' represents an increasing
// relationship between two numbers. And our secret signature was constructed by
// a special integer array, which contains uniquely all the different number
// from 1 to n (n is the length of the secret signature plus 1).
// Your job is to find the lexicographically smallest permutation of [1, 2, ... n]
// could refer to the given secret signature in the input.
public class FindPermutation {
    // Greedy
    // beats 60.26%(8 ms for 26 tests)
    public int[] findPermutation(String s) {
        final int len = s.length();
        int[] res = new int[len + 1];
        for (int i = 0, j = 0, count = 0, base = 1; i <= len; i++) {
            if (i < len && s.charAt(i) == 'D') {
                count++;
            } else {
                for (int n = base + count; n >= base; n--) {
                    res[j++] = n;
                }
                base += count + 1;
                count = 0;
            }
        }
        return res;
    }

    // Greedy
    // beats 91.86%(6 ms for 26 tests)
    public int[] findPermutation2(String s) {
        final int len = s.length();
        int[] res = new int[len + 1];
        int base = 1;
        for (int i = 0; i <= len; i++) {
            if (i < len && s.charAt(i) != 'D') {
                res[i] = base++;
            } else {
                int oldI = i;
                while (i < len && s.charAt(i) == 'D') {
                    i++;
                }
                for (int k = i; k >= oldI; k--) {
                    res[k] = base++;
                }
            }
        }
        return res;
    }

    // Reverse(two-pass)
    // beats 77.52%(7 ms for 26 tests)
    public int[] findPermutation3(String s) {
        final int len = s.length();
        int[] res = new int[len + 1];
        for (int i = 0; i <= len; i++) {
            res[i] = i + 1;
        }
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == 'D') {
                int j = i;
                for (; i < len && s.charAt(i) == 'D'; i++) {}
                reverse(res, j, i);
            }
        }
        return res;
    }

    // Reverse(two-pass)
    // beats 38.76%(10 ms for 26 tests)
    public int[] findPermutation4(String s) {
        final int len = s.length();
        int[] res = new int[len + 1];
        res[0] = 1;
        int count = 0;
        for (int i = 0; i < len; i++, count++) {
            res[i + 1] = i + 2;
            if (s.charAt(i) == 'I') {
                reverse(res, i - count, i);
                count = -1;
            }
        }
        reverse(res, len - count, len);
        return res;
    }

    private void reverse(int[] nums, int start, int end) {
        for (int i = start, j = end; i < j; i++, j--) {
            nums[i] ^= nums[j];
            nums[j] ^= nums[i];
            nums[i] ^= nums[j];
        }
    }

    void test(String s, int ... expected) {
        assertArrayEquals(expected, findPermutation(s));
        assertArrayEquals(expected, findPermutation2(s));
        assertArrayEquals(expected, findPermutation3(s));
        assertArrayEquals(expected, findPermutation4(s));
    }

    @Test
    public void test() {
        test("I", 1, 2);
        test("DI", 2, 1, 3);
        test("DDDID", 4, 3, 2, 1, 6, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindPermutation");
    }
}
