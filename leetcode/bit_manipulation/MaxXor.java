import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC421: https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
//
// Given a non-empty array of numbers, a0, a1, a2, … , an-1, where 0 ≤ ai < 2 ^31.
// Find the maximum result of ai XOR aj, where 0 ≤ i, j < n.
public class MaxXor {
    // Trie
    // time complexity: O(N), space complexity: O(N)
    // 26 ms(91.01%), 47.9 MB(34.08%) for 39 tests
    public int findMaximumXOR(int[] nums) {
        int hiBit = getHighestBit(nums);
        BinaryNode root = new BinaryNode();
        for (int num : nums) {
            BinaryNode cur = root;
            for (int bitPos = hiBit; bitPos >= 0; bitPos--) {
                cur = cur.getChild((num >>> bitPos) & 1);
            }
        }
        int max = 0;
        for (int num : nums) {
            BinaryNode cur = root;
            int xor = 0;
            for (int bitPos = hiBit; bitPos >= 0; bitPos--) {
                int index = (num >>> bitPos) & 1;
                if (cur.children[index ^ 1] != null) { // try to maximize xor
                    index ^= 1;
                    xor |= 1 << bitPos;
                }
                cur = cur.children[index];
            }
            max = Math.max(max, xor);
        }
        return max;
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

    private int getHighestBit(int[] nums) {
        int totalOr = 0;
        for (int num : nums) {
            totalOr |= num;
        }
        if (totalOr == 0) { return 0; }

        int hiBit = Integer.SIZE - 2;
        // this calculation is just for efficiency
        for (int mask = (1 << hiBit); (mask & totalOr) == 0; mask >>= 1, hiBit--) {}
        return hiBit;
    }

    // Trie
    // time complexity: O(N), space complexity: O(N)
    // 20 ms(95.69%), 47.5 MB(39.33%) for 39 tests
    public int findMaximumXOR2(int[] nums) {
        int hiBit = getHighestBit(nums);
        int max = 0;
        BinaryNode root = new BinaryNode();
        for (int num : nums) {
            int curMax = 0;
            BinaryNode cur = root;
            BinaryNode complementNode = root;
            for (int bitPos = hiBit; bitPos >= 0; bitPos--) {
                int index = (num >>> bitPos) & 1;
                complementNode = complementNode.getChild(index ^ 1);
                if (cur.children[index] != null) {
                    curMax |= (1 << bitPos);
                    cur = cur.children[index];
                } else {
                    cur = cur.children[index ^ 1];
                }
            }
            max = Math.max(max, curMax);
        }
        return max;
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // 71 ms(60.30%), 40.2 MB(66.10%) for 39 tests
    public int findMaximumXOR3(int[] nums) {
        int hiBit = getHighestBit(nums);
        int max = 0;
        for (int i = hiBit, mask = 1 << i; i >= 0; mask |= (1 << --i)) {
            Set<Integer> prefixSet = new HashSet<>();
            for (int num : nums) {
                prefixSet.add(num & mask);
            }
            int nextMax = max | (1 << i);
            for (int prefix : prefixSet) {
                if (prefixSet.contains(nextMax ^ prefix)) { // (a ^ b = c) => (a = b ^ c)
                    max = nextMax;
                    break;
                }
            }
        }
        return max;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, findMaximumXOR(nums));
        assertEquals(expected, findMaximumXOR2(nums));
        assertEquals(expected, findMaximumXOR3(nums));
    }

    @Test public void test1() {
        test(new int[] {0b11, 0b1010, 0b101, 0b11001, 0b10, 0b1000}, 0b11100);
        test(new int[] {3, 10, 5, 25, 2, 8}, 28);
        test(new int[] {3, 10, 5, 25, 2, 8, 7, 8, 11, 23}, 31);
        test(new int[] {300, 109, 35, 25, 12, 18, 73, 68, 101, 213}, 505);
        test(new int[] {0}, 0);
        test(new int[] {Integer.MAX_VALUE, 1}, 2147483646);
        test(new int[] {Integer.MAX_VALUE, 2, 1}, 2147483646);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
