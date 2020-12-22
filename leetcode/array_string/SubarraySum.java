import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC560: https://leetcode.com/problems/subarray-sum-equals-k/
//
// Given an array of integers and an integer k, you need to find the total number
// of continuous subarrays whose sum equals to k.
public class SubarraySum {
    // Hash Table + List
    // time complexity: O(N)
    // beats 72.70%(47 ms for 80 tests)
    public int subarraySum(int[] nums, int k) {
        int n = nums.length;
        int[] sum = new int[n + 1];
        Map<Integer, List<Integer> > map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(0);
        map.put(0, list);
        for (int i = 0; i < n; i++) {
            int s = sum[i + 1] = nums[i] + sum[i];
            list = map.get(s);
            if (list == null) {
                map.put(s, list = new ArrayList<>());
            }
            list.add(i + 1);
        }
        int count = 0;
        for (int i = n; i > 0; i--) {
            list = map.get(sum[i] - k);
            if (list != null && list.get(0) < i) {
                for (int j = 0; j < list.size() && list.get(j) < i; j++) {
                    count++;
                }
            }
        }
        return count;
    }

    // Solution of Choice
    // Hash Table + Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 18 ms(68.65%), 42.3 MB(22.38%) for 89 tests
    public int subarraySum2(int[] nums, int k) {
        int sum = 0;
        Map<Integer, Integer> sumCounter = new HashMap<>();
        sumCounter.put(0, 1);
        int res = 0;
        for (int num : nums) {
            sum += num;
            res += sumCounter.getOrDefault(sum - k, 0);
            sumCounter.put(sum, sumCounter.getOrDefault(sum, 0) + 1);
        }
        return res;
    }

    // time complexity: O(N ^ 2)
    // beats 31.48%(249 ms for 80 tests)
    public int subarraySum3(int[] nums, int k) {
        int count = 0;
        for (int start = 0; start < nums.length; start++) {
            int sum = 0;
            for (int end = start; end < nums.length; end++) {
                sum += nums[end];
                if (sum == k) {
                    count++;
                }
            }
        }
        return count;
    }

    // time complexity: O(N ^ 2)
    // beats 17.36%(375 ms for 80 tests)
    public int subarraySum4(int[] nums, int k) {
        int count = 0;
        int n = nums.length;
        int[] sum = new int[n + 1];
        sum[0] = 0;
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }
        for (int start = 0; start < n; start++) {
            for (int end = start + 1; end <= n; end++) {
                if (sum[end] - sum[start] == k) {
                    count++;
                }
            }
        }
        return count;
    }

    void test(int[] nums, int k, int expected) {
        assertEquals(expected, subarraySum(nums, k));
        assertEquals(expected, subarraySum2(nums, k));
        assertEquals(expected, subarraySum3(nums, k));
        assertEquals(expected, subarraySum4(nums, k));
    }

    @Test
    public void test() {
        test(new int[] {1}, 0, 0);
        test(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 0, 55);
        test(new int[] {1, 2, 3}, 3, 2);
        test(new int[] {1, 1, 1}, 2, 2);
        test(new int[] {1, 2, 3, 5, -1, -2, 2, 0, 1, -3, 1, 1}, 3, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
