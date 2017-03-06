import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC532: https://leetcode.com/problems/k-diff-pairs-in-an-array
//
// Given an array of integers and an integer k, you need to find the number of
// unique k-diff pairs in the array. Here a k-diff pair is defined as an integer
// pair (i, j), where i and j are both numbers in the array and their absolute difference is k.
public class KDiffPairs {
    // Sort + Set
    // beats 0%(29 ms for 72 tests)
    public int findPairs(int[] nums, int k) {
        if (k < 0) return 0;

        int count = 0;
        if (k == 0) {
            Arrays.sort(nums);
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] == nums[i - 1]) {
                    count++;
                    while (i + 1 < nums.length && nums[i + 1] == nums[i]) {
                        i++;
                    }
                }
            }
            return count;
        }
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        for (int num : set) {
            if (set.contains(num + k)) {
                count++;
            }
        }
        return count;
    }

    // Hash Table + Set
    // beats 0%(28 ms for 72 tests)
    public int findPairs2(int[] nums, int k) {
        if (k < 0) return 0;

        int count = 0;
        if (k == 0) {
            Map<Integer, Integer> map = new HashMap<>();
            for (int num : nums) {
                map.put(num, map.getOrDefault(num, 0) + 1);
            }
            for (int num : map.keySet()) {
                if (map.get(num) > 1) {
                    count++;
                }
            }
            return count;
        }
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        for (int num : set) {
            if (set.contains(num + k)) {
                count++;
            }
        }
        return count;
    }

    // Hash Table
    // beats 0%(37 ms for 72 tests)
    public int findPairs2_2(int[] nums, int k) {
        if (k < 0) return 0;

        Map<Integer, Integer> map = new HashMap<>();
        int count = 0;
        for (int i : nums) {
            map.put(i, map.getOrDefault(i, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (k == 0) {
                if (entry.getValue() > 1) {
                    count++;
                }
            } else {
                if (map.containsKey(entry.getKey() + k)) {
                    count++;
                }
            }
        }
        return count;
    }

    // Sort + Two Pointers
    // beats 100.00%(16 ms for 72 tests)
    public int findPairs3(int[] nums, int k) {
        if (k < 0) return 0;

        int count = 0;
        Arrays.sort(nums);
        for (int i = 0, j = i + 1, n = nums.length; i < n; ) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                i++;
                continue;
            }
            if ((j = Math.max(i + 1, j)) >= n) break;

            int diff = nums[i] + k - nums[j];
            if (diff == 0) {
                count++;
                i++;
                j++;
            } else if (diff < 0) {
                i++;
            } else {
                j++;
            }
        }
        return count;
    }

    // Sort + Two Pointers
    // beats 0%(27 ms for 72 tests)
    public int findPairs4(int[] nums, int k) {
        int count = 0;
        Arrays.sort(nums);
        for (int i = 0, j = 0, n = nums.length; i < n; i++) {
            for (j = Math.max(j, i + 1); j < n && nums[j] - nums[i] < k; j++) {}
            if (j < n && nums[j] - nums[i] == k) {
                count++;
            }
            for (; i + 1 < n && nums[i] == nums[i + 1]; i++) {}
        }
        return count;
    }

    // Two Sets
    // beats 0%(43 ms for 72 tests)
    public int findPairs5(int[] nums, int k) {
        if (k < 0) return 0;

        Set<Integer> starts = new HashSet<>();
        Set<Integer> distincts = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (distincts.contains(nums[i] - k)) {
                starts.add(nums[i] - k);
            }
            if (distincts.contains(nums[i] + k)) {
                starts.add(nums[i]);
            }
            distincts.add(nums[i]);
        }
        return starts.size();
    }

    // Hash Table
    // beats 0%(41 ms for 72 tests)
    public int findPairs6(int[] nums, int k) {
        if (k < 0) return 0;

        Map<Integer, Integer> map = new HashMap<>();
        int count = 0;
        int n = nums.length;
        for (int i = 0, j = 0; j < 2 * n; j++, i = j % n) {
            map.putIfAbsent(nums[i], i);
            int pair = map.getOrDefault(nums[i] + k, -1);
            if (pair >= 0 && pair != i) {
                count++;
                map.put(nums[i] + k, -1);
            }
        }
        return count;
    }

    void test(int[] nums, int k, int expected) {
        assertEquals(expected, findPairs(nums.clone(), k));
        assertEquals(expected, findPairs2(nums.clone(), k));
        assertEquals(expected, findPairs2_2(nums.clone(), k));
        assertEquals(expected, findPairs3(nums.clone(), k));
        assertEquals(expected, findPairs4(nums.clone(), k));
        assertEquals(expected, findPairs5(nums.clone(), k));
        assertEquals(expected, findPairs6(nums.clone(), k));
    }

    @Test
    public void test() {
        test(new int[] {1, 3, 3, 1, 3, 3}, 0, 2);
        test(new int[] {1, 1, 3, 1, 5, 4}, 0, 1);
        test(new int[] {1, 2, 2, 2, 1}, 1, 1);
        test(new int[] {1, 3, 1, 5, 4}, 0, 1);
        test(new int[] {1, 3, 1, 5, 4}, 0, 1);
        test(new int[] {3, 1, 4, 1, 5}, 2, 2);
        test(new int[] {1, 2, 3, 4, 5}, 1, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KDiffPairs");
    }
}
