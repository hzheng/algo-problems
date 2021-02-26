import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1200: https://leetcode.com/problems/minimum-absolute-difference/
//
// Given an array of distinct integers arr, find all pairs of elements with the minimum absolute
// difference of any two elements. Return a list of pairs in ascending order(with respect to pairs),
// each pair [a, b] follows
// a, b are from arr
// a < b
// b - a equals to the minimum absolute difference of any two elements in arr
//
// Constraints:
// 2 <= arr.length <= 10^5
// -10^6 <= arr[i] <= 10^6
public class MinimumAbsDifference {
    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 14 ms(98.02%), 50.2 MB(51.13%) for 42 tests
    public List<List<Integer>> minimumAbsDifference(int[] arr) {
        Arrays.sort(arr);
        int minDiff = Integer.MAX_VALUE;
        for (int i = 1, n = arr.length; i < n; i++) {
            minDiff = Math.min(minDiff, arr[i] - arr[i - 1]);
        }
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 1, n = arr.length; i < n; i++) {
            if (arr[i] - arr[i - 1] == minDiff) {
                res.add(Arrays.asList(arr[i - 1], arr[i]));
            }
        }
        return res;
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 14 ms(98.02%), 49.5 MB(97.73%) for 42 tests
    public List<List<Integer>> minimumAbsDifference2(int[] arr) {
        Arrays.sort(arr);
        List<List<Integer>> res = new ArrayList<>();
        int minDiff = Integer.MAX_VALUE;
        for (int i = 1, n = arr.length; i < n; i++) {
            if (arr[i] - arr[i - 1] > minDiff) { continue; }

            if (arr[i] - arr[i - 1] < minDiff) {
                minDiff = arr[i] - arr[i - 1];
                res = new ArrayList<>();
            }
            res.add(Arrays.asList(arr[i - 1], arr[i]));
        }
        return res;
    }

    private void test(int[] arr, Integer[][] expected) {
        List<List<Integer>> expectedList = Utils.toList(expected);
        assertEquals(expectedList, minimumAbsDifference(arr));
        assertEquals(expectedList, minimumAbsDifference2(arr));
    }

    @Test public void test() {
        test(new int[] {4, 2, 1, 3}, new Integer[][] {{1, 2}, {2, 3}, {3, 4}});
        test(new int[] {1, 3, 6, 10, 15}, new Integer[][] {{1, 3}});
        test(new int[] {3, 8, -10, 23, 19, -4, -14, 27},
             new Integer[][] {{-14, -10}, {19, 23}, {23, 27}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
