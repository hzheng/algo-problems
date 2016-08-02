import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/intersection-of-two-arrays/
//
// Given two arrays, write a function to compute their intersection.
// Note:
// Each element in the result must be unique.
public class ArrayIntersection {
    // time complexity: O(N), space complexity: O(N)
    // beats 35.29%(7 ms)
    public int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums1) {
            set.add(num);
        }
        Set<Integer> intersection = new HashSet<>();
        for (int num : nums2) {
            if (set.contains(num)) {
                intersection.add(num);
            }
        }
        int[] res = new int[intersection.size()];
        int i = 0;
        for (int x : intersection) {
            res[i++] = x;
        }
        return res;
    }

    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 35.29%(7 ms)
    public int[] intersection2(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        List<Integer> intersection = new LinkedList<>();
        int n1 = nums1.length;
        int n2 = nums2.length;
        for (int i = 0, j = 0; i < n1 && j < n2; ) {
            if (nums1[i] < nums2[j]) {
                i++;
            } else if (nums1[i] > nums2[j]) {
                j++;
            } else {
                int size = intersection.size();
                if (size == 0 || nums1[i] != intersection.get(size - 1)) {
                    intersection.add(nums1[i]);
                }
                i++;
                j++;
            }
        }
        int[] res = new int[intersection.size()];
        int i = 0;
        for (int x : intersection) {
            res[i++] = x;
        }
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], int[], int[]> intersection,
              int[] nums1, int[] nums2, int... expected) {
        int[] res = intersection.apply(nums1, nums2);
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(int[] nums1, int[] nums2, int... expected) {
        ArrayIntersection a = new ArrayIntersection();
        test(a::intersection, nums1, nums2, expected);
        test(a::intersection2, nums1, nums2, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 2, 1}, new int[] {2, 2}, 2);
        test(new int[] {1, 2, 3, 4, 5, 6}, new int[] {2, 7, 5, 2, 3}, 2, 3, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ArrayIntersection");
    }
}
