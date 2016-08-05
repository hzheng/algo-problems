import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/find-k-pairs-with-smallest-sums/
//
// You are given two integer arrays nums1 and nums2 sorted in ascending order
// and an integer k. Define a pair (u,v) which consists of one element from the
// first array and one element from the second array.
// Find the k pairs (u1,v1),(u2,v2) ...(uk,vk) with the smallest sums.
public class KSmallestPairs {
    // time complexity: O(K ^ 2)
    // beats 56.74%(13 ms)
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
                int[] pair = new int[]{nums1[i], nums2[j]};
                if (pq.size() < k) {
                    pq.offer(pair);
                } else {
                    int[] topPair = pq.peek();
                    if (topPair[0] + topPair[1] <= pair[0] + pair[1]) break;

                    pq.poll();
                    pq.offer(pair);
                }
            }
        }
        return new ArrayList<>(pq);
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(Function<int[], int[], Integer, List<int[]> > kSmallestPairs,
              int[] nums1, int[] nums2, int k, int[][] expected) {
        List<int[]> res = kSmallestPairs.apply(nums1, nums2, k);
        assertArrayEquals(expected, res.toArray(new int[0][0]));
    }

    void test(int[] nums1, int[] nums2, int k, int[][] expected) {
        KSmallestPairs p = new KSmallestPairs();
        test(p::kSmallestPairs, nums1, nums2, k, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 7, 11}, new int[] {2, 4, 6}, 3,
             new int[][] {{1, 2}, {1, 4}, {1, 6}});
        test(new int[] {1, 1, 2}, new int[] {1, 2, 3}, 2,
             new int[][] {{1, 1}, {1, 1}});
        test(new int[] {1, 2}, new int[] {3}, 3, new int[][] {{1, 3}, {2, 3}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KSmallestPairs");
    }
}
