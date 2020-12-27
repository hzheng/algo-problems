import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1701: https://leetcode.com/problems/average-waiting-time/
//
// There is a restaurant with a single chef. You are given an array customers, where
// customers[i] = [arrivali, timei]:
// arrivali is the arrival time of the ith customer. They are sorted in non-decreasing order.
// timei is the time needed to prepare the order of the ith customer.
// When a customer arrives, he gives the chef his order, and the chef starts preparing it once he is
// idle. The customer waits till the chef finishes preparing his order. The chef does not prepare
// food for more than one customer at a time. The chef prepares food for customers in the order they
// were given in the input. Return the average waiting time of all customers. Solutions within 10^-5
// from the actual answer are considered accepted.
//
// Constraints:
// 1 <= customers.length <= 10^5
// 1 <= arrivali, timei <= 10^4
// arrivali <= arrivali+1
public class AverageWaitingTime {
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(100.00%), 90.2 MB(100.00%) for 38 tests
    public double averageWaitingTime(int[][] customers) {
        double total = 0;
        int nextStart = 0;
        for (int[] c : customers) {
            int arrival = c[0];
            int time = c[1];
            nextStart = Math.max(nextStart, arrival) + time;
            total += nextStart - arrival;
        }
        return total / customers.length;
    }

    private void test(int[][] customers, double expected) {
        assertEquals(expected, averageWaitingTime(customers), 1E-5);
    }

    @Test public void test() {
        test(new int[][] {{1, 2}, {2, 5}, {4, 3}}, 5.00000);
        test(new int[][] {{5, 2}, {5, 4}, {10, 3}, {20, 1}}, 3.25000);
        test(new int[][] {{1, 496}, {2, 8112}, {6, 1395}, {7, 7068}, {7, 9388}, {8, 905}, {8, 8590},
                          {8, 5521}, {8, 6158}, {10, 8466}, {11, 2337}, {12, 1220}, {12, 3716},
                          {13, 7951}, {14, 6366}, {14, 4162}, {14, 7002}, {15, 2950}, {17, 6518},
                          {17, 7449}, {18, 4937}, {19, 1821}, {20, 8373}, {22, 1358}, {23, 4917},
                          {24, 9426}, {26, 2589}, {27, 7284}, {30, 7260}, {30, 6943}, {31, 4695},
                          {32, 7456}, {32, 3251}, {33, 278}, {34, 9565}, {35, 473}, {36, 4857},
                          {9972, 3107}, {9972, 7989}, {9973, 940}, {9973, 7893}, {9974, 7176},
                          {9975, 9719}, {9977, 4666}, {9977, 3218}, {9977, 2915}, {9977, 150},
                          {9979, 5489}, {9979, 4097}, {9981, 8736}, {9981, 2571}, {9982, 5291},
                          {9982, 2086}, {9983, 7838}, {9984, 8205}, {9984, 9483}, {9985, 6610},
                          {9989, 3346}, {9991, 795}, {9991, 9126}, {9992, 4644}, {9993, 1947},
                          {9993, 3954}, {9994, 2583}, {9995, 2203}, {9996, 123}, {9998, 575},
                          {9998, 5405}, {9999, 209}}, 175196.884058);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
