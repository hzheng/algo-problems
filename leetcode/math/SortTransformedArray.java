import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC360: https://leetcode.com/problems/sort-transformed-array
//
// Given a sorted array of integers nums and integer values a, b and c. Apply a function of the form f(x) = ax2 + bx + c to each element x in the array.
// The returned array must be in sorted order.
// Expected time complexity: O(n)
public class SortTransformedArray {
    // Binary Search + Two Pointers
    // beats 44.36%(1 ms for 110 tests)
    public int[] sortTransformedArray(int[] nums, int a, int b, int c) {
        int n = nums.length;
        int[] res = new int[n];
        if (a == 0) {
            for (int i = 0; i < n; i++) {
                res[i] = (b >= 0 ? nums[i] : nums[n - 1 - i]) * b + c;
            }
            return res;
        }
        double pivot = -0.5 * b / a;
        int low = 0;
        for (int high = n - 1; low <= high; ) {
            int mid = (low + high) >>> 1;
            if (nums[mid] > pivot) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        for (int i = low - 1, j = low, k = 0; i >= 0 || j < n; k++) {
            int min = 0;
            if (j >= n || i >= 0 && Math.abs(nums[i] - pivot) < Math.abs(pivot - nums[j])) {
                min = nums[i--];
            } else {
                min = nums[j++];
            }
            res[k] = a * min * min + b * min + c;
        }
        if (a < 0) {
            for (int i = 0, j = n - 1; i < j; i++, j--) {
                int tmp = res[i];
                res[i] = res[j];
                res[j] = tmp;
            }
        }
        return res;
    }

    // Two Pointers
    // beats 13.79%(2 ms for 110 tests)
    public int[] sortTransformedArray2(int[] nums, int a, int b, int c) {
        int n = nums.length;
        int[] res = new int[n];
        for (int i = 0, j = n - 1, k = (a >= 0) ? n - 1 : 0; i <= j; ) {
            int leftVal = eval(nums[i], a, b, c);
            int rightVal = eval(nums[j], a, b, c);
            if (a >= 0) { // max value must be one of two ends
                res[k--] = Math.max(leftVal, rightVal);
            } else {
                res[k++] = Math.min(leftVal, rightVal);
            }
            if ((leftVal > rightVal) ^ (a >= 0)) {
                j--;
            } else {
                i++;
            }
        }
        return res;
    }

    private int eval(int x, int a, int b, int c) {
        return a * x * x + b * x + c;
    }

    void test(int[] nums, int a, int b, int c, int[] expected) {
        assertArrayEquals(expected, sortTransformedArray(nums, a, b, c));
        assertArrayEquals(expected, sortTransformedArray2(nums, a, b, c));
    }

    @Test
    public void test() {
        test(new int[] {-4, -2, 2, 4}, 1, 3, 5, new int[] {3, 9, 15, 33});
        test(new int[] {-4, -2, 2, 4}, -1, 3, 5, new int[] {-23, -5, 1, 7});
        test(new int[] {-99,-98,-94,-93,-93,-93,-88,-83,-77,-77,-77,-74,-74,-72,
                        -71,-70,-67,-64,-63,-62,-61,-58,-56,-54,-54,-53,-51,-51,-50,-48,-46,
                        -45,-42,-41,-35,-31,-22,-22,-20,-20,-16,-16,-12,-11,-8,-3,-2,-1,0,
                        1,3,4,6,9,10,11,11,11,12,13,26,27,29,31,32,34,36,42,48,49,54,56,
                        59,62,62,65,65,69,70,72,73,74,75,82,85,86,91,92,92,97,98},
             13, 22, -16,
             new int[] {-25,-16,-8,19,35,167,280,584,640,1235,1315,1504,1592,
                        1799,1799,1799,2120,2467,2960,2960,4744,4744,5792,5792,9344,
                        10055,11555,11795,13159,14000,15139,15760,17624,20935,21992,
                        23840,25319,26480,28880,30992,31384,32275,32675,32675,35335,
                        36704,36704,39080,39520,41984,42440,46535,47015,48592,50195,
                        51320,51320,51824,56339,56339,56867,62144,63395,63955,65224,
                        65792,68960,69544,69544,70867,72800,74759,75367,75367,75367,
                        87715,89200,95779,98024,98720,109639,110375,110375,110375,
                        112040,112040,112784,122680,124435,125219,126992});
        test(new int[] {-99,-94,-90,-88,-84,-83,-79,-68,-58,-52,-52,-50,-47,
                        -45,-35,-29,-5,-1,9,12,13,25,27,33,36,38,40,46,47,49,57,57,
                        61,63,73,75,79,97,98},
             -2, 44, -56,
             new int[] {-24014,-21864,-20216,-19416,-17864,-17486,-16014,-14952,
                        -14606,-12296,-9336,-9062,-8006,-7752,-7752,-7502,-7256,-6542,
                        -6086,-5222,-4814,-4046,-4046,-4046,-3014,-2702,-2406,-2264,-1496,
                        -1272,-1064,-782,-326,-326,-206,-102,178,178,184});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SortTransformedArray");
    }
}
