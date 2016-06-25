import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/majority-element/
//
// Given an array of size n, find the majority element. The majority element is
// the element that appears more than ⌊ n/2 ⌋ times.
public class MajorityElement {
    // beats 67.68%
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    public int majorityElement(int[] nums) {
        return majorityElement(nums, 0, nums.length - 1);
    }

    private int majorityElement(int[] nums, int start, int end) {
        if (start == end) return nums[start];

        int mid = start + (end - start) / 2;
        int left = majorityElement(nums, start, mid);
        if (mid >= end) return left;

        int right = majorityElement(nums, mid + 1, end);
        if (left == right) return left;

        int leftCount = 0;
        for (int i = start; i <= end; i++) {
            if (nums[i] == left) {
                leftCount++;
            }
        }
        return (leftCount * 2 > (end - start)) ? left : right;
    }

    // beats 67.68%
    // time complexity: O(N), space complexity: O(1)
    public int majorityElement2(int[] nums) {
        int major = 0;
        int count = 0;
        int n = nums.length;
        for (int i = 0; i < n; i += 2) {
            int num = nums[i];
            if (i + 1 == n) return (count == 0) ? num : major;

            if (num == nums[i + 1]) {
                if (count > 0 && major != num) {
                    count -= 2;
                } else {
                    count += 2;
                    major = num;
                }
            }
        }
        return major;
    }

    // Moore's voting algorithm
    // beats 67.68%
    // time complexity: O(N), space complexity: O(1)
    public int majorityElement3(int[] nums) {
        int major = nums[0];
        int count = 1;
        for (int i = nums.length - 1; i > 0; i--) {
            if (major == nums[i]) {
                count++;
            } else {
                count--;
            }
            if (count == 0) {
                major = nums[i];
                count = 1;
            }
        }
        return major;
    }

    // bit manipulation
    // beats 28.89%
    public int majorityElement4(int[] nums) {
        int major = 0;
        int majorCount = nums.length >> 1;
        for (int i = 0, mask = 1; i < 32; i++, mask <<= 1) {
            int bitCount = 0;
            for (int num : nums) {
                if ((num & mask) != 0) {
                    bitCount++;
                }
            }
            if (bitCount > majorCount) {
                major |= mask;
            }
        }
        return major;
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, majorityElement(nums));
        assertEquals(expected, majorityElement2(nums));
        assertEquals(expected, majorityElement3(nums));
        assertEquals(expected, majorityElement4(nums));
    }

    @Test
    public void test1() {
        test(5, 5);
        test(5, 5, 5);
        test(5, 6, 5, 5);
        test(3, 3, 2, 3, 5, 3, 7, 8, 3, 1, 3, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MajorityElement");
    }
}
