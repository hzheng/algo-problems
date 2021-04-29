import org.junit.Test;

import static org.junit.Assert.*;

// LC1837: https://leetcode.com/problems/sum-of-digits-in-base-k/
//
// Given an integer n (in base 10) and a base k, return the sum of the digits of n after converting
// n from base 10 to base k.
// After converting, each digit should be interpreted as a base 10 number, and the sum should be
// returned in base 10.
// Given a string S, you are allowed to convert it to a palindrome by adding
// characters in front of it. Find and return the shortest palindrome you can
// find by performing this transformation.
//
// Constraints:
// 1 <= n <= 100
// 2 <= k <= 10
public class SumBase {
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100%), 35.4 MB(91.70%) for 65 tests
    public int sumBase(int n, int k) {
        int res = 0;
        for (int x = n; x > 0; x /= k) {
            res += x % k;
        }
        return res;
    }

    private void test(int n, int k, int expected) {
        assertEquals(expected, sumBase(n, k));
    }

    @Test public void test1() {
        test(34, 6, 9);
        test(10, 10, 1);
        test(98, 7, 2);
        test(97, 8, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
