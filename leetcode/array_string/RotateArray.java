import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC189: https://leetcode.com/problems/rotate-array/
//
// Rotate an array of n elements to the right by k steps.
public class RotateArray {
    // beats 13.50%(1 ms)
    // time complexity: O(N), space complexity: O(K)
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return;

        k %= n;
        int[] buffer = new int[k];
        for (int i = n - k; i < n; i++) {
            buffer[i - n + k] = nums[i];
        }
        for (int i = n - k - 1; i >= 0; i--) {
            nums[i + k] = nums[i];
        }
        for (int i = 0; i < k; i++) {
            nums[i] = buffer[i];
        }
    }

    // beats 13.50%(1 ms)
    // time complexity: O(N), space complexity: O(K)
    public void rotate2(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return;

        k %= n;
        if (k <= n / 2) {
            int[] buffer = new int[k];
            for (int i = n - k; i < n; i++) {
                buffer[i - n + k] = nums[i];
            }
            for (int i = n - k - 1; i >= 0; i--) {
                nums[i + k] = nums[i];
            }
            for (int i = 0; i < k; i++) {
                nums[i] = buffer[i];
            }
        } else {
            int[] buffer = new int[n - k];
            for (int i = 0; i < n - k; i++) {
                buffer[i] = nums[i];
            }
            for (int i = n - k; i < n; i++) {
                nums[i - n + k] = nums[i];
            }
            for (int i = k; i < n; i++) {
                nums[i] = buffer[i - k];
            }
        }
    }

    // time complexity: O(N * K), space complexity: O(1)
    // Time Limit Exceeded
    public void rotate3(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return;

        k %= n;
        for (int i = 0; i < k; i++) {
            int last = nums[n - 1];
            for (int j = n - 1; j > 0; j--) {
                nums[j] = nums[j - 1];
            }
            nums[0] = last;
        }
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // beats 3.29%(3 ms)
    public void rotate4(int[] nums, int k) {
        int n = nums.length;
        k %= n;
        reverse(nums, 0, n - k);
        reverse(nums, n - k, n);
        reverse(nums, 0, n);
    }

    private void reverse(int[] nums, int start, int end) {
        for (int i = (start + end) / 2 - 1; i >= start; i--) {
            nums[i] ^= nums[start + end - i - 1];
            nums[start + end - i - 1] ^= nums[i];
            nums[i] ^= nums[start + end - i - 1];
        }
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 12.84%(1 ms for 33 tests)
    public void rotate5(int[] nums, int k) {
        int n = nums.length;
        int cur = nums[0];
        for (int i = 0, next = k % n; i < n; i++, next = (next + k) % n) {
           int tmp = cur;
           cur = nums[next];
           nums[next] = tmp;
           if (k * (i + 1) % n == 0) {
               next = ++next % n;
               cur = nums[next];
           }
        }
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 13.50%(1 ms)
    public void rotate6(int[] nums, int k) {
        int n = nums.length;
        k %= n;
        for (int i = 0, startIndex = 0; i < n; i++) {
            int startNum = nums[startIndex];
            for (int j = startIndex, prev; ; i++, j = prev) {
                prev = (j - k + n) % n;
                if (prev == startIndex) {
                    nums[j] = startNum;
                    startIndex = prev + 1;
                    break;
                }
                nums[j] = nums[prev];
            }
        }
    }

    // Solution of Choice
    // beats 12.84%(1 ms for 33 tests)
    public void rotate7(int[] nums, int k) {
        int n = nums.length;
        int curNum = nums[0];
        for (int i = 0, start = 0, next = k; i < n; i++, next += k) {
            int nextNum = nums[next %= n];
            nums[next] = curNum;
            if (next == start) {
                next = ++start % n;
                curNum = nums[next];
            } else {
                curNum = nextNum;
            }
        }
    }

    @FunctionalInterface
    interface Function<A, B> {
        public void apply(A a, B b);
    }

    void test(Function<int[], Integer> rotate, int k, int[] nums, int... expected) {
        int[] input = nums.clone();
        rotate.apply(input, k);
        assertArrayEquals(expected, input);
    }

    void test(int k, int[] nums, int... expected) {
        RotateArray r = new RotateArray();
        test(r::rotate, k, nums, expected);
        test(r::rotate2, k, nums, expected);
        test(r::rotate3, k, nums, expected);
        test(r::rotate4, k, nums, expected);
        test(r::rotate5, k, nums, expected);
        test(r::rotate6, k, nums, expected);
        test(r::rotate7, k, nums, expected);
    }

    @Test
    public void test1() {
        test(3, new int[]{1, 2, 3, 4, 5, 6, 7}, 5, 6, 7, 1, 2, 3, 4);
        test(6, new int[]{1, 2, 3, 4, 5, 6, 7}, 2, 3, 4, 5, 6, 7, 1);
        test(0, new int[]{1, 2, 3, 4, 5, 6, 7}, 1, 2, 3, 4, 5, 6, 7);
        test(2, new int[]{1, 2}, 1, 2);
        test(3, new int[]{1, 2}, 2, 1);
        test(4, new int[]{1, 2, 3, 4, 5, 6}, 3, 4, 5, 6, 1, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RotateArray");
    }
}
