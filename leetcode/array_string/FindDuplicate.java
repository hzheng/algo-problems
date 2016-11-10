import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// LC287: https://leetcode.com/problems/find-the-duplicate-number/
//
// Given an array nums containing n + 1 integers where each integer is between 1
// and n(inclusive), at least one duplicate number must exist. Assume that there
// is only one duplicate number, find the duplicate one.
// You must not modify the array (assume the array is read only).
// You must use only constant, O(1) extra space.
// Your runtime complexity should be less than O(n ^ 2).
// There is only one duplicate number, but it could be repeated more than once.
public class FindDuplicate {
    // naive method
    // time complexity: O(N ^ 2)
    // beats 3.80% (163 ms)
    public int findDuplicate(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == nums[j]) return nums[i];
            }
        }
        return -1;
    }

    // Bit Manipulation
    // time complexity: O(N)
    // beats 8.09% (19 ms)
    public int findDuplicate2(int[] nums) {
        int[] bitCounts = new int[32];
        for (int n = 1; n < nums.length; n++) {
            for (int i = 0; i < 32; i++) {
                if (((n >> i) & 1) != 0) { // or: if ((n & (1 << i)) != 0) {
                    bitCounts[i]++;
                }
            }
        }
        for (int num : nums) {
            for (int i = 0; i < 32; i++) {
                if (((num >> i) & 1) != 0) {
                    bitCounts[i]--;
                }
            }
        }
        int duplicate = 0;
        for (int i = 0; i < 32; i++) {
            if (bitCounts[i] < 0) {
                duplicate |= (1 << i);
            }
        }
        return duplicate;
    }

    // Bit Manipulation
    // time complexity: O(N)
    // beats 8.75%(14 ms)
    public int findDuplicate3(int[] nums) {
        int duplicate = 0;
        for (int i = 0; i < 32; i++) {
            int mask = 1 << i;
            int bitCounts = 0;
            for (int n = 1; n < nums.length; n++) {
                if ((n & mask) != 0) {
                    bitCounts++;
                }
            }
            for (int num : nums) {
                if ((num & mask) != 0 && (--bitCounts < 0)) {
                    duplicate |= mask;
                    break;
                }
            }
        }
        return duplicate;
    }

    // Bit Manipulation
    // time complexity: O(N)
    // beats 8.22%(12 ms for 53 tests)
    public int findDuplicate3_2(int[] nums) {
        int duplicate = 0;
        for (int bitPos = 0, n = nums.length; bitPos < 32; bitPos++) {
            int mask = (1 << bitPos);
            int count = 0;
            for (int i = 0; i < n; i++) {
                if ((i & mask) > 0) {
                    count++;
                }
                if ((nums[i] & mask) > 0) {
                    count--;
                }
            }
            if (count < 0) {
                duplicate |= mask;
            }
        }
        return duplicate;
    }

    // Binary Search
    // time complexity: O(N * Log(N))
    // Since N < 2 ^ 32, hence Log(N) < 32, it's actually faster than the above
    // beats 46.53%(5 ms)
    public int findDuplicate4(int[] nums) {
        int low = 1;
        int high = nums.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int count = 0;
            for (int n : nums) {
                if (n <= mid) {
                    count++;
                }
            }
            if (count <= mid) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    // Solution of Choice
    // Floyd's Algorithm
    // time complexity: O(N))
    // beats 64.42%(1 ms for 53 tests)
    public int findDuplicate5(int[] nums) {
        int slow = 0;
        int fast = 0;
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);
        for (slow = 0; slow != fast; slow = nums[slow], fast = nums[fast]) {}
        return slow;
    }

    void test(Function<int[], Integer> find, int expected, int ... nums) {
        assertEquals(expected, (int)find.apply(nums));
    }

    void test(int expected, int ... nums) {
        FindDuplicate f = new FindDuplicate();
        test(f::findDuplicate, expected, nums);
        test(f::findDuplicate2, expected, nums);
        test(f::findDuplicate3, expected, nums);
        test(f::findDuplicate3_2, expected, nums);
        test(f::findDuplicate4, expected, nums);
        test(f::findDuplicate5, expected, nums);
    }

    @Test
    public void test1() {
        test(3, 2, 1, 4, 3, 5, 3);
        test(7, 2, 7, 1, 4, 5, 3, 9, 8, 7, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindDuplicate");
    }
}
