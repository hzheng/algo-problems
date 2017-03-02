import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC280: https://leetcode.com/problems/wiggle-sort/
//
// Given an unsorted array nums, reorder it in-place such that
// nums[0] <= nums[1] >= nums[2] <= nums[3]....
public class WiggleSort {
    // Sort
    // time complexity: O(N * log(N))
    // beats 9.28%(6 ms for 126 tests)
    public void wiggleSort(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        for (int i = 1, j = n + (n % 2) - 2; i < j; i += 2, j -= 2) {
            swap(nums, i, j);
        }
    }

    // Sort
    // time complexity: O(N * log(N))
    // beats 9.28%(6 ms for 126 tests)
    public void wiggleSort_2(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        for (int i = 1; i < n - 1; i += 2) {
            swap(nums, i, i + 1);
        }
    }

    // time complexity: O(N)
    // beats 74.98%(1 ms for 126 tests)
    public void wiggleSort2(int[] nums) {
        int n = nums.length;
        for (int i = 1; i < n; i += 2) {
            if (nums[i] < nums[i - 1]) {
                swap(nums, i, i - 1);
            }
            if (i + 1 == n) break;

            if (nums[i] < nums[i + 1]) {
                swap(nums, i, i + 1);
            }
        }
    }

    // time complexity: O(N)
    // beats 21.21%(3 ms for 126 tests)
    public void wiggleSort3(int[] nums) {
        int n = nums.length;
        for (int i = 1; i < n; i++) {
            if ((i % 2 == 1) == (nums[i] < nums[i - 1])) {
                swap(nums, i, i - 1);
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<int[]> sort, String name, int[] nums) {
        nums = nums.clone();
        sort.apply(nums);
        for (int i = 1; i < nums.length; i += 2) {
            String msg = "nums[%d](=%d) should >= nums[%d](=%d)";
            assertTrue(String.format(msg, i, nums[i], i - 1, nums[i - 1]),
                       nums[i] >= nums[i - 1]);
            if (i + 1 == nums.length) break;

            assertTrue(String.format(msg, i, nums[i], i + 1, nums[i + 1]),
                       nums[i] >= nums[i + 1]);
        }
    }

    void test(int[] nums) {
        WiggleSort w = new WiggleSort();
        test(w::wiggleSort, "wiggleSort", nums);
        test(w::wiggleSort_2, "wiggleSort_2", nums);
        test(w::wiggleSort2, "wiggleSort2", nums);
        test(w::wiggleSort3, "wiggleSort3", nums);
    }

    @Test
    public void test1() {
        test(new int[] {3, 5, 2, 1, 6, 4, 7});
        test(new int[] {3, 5, 2, 1, 6, 4});
        test(new int[] {3, 5, 2, 1, 6, 4, 8, 9});
        test(new int[] {3, 5, 2, 1, 6, 4, 8, 9, 10, 12, 12, 14});
        test(new int[] {15, 3, 5, 2, 1, 6, 4, 8, 9, 10, 12, 12, 14});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WiggleSort");
    }
}
