import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1803: https://leetcode.com/problems/count-pairs-with-xor-in-a-range/
//
// Given a (0-indexed) integer array nums and two integers low and high, return the number of nice
// pairs. A nice pair is a pair (i, j) where 0 <= i < j < nums.length and
// low <= (nums[i] XOR nums[j]) <= high.
//
// Constraints:
// 1 <= nums.length <= 2 * 10^4
// 1 <= nums[i] <= 2 * 10^4
// 1 <= low <= high <= 2 * 10^4
public class CountPairsWithXORInRange {
    // Trie
    // time complexity: O(N), space complexity: O(N)
    // 108 ms(%), 46.5 MB(%) for 61 tests
    public int countPairs(int[] nums, int low, int high) {
        TrieNode root = new TrieNode();
        int res = 0;
        for (int num : nums) {
            root.insert(num);
            res += countSmaller(root, num, high + 1) - countSmaller(root, num, low);
        }
        return res;
    }

    private int countSmaller(TrieNode root, int num, int limit) {
        int res = 0;
        for (int i = Integer.SIZE - 2; i >= 0 && root != null; i--) {
            int bit = (num >> i) & 1;
            if (((limit >> i) & 1) > 0) {
                if (root.child[bit] != null) {
                    res += root.child[bit].count;
                }
                bit ^= 1;
            }
            root = root.child[bit];
        }
        return res;
    }

    private static class TrieNode {
        private final TrieNode[] child = new TrieNode[2];
        private int count;

        public void insert(int num) {
            TrieNode root = this;
            for (int i = Integer.SIZE - 2; i >= 0; i--) {
                int bit = (num >> i) & 1;
                if (root.child[bit] == null) {
                    root.child[bit] = new TrieNode();
                }
                root = root.child[bit];
                root.count++;
            }
        }
    }

    // time complexity: O(N), space complexity: O(N)
    // 108 ms(%), 46.5 MB(%) for 61 tests
    public int countPairs2(int[] nums, int low, int high) {
        return count(nums, high + 1) - count(nums, low);
    }

    private int count(int[] nums, int limit) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int a : nums) {
            count.put(a, count.getOrDefault(a, 0) + 1);
        }
        int res = 0;
        for (int a = limit; a > 0; a >>= 1) {
            Map<Integer, Integer> nextCount = new HashMap<>();
            for (int b : count.keySet()) {
                int cnt = count.get(b);
                nextCount.put(b >> 1, nextCount.getOrDefault(b >> 1, 0) + cnt);
                if ((a & 1) > 0) {
                    res += cnt * count.getOrDefault((a - 1) ^ b, 0);
                }
            }
            count = nextCount;
        }
        return res / 2;
    }

    private void test(int[] nums, int low, int high, int expected) {
        assertEquals(expected, countPairs(nums, low, high));
        assertEquals(expected, countPairs2(nums, low, high));
    }

    @Test public void test() {
        test(new int[] {1, 4, 2, 7}, 2, 6, 6);
        test(new int[] {9, 8, 4, 2, 1}, 5, 14, 8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
