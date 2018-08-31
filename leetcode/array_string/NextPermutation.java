import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC031: https://leetcode.com/problems/next-permutation/
//
// Implement next permutation, which rearranges numbers into the
// lexicographically next greater permutation of numbers.
public class NextPermutation {
    // beats 64.50%(1 ms)
    public void nextPermutation(int[] nums) {
        int len = nums.length;
        int i = len - 1;
        for (; i > 0; i--) {
            if (nums[i] > nums[i - 1]) { // swap
                int j = i - 1;
                // for (++i; i < len && nums[j] < nums[i]; i++) {}
                for (++i; i < len; i++) {
                    if (nums[j] >= nums[i]) break;
                }
                swap(nums, --i, j);
                i = j + 1;
                break;
            }
        }
        // reverse from i to the end
        for (int j = len - 1; i < j; i++, j--) {
            swap(nums, i, j);
        }
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // beats 93.65%(11 ms for 265 tests)
    public void nextPermutation2(int[] nums) {
        int len = nums.length;
        int i = len - 1;
        for (; i > 0 && nums[i - 1] >= nums[i]; i--) {}

        if (i > 0) {
            int j = len - 1;
            for ( ; nums[j] <= nums[i - 1]; j--) {} // or binary search
            swap(nums, i - 1, j);
        }
        // reverse from i to the end
        for (int j = len - 1; j > i; i++, j--) {
            swap(nums, i, j);
        }
    }

    // Binary Search
    // beats 4.82%(22 ms)
    public void nextPermutation3(int[] nums) {
        int len = nums.length;
        int i = len - 2;
        for (; i >= 0 && nums[i] >= nums[i + 1]; i--) {}

        for (int j = len - 1, k = i + 1; j > k; k++, j--) {
            swap(nums, k, j);
        }
        if (i < 0) return;

        int index = Arrays.binarySearch(nums, i + 1, len, nums[i]);
        if (index < 0) {
            index = -index - 1;
        } else {
            while (++index < len && nums[index] == nums[i]) {}
        }
        swap(nums, i, index);
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

    void test(Function<int[]> next, int[] nums, int[] expected) {
        int[] input = nums.clone();
        next.apply(input);
        System.out.println(Arrays.toString(input));
        assertArrayEquals(expected, input);
    }

    void test(int[] nums, int[] expected) {
        NextPermutation n = new NextPermutation();
        test(n::nextPermutation, nums, expected);
        test(n::nextPermutation2, nums, expected);
        test(n::nextPermutation3, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {}, new int[] {});
        test(new int[] {1}, new int[] {1});
        test(new int[] {1, 2, 3}, new int[] {1, 3, 2});
        test(new int[] {3, 2, 1}, new int[] {1, 2, 3});
        test(new int[] {1, 1, 5}, new int[] {1, 5, 1});
        test(new int[] {1, 5, 1}, new int[] {5, 1, 1});
        test(new int[] {1, 3, 2}, new int[] {2, 1, 3});
        test(new int[] {1, 5, 8, 4, 7, 6, 5, 3, 1},
             new int[] {1, 5, 8, 5, 1, 3, 4, 6, 7});
        test(new int[] {2, 1, 2, 2, 2, 2, 2, 1},
             new int[] {2, 2, 1, 1, 2, 2, 2, 2});
        test(new int[] {2, 2, 7, 5, 4, 3, 2, 2, 1},
             new int[] {2, 3, 1, 2, 2, 2, 4, 5, 7});
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
