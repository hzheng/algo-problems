import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC594: https://leetcode.com/problems/longest-harmonious-subsequence/
//
// We define a harmonious array is an array where the difference between its maximum
// value and its minimum value is exactly 1. Given an array, find the length of its
// longest harmonious subsequence among all its possible subsequences.
public class LongestHarmoniousSequence {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 83.33%(71 ms for 201 tests)
    public int findLHS(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        int max = 0;
        for (int num : map.keySet()) {
            int next = map.getOrDefault(num + 1, 0);
            if (next > 0) {
                max = Math.max(max, map.get(num) + next);
            }
        }
        return max;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 59.83%(88 ms for 201 tests)
    public int findLHS2(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for (int num : nums) {
            int count = map.getOrDefault(num, 0) + 1;
            map.put(num, count);
            int next = map.getOrDefault(num + 1, 0);
            if (next > 0) {
                max = Math.max(max, count + next);
            }
            int prev = map.getOrDefault(num - 1, 0);
            if (prev > 0) {
                max = Math.max(max, count + prev);
            }
        }
        return max;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 100.00%(44 ms for 201 tests)
    public int findLHS3(int[] nums) {
        Arrays.sort(nums);
        int max = 0;
        for (int i = 1, count1 = 1, count2 = 0; i < nums.length; i++) {
            int diff = nums[i] - nums[i - 1];
            if (diff > 1) {
                count1 = 1;
                count2 = 0;
            } else if (diff == 0 && count2 == 0) {
                count1++;
            } else {
                if (diff == 1 && count2 > 0) {
                    count1 = count2;
                    count2 = 0;
                }
                max = Math.max(max, count1 + (++count2));
            }
        }
        return max;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 94.24%(50 ms for 201 tests)
    public int findLHS4(int[] nums) {
        Arrays.sort(nums);
        int max = 0;
        for (int i = 1, start1 = 0, start2 = 1; i < nums.length; i++) {
            if (nums[i] - nums[start1] > 1) {
                start1 = start2++;
                i--;
            } else if (nums[i] - nums[start1] == 1) {
                max = Math.max(max, i - start1 + 1);
                if (nums[i] != nums[i - 1]) {
                    start2 = i;
                }
            }
        }
        return max;
    }

    // Brute Force
    // time complexity: O(N ^ 2), space complexity: O(1)
    // Time Limit Exceeded
    public int findLHS5(int[] nums) {
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            int count = 0;
            boolean flag = false;
            for (int j = 0; j < nums.length; j++) {
                if (nums[j] == nums[i]) {
                    count++;
                } else if (nums[j] + 1 == nums[i]) {
                    count++;
                    flag = true;
                }
            }
            if (flag) {
                max = Math.max(count, max);
            }
        }
        return max;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, findLHS(nums.clone()));
        assertEquals(expected, findLHS2(nums.clone()));
        assertEquals(expected, findLHS3(nums.clone()));
        assertEquals(expected, findLHS4(nums.clone()));
        assertEquals(expected, findLHS5(nums.clone()));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 2, 1}, 4);
        test(new int[] {1, 3, 2, 2, 5, 2, 3, 7}, 5);
        test(new int[] {1, 1, 3, 3, 2, 2, 5, 2, 3, 7}, 6);
        test(new int[] {1, 1, 1}, 0);
        test(new int[] {1, 1, 2}, 3);
        test(new int[] {1}, 0);
        test(new int[] { 15949, -56289, -41595, 10954, 24834, -64191, -84385, 53024, 41655, -95488,
                         -50184, 46287, -36887, -56048, -99189, -3631, -59654, 72889, -82227, -57795,
                         71209, 22093, 42798, 16344, -72296, -28959, 83629, 81546, -36953, 71344, 56186,
                         -68753, 13024, 50278, 52896, 81452, -27968, -37735, 50879, 68912, 97506, -54604,
                         -76170, -64191, 77171, -12635, -12852, 52469, 18389, 17243, -33061, 63197, 13838,
                         367, 49788, -98802, 65533, -2888, -87919, 71292, 17859, 37045, 94129, 3514, 35361,
                         65557, -37461, -57064, 18567, 70447, 33977, 89781, 17431, -11077, 75892, 13637,
                         -12206, -3672, 4525, -85936, 25683, -43659, -22527, 37595, 84629, 351, 70867, 6919,
                         -83946, 56772, -47428, -14777, 71555, -63280, 89638, 51863, -88705, -76918, 26463,
                         -34400, 36266, -10441, 94502, 48609, 95372, 97713, 8758, -50912, -92167, -66819,
                         -53850, -39255, 8642, 25911, 8885, 42549, -61347, 45833, -25529, 54770, 87129, 63653,
                         -70460, -24794, 18870, 40010, 34549, 11376, 80355, 69872, 70550, 77550, 30580, 3910,
                         94946, 66056, 52781, 53132, 29986, 5797, 27324, 36539, 33021, 58595, 22812, -94138,
                         -73855, 19117, 64077, 12285, 64427, 90994, 95331, 6237, -39471, 78814, 63665, -80308,
                         89126, 41034, -52103, 62980, 29656, 82678, -64012, -73964, 84873, 63121, 28559, 27955,
                         31523, 32526, -89573, -42127, 29493, 13497, -6682, 92212, -96664, 46064, 51314, -8181,
                         -78760, 29947, -25923, 74928, 39332, 17922, 58260, 34044, 11037, 17277, -84723, 10184,
                         94182, 93436, 82379, 55599, 75643, 99231, 48781, -78240, 97991, 75086, 49351, 135,
                         79708, 11517, 46995, -25267, 64370, -15800, 40267, 89282, 90270, 94130, 14506, 36756,
                         -32254, 21779, 96000, 54894, }, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
