import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC398: https://leetcode.com/problems/random-pick-index/
//
// Given an array of integers with possible duplicates, randomly output the
// index of a given target number. You can assume that the given target number
// must exist in the array.
// Note:
// The array size can be very large. Solution that uses too much extra space
// will not pass the judge.
public class RandomPickIndex {
    static interface IRandomPickIndex {
        public int pick(int target);
    }

    // beats N/A(350 ms) or Memory Limit Exceeded
    static class RandomPickIndex1 implements IRandomPickIndex {
        private Map<Integer, Indices> map = new HashMap<>();
        private Random rand = new Random();
        private int[] nums;

        private static class Indices {
            int count;
            int index;
            int lastIndex;

            Indices(int count, int index) {
                this.count = count;
                this.index = index;
                lastIndex = index;
            }
        }

        // time complexity: O(N), space complexity: O(N)
        public RandomPickIndex1(int[] nums) {
            this.nums = nums;
            for (int i = 0; i < nums.length; i++) {
                int a = nums[i];
                if (!map.containsKey(a)) {
                    map.put(a, new Indices(1, i));
                } else {
                    Indices indices = map.get(a);
                    indices.count++;
                    nums[indices.lastIndex] = i;
                    indices.lastIndex = i;
                }
            }
        }

        // time complexity: O(1)
        public int pick(int target) {
            Indices indices = map.get(target);
            int chosen = rand.nextInt(indices.count);
            int res = indices.index;
            for (int i = 0; i < chosen; i++) {
                res = nums[res];
            }
            return res;
        }
    }

    // Memory Limit Exceeded
    static class RandomPickIndex2 implements IRandomPickIndex {
        private int[] next;
        private Map<Integer, Integer> headMap = new HashMap<>();
        private Random rand = new Random();

        // time complexity: O(N), space complexity: O(N)
        public RandomPickIndex2(int[] nums) {
            next = new int[nums.length + 1];
            for (int i = 0; i < nums.length; i++) {
                int num = nums[i];
                if (headMap.containsKey(num)) { // reversed linked list
                    next[i + 1] = headMap.get(num);
                }
                headMap.put(num, i + 1);
            }
        }

        // time complexity: O(1)
        public int pick(int target) {
            int index = headMap.get(target);
            int count = 0;
            for (int i = index; i > 0; i = next[i], count++) {}
            int chosen = rand.nextInt(count);
            for (int i = 0; i < chosen; i++) {
                index = next[index];
            }
            return index - 1;
        }
    }

    // Reservoir sampling
    // beats N/A(397 ms)
    static class RandomPickIndex3 implements IRandomPickIndex {
        private Random rand = new Random();
        private int[] nums;

        // time complexity: O(1), space complexity: O(1)
        public RandomPickIndex3(int[] nums) {
            this.nums = nums;
        }

        // time complexity: O(N)
        public int pick(int target) {
            int res = -1;
            for (int i = 0, count = 0; i < nums.length; i++) {
                if (nums[i] == target && rand.nextInt(++count) == 0) {
                    res = i;
                }
            }
            return res;
        }
    }

    void test(IRandomPickIndex picker, int target, int[] range) {
        int n = 100000;
        Map<Integer, Integer> counts = new HashMap<>();
        for (int i : range) {
            counts.put(i, 0);
        }
        for (int i = 0; i < n; i++) {
            int index = picker.pick(target);
            counts.put(index, counts.get(index) + 1);
        }
        double avg = (double)n / range.length;
        for (int k : counts.keySet()) {
            double diff = Math.abs((counts.get(k) - avg) / n);
            // System.out.println(diff);
            assertTrue(diff < 0.005);
        }
    }

    void test(int[] nums, int target, int[] range) {
        test(new RandomPickIndex1(nums.clone()), target, range);
        test(new RandomPickIndex2(nums), target, range);
        test(new RandomPickIndex3(nums), target, range);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3, 3, 3}, 3, new int[] {2, 3, 4});
        test(new int[] {1, 2, 3, 3, 3, 2, 3, 6, 3},
             3, new int[] {2, 3, 4, 6, 8});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RandomPickIndex");
    }
}
