import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/range-sum-query-immutable/
//
// Given an integer array nums, find the sum of the elements between indices
// i and j (i <= j), inclusive. The update(i, val) function modifies nums by
// updating the element at index i to val.
public class RangeSumQueryMutable {
    interface INumArray {
        void update(int i, int val);
        int sumRange(int i, int j);
    }

    //  Time Limit Exceeded
    class NumArray implements INumArray {
        private int[] sums;
        private int[] nums;
        private SortedMap<Integer, Integer> diffMap = new TreeMap<>();

        public NumArray(int[] nums) {
            this.nums = nums;
            int n = nums.length;
            sums = new int[n + 1];
            for (int i = 0; i < n; i++) {
                sums[i + 1] = sums[i] + nums[i];
            }
        }

        // time complexity: O(N)
        public int sumRange(int i, int j) {
            int sum = sums[j + 1] - sums[i];
            for (int diff : diffMap.subMap(i, j + 1).values()) {
                sum += diff;
            }
            return sum;
        }

        // time complexity: O(1)
        public void update(int i, int val) {
            int diff = val - nums[i];
            if (diff != 0) {
                nums[i] = val;
                if (diffMap.containsKey(i)) {
                    diff += diffMap.get(i);
                }
                diffMap.put(i, diff);
            }
        }
    }

    //  Time Limit Exceeded
    class NumArray2 implements INumArray {
        private int[] sums;
        private int[] nums;

        public NumArray2(int[] nums) {
            this.nums = nums;
            int n = nums.length;
            sums = new int[n + 1];
            for (int i = 0; i < n; i++) {
                sums[i + 1] = sums[i] + nums[i];
            }
        }

        // time complexity: O(1)
        public int sumRange(int i, int j) {
            return sums[j + 1] - sums[i];
        }

        // time complexity: O(N)
        public void update(int i, int val) {
            int diff = val - nums[i];
            if (diff != 0) {
                nums[i] = val;
                for (int j = i; j < nums.length; j++) {
                    sums[j + 1] += diff;
                }
            }
        }
    }

    // Binary Indexed Tree
    // beats 68.67%(7 ms)
    class NumArray3 implements INumArray {
        private int[] bit;
        private int[] nums;

        public NumArray3(int[] nums) {
            int n = nums.length;
            bit = new int[n + 1];
            this.nums = nums;
            for (int i = 0; i < n; i++) {
                add(i, nums[i]);
            }
        }

        // time complexity: O(log(N))
        public int sumRange(int i, int j) {
            return sum(j + 1) - sum(i);
        }

        private int sum(int i) {
            int sum = 0;
            for (int j = i; j > 0; j -= (j & -j)) {
                sum += bit[j];
            }
            return sum;
        }

        // time complexity: O(log(N))
        public void update(int i, int val) {
            add(i, val - nums[i]);
            nums[i] = val;
        }

        private void add(int i, int diff) {
            for (int j = i + 1; j < bit.length; j += (j & -j)) {
                bit[j] += diff;
            }
        }
    }

    void test(INumArray obj, int[] query1, int[] update, int[] query2) {
        assertEquals(query1[2], obj.sumRange(query1[0], query1[1]));
        for (int i = 0; i < update.length - 1; i++) {
            obj.update(update[i], update[++i]);
        }
        assertEquals(query2[2], obj.sumRange(query2[0], query2[1]));
    }

    void test(int[] nums, int[] query1, int[] update, int[] query2) {
        test(new NumArray(nums.clone()), query1, update, query2);
        test(new NumArray2(nums.clone()), query1, update, query2);
        test(new NumArray3(nums.clone()), query1, update, query2);
    }

    @Test
    public void test1() {
        test(new int[] {1, 3, 5},
             new int[] {0, 2, 9}, new int[] {1, 2, 2, 3}, new int[] {0, 2, 6});
        test(new int[] {1, 2, 3, 4, 5},
             new int[] {1, 3, 9}, new int[] {1, 3, 2, 4, 3, 5},
             new int[] {1, 3, 12});
        test(new int[] {7, 2, 7, 2, 0},
             new int[] {0, 2, 16}, new int[] {4, 6, 0, 2, 0, 9, 3, 8},
             new int[] {0, 4, 32});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RangeSumQueryMutable");
    }
}
