import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC373: https://leetcode.com/problems/find-k-pairs-with-smallest-sums/
//
// You are given two integer arrays nums1 and nums2 sorted in ascending order
// and an integer k. Define a pair (u,v) which consists of one element from the
// first array and one element from the second array.
// Find the k pairs (u1,v1),(u2,v2) ...(uk,vk) with the smallest sums.
public class KSmallestPairs {
    // Max Heap
    // time complexity: O(min(M, K) * min(N, K) * log(K)), space complexity: O(K)
    // beats 50.03%(11 ms for 27 tests)
    public List<int[]> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return b[0] + b[1] - a[0] - a[1];
            }
        });
        int n1 = Math.min(nums1.length, k);
        int n2 = Math.min(nums2.length, k);
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                int[] pair = {nums1[i], nums2[j]};
                if (pq.size() < k) {
                    pq.offer(pair);
                } else {
                    int[] topPair = pq.peek();
                    // early break improves performance
                    if (topPair[0] + topPair[1] <= pair[0] + pair[1]) break;

                    pq.poll();
                    pq.offer(pair);
                }
            }
        }
        return new ArrayList<>(pq);
    }

    // Solution of Choice
    // Min Heap
    // time complexity: O(K * log(K)), space complexity: O(K)
    // beats 77.24%(7 ms for 27 tests)
    public List<int[]> kSmallestPairs2(int[] nums1, int[] nums2, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] + a[1] - b[0] - b[1];
            }
        });
        List<int[]> res = new ArrayList<>();
        int n1 = Math.min(nums1.length, k);
        int n2 = Math.min(nums2.length, k);
        if (n1 == 0 || n2 == 0) return res;

        for (int i = 0; i < n1; i++) {
            pq.offer(new int[] {nums1[i], nums2[0], 0});
        }
        while (k-- > 0 && !pq.isEmpty()) {
            int[] cur = pq.poll();
            res.add(new int[] {cur[0], cur[1]});
            if (++cur[2] < n2) {
                pq.offer(new int[] {cur[0], nums2[cur[2]], cur[2]});
            }
        }
        return res;
    }

    // Min Heap
    // time complexity: O(K * log(min(N, K)), space complexity: O(K)
    // beats 64.78%(8 ms for 27 tests)
    public List<int[]> kSmallestPairs3(int[] nums1, int[] nums2, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return nums1[a[0]] + nums2[a[1]] - nums1[b[0]] - nums2[b[1]];
            }
        });
        List<int[]> res = new ArrayList<>();
        int n1 = Math.min(nums1.length, k);
        int n2 = Math.min(nums2.length, k);
        if (n1 == 0 || n2 == 0) return res;

        pq.offer(new int[]{0, 0});
        while (k-- > 0 && !pq.isEmpty()) {
            int[] index = pq.poll();
            res.add(new int[] {nums1[index[0]], nums2[index[1]]});
            if (index[0] + 1 < n1) {
                pq.offer(new int[]{index[0] + 1, index[1]});
            }
            if (index[0] == 0 && index[1] + 1 < n2) { // avoid duplicate
                pq.offer(new int[]{index[0], index[1] + 1});
            }
        }
        return res;
    }

    // time complexity: O(K * min(M, N, K)), space complexity: O(max(M, N, K))
    // beats 50.03%(11 ms for 27 tests)
    public List<int[]> kSmallestPairs4(int[] nums1, int[] nums2, int k) {
        int n1 = Math.min(nums1.length, k);
        int n2 = Math.min(nums2.length, k);
        k = Math.min(k, n1 * n2);
        List<int[]> res = new ArrayList<>();
        int[] next = new int[n1];
        while (k-- > 0) {
            int min = Integer.MAX_VALUE;
            int i = 0;
            for (int j = 0; j < n1; j++) {
                if (next[j] < n2 && nums1[j] + nums2[next[j]] < min) {
                    i = j;
                    min = nums1[j] + nums2[next[j]];
                }
            }
            res.add(new int[] {nums1[i], nums2[next[i]++]});
        }
        return res;
    }

    // TODO: time complexity: O(K)
    // https://discuss.leetcode.com/topic/53380/o-k-solution

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(Function<int[], int[], Integer, List<int[]> > kSmallestPairs,
              int[] nums1, int[] nums2, int k, int[][] expected) {
        List<int[]> res = kSmallestPairs.apply(nums1, nums2, k);
        Collections.sort(res, ((a, b) -> {
            int diff = a[0] + a[1] - b[0] - b[1];
            return diff != 0 ? diff : a[0] - b[0];
        }));
        assertArrayEquals(expected, res.toArray(new int[0][0]));
    }

    void test(int[] nums1, int[] nums2, int k, int[][] expected) {
        KSmallestPairs p = new KSmallestPairs();
        test(p::kSmallestPairs, nums1, nums2, k, expected);
        test(p::kSmallestPairs2, nums1, nums2, k, expected);
        test(p::kSmallestPairs3, nums1, nums2, k, expected);
        test(p::kSmallestPairs4, nums1, nums2, k, expected);
    }

    @Test
    public void test1() {
        test(new int[] {3, 5, 7, 9}, new int[0], 1, new int[][] {});
        test(new int[] {1, 1, 2}, new int[] {1, 2, 3}, 10,
             new int[][] {{1, 1}, {1, 1}, {1, 2}, {1, 2}, {2, 1}, {1, 3},
                          {1, 3}, {2, 2}, {2, 3}});
        test(new int[] {1, 7, 11}, new int[] {2, 4, 6}, 3,
             new int[][] {{1, 2}, {1, 4}, {1, 6}});
        test(new int[] {1, 1, 2}, new int[] {1, 2, 3}, 2,
             new int[][] {{1, 1}, {1, 1}});
        test(new int[] {1, 2}, new int[] {3}, 3, new int[][] {{1, 3}, {2, 3}});
        test(new int[] {-10, -4, 0, 0, 6}, new int[] {3, 5, 6, 7, 8, 100}, 10,
             new int[][] {{-10, 3}, {-10, 5}, {-10, 6}, {-10, 7}, {-10, 8},
                          {-4, 3}, {-4, 5}, {-4, 6}, {-4, 7}, {0, 3}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KSmallestPairs");
    }
}
