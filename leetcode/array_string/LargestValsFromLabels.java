import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1090: https://leetcode.com/problems/largest-values-from-labels/
//
// We have a set of items: the i-th item has value values[i] and label labels[i].
// Then, we choose a subset S of these items, such that:
// |S| <= num_wanted
// For every label L, the number of items in S with label L is <= use_limit.
// Return the largest possible sum of the subset S.
//
// Note:
// 1 <= values.length == labels.length <= 20000
// 0 <= values[i], labels[i] <= 20000
// 1 <= num_wanted, use_limit <= values.length
public class LargestValsFromLabels {
    // Heap + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 14 ms(94.48%), 41.7 MB(40.73%) for 81 tests
    public int largestValsFromLabels(int[] values, int[] labels, int num_wanted, int use_limit) {
        int n = values.length;
        Map<Integer, PriorityQueue<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int label = labels[i];
            PriorityQueue<Integer> valQueue = map.get(label);
            if (valQueue == null) {
                map.put(label, valQueue = new PriorityQueue<>(use_limit + 1));
            }
            valQueue.offer(values[i]);
            if (valQueue.size() > use_limit) {
                valQueue.poll();
            }
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (PriorityQueue<Integer> valQueue : map.values()) {
            for (int val : valQueue) {
                pq.offer(val);
            }
            for (; pq.size() > num_wanted; pq.poll()) {}
        }
        int res = 0;
        while (!pq.isEmpty()) {
            res += pq.poll();
        }
        return res;
    }

    // Heap + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 16 ms(75.35%), 40.7 MB(95.67%) for 81 tests
    public int largestValsFromLabels2(int[] values, int[] labels, int num_wanted, int use_limit) {
        int n = values.length;
        PriorityQueue<int[]> valueQueue =
                new PriorityQueue<>(n, Comparator.comparingInt(a -> -a[1]));
        for (int i = 0; i < n; i++) {
            valueQueue.offer(new int[] {labels[i], values[i]});
        }
        Map<Integer, Integer> count = new HashMap<>();
        int res = 0;
        for (int i = num_wanted; i > 0 && !valueQueue.isEmpty(); ) {
            int[] cur = valueQueue.poll();
            int label = cur[0];
            int used = count.getOrDefault(label, 0) + 1;
            count.put(label, used);
            if (used <= use_limit) {
                res += cur[1];
                i--;
            }
        }
        return res;
    }

    // Sort + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N + MAX(label))
    // 12 ms(99.25%), 40.3 MB(96.24%) for 81 tests
    public int largestValsFromLabels3(int[] values, int[] labels, int num_wanted, int use_limit) {
        int n = values.length;
        int[][] nums = new int[n][];
        int maxLabel = 0;
        for (int i = 0; i < n; i++) {
            nums[i] = new int[] {values[i], labels[i]};
            maxLabel = Math.max(maxLabel, labels[i]);
        }
        Arrays.sort(nums, (a, b) -> b[0] - a[0]);
        int res = 0;
        int[] used = new int[maxLabel + 1];
        for (int i = 0, k = num_wanted; i < n && k > 0; i++) {
            if (used[nums[i][1]]++ < use_limit) {
                res += nums[i][0];
                k--;
            }
        }
        return res;
    }

    private void test(int[] values, int[] labels, int num_wanted, int use_limit, int expected) {
        assertEquals(expected, largestValsFromLabels(values, labels, num_wanted, use_limit));
        assertEquals(expected, largestValsFromLabels2(values, labels, num_wanted, use_limit));
        assertEquals(expected, largestValsFromLabels3(values, labels, num_wanted, use_limit));
    }

    @Test public void test() {
        test(new int[] {4, 7, 4, 6, 3}, new int[] {2, 0, 0, 2, 2}, 1, 2, 7);
        test(new int[] {5, 4, 3, 2, 1}, new int[] {1, 1, 2, 2, 3}, 3, 1, 9);
        test(new int[] {5, 4, 3, 2, 1}, new int[] {1, 3, 3, 3, 2}, 3, 2, 12);
        test(new int[] {9, 8, 8, 7, 6}, new int[] {0, 0, 0, 1, 1}, 3, 1, 16);
        test(new int[] {9, 8, 8, 7, 6}, new int[] {0, 0, 0, 1, 1}, 3, 2, 24);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
