import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1707: https://leetcode.com/problems/maximum-xor-with-an-element-from-array/
//
// You are given an array nums consisting of non-negative integers. You are also given a queries
// array, where queries[i] = [xi, mi]. The answer to the ith query is the maximum bitwise XOR value
// of xi and any element of nums that does not exceed mi. In other words, the answer is
// max(nums[j] XOR xi) for all j such that nums[j] <= mi. If all elements in nums are larger than
// mi, then the answer is -1. Return an integer array answer where answer.length == queries.length
// and answer[i] is the answer to the ith query.
//
// Constraints:
// 1 <= nums.length, queries.length <= 10^5
// queries[i].length == 2
// 0 <= nums[j], xi, mi <= 10^9
public class MaximizeXor {
    // Trie + Sort + Heap
    // time complexity: O(N*log(N)+Q*log(Q)), space complexity: O(N+Q)
    // 115 ms(69.23%), 111.3 MB(61.54%) for 65 tests
    public int[] maximizeXor(int[] nums, int[][] queries) {
        Arrays.sort(nums);
        int[] res = new int[queries.length];
        PriorityQueue<int[]> queryQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p[1]));
        int queryIndex = 0;
        for (int[] query : queries) {
            queryQueue.offer(new int[] {query[0], query[1], queryIndex++});
        }
        final BinaryNode root = new BinaryNode();
        final int hiBit = Integer.SIZE - 2;
        for (int i = 0, n = nums.length; !queryQueue.isEmpty(); ) {
            int[] query = queryQueue.poll();
            for (int limit = query[1]; i < n && nums[i] <= limit; i++) {
                BinaryNode cur = root; // incrementally build trie
                for (int bitPos = hiBit; bitPos >= 0; bitPos--) {
                    cur = cur.getChild((nums[i] >>> bitPos) & 1);
                }
            }
            if (i == 0) {
                res[query[2]] = -1;
                continue;
            }
            BinaryNode cur = root;
            int xor = 0;
            for (int a = query[0], bitPos = hiBit; bitPos >= 0; bitPos--) {
                int index = (a >>> bitPos) & 1;
                if (cur.children[index ^ 1] != null) { // try to maximize xor
                    index ^= 1;
                    xor |= 1 << bitPos;
                }
                cur = cur.children[index];
            }
            res[query[2]] = xor;
        }
        return res;
    }

    // Trie + DFS + Recursion
    // time complexity: O(N+Q), space complexity: O(N+Q)
    // 115 ms(69.23%), 111.3 MB(61.54%) for 65 tests
    public int[] maximizeXor2(int[] nums, int[][] queries) {
        BinaryNode root = new BinaryNode();
        final int hiBit = Integer.SIZE - 2;
        for (int num : nums) {
            BinaryNode cur = root;
            for (int bitPos = hiBit; bitPos >= 0; bitPos--) {
                cur = cur.getChild((num >>> bitPos) & 1);
            }
        }
        int[] res = new int[queries.length];
        int queryIndex = 0;
        for (int[] query : queries) {
            res[queryIndex++] = maxXor(root, query[0], query[1], hiBit, 0);
        }
        return res;
    }

    private int maxXor(BinaryNode cur, int a, int limit, int bitPos, int best) {
        if (cur == null || best > limit) { return -1; }
        if (bitPos < 0) { return a ^ best; }

        int nextBest = best;
        int index = (a >>> bitPos) & 1;
        if (cur.children[index ^ 1] != null) { // try to maximize xor
            index ^= 1;
        }
        if (index == 1) {
            nextBest |= 1 << bitPos;
        }
        int res = maxXor(cur.children[index], a, limit, bitPos - 1, nextBest);
        if (res >= 0) { return res; }

        if (index == 0) { return -1; }

        return maxXor(cur.children[0], a, limit, bitPos - 1, best);
    }

    private static class BinaryNode {
        BinaryNode[] children = new BinaryNode[2];

        BinaryNode getChild(int bit) {
            if (children[bit] == null) {
                children[bit] = new BinaryNode();
            }
            return children[bit];
        }
    }

    private void test(int[] nums, int[][] queries, int[] expected) {
        assertArrayEquals(expected, maximizeXor(nums.clone(), queries));
        assertArrayEquals(expected, maximizeXor2(nums.clone(), queries));
    }

    @Test public void test() {
        test(new int[] {0, 1, 2, 3, 4}, new int[][] {{3, 1}, {1, 3}, {5, 6}}, new int[] {3, 3, 7});
        test(new int[] {5, 2, 4, 6, 6, 3}, new int[][] {{12, 4}, {8, 1}, {6, 3}},
             new int[] {15, -1, 5});
        test(new int[] {1}, new int[][] {{1, 1}}, new int[] {0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
