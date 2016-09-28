import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC026: https://leetcode.com/problems/remove-duplicates-from-sorted-array/
//
// Given a sorted array, remove the duplicates in place such that each element
// appear only once and return the new length.
// Do not allocate extra space for another array, you must do this in place
// with constant memory.
public class RemoveDup {
    // beats 7.99%(2 ms)
    public int removeDuplicates(int[] nums) {
        int len = nums.length;
        int end = 0;
        for (int cur = 1; end < len; cur++) {
            int val = nums[end];
            for (; cur < len && nums[cur] == val; cur++);
            if (cur >= len) break;

            nums[++end] = nums[cur];
        }
        return end + 1;
    }

    // beats 7.99%(2 ms)
    public int removeDuplicates2(int[] nums) {
        int len = nums.length;
        if (len < 2) return len;

        int val = nums[0];
        int end = 0;
        for (; end < len - 1; end++) {
            if (nums[end + 1] == nums[end]) break;
        }
        for (int cur = end + 1; end < len; cur++) {
            val = nums[end];
            for (; cur < len && nums[cur] == val; cur++);
            if (cur >= len) break;

            nums[++end] = nums[cur];
        }
        return end + 1;
    }

    // old: beats 54.30%(1 ms)
    // beats 29.63%(14 ms)
    public int removeDuplicates3(int[] nums) {
        if (nums.length < 2) return nums.length;

        int end = 0;
        for (int cur = 1; cur < nums.length; cur++) {
            if (nums[cur] != nums[end]) {
                nums[++end] = nums[cur];
            }
        }
        return end + 1;
    }

    // Solution of Choice
    // beats 22.00%(15 ms)
    public int removeDuplicates4(int[] nums) {
        int i = 0;
        for (int n : nums) {
            if (i < 1 || n > nums[i - 1]) {
                nums[i++] = n;
            }
        }
        return i;
    }

    void test(Function<int[], Integer> removeDup, int [] nums, int[] expected) {
        nums = nums.clone();
        int count = removeDup.apply(nums);
        int[] removed = Arrays.copyOf(nums, count);
        assertArrayEquals(expected, removed);
    }

    void test(int[] nums, int[] expected) {
        RemoveDup rm = new RemoveDup();
        test(rm::removeDuplicates, nums, expected);
        test(rm::removeDuplicates2, nums, expected);
        test(rm::removeDuplicates3, nums, expected);
        test(rm::removeDuplicates4, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1}, new int[] {1});
        test(new int[] {1, 1}, new int[] {1});
        test(new int[] {1, 1, 2}, new int[] {1, 2});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9},
             new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
        test(new int[] {1, 2, 2, 4, 6, 6, 8, 8, 9},
             new int[] {1, 2, 4, 6, 8, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveDup");
    }
}
