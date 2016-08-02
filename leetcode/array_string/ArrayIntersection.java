import java.util.*;
import java.util.stream.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/intersection-of-two-arrays/
//
// Given two arrays, write a function to compute their intersection.
// Note:
// Each element in the result must be unique.
public class ArrayIntersection {
    // Hash Table
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

    // Sort + Two Pointers
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

    // Sort + Binary Search
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 56.97%(6 ms)
    public int[] intersection3(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Set<Integer> intersection = new HashSet<>();
        for (int num : nums2) {
            if (Arrays.binarySearch(nums1, num) >= 0) {
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

    // FP
    // beats 0.75%(125 ms)
    public int[] intersection4(int[] nums1, int[] nums2) {
        Set<Integer> set = Arrays.stream(nums2).boxed().collect(Collectors.toSet());
        return Arrays.stream(nums1).distinct().filter(e -> set.contains(e)).toArray();
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], int[], int[]> intersection,
              int[] nums1, int[] nums2, int... expected) {
        int[] res = intersection.apply(nums1.clone(), nums2.clone());
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(int[] nums1, int[] nums2, int... expected) {
        ArrayIntersection a = new ArrayIntersection();
        test(a::intersection, nums1, nums2, expected);
        test(a::intersection2, nums1, nums2, expected);
        test(a::intersection3, nums1, nums2, expected);
        test(a::intersection4, nums1, nums2, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 2, 1}, new int[] {2, 2}, 2);
        test(new int[] {1, 2, 3, 4, 5, 6}, new int[] {2, 7, 5, 2, 3}, 2, 3, 5);
        test(new int[] {4, 9, 5}, new int[] {9, 4, 9, 8, 4}, 4, 9);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ArrayIntersection");
    }
}
