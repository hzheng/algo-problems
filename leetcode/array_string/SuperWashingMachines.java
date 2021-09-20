import org.junit.Test;

import static org.junit.Assert.*;

// LC517: https://leetcode.com/problems/super-washing-machines/
//
// You have n super washing machines on a line. Initially, each washing machine
// has some dresses or is empty. For each move, you could choose any m (1 ≤ m ≤ n)
// washing machines, and pass one dress of each washing machine to one of its
// adjacent washing machines at the same time .
// Given an array representing the number of dresses in each washing machine from
// left to right on the line, you should find the minimum number of moves to make
// all the washing machines have the same number of dresses. If it is not possible
// to do it, return -1.
// Note:
// The range of n is [1, 10000].
// The range of dresses number in a super washing machine is [0, 1e5].
public class SuperWashingMachines {
    // time complexity: O(N), space complexity: O(N)
    // beats 66.36%(13 ms for 120 tests)
    public int findMinMoves(int[] machines) {
        int n = machines.length;
        int sum = 0;
        for (int machine : machines) {
            sum += machine;
        }
        if (sum % n != 0) {return -1;}

        int mean = sum / n;
        int[] count = new int[n];
        int res = 0;
        for (int i = 0; i < n - 1; i++) {
            int diff = machines[i] - mean;
            if (diff > 0) {
                res = Math.max(res, count[i] += diff);
            } else if (diff < 0) {
                res = Math.max(res, count[i + 1] = -diff);
            }
            machines[i + 1] += diff;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 66.36%(13 ms for 120 tests)
    public int findMinMoves2(int[] machines) {
        int n = machines.length;
        int sum = 0;
        for (int machine : machines) {
            sum += machine;
        }
        if (sum % n != 0) {return -1;}

        int mean = sum / n;
        int res = 0;
        int totalDiff = 0;
        for (int machine : machines) {
            totalDiff += machine - mean;
            res = Math.max(Math.max(res, Math.abs(totalDiff)), machine - mean);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 22.43%(18 ms for 120 tests)
    public int findMinMoves3(int[] machines) {
        int n = machines.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + machines[i];
        }
        if (sum[n] % n != 0) {return -1;}

        int mean = sum[n] / n;
        int res = 0;
        for (int i = 0; i < n; i++) {
            int left = i * mean - sum[i];
            int right = (n - i - 1) * mean - (sum[n] - sum[i] - machines[i]);
            if (left > 0 && right > 0) {
                res = Math.max(res, left + right);
            } else {
                res = Math.max(res, Math.max(Math.abs(left), Math.abs(right)));
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 47.66%(14 ms for 120 tests)
    public int findMinMoves3_2(int[] machines) {
        int n = machines.length;
        int sum = 0;
        for (int machine : machines) {
            sum += machine;
        }
        if (sum % n != 0) {return -1;}

        int mean = sum / n;
        int res = 0;
        int left = 0;
        for (int machine : machines) {
            int right = machine - mean - left;
            if (left > 0 && right > 0) {
                res = Math.max(res, left + right); //=Math.max(res, machine - mean);
            } else {
                res = Math.max(res, Math.max(Math.abs(left), Math.abs(right)));
            }
            left += mean - machine;
        }
        return res;
    }

    void test(int[] machines, int expected) {
        assertEquals(expected, findMinMoves(machines.clone()));
        assertEquals(expected, findMinMoves2(machines));
        assertEquals(expected, findMinMoves3(machines));
        assertEquals(expected, findMinMoves3_2(machines));
    }

    @Test public void test() {
        test(new int[] {1, 1}, 0);
        test(new int[] {1, 0, 5}, 3);
        test(new int[] {0, 3, 0}, 2);
        test(new int[] {0, 2, 0}, -1);
        test(new int[] {4, 0, 0, 4}, 2);
        test(new int[] {0, 0, 5, 0, 0}, 4);
        test(new int[] {0, 0, 5, 0, 0, 0, 5, 0, 0, 0}, 4);
        test(new int[] {0, 0, 10, 0, 0, 0, 10, 0, 0, 0}, 8);
        test(new int[] {1000, 0, 1000, 0, 1000, 0, 1000, 0, 1000, 0, 1000, 0}, 500);
        test(new int[] {100000, 0, 100000, 0, 100000, 0, 100000, 0, 100000, 0, 100000, 0}, 50000);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
