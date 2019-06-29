import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1095: https://leetcode.com/problems/find-in-mountain-array/
//
// An array A is a mountain array if and only if:
// A.length >= 3
// There exists some i with 0 < i < A.length - 1 such that:
// A[0] < A[1] < ... A[i-1] < A[i]
// A[i] > A[i+1] > ... > A[A.length - 1]
// Given a mountain array, return the minimum index such that mountainArr.get(index) == target.
// If such an index doesn't exist, return -1.
// You may only access the array using a MountainArray interface:
// Submissions making more than 100 calls to MountainArray.get will be judged Wrong Answer.
public class FindInMountainArray {
    static class MountainArray {
        int[] array;

        MountainArray(int[] array) {
            this.array = array;
        }

        public int get(int index) {
            return array[index];
        }

        public int length() {
            return array.length;
        }
    }

    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100%), 33 MB(100%) for 77 tests
    public int findInMountainArray(int target, MountainArray mountainArr) {
        int n = mountainArr.length();
        int peak = getPeak(n, mountainArr);
        int res = find(target, 0, peak, mountainArr, true);
        return (res >= 0) ? res : find(target, peak, n - 1, mountainArr, false);
    }

    private int getPeak(int n, MountainArray mountainArr) {
        int low = 0;
        for (int high = n - 1; low < high; ) {
            int mid = (low + high) >>> 1;
            if (mountainArr.get(mid) > mountainArr.get(mid + 1)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private int find(int target, int start, int end, MountainArray mountainArr, boolean increase) {
        for (int low = start, high = end; low <= high; ) {
            int mid = (low + high) >>> 1;
            int val = mountainArr.get(mid);
            if (val == target) {
                return mid;
            }
            if ((val > target) ^ increase) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    void test(int[] array, int target, int expected) {
        MountainArray mountainArray = new MountainArray(array);
        assertEquals(expected, findInMountainArray(target, mountainArray));
    }

    @Test
    public void test() {
        test(new int[]{1, 2, 3, 4, 5, 3, 1}, 3, 2);
        test(new int[]{0, 1, 2, 4, 2, 1}, 3, -1);
        test(new int[]{1, 2, 3, 4, 5, 3, 1}, 2, 1);
        test(new int[]{1, 2, 3, 4, 5, 3, 1}, 3, 2);
        test(new int[]{1, 5, 2}, 2, 2);
        test(new int[]{1, 5, 2}, 1, 0);
        test(new int[]{1, 2, 5, 1}, 0, -1);
        test(new int[]{1, 2, 3, 4, 5, 3, 1}, 2, 1);
        test(new int[]{3, 5, 3, 2, 0}, 5, 1);
        test(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
                       22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                       41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
                       60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78,
                       79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97,
                       98, 99, 100, 101, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87,
                       86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 76, 75, 74, 73, 72, 71, 70, 69, 68,
                       67, 66, 65, 64, 63, 62, 61, 60, 59, 58, 57, 56, 55, 54, 53, 52, 51, 50, 49,
                       48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30,
                       29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11,
                       10, 9, 8, 7, 6, 5, 4, 3, 2}, 101, 100);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
