import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given an unsorted integer array, find the first missing positive integer.
// Your algorithm should run in O(n) time and uses constant space.
public class FirstMissingPositive {
    // beats 17.28%
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int j = nums[i];
            while (j > 0 && j <= n && nums[j - 1] != j) {
                int next = nums[j - 1];
                nums[j - 1] = j;
                j = next;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) return i + 1;
        }
        return n + 1;
    }

    // beats 6.87%
    public int firstMissingPositive2(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            // nums[i] != i + 1 condition can be omitted
            while (nums[i] > 0 && nums[i] <= n && nums[i] != i + 1) {
                if (nums[i] == nums[nums[i] - 1]) break;
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

    // essentially same as the above solution
    // beats 17.28%
    public int firstMissingPositive3(int nums[]) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] > 0 && nums[i] <= n && (nums[nums[i] - 1] != nums[i])) {
                swap(nums, i, nums[i] - 1);
                i--; // cancel i++ to repeat
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
