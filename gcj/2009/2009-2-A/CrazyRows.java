import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/204113/dashboard#s=p0
// Round 2 2009: Problem A - Crazy Rows
//
// You are given an N x N matrix with 0 and 1 values. You can swap any two adjacent rows of the matrix.
// Your goal is to have all the 1 values in the matrix below or on the main diagonal.
// Return the minimum number of row swaps you need to achieve the goal.
// Input
// The first line of input gives the number of cases, T. T test cases follow.
// The first line of each test case has one integer, N. Each of the next N lines
// contains N characters. Each character is either 0 or 1.
// Output
// For each test case, output
// Case #X: K
// where X is the test case number, starting from 1, and K is the minimum number of
// row swaps needed to have all the 1 values in the matrix below or on the main diagonal.
// You are guaranteed that there is a solution for each test case.
// Limits
// 1 ≤ T ≤ 60
// Small dataset
// 1 ≤ N ≤ 8
// Large dataset
// 1 ≤ N ≤ 40
public class CrazyRows {
    // time complexity: O(N ^ 2), space complexity: O(N)
    public static int minSwap(String[] matrix) {
        int n = matrix.length;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = matrix[i].lastIndexOf('1');
        }
        int swaps = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] <= i) continue;

            for (int j = i + 1; i < n; j++) {
                if (nums[j] <= i) {
                    System.arraycopy(nums, i, nums, i + 1, j - i);
                    swaps += j - i;
                    break;
                }
            }
        }
        return swaps;
    }

    // time complexity: O(N ^ 2), space complexity: O(N)
    public static int minSwap2(String[] matrix) {
        int n = matrix.length;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = matrix[i].lastIndexOf('1');
        }
        int swaps = 0;
        for (int i = 0; i < n; i++) {
            int pos = -1;
            for (int j = i; j < n; j++) {
                if (nums[j] <= i) {
                    pos = j;
                    break;
                }
            }
            for (int j = pos; j > i; j--) {
                swap(nums, j, j - 1);
                swaps++;
            }
        }
        return swaps;
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // time complexity: O(N ^ 2), space complexity: O(N)
    public static int minSwap3(String[] matrix) {
        int n = matrix.length;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = matrix[i].lastIndexOf('1');
        }
        int[] designation = new int[n];
        Arrays.fill(designation, -1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (designation[j] < 0 && nums[j] <= i) {
                    designation[j] = i;
                    break;
                }
            }
        }
        int swaps = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (designation[i] > designation[j]) {
                    swaps++;
                }
            }
        }
        return swaps;
    }

    void test(String[] matrix, int expected) {
        assertEquals(expected, minSwap(matrix));
        assertEquals(expected, minSwap2(matrix));
        assertEquals(expected, minSwap3(matrix));
    }

    @Test
    public void test() {
        test(new String[] {"10", "11"}, 0);
        test(new String[] {"001", "100", "010"}, 2);
        test(new String[] {"1110", "1100", "1100", "1000"}, 4);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n", clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }
        try {
            in = new Scanner(new File(args[0]));
            if (args.length > 1) {
                out = new PrintStream(args[1]);
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int n = in.nextInt();
        String[] matrix = new String[n];
        for (int i = 0; i < n; i++) {
            matrix[i] = in.next();
        }
        out.println(minSwap3(matrix));
    }
}
