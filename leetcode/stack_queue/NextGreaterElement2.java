import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC503: https://leetcode.com/problems/next-greater-element-ii/
//
// Given a circular array (the next element of the last element is the first
// element of the array), print the Next Greater Number for every element. The
// Next Greater Number of a number x is the first greater number to its
// traversing-order next in the array, which means you could search circularly
// to find its next greater number. If it doesn't exist, output -1 for this number.
public class NextGreaterElement2 {
    // brute-force
    // time complexity: O(N ^ 2)
    // beats 41.35%(212 ms for 224 tests)
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = -1;
            for (int j = i + 1, k = nums[i]; j < i + n; j++) {
                int index = j % n;
                if (nums[index] > k) {
                    res[i] = nums[index];
                    break;
                }
            }
        }
        return res;
    }

    // brute-force
    // time complexity: O(N ^ 2)
    // beats 9.09%(535 ms for 224 tests)
    public int[] nextGreaterElements2(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            for (int k = nums[i]; j != i && nums[j] <= k; j = (j + 1) % n) {}
            res[i] = (j == i) ? -1 : nums[j];
        }
        return res;
    }

    // Stack
    // time complexity: O(N)
    // beats 61.00%(67 ms for 224 tests)
    public int[] nextGreaterElements3(int[] nums) {
        int n = nums.length;
        Stack<Integer> stack = new Stack<>();
        for (int i = n - 1; i >= 0; i--) {
            stack.push(i);
        }
        int[] res = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
                stack.pop();
            }
            res[i] = stack.isEmpty() ? -1 : nums[stack.peek()];
            stack.push(i);
        }
        return res;
    }

    // Stack
    // time complexity: O(N)
    // beats 70.97%(52 ms for 224 tests)
    public int[] nextGreaterElements4(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        Arrays.fill(res, -1);
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n * 2; i++) {
            int num = nums[i % n];
            while (!stack.isEmpty() && nums[stack.peek()] < num) {
                res[stack.pop()] = num;
            }
            if (i < n) {
                stack.push(i);
            }
        }
        return res;
    }

    void test(int[] nums, int ... expected) {
        assertArrayEquals(expected, nextGreaterElements(nums));
        assertArrayEquals(expected, nextGreaterElements2(nums));
        assertArrayEquals(expected, nextGreaterElements3(nums));
        assertArrayEquals(expected, nextGreaterElements4(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 1}, 2, -1, 2);
        test(new int[] {1, 2, 1, 5, 4, 3}, 2, 5, 5, -1, 5, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NextGreaterElement2");
    }
}
