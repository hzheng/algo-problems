import org.junit.Test;

import static org.junit.Assert.*;

// LC1716: https://leetcode.com/problems/calculate-money-in-leetcode-bank/
//
// Hercy wants to save money for his first car. He puts money in the Leetcode bank every day.
// He starts by putting in $1 on Monday, the first day. Every day from Tuesday to Sunday, he will
// put in $1 more than the day before. On every subsequent Monday, he will put in $1 more than the
// previous Monday. Given n, return the total amount of money he will have in the Leetcode bank at
// the end of the nth day.
//
// Constraints:
//
// 1 <= n <= 1000
public class TotalMoney {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 35.7 MB(80.67%) for 106 tests
    public int totalMoney(int n) {
        int res = 0;
        int i = 1;
        for (int weeks = n / 7; i <= weeks; i++) {
            res += (i + 3) * 7;
        }
        return res + (i * 2 + n % 7 - 1) * (n % 7) / 2;
    }

    // time complexity: O(N), space complexity: O(1)
    // 1 ms(65.35%), 35.6 MB(91.49%) for 106 tests
    public int totalMoney2(int n) {
        int res = 0;
        for (int start = 1, days = n; days > 0; start++) {
            for (int day = 0; day < 7 && days-- > 0; day++) {
                res += start + day;
            }
        }
        return res;
    }

    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 36 MB(71.91%) for 106 tests
    public int totalMoney3(int n) {
        int weeks = n / 7;
        int days = n % 7;
        return (49 + 7 * weeks) * weeks / 2 + (2 * weeks + days + 1) * days / 2;
    }

    private void test(int n, int expected) {
        assertEquals(expected, totalMoney(n));
        assertEquals(expected, totalMoney2(n));
        assertEquals(expected, totalMoney3(n));
    }

    @Test public void test() {
        test(4, 10);
        test(10, 37);
        test(20, 96);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
