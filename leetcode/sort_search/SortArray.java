import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

// LC912: https://leetcode.com/problems/sort-an-array/
//
// Given an array of integers nums, sort the array in ascending order.
//
// Constraints:
// 1 <= nums.length <= 50000
// -50000 <= nums[i] <= 50000
public class SortArray {
    // Quick Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 4 ms(90.34%), 47.3 MB(10.42%) for 11 tests
    public int[] sortArray(int[] nums) {
        int[] res = nums.clone();
        quickSort(res, 0, nums.length - 1);
        return res;
    }

    private void quickSort(int[] nums, int low, int high) {
        if (low >= high) { return; }

        int pos = partition(nums, low, high);
        quickSort(nums, low, pos - 1);
        quickSort(nums, pos + 1, high);
    }

    private int partition(int[] nums, int low, int high) {
        int pivot = nums[high];
        int i = low;
        for (int j = low; j < high; j++) {
            if (nums[j] < pivot) {
                swap(nums, i++, j);
            }
        }
        swap(nums, i, high);
        return i;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // Merge Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 5 ms(65.74%), 46.8 MB(10.42%) for 11 tests
    public int[] sortArray2(int[] nums) {
        int[] res = nums.clone();
        mergeSort(nums.clone(), res, 0, nums.length);
        return res;
    }

    private void mergeSort(int[] src, int[] dest, int low, int high) {
        if (low + 1 >= high) { return; }

        int mid = (low + high) >>> 1;
        // sort dest into src
        mergeSort(dest, src, low, mid);
        mergeSort(dest, src, mid, high);
        // merge src to dest
        for (int i = low, j = mid, k = low; k < high; k++) {
            if (i < mid && (j >= high || src[i] <= src[j])) {
                dest[k] = src[i++];
            } else {
                dest[k] = src[j++];
            }
        }
    }

    // Heap Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 7 ms(33.63%), 46.9 MB(10.42%) for 11 tests
    public int[] sortArray3(int[] nums) {
        int[] res = nums.clone();
        int n = res.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(res, n, i);
        }
        for (int i = n - 1; i > 0; i--) {
            int tmp = res[0];
            res[0] = res[i];
            res[i] = tmp;
            heapify(res, i, 0);
        }
        return res;
    }

    private void heapify(int nums[], int size, int parent) {
        int maxIndex = parent;
        int left = 2 * parent + 1;
        if (left < size && nums[left] > nums[maxIndex]) {
            maxIndex = left;
        }
        int right = 2 * parent + 2;
        if (right < size && nums[right] > nums[maxIndex]) {
            maxIndex = right;
        }
        if (maxIndex != parent) {
            int tmp = nums[parent];
            nums[parent] = nums[maxIndex];
            nums[maxIndex] = tmp;
            heapify(nums, size, maxIndex);
        }
    }

    private void test(int[] nums) {
        int[] expected = nums.clone();
        java.util.Arrays.sort(expected);
        assertArrayEquals(expected, sortArray(nums));
        assertArrayEquals(expected, sortArray2(nums));
        assertArrayEquals(expected, sortArray3(nums));
    }

    @Test public void test() {
        test(new int[] {5, 2, 3, 1});
        test(new int[] {5, 1, 1, 2, 0, 0});
        test(new int[] {12, 11, 13, 5, 6, 7, 9, 32, 19, 4, 8, 3, 32, 43, 93, 7});
        test(new int[] {15, 21, 9, 78, 3423, 934, 18, 72, 346, 72, 10, 0, 21, 6453, 434});
        test(new int[] {5, 3, 6, 89, 243, 349023, 9312, 8342, 904853, 24, 63, 8342, 1, 1, 2, 0, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
