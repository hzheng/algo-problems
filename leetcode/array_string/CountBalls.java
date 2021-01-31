import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1742: https://leetcode.com/problems/maximum-number-of-balls-in-a-box/
//
// You are working in a ball factory where you have n balls numbered from lowLimit up to highLimit
// inclusive (i.e., n == highLimit - lowLimit + 1), and an infinite number of boxes numbered from 1
// to infinity. Your job at this factory is to put each ball in the box with a number equal to the
// sum of digits of the ball's number. For example, the ball number 321 will be put in the box
// number 3 + 2 + 1 = 6 and the ball number 10 will be put in the box number 1 + 0 = 1. Given two
// integers lowLimit and highLimit, return the number of balls in the box with the most balls.
//
// Constraints:
// 1 <= lowLimit <= highLimit <= 10^5
public class CountBalls {
    // Hash Table
    // time complexity: O((H-L)*log(MAX)), space complexity: O(H-L)
    // 60 ms(12.50%), 47 MB(12.50%) for 97 tests
    public int countBalls(int lowLimit, int highLimit) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = lowLimit; i <= highLimit; i++) {
            int digitSum = 0;
            for (int k = i; k > 0; digitSum += k % 10, k /= 10) {}
            map.put(digitSum, map.getOrDefault(digitSum, 0) + 1);
        }
        int res = 0;
        for (int v : map.values()) {
            res = Math.max(res, v);
        }
        return res;
    }

    // Hash Table
    // time complexity: O((H-L)*log(MAX)), space complexity: O(log(H))
    // 24 ms(37.50%), 37.8 MB(25.00%) for 97 tests
    public int countBalls2(int lowLimit, int highLimit) {
        int res = 0;
        int[] count = new int[9 * 5 + 1]; // max number is 99999
        for (int i = lowLimit; i <= highLimit; i++) {
            int digitSum = 0;
            for (int k = i; k > 0; digitSum += k % 10, k /= 10) {}
            res = Math.max(res, ++count[digitSum]);
        }
        return res;
    }

    private void test(int lowLimit, int highLimit, int expected) {
        assertEquals(expected, countBalls(lowLimit, highLimit));
        assertEquals(expected, countBalls2(lowLimit, highLimit));
    }

    @Test public void test() {
        test(1, 10, 2);
        test(5, 15, 2);
        test(19, 28, 2);
        test(19, 243, 23);
        test(1379, 99878, 5985);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
