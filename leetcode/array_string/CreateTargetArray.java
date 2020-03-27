import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1389: https://leetcode.com/problems/create-target-array-in-the-given-order/
//
// Given two arrays of integers nums and index. Your task is to create target array under the following rules:
// Initially target array is empty.
// From left to right read nums[i] and index[i], insert at index index[i] the value nums[i] in target array.
// Repeat the previous step until there are no elements to read in nums and index.
// Return the target array.
//
// It is guaranteed that the insertion operations will be valid.
public class CreateTargetArray {
    // time complexity: O(N ^ 2), space complexity: O(N)
    // 0 ms(100%), 37.9 MB(100%) for 44 tests
    public int[] createTargetArray(int[] nums, int[] index) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            list.add(index[i], nums[i]);
        }
        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    // time complexity: O(N ^ 2), space complexity: O(N)
    // 2 ms(27.54%), 37.9 MB(100%) for 44 tests
    public int[] createTargetArray2(int[] nums, int[] index) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            list.add(index[i], nums[i]);
        }
        return list.stream().mapToInt(i -> i).toArray();
    }

    private void test(int[] nums, int[] index, int[] expected) {
        assertArrayEquals(expected, createTargetArray(nums, index));
        assertArrayEquals(expected, createTargetArray2(nums, index));
    }

    @Test
    public void test() {
        test(new int[]{0, 1, 2, 3, 4}, new int[]{0, 1, 2, 2, 1}, new int[]{0, 4, 1, 3, 2});
        test(new int[]{1, 2, 3, 4, 0}, new int[]{0, 1, 2, 3, 0}, new int[]{0, 1, 2, 3, 4});
        test(new int[]{1}, new int[]{0}, new int[]{1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
