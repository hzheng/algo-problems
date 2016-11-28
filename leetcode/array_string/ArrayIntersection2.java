import java.util.*;
import java.util.stream.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC350: https://leetcode.com/problems/intersection-of-two-arrays-ii/
//
// Given two arrays, write a function to compute their intersection.
// Note:
// Each element in the result should appear as many times as it shows in both arrays.
public class ArrayIntersection2 {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 53.11%(7 ms)
    public int[] intersection(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums1) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        int[] intersection = new int[nums1.length];
        int n = 0;
        for (int num : nums2) {
            int count = map.getOrDefault(num, 0);
            if (count > 0) {
                intersection[n++] = num;
                map.put(num, --count);
            }
        }
        return Arrays.copyOf(intersection, n);
    }

    // Solution of Choice
    // Sort + Two Pointers
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 84.91%(4 ms)
    public int[] intersection2(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int n1 = nums1.length;
        int n2 = nums2.length;
        int n = 0;
        int[] intersection = new int[n1];
        for (int i = 0, j = 0; i < n1 && j < n2; ) {
            if (nums1[i] < nums2[j]) {
                i++;
            } else if (nums1[i] == nums2[j++]) {
                intersection[n++] = nums1[i++];
            }
        }
        return Arrays.copyOf(intersection, n);
    }

    // Sort + Binary Search
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 69.57%(5 ms)
    public int[] intersection3(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int n1 = nums1.length;
        int n2 = nums2.length;
        int n = 0;
        int[] intersection = new int[n1];
        for (int i = 0; i < n2; i++) {
            int num = nums2[i];
            int j = Arrays.binarySearch(nums1, num);
            if (j < 0) continue;

            while (j > 0 && nums1[j - 1] == num) {
                j--;
            }
            while (i < n2 && j < n1 && nums2[i] == num && nums1[j] == num) {
                intersection[n++] = num;
                i++;
                j++;
            }
            while (i < n2 && nums2[i] == num) {
                i++;
            }
            i--;
        }
        return Arrays.copyOf(intersection, n);
    }

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
        ArrayIntersection2 a = new ArrayIntersection2();
        test(a::intersection, nums1, nums2, expected);
        test(a::intersection2, nums1, nums2, expected);
        test(a::intersection3, nums1, nums2, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1}, new int[] {1, 1}, 1);
        test(new int[] {1, 2, 2, 1}, new int[] {2, 2}, 2, 2);
        test(new int[] {1, 2, 3, 4, 5, 6}, new int[] {2, 7, 5, 2, 3}, 2, 3, 5);
        test(new int[] {4, 9, 5}, new int[] {9, 4, 9, 8, 4}, 4, 9);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ArrayIntersection2");
    }
}
