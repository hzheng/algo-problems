import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 17.8:
 * Given an array of integers, find the contiguous sequence with the largest sum.
 */
public class MaxSum {
    public static int findMaxSum(int[] a) {
        if (a == null || a.length == 0) return 0;

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

    private int test(Function<int[], Integer> maxSum, String name, int[] a) {
        long t1 = System.nanoTime();
        int n = maxSum.apply(a);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return n;
    }

    private void test(int[] a, int expected) {
        assertEquals(expected, test(MaxSum::findMaxSum, "findMaxSum", a));
        if (expected >= 0) {
            assertEquals(expected, test(MaxSum::getMaxSum, "getMaxSum", a));
        }
    }

    @Test
    public void test1() {
        test(new int[] {-2, -4}, -2);
        test(new int[] {-2, -4, 0}, 0);
        test(new int[] {-2, -4, 5, 7}, 12);
        test(new int[] {2, -4, 5, 7}, 12);
        test(new int[] {5, -4, 5, 7}, 13);
        test(new int[] {6, -2, -2, 3, -2, -2, 4, 5, -4, -4, 5, 7, -9}, 14);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxSum");
    }
}
