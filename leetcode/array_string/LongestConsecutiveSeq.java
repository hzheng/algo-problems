import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC128: https://leetcode.com/problems/longest-consecutive-sequence/
//
// Given an unsorted array of integers, find the length of the longest
// consecutive elements sequence.
public class LongestConsecutiveSeq {
    // Hashtable
    // beats 77.37%(12 ms)
    public int longestConsecutive(int[] nums) {
        int maxLen = 1;
        Map<Integer, int[]> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, new int[] {num, num});
        }
        for (int num : nums) {
            int[] range = map.remove(num);
            if (range == null) continue;

            for (int i = num + 1; map.remove(i) != null; i++, range[1]++) {}
            for (int i = num - 1; map.remove(i) != null; i--, range[0]--) {}
            maxLen = Math.max(maxLen, range[1] - range[0] + 1);
        }
        return maxLen;
    }

    // Solution of Choice
    // Set
    // beats 91.16%(10 ms)
    public int longestConsecutive2(int[] nums) {
        int maxLen = 1;
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        for (int num : nums) {
            if (!set.remove(num)) continue;

            int count = 1;
            for (int i = num + 1; set.remove(i); i++, count++) {}
            for (int i = num - 1; set.remove(i); i--, count++) {}
            maxLen = Math.max(maxLen, count);
        }
        return maxLen;
    }

    // Set
    // beats 77.37%(12 ms)
    public int longestConsecutive3(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int maxLen = 0;
        for (int num : nums) {
            set.add(num);
        }
        for (int num : nums) {
            if (!set.contains(num - 1)) {
                int end = num;
                while (set.remove(end++)) {}
                maxLen = Math.max(maxLen, end - num - 1);
            }
        }
        return maxLen;
    }

    // Hash Table
    // beats 12.68%(21 ms)
    public int longestConsecutive4(int[] nums) {
        int maxLen = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            if (!map.containsKey(n)) {
                int left = map.getOrDefault(n - 1, 0);
                int right = map.getOrDefault(n + 1, 0);
                int sum = left + right + 1;
                map.put(n, sum);
                map.put(n - left, sum);
                map.put(n + right, sum);
                maxLen = Math.max(maxLen, sum);
            }
        }
        return maxLen;
    }

    // Hash Table + Union Find
    // beats 28.09%(17 ms)
    public int longestConsecutive5(int[] nums) {
        UnionFind unionFind = new UnionFind(nums.length);
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++){
            if (map.containsKey(nums[i])) continue;

            map.put(nums[i], i);
            if (map.containsKey(nums[i] + 1)) {
                unionFind.union(i, map.get(nums[i] + 1));
            }
            if (map.containsKey(nums[i] - 1)) {
                unionFind.union(i, map.get(nums[i] - 1));
            }
        }
        return unionFind.maxUnion();
    }

    private static class UnionFind {
        private int[] id;

        public UnionFind(int n) {
            id = new int[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
            }
        }

        private int root(int i) {
            while (i != id[i]) {
                id[i] = id[id[i]];
                i = id[i];
            }
            return i;
        }

        public void union(int p, int q){
            id[root(p)] = root(q);
        }

        public int maxUnion() {
            int[] count = new int[id.length];
            int max = 0;
            for (int i = 0; i < id.length; i++) {
                count[root(i)]++;
                max = Math.max(max, count[root(i)]);
            }
            return max;
        }
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, longestConsecutive(nums));
        assertEquals(expected, longestConsecutive2(nums));
        assertEquals(expected, longestConsecutive3(nums));
        assertEquals(expected, longestConsecutive4(nums));
        assertEquals(expected, longestConsecutive5(nums));
    }

    @Test
    public void test1() {
        test(4, 100, 4, 200, 1, 3, 2);
        test(5, 100, 4, 101, 98, 1, 99, 3, 2, 102);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestConsecutiveSeq");
    }
}
