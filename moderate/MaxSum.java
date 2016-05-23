import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 17.8:
 * Given an array of integers, find the contiguous sequence with the largest sum.
 */
public class MaxSum {
    // beats 13.99%
    public static int findMaxSum(int[] a) {
        int maxSingle = Integer.MIN_VALUE; // in case of all negatives
        int sum = 0;
        int maxSum = 0;
        for (int i = 0; i < a.length; i++) {
            if ((maxSingle < 0) && (a[i] > maxSingle)) {
                maxSingle = a[i];
            }
            sum += a[i];
            if (maxSum < sum) {
                maxSum = sum;
            } else if (sum < 0) { // start a new segment
                sum = 0;
            }
        }
        return maxSingle < 0 ? maxSingle : maxSum;
    }

    // beats 13.99%
    public static int maxSubArray(int[] nums) {
        int maxSum = 0;
        int sum = 0;
        for (int num : nums) {
            sum += num;
            if (maxSum < sum) {
                maxSum = sum;
            } else if (sum < 0) {
                sum = 0;
            }
        }
        if (maxSum == 0) {
            maxSum = Integer.MIN_VALUE;
            for (int num : nums) {
                maxSum = Math.max(num, maxSum);
            }
        }
        return maxSum;
    }

    // from the book
    public static int getMaxSum(int[] a) {
        int maxSum = 0;
        int runningSum = 0;
        for (int i = 0; i < a.length; i++) {
            runningSum += a[i];
            if (maxSum < runningSum) {
                maxSum = runningSum;
            } else if (runningSum < 0) {
                runningSum = 0;
            }
        }
        return maxSum;
    }

    private void test(Function<int[], Integer> maxSum, String name,
                      int expected, int ... a) {
        long t1 = System.nanoTime();
        int n = maxSum.apply(a);
        if (a.length > 100) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
        assertEquals(expected, n);
    }

    private void test(int expected, int ... a) {
        test(MaxSum::findMaxSum, "findMaxSum", expected, a);
        test(MaxSum::maxSubArray, "maxSubArray", expected, a);
        if (expected >= 0) {
            test(MaxSum::getMaxSum, "getMaxSum", expected, a);
        }
    }

    @Test
    public void test1() {
        test(0, -2, -4, 0);
        test(12, -2, -4, 5, 7);
        test(6, -2, 1, -3, 4, -1, 2, 1, -5, 4);
        test(1, -2, 1);
        test(-1, -2, -1);
        test(14, 6, -2, -2, 3, -2, -2, 4, 5, -4, -4, 5, 7, -9);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxSum");
    }
}
