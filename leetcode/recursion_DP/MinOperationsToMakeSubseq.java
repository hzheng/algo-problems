import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1713: https://leetcode.com/problems/minimum-operations-to-make-a-subsequence/
//
// You are given an array target that consists of distinct integers and another integer array arr
// that can have duplicates. In one operation, you can insert any integer at any position in arr.
// For example, if arr = [1,4,1,2], you can add 3 in the middle and make it [1,4,3,1,2]. Note that
// you can insert the integer at the very beginning or end of the array.
// Return the minimum number of operations needed to make target a subsequence of arr.
//
// Constraints:
// 1 <= target.length, arr.length <= 10^5
// 1 <= target[i], arr[i] <= 10^9
// target contains no duplicates.
public class MinOperationsToMakeSubseq {
    // Dynamic Programming + Binary Search
    // time complexity: O(N*logN), space complexity: O(N)
    // 60 ms(98.76%), 56.4 MB(88.82%) for 82 tests
    public int minOperations(int[] target, int[] arr) {
        Map<Integer, Integer> order = new HashMap<>();
        for (int i = target.length - 1; i >= 0; i--) {
            order.put(target[i], i);
        }
        int[] seq = new int[arr.length];
        int len = 0;
        for (int a : arr) {
            int pos = order.getOrDefault(a, Integer.MAX_VALUE);
            int index = Arrays.binarySearch(seq, 0, len, pos);
            if (index < 0) {
                index = -(index + 1);
            }
            seq[index] = pos;
            if (index == len) {
                len++;
            }
        }
        return target.length - len + ((len > 0 && seq[len - 1] == Integer.MAX_VALUE) ? 1 : 0);
    }

    // Dynamic Programming + Binary Search
    // time complexity: O(N*logN), space complexity: O(N)
    // 60 ms(98.76%), 56.4 MB(88.82%) for 82 tests
    public int minOperations2(int[] target, int[] arr) {
        Map<Integer, Integer> order = new HashMap<>();
        for (int i = target.length - 1; i >= 0; i--) {
            order.put(target[i], i);
        }
        int[] seq = new int[arr.length];
        int len = 0;
        for (int a : arr) {
            int pos = order.getOrDefault(a, -1);
            if (pos < 0) { continue; }

            int index = Arrays.binarySearch(seq, 0, len, pos);
            if (index < 0) {
                index = -(index + 1);
            }
            seq[index] = pos;
            if (index == len) {
                len++;
            }
        }
        return target.length - len;
    }

    // Dynamic Programming + Binary Search
    // time complexity: O(N*logN), space complexity: O(N)
    // 69 ms(92.55%), 67.5 MB(40.37%) for 82 tests
    public int minOperations3(int[] target, int[] arr) {
        Map<Integer, Integer> order = new HashMap<>();
        for (int i = target.length - 1; i >= 0; i--) {
            order.put(target[i], i);
        }
        List<Integer> seq = new ArrayList<>();
        seq.add(-1);
        for (int a : arr) {
            int pos = order.getOrDefault(a, -1);
            if (pos < 0) { continue; }

            if (pos > seq.get(seq.size() - 1)) {
                seq.add(pos);
                continue;
            }
            int low = 0;
            for (int high = seq.size() - 1; low < high; ) {
                int mid = (low + high) >>> 1;
                if (seq.get(mid) < pos) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            seq.set(low, pos);
        }
        return target.length - seq.size() + 1;
    }

    private void test(int[] target, int[] arr, int expected) {
        assertEquals(expected, minOperations(target, arr));
        assertEquals(expected, minOperations2(target, arr));
        assertEquals(expected, minOperations3(target, arr));
    }

    @Test public void test() {
        test(new int[] {5, 1, 3}, new int[] {9, 4, 2, 3, 4}, 2);
        test(new int[] {6, 4, 8, 1, 3, 2}, new int[] {4, 7, 6, 2, 3, 8, 6, 1}, 3);
        test(new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 5, 2}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
