import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC719: https://leetcode.com/problems/find-k-th-smallest-pair-distance/
//
// Given an integer array, return the k-th smallest distance among all the 
// pairs. The distance of a pair (A, B) is defined as the absolute difference 
// between A and B.
// Note:
// 2 <= len(nums) <= 10000.
// 0 <= nums[i] < 1000000.
// 1 <= k <= len(nums) * (len(nums) - 1) / 2.
public class SmallestDistancePair {
    // Binary Search
    // time complexity: O((N + log(W)) * log(N)), space complexity: O(N)
    // beats %(99 ms for 19 tests)
    public int smallestDistancePair(int[] nums, int k) {
        int n = nums.length;
        double[] numArray = Arrays.stream(nums).asDoubleStream().toArray();
        Arrays.sort(numArray);
        int low = 0;
        for (int high = (int)(numArray[n - 1] - numArray[0]); low < high; ) {
            int mid = (low + high) >>> 1;
            if (morePairs(numArray, mid, k)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private boolean morePairs(double[] nums, int diff, int k) {
        for (int i = 0, total = 0, n = nums.length; i < n; i++) {
            total +=
                -Arrays.binarySearch(nums, i, n, nums[i] + diff + 0.5) - 2 - i;
            if (total >= k) return true;
        }
        return false;
    }

    // Binary Search
    // time complexity: O((N + log(W)) * log(N)), space complexity: O(log(N))
    // beats %(20 ms for 19 tests)
    public int smallestDistancePair2(int nums[], int k) {
        int n = nums.length;
        Arrays.sort(nums);
        int low = 0;
        for (int high = nums[n - 1] - nums[0]; low < high; ) {
            int mid = (low + high) >>> 1;
            if (countPairs(nums, mid) >= k) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private int countPairs(int[] nums, int mid) {
        int n = nums.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            count += upperBound(nums, i, n - 1, nums[i] + mid) - i - 1;
        }
        return count;
    }

    private int upperBound(int[] nums, int low, int high, int key) {
        if (nums[high] <= key) return high + 1;

        while (low < high) {
            int mid = (low + high) >>> 1;
            if (key >= nums[mid]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Binary Search
    // time complexity: O(N * (log(W) + log(N)), space complexity: O(log(N))
    // beats %(13 ms for 19 tests)
    public int smallestDistancePair3(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;
        int low = 0;
        for (int high = nums[n - 1] - nums[0]; low < high; ) {
            int mid = (low + high) >>> 1;
            int count = 0;
            for (int i = 1, j = 0; i < n; i++) {
                for (; j < i && nums[i] - nums[j] > mid; j++) {}
                count += i - j;
            }
            if (count >= k) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    // Binary Search + Prefix Sum
    // https://leetcode.com/articles/find-k-th-smallest-pair-distance/ #2
    // time complexity: O(W + N * (log(W) + log(N)), space complexity: O(N + W)
    // beats %(33 ms for 19 tests)
    public int smallestDistancePair4(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;
        int[] multiplicity = new int[n];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                multiplicity[i] = 1 + multiplicity[i - 1];
            }
        }
        final int width = 2 * nums[n - 1];
        int[] prefix = new int[width]; // prefix[v] = number of values <= v
        for (int i = 0, count = 0; i < width; i++) {
            for (; count < n && nums[count] == i; count++) {}
            prefix[i] = count;
        }
        int low = 0;
        for (int high = nums[n - 1] - nums[0]; low < high; ) {
            int mid = (low + high) >>> 1;
            int count = 0;
            for (int i = 0; i < n; i++) {
                count += prefix[nums[i] + mid] - prefix[nums[i]]
                         + multiplicity[i];
            }
            if (count >= k) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }


    // Bucket Sort
    // https://leetcode.com/articles/find-k-th-smallest-pair-distance/ #2
    // time complexity: O(N ^ 2), space complexity: O(max(nums))
    // beats %(778 ms for 19 tests)
    public int smallestDistancePair5(int[] nums, int k) {
        int max = 0;
        int min = Integer.MAX_VALUE;
        for (int num : nums) {
            max = Math.max(max, num);
            min = Math.min(min, num);
        }
        int[] count = new int[max - min + 1];
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                count[Math.abs(nums[i] - nums[j])]++;
            }
        }
        for (int i = 0, left = k; ; left -= count[i++]) {
            if (count[i] >= left) return i;
        }
    }

    void test(int[] nums, int k, int expected) {
        assertEquals(expected, smallestDistancePair(nums, k));
        assertEquals(expected, smallestDistancePair2(nums, k));
        assertEquals(expected, smallestDistancePair3(nums, k));
        assertEquals(expected, smallestDistancePair4(nums, k));
        assertEquals(expected, smallestDistancePair5(nums, k));
    }

    @Test
    public void test() {
        test(new int[] {1, 6, 1}, 3, 5);
        test(new int[] {1, 3, 1}, 1, 0);
        test(new int[] {62, 100, 4}, 2, 58);
        test(new int[] {38, 33, 57, 65, 13, 2, 86, 75, 4, 56}, 26, 36);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
