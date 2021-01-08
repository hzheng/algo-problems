import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

// LC1227: https://leetcode.com/problems/airplane-seat-assignment-probability/
//
// n passengers board an airplane with exactly n seats. The first passenger has lost the ticket and
// picks a seat randomly. But after that, the rest of passengers will:
// Take their own seat if it is still available,
// Pick other seats randomly when they find their seat occupied
// What is the probability that the n-th person can get his own seat?
//
// Constraints:
// 1 <= n <= 10^5
public class SeatAssignmentProbability {
    // Recursion
    // Time Limit Exceeded
    public double nthPersonGetsNthSeat(int n) {
        if (n == 1) { return 1; }

        double res = 1d / n; // 1st person takes his own
        for (int i = 2; i < n; i++) { //
            res += nthPersonGetsNthSeat(n - i + 1) / n; // 1st person takes i-th's seat
        }
        return res;
    }

    // Math
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 38.1 MB(16.30%) for 100 tests
    public double nthPersonGetsNthSeat2(int n) {
        // method 1: simplify the above formula: f(n) = 1/n * (f(n-1) + f(n-2) + f(n-3) + ... + f(1))
        // method 2: probablity of everyone except the last doesn't choose the last seat is
        //           (n-1)/n * [(n-2)/(n-1) + 1/(n-1)*(n-2)/(n-1)] * ...
        //          =(n-1)/n * [(n-2)/(n-1)*(n/(n-1))] * ... = 1/2
        // method 3:
        // assume the first person choose between the first and the last seat, all others take their own
        return n == 1 ? 1 : 0.5;
    }

    private void test(int n, double expected) {
        if (n <= 20) {
            assertEquals(expected, nthPersonGetsNthSeat(n), 1E-6);
        }
        assertEquals(expected, nthPersonGetsNthSeat2(n), 1E-6);
    }

    @Test public void test() {
        test(1, 1.00000);
        test(2, 0.50000);
        test(3, 0.50000);
        test(13, 0.50000);
        test(20, 0.50000);
        test(30, 0.50000);
        test(3000, 0.50000);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
