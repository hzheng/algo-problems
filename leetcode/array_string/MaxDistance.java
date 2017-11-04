import java.util.*;

import org.junit.Test;

import common.Utils;

import static org.junit.Assert.*;

// LC624: https://leetcode.com/problems/maximum-distance-in-arrays/
//
// Given m arrays, and each array is sorted in ascending order. Now you can pick
// up two integers from two different arrays (each array picks one) and
// calculate the distance. We define the distance between two integers a and b
// to be their absolute difference |a-b|. Find the maximum distance.
// Note:
// Each given array will have at least 1 number. There will be at least two
// non-empty arrays. The total number of the integers in all the m arrays will
// be in the range of [2, 10000].
// The integers in the m arrays will be in the range of [-10000, 10000].
public class MaxDistance {
    // time complexity: O(N), space complexity: O(1)
    // beats 72.75%(18 ms for 124 tests)
    public int maxDistance(List<List<Integer> > arrays) {
        int minVal1 = Integer.MAX_VALUE;
        List<Integer> minIndex1 = new ArrayList<>();
        int minVal2 = Integer.MAX_VALUE;
        List<Integer> minIndex2 = new ArrayList<>();
        int maxVal1 = Integer.MIN_VALUE;
        List<Integer> maxIndex1 = new ArrayList<>();
        int maxVal2 = Integer.MIN_VALUE;
        List<Integer> maxIndex2 = new ArrayList<>();
        int i = -1;
        for (List<Integer> array : arrays) {
            i++;
            int first = array.get(0);
            if (first < minVal1) {
                minVal2 = minVal1;
                minVal1 = first;
                minIndex2 = minIndex1;
                minIndex1 = new ArrayList<>();
                minIndex1.add(i);
            } else if (first == minVal1) {
                minIndex1.add(i);
            } else if (first < minVal2) {
                minVal2 = first;
                minIndex2.clear();
                minIndex2.add(i);
            } else if (first == minVal2) {
                minIndex2.add(i);
            }
            int last = array.get(array.size() - 1);
            if (last > maxVal1) {
                maxVal2 = maxVal1;
                maxVal1 = last;
                maxIndex2 = maxIndex1;
                maxIndex1 = new ArrayList<>();
                maxIndex1.add(i);
            } else if (last == maxVal1) {
                maxIndex1.add(i);
            } else if (last > maxVal2) {
                maxVal2 = last;
                maxIndex2.clear();
                maxIndex2.add(i);
            } else if (last == maxVal2) {
                maxIndex2.add(i);
            }
        }
        if (minIndex1.size() > 1 || maxIndex1.size() > 1
            || minIndex1.get(0) != maxIndex1.get(0)) return maxVal1 - minVal1;

        return Math.max(maxVal2 - minVal1, maxVal1 - minVal2);
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 72.75%(18 ms for 124 tests)
    public int maxDistance2(List<List<Integer> > arrays) {
        int res = 0;
        int minVal = arrays.get(0).get(0);
        int maxVal = arrays.get(0).get(arrays.get(0).size() - 1);
        for (int i = arrays.size() - 1; i > 0; i--) {
            List<Integer> array = arrays.get(i);
            res = Math.max(res, Math.max(
                               Math.abs(array.get(array.size() - 1) - minVal),
                               Math.abs(maxVal - array.get(0))));
            minVal = Math.min(minVal, array.get(0));
            maxVal = Math.max(maxVal, array.get(array.size() - 1));
        }
        return res;
    }

    void test(Integer[][] arrays, int expected) {
        List<List<Integer> > arrayList = Utils.toList(arrays);
        assertEquals(expected, maxDistance(arrayList));
        assertEquals(expected, maxDistance2(arrayList));
    }

    @Test
    public void test() {
        test(new Integer[][] {{1, 2, 3}, {4, 5}, {1, 2, 3}}, 4);
        test(new Integer[][] {{1}, {2}}, 1);
        test(new Integer[][] {{1, 5}, {3, 4}}, 3);
        test(new Integer[][] {{1, 4}, {0, 5}}, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
