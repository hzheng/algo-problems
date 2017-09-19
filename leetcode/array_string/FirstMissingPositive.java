import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC041: https://leetcode.com/problems/first-missing-positive/
//
// Given an unsorted integer array, find the first missing positive integer.
// Your algorithm should run in O(n) time and uses constant space.
public class FirstMissingPositive {
    // beats 17.28%(1 ms)
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int num : nums) {
            for (int i = num; i > 0 && i <= n && nums[i - 1] != i; ) {
                int next = nums[i - 1];
                nums[i - 1] = i;
                i = next;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) return i + 1;
        }
        return n + 1;
    }

    // beats 6.87%(2 ms)
    public int firstMissingPositive2(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) return i + 1;
        }
        return n + 1;
    }

    private void swap(int[] nums, int a, int b) {
        int temp = nums[a];
        nums[a] = nums[b];
        nums[b] = temp;
    }

    // Solution of Choice
    // essentially same as the above solution
    // beats 17.28%(1 ms)
    public int firstMissingPositive3(int nums[]) {
        int n = nums.length;
        for (int i = 0; i < n; ) {
            if (nums[i] > 0 && nums[i] <= n && (nums[nums[i] - 1] != nums[i])) {
                swap(nums, i, nums[i] - 1);
            } else {
                i++;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) return i + 1;
        }
        return n + 1;
    }

    void test(Function<int[], Integer> first, int expected, int ... nums) {
        assertEquals(expected, (int)first.apply(nums.clone()));
    }

    void test(int expected, int ... nums) {
        FirstMissingPositive first = new FirstMissingPositive();
        test(first::firstMissingPositive, expected, nums);
        test(first::firstMissingPositive2, expected, nums);
        test(first::firstMissingPositive3, expected, nums);
    }

    @Test
    public void test1() {
        test(3, 1, 2, 0);
        test(2, 3, 4, -1, 1);
        test(4, 1, 2, 3);
        test(1, 2, 2, 3);
        test(4, 5, 1, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FirstMissingPositive");
    }
}
