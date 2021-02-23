import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC169: https://leetcode.com/problems/majority-element/
//
// Given an array of size n, find the majority element. The majority element is
// the element that appears more than ⌊ n/2 ⌋ times.
public class MajorityElement {
    // Divide & Conquer
    // beats 51.12%(2 ms for 44 tests)
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    public int majorityElement(int[] nums) {
        return majorityElement(nums, 0, nums.length - 1);
    }

    private int majorityElement(int[] nums, int start, int end) {
        if (start == end) { return nums[start]; }

        int mid = (start + end) >>> 1;
        int left = majorityElement(nums, start, mid);
        int right = majorityElement(nums, mid + 1, end);
        if (left == right) { return left; }

        int leftCount = 0;
        for (int i = start; i <= end; i++) {
            if (nums[i] == left) {
                leftCount++;
            }
        }
        return (leftCount * 2 > (end - start)) ? left : right;
    }

    // beats 67.68%(2 ms)
    // time complexity: O(N), space complexity: O(1)
    public int majorityElement2(int[] nums) {
        int major = 0;
        int count = 0;
        int n = nums.length;
        for (int i = 0; i < n; i += 2) {
            int num = nums[i];
            if (i + 1 == n) { return (count == 0) ? num : major; }

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

    // Solution of Choice
    // Moore's voting algorithm
    // beats 51.12%(2 ms for 44 tests)
    // time complexity: O(N), space complexity: O(1)
    public int majorityElement3(int[] nums) {
        int major = 0;
        int count = 0;
        for (int num : nums) {
            if (count == 0) {
                major = num;
                count = 1;
            } else if (major == num) {
                count++;
            } else {
                count--;
            }
        }
        return major;
    }

    // Bit Manipulation
    // beats 26.15%(12 ms for 44 tests)
    // time complexity: O(N), space complexity: O(1)
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

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 36.05%(3 ms for 44 tests)
    public int majorityElement5(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    // Hash Table
    // beats 13.94%(27 ms for 44 tests)
    public int majorityElement6(int[] nums) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int num : nums) {
            int count = counts.getOrDefault(num, 0) + 1;
            if (count > nums.length / 2) { return num; }

            counts.put(num, count);
        }
        return 0;
    }

    // Randomization
    // beats 27.63%(7 ms for 44 tests)
    public int majorityElement7(int[] nums) {
        Random rand = new Random();
        int n = nums.length;
        while (true) {
            int candidate = nums[rand.nextInt(n)];
            int count = 0;
            for (int num : nums) {
                if (num == candidate && ++count > n / 2) { return candidate; }
            }
        }
    }

    void test(int expected, int... nums) {
        assertEquals(expected, majorityElement(nums));
        assertEquals(expected, majorityElement2(nums));
        assertEquals(expected, majorityElement3(nums));
        assertEquals(expected, majorityElement4(nums));
        assertEquals(expected, majorityElement5(nums.clone()));
        assertEquals(expected, majorityElement6(nums));
        assertEquals(expected, majorityElement7(nums));
    }

    @Test public void test1() {
        test(5, 5);
        test(5, 5, 5);
        test(5, 6, 5, 5);
        test(3, 3, 2, 3, 5, 3, 7, 8, 3, 1, 3, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
