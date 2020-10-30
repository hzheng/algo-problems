import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1521: https://leetcode.com/problems/find-a-value-of-a-mysterious-function-closest-to-target/
//
// func(arr, l, r) {
//    if (r < l) return -1000000000
//    ans = arr[l]
//    for (i = l + 1; i <= r; i++) {
//        ans = ans & arr[i]
//    }
//    return ans
// }
// Winston was given the above mysterious function func. He has an integer array arr and an integer
// target and he wants to find the values l and r that make the value |func(arr, l, r) - target|
// minimum possible. Return the minimum possible value of |func(arr, l, r) - target|.
// Notice that func should be called with the values l and r where 0 <= l, r < arr.length.
// Constraints:
// 1 <= arr.length <= 10^5
// 1 <= arr[i] <= 10^6
// 0 <= target <= 10^7
public class MysteriousFunctionClosestToTarget {
    // Set
    // time complexity: O(N*log(MAX)), space complexity: O(n)
    // 96 ms(68.42%), 48.9 MB(6.01%) for 29 tests
    public int closestToTarget(int[] arr, int target) {
        int res = Integer.MAX_VALUE;
        Set<Integer> ands = new HashSet<>();
        for (int i = arr.length - 1; i >= 0; i--) {
            Set<Integer> nextAnds = new HashSet<>();
            nextAnds.add(arr[i]);
            res = Math.min(res, Math.abs(arr[i] - target));
            for (int and : ands) {
                int val = arr[i] & and;
                nextAnds.add(val);
                res = Math.min(res, Math.abs(val - target));
            }
            ands = nextAnds;
        }
        return res;
    }

    // time complexity: O(N*log(MAX)), space complexity: O(n)
    // 10 ms(97.52%), 51.3 MB(6.61%) for 29 tests
    public int closestToTarget2(int[] arr, int target) {
        int res = Integer.MAX_VALUE;
        int[] ands = new int[0];
        int len = 0;
        for (int a : arr) {
            int[] nextAnds = new int[len + 1];
            int p = 0;
            for (int i = 0; i < len; i++) {
                int val = a & ands[i];
                if (p == 0 || nextAnds[p - 1] != val) {
                    nextAnds[p++] = val;
                }
            }
            if (p == 0 || nextAnds[p - 1] != a) {
                nextAnds[p++] = a;
            }
            for (int i = 0; i < p; i++) {
                res = Math.min(res, Math.abs(target - nextAnds[i]));
            }
            ands = nextAnds;
            len = p;
        }
        return res;
    }

    // Segment Tree + Binary Search
    // time complexity: O(N*log(N)^2), space complexity: O(n)
    // 1122 ms(10.75%), 52.2 MB(6.61%) for 29 tests
    public int closestToTarget3(int[] arr, int target) {
        int n = arr.length;
        SegmentTreeNode root = SegmentTreeNode.build(arr, 0, n - 1);
        int res = Integer.MAX_VALUE;
        for (int start = 0; start < n; start++) {
            int end = lastLarger(root, start, n - 1, target);
            int and = root.query(start, end);
            res = Math.min(res, Math.abs(target - and));
            if (end < n - 1) {
                res = Math.min(res, Math.abs(target - (and & arr[end + 1])));
            }
        }

        return res;
    }

    private int lastLarger(SegmentTreeNode root, int start, int end, int target) {
        int low = start;
        for (int high = end; low < high; ) {
            int mid = (low + high + 1) >>> 1;
            if (root.query(start, mid) >= target) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    private static class SegmentTreeNode {
        private final int start;
        private final int end;
        private int val;
        private SegmentTreeNode left, right;

        public SegmentTreeNode(int start, int end, int val) {
            this.start = start;
            this.end = end;
            this.val = val;
        }

        public static SegmentTreeNode build(int[] arr, int start, int end) {
            SegmentTreeNode root = new SegmentTreeNode(start, end, arr[start]);
            if (start == end) { return root; }

            int mid = (start + end) >>> 1;
            root.left = build(arr, start, mid);
            root.right = build(arr, mid + 1, end);
            root.val = root.left.val & root.right.val;
            return root;
        }

        public int query(int start, int end) {
            if (start == this.start && end == this.end) { return val; }

            int mid = (this.start + this.end) >>> 1;
            if (end <= mid) { return this.left.query(start, end); }

            if (start > mid) { return this.right.query(start, end); }

            return left.query(start, mid) & right.query(mid + 1, end);
        }
    }

    // Bit Manipulation + Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 82 ms(70.25%), 50.9 MB(6.61%) for 29 tests
    public int closestToTarget4(int[] arr, int target) {
        int res = Integer.MAX_VALUE;
        int[] bitCounts = new int[32];
        for (int start = 0, end = 0, and; end < arr.length; end++) {
            updateBitCount(arr[end], bitCounts, 1);
            and = computeAnd(bitCounts, end - start + 1);
            res = Math.min(res, Math.abs(and - target));
            for (; start <= end && and < target; start++) {
                updateBitCount(arr[start], bitCounts, -1);
                and = computeAnd(bitCounts, end - start);
                res = Math.min(res, Math.abs(and - target));
            }
        }
        return res;
    }

    private void updateBitCount(int num, int[] bitCounts, int update) {
        for (int i = 31; i >= 0; i--) {
            if (((num >> i) & 1) != 0) {
                bitCounts[31 - i] += update;
            }
        }
    }

    private int computeAnd(int[] bitCounts, int len) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if (bitCounts[i] > 0 && bitCounts[i] == len) { // all 1's
                res |= (1 << (31 - i));
            }
        }
        return res;
    }

    private void test(int[] arr, int target, int expected) {
        assertEquals(expected, closestToTarget(arr, target));
        assertEquals(expected, closestToTarget2(arr, target));
        assertEquals(expected, closestToTarget3(arr, target));
        assertEquals(expected, closestToTarget4(arr, target));
    }

    @Test public void test() {
        test(new int[] {9, 12, 3, 7, 15}, 5, 2);
        test(new int[] {1000000, 1000000, 1000000}, 1, 999999);
        test(new int[] {1, 2, 4, 8, 16}, 0, 0);
        test(new int[] {1, 2, 4, 8, 16, 32, 100, 23, 933, 15, 12, 91}, 3, 1);
        test(new int[] {70, 15, 21, 96}, 4, 0);
        test(new int[] {1023, 511, 255, 262143, 2047, 524287, 524287, 31, 16383, 16383, 31, 63,
                        2047, 262143, 511, 262143, 32767, 2047, 31, 511, 127, 127, 31, 262143,
                        32767, 8191, 65535, 63, 4095, 4095, 2047, 511, 32767, 262143, 2047, 63,
                        2047, 63, 2047, 31, 8191, 127, 31, 4095, 524287, 65535, 65535, 15, 1023,
                        127, 524287, 4095, 262143, 131071, 2047, 127, 511, 15, 262143, 262143,
                        65535, 63, 32767, 32767, 32767, 4095, 262143, 16383, 127, 15, 32767, 65535,
                        524287, 2047, 15, 2047}, 890, 133);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
