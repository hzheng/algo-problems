import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC496: https://leetcode.com/problems/next-greater-element-i/
//
// Given two arrays (without duplicates) nums1 and nums2 where nums1’s elements
// are subset of nums2. Find all the next greater numbers for nums1's elements
// in the corresponding places of nums2.
// The Next Greater Number of a number x in nums1 is the first greater number to
// its right in nums2. If it does not exist, output -1 for this number.
public class NextGreaterElement {
    // Hash Table
    // time complexity: O(N * M)
    // beats 60.89%(9 ms for 17 tests)
    public int[] nextGreaterElement(int[] findNums, int[] nums) {
        int n = findNums.length;
        int[] res = new int[n];
        Map<Integer, Integer> pos = new HashMap<>();
        int m = nums.length;
        for (int i = 0; i < m; i++) {
            pos.put(nums[i], i);
        }
        for (int i = 0; i < n; i++) {
            int k = findNums[i];
            int index = pos.get(k);
            res[i] = -1;
            for (int j = index + 1; j < m; j++) {
                if (nums[j] > k) {
                    res[i] = nums[j];
                    break;
                }
            }
        }
        return res;
    }

    // brute-force
    // time complexity: O(N * M)
    // beats 24.86%(15 ms for 17 tests)
    public int[] nextGreaterElement2(int[] findNums, int[] nums) {
        int n = findNums.length;
        int[] res = new int[n];
        int m = nums.length;
        for (int i = 0; i < n; i++) {
            int k = findNums[i];
            int j = 0;
            for (; j < m && nums[j] != k; j++) {}
            res[i] = -1;
            for (j++; j < m; j++) {
                if (nums[j] > k) {
                    res[i] = nums[j];
                    break;
                }
            }
        }
        return res;
    }

    // Hash Table + Stack
    // time complexity: O(M)
    // beats 60.89%(9 ms for 17 tests)
    public int[] nextGreaterElement3(int[] findNums, int[] nums) {
        ArrayDeque<Integer> stack = new ArrayDeque<>(); // or Stack
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && stack.peek() <= nums[i]) {
                stack.pop();
            }
            map.put(nums[i], stack.isEmpty() ? -1 : stack.peek());
            stack.push(nums[i]);
        }
        int[] res = new int[findNums.length];
        for (int i = 0; i < findNums.length; i++) {
            res[i] = map.get(findNums[i]);
        }
        return res;
    }

    // Hash Table + Stack
    // time complexity: O(M)
    // beats 35.20%(12 ms for 17 tests)
    public int[] nextGreaterElement4(int[] findNums, int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        for (int num : nums) {
            while (!stack.isEmpty() && stack.peek() < num) {
                map.put(stack.pop(), num);
            }
            stack.push(num);
        }
        int[] res = new int[findNums.length];
        for (int i = 0; i < findNums.length; i++) {
            res[i] = map.getOrDefault(findNums[i], -1);
        }
        return res;
    }

    void test(int[] findNums, int[] nums, int ... expected) {
        assertArrayEquals(expected, nextGreaterElement(findNums, nums));
        assertArrayEquals(expected, nextGreaterElement2(findNums, nums));
        assertArrayEquals(expected, nextGreaterElement3(findNums, nums));
        assertArrayEquals(expected, nextGreaterElement4(findNums, nums));
    }

    @Test
    public void test() {
        test(new int[] {4, 1, 2}, new int[] {1, 3, 4, 2}, -1, 3, -1);
        test(new int[] {2, 4}, new int[] {1, 2, 3, 4}, 3, -1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NextGreaterElement");
    }
}
