import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1835: https://leetcode.com/problems/find-xor-sum-of-all-pairs-bitwise-and/
//
// The XOR sum of a list is the bitwise XOR of all its elements. If the list only contains one
// element, then its XOR sum will be equal to this element.
// For example, the XOR sum of [1,2,3,4] is equal to 1 XOR 2 XOR 3 XOR 4 = 4, and the XOR sum of [3]
// is equal to 3.
// You are given two 0-indexed arrays arr1 and arr2 that consist only of non-negative integers.
// Consider the list containing the result of arr1[i] AND arr2[j] (bitwise AND) for every (i, j)
// pair where 0 <= i < arr1.length and 0 <= j < arr2.length.
// Return the XOR sum of the aforementioned list.
//
// Constraints:
// 1 <= arr1.length, arr2.length <= 10^5
// 0 <= arr1[i], arr2[j] <= 10^9
public class GetXORSum {
    // Math (AND and XOR has distributive property like + and *)
    // time complexity: O(M+N), space complexity: O(1)
    // 1 ms(100.00%), 52 MB(50.00%) for 84 tests
    public int getXORSum(int[] arr1, int[] arr2) {
        int xor1 = 0;
        for (int a : arr1) {
            xor1 ^= a;
        }
        int xor2 = 0;
        for (int a : arr2) {
            xor2 ^= a;
        }
        return xor1 & xor2;
    }

    // Bit Manipulation
    // time complexity: O(M+N), space complexity: O(1)
    // 32 ms(25.00%), 51.8 MB(50.00%) for 84 tests
    public int getXORSum2(int[] arr1, int[] arr2) {
        int res = 0;
        for (int mask = 1 << (Integer.SIZE - 2); mask > 0; mask >>= 1) {
            int bits1 = 0;
            for (int a : arr1) {
                bits1 += ((a & mask) != 0) ? 1 : 0;
            }
            int bits2 = 0;
            for (int a : arr2) {
                bits2 += ((a & mask) != 0) ? 1 : 0;
            }
            if (bits1 % 2 + bits2 % 2 == 2) {
                res |= mask;
            }
        }
        return res;
    }

    private void test(int[] arr1, int[] arr2, int expected) {
        assertEquals(expected, getXORSum(arr1, arr2));
        assertEquals(expected, getXORSum2(arr1, arr2));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3}, new int[] {6, 5}, 0);
        test(new int[] {12}, new int[] {4}, 4);
        test(new int[] {1, 2, 3, 4}, new int[] {5, 6, 7}, 4);
        test(new int[] {1, 2, 3, 4, 5}, new int[] {5, 6, 7}, 0);
        test(new int[] {1, 2, 3, 4, 5, 6}, new int[] {5, 6, 7, 8}, 4);
        test(new int[] {1, 2, 3, 4, 5, 6, 7}, new int[] {5, 6, 7, 8, 9}, 0);
        test(new int[] {818492001, 823729238, 2261353, 747144854, 478230859, 285970256, 774747711,
                        860954509, 245631564, 634746160}, new int[] {967900366, 340837476},
             81790984);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
