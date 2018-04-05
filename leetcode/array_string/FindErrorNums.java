import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC645: https://leetcode.com/problems/set-mismatch/
//
// The set S originally contains numbers from 1 to n. Due to the data error, one
// of the numbers in the set got duplicated to another number in the set, which
// results in repetition of one number and loss of another number.
// Given an array nums representing the data status of this set after the error.
// Your task is to firstly find the number occurs twice and then find the number
// that is missing. Return them in the form of an array.
public class FindErrorNums {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 41.63%(26 ms for 49 tests)
    public int[] findErrorNums(int[] nums) {
        Arrays.sort(nums);
        int dup = 0;
        int miss = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            int diff = num - ((i > 0) ? nums[i - 1] : 0);
            if (diff > 1) {
                miss = num - 1;
                if (dup > 0) break;
            } else if (diff == 0) {
                dup = num;
                if (miss > 0) break;
            }
        }
        return new int[] {dup, (miss > 0) ? miss : n};
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 42.84%(25 ms for 49 tests)
    public int[] findErrorNums_2(int[] nums) {
        Arrays.sort(nums);
        int dup = 0;
        int miss = 1;
        int n = nums.length;
        for (int i = 1; i < n; i++) {
            if (nums[i] == nums[i - 1]) {
                dup = nums[i];
            } else if (nums[i] > nums[i - 1] + 1) {
                miss = nums[i] - 1;
            }
        }
        return new int[] {dup, (nums[n - 1] == n) ? miss : n};
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // beats 26.21%(26 ms for 49 tests)
    public int[] findErrorNums2(int[] nums) {
        int dup = 0;
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (!set.add(num)) {
                dup = num;
            }
        }
        for (int i = 1;; i++) {
            if (!set.contains(i)) return new int[] {dup, i};
        }
    }

    // Array Set
    // time complexity: O(N), space complexity: O(N)
    // beats 100%(7 ms for 49 tests)
    public int[] findErrorNums3(int[] nums) {
        int dup = 0;
        boolean[] set = new boolean[nums.length + 1];
        for (int num : nums) {
            if (set[num]) {
                dup = num;
            }
            set[num] = true;
        }
        for (int i = 1;; i++) {
            if (!set[i]) return new int[] {dup, i};
        }
    }

    // Array Set
    // time complexity: O(N), space complexity: O(N)
    // beats 97.38%(8 ms for 49 tests)
    public int[] findErrorNums3_2(int[] nums) {
        int dup = 0;
        int miss = 0;
        int[] count = new int[nums.length + 1];
        for (int num : nums) {
            count[num]++;
        }
        for (int i = nums.length; i > 0; i--) {
            if (count[i] == 0) {
                miss = i;
            } else if (count[i] == 2) {
                dup = i;
            }
        }
        return new int[] {dup, miss};
    }

    // Array Set
    // time complexity: O(N), space complexity: O(1)
    // beats 69.96%(11 ms for 49 tests)
    public int[] findErrorNums4(int[] nums) {
        int dup = 0;
        for (int n : nums) {
            if (nums[Math.abs(n) - 1] < 0) {
                dup = Math.abs(n);
            } else {
                nums[Math.abs(n) - 1] *= -1;
            }
        }
        for (int i = 0;; i++) {
            if (nums[i] > 0) return new int[] {dup, i + 1};
        }
    }

    // Bit Manipulation
    // time complexity: O(N), space complexity: O(1)
    // beats 80.84%(10 ms for 49 tests)
    public int[] findErrorNums5(int[] nums) {
        int xor2 = 0; // will hold (duplicate ^ missing)
        int i = 0;
        for (int num : nums) {
            xor2 ^= (++i ^ num);
        }
        int discerner = xor2 & ~(xor2 - 1); // rightmost bit
        int xor1 = 0; // will hold either duplicate or missing
        i = 0;
        for (int num : nums) {
            if ((num & discerner) != 0) {
                xor1 ^= num;
            }
            if ((++i & discerner) != 0) {
                xor1 ^= i;
            }
        }
        for (i--; i >= 0; i--) {
            if (nums[i] == xor1) return new int[] {xor1, xor2 ^ xor1};
        }
        return new int[] {xor2 ^ xor1, xor1};
    }

    // Array Swap
    // time complexity: O(N), space complexity: O(1)
    // beats 88.10%(9 ms for 49 tests)
    public int[] findErrorNums6(int[] nums) {
        // for (int i = nums.length - 1; i >= 0; i--) {
        //     while (nums[i] != nums[nums[i] - 1]) {
        //         swap(nums, i, nums[i] - 1);
        //     }
        // }
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
                i++; // retry
            }
        }
        for (int i = 0; ; i++) {
            if (nums[i] != i + 1) return new int[] {nums[i], i + 1};
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    void test(int[] nums, int[] expected) {
        assertArrayEquals(expected, findErrorNums(nums.clone()));
        assertArrayEquals(expected, findErrorNums_2(nums.clone()));
        assertArrayEquals(expected, findErrorNums2(nums));
        assertArrayEquals(expected, findErrorNums3(nums));
        assertArrayEquals(expected, findErrorNums3_2(nums));
        assertArrayEquals(expected, findErrorNums4(nums.clone()));
        assertArrayEquals(expected, findErrorNums5(nums));
        assertArrayEquals(expected, findErrorNums6(nums));
    }

    @Test
    public void test() {
        test(new int[] {2, 2, 1, 4}, new int[] {2, 3});
        test(new int[] {1, 5, 3, 2, 2, 7, 6, 4, 8, 9}, new int[] {2, 10});
        test(new int[] {1, 3, 3}, new int[] {3, 2});
        test(new int[] {1, 1}, new int[] {1, 2});
        test(new int[] {2, 2}, new int[] {2, 1});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
