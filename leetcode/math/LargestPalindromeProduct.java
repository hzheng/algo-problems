import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC479: https://leetcode.com/problems/largest-palindrome-product/
//
// Find the largest palindrome made from the product of two n-digit numbers.
// You should return the largest palindrome mod 1337.
// Note: The range of n is [1,8].
public class LargestPalindromeProduct {
    final static int MOD = 1337;

    // beats 72.41%(485 ms for 8 tests)
    public int largestPalindrome(int n) {
        int max = (int)Math.pow(10, n) - 1;
        for (int a = max; a > max / 10; a--) {
            String firstHalf = String.valueOf(a);
            long cand = Long.valueOf(firstHalf
                                     + new StringBuilder(firstHalf).reverse());
            for (long factor = max; factor * factor >= cand; factor--) {
                if (cand % factor == 0) return (int)(cand % MOD);
            }
        }
        return 9; // for n == 1
    }

    // beats 91.90%(382 ms for 8 tests)
    public int largestPalindrome2(int n) {
        long max = (long)Math.pow(10, n) - 1;
        long min = max / 10;
        for (long a = max; a > min; a--) {
            long left = a;
            long right = 0;
            for (long i = a; i != 0;
                 right = right * 10 + i % 10, i /= 10, left *= 10) {}
            long cand = left + right;  // construct the palindrome
            for (long i = max; i > min; i--) {
                long j = cand / i;
                if (j > i || j <= min) break;
                if (cand % i == 0) return (int)(cand % MOD);
            }
        }
        return 9; // for n == 1
    }

    // Heap
    // https://discuss.leetcode.com/topic/75034/java-solutions-with-two-different-approaches/2
    // beats 93.70%(287 ms for 8 tests)
    public int largestPalindrome3(int n) {
        int max = (int)Math.pow(10, n) - 1;
        int min = max - (int)Math.pow(10, (n + 1) >> 1);
        PriorityQueue<int[]> pq = new PriorityQueue<>(
            max - min, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Long.compare((long)b[0] * b[1], (long)a[0] * a[1]);
            }
        });
        for (int i = max; i > min; i--) {
            int lastDigit = i % 10;
            if (lastDigit == 3 || lastDigit == 7) {
                pq.offer(new int[] {i, i});
            } else if (lastDigit == 1) {
                pq.offer(new int[] {i, i - 2});
            } else if (lastDigit == 9) {
                pq.offer(new int[] {i, i - 8});
            }
        }
        while (!pq.isEmpty()) {
            int[] factors = pq.poll();
            long p = (long)factors[0] * factors[1];
            if (isPalindrome(p)) return (int)(p % MOD);

            if (factors[1] > min) {
                factors[1] -= 10;
                pq.offer(factors);
            }
        }
        return 0;
    }

    private boolean isPalindrome(long n) {
        long m = 0;
        for (long i = n; i > 0; m = m * 10 + i % 10, i /= 10) {}
        return m == n;
    }

    // TODO: even case is easy to get in O(1), how about odd case?

    void test(int n, int expected) {
        assertEquals(expected, largestPalindrome(n));
        assertEquals(expected, largestPalindrome2(n));
        assertEquals(expected, largestPalindrome3(n));
    }

    @Test
    public void test() {
        test(1, 9);
        test(2, 987);
        test(3, 123);
        test(4, 597);
        test(5, 677);
        test(6, 1218);
        test(7, 877);
        test(8, 475);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
