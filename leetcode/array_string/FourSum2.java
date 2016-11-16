import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC454: https://leetcode.com/problems/4sum-ii/
//
// Given four lists A, B, C, D of integer values, compute how many tuples
// (i, j, k, l) there are such that A[i] + B[j] + C[k] + D[l] is zero.
// To make problem a bit easier, all A, B, C, D have same length of N where
// 0 ≤ N ≤ 500. All integers are in the range of -2 ^ 28 to 2 ^ 28 - 1 and the
// result is guaranteed to be at most 2 ^ 31 - 1.
public class FourSum2 {
    // Hash Table
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats N/A(144 ms for 48 tests)
    public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
        Map<Integer, Integer> sumMap = new HashMap<>();
        for (int a : A) {
            for (int b : B) {
                sumMap.put(a + b, sumMap.getOrDefault(a + b, 0) + 1);
            }
        }
        int count = 0;
        for (int c : C) {
            for (int d : D) {
                count += sumMap.getOrDefault(-c - d, 0);
            }
        }
        return count;
    }

    // SortedMap
    // Time Limit Exceeded
    public int fourSumCount2(int[] A, int[] B, int[] C, int[] D) {
        NavigableMap<Integer, Integer> map1 = new TreeMap<>();
        countSum(A, B, map1, true);
        NavigableMap<Integer, Integer> map2 = new TreeMap<>();
        countSum(C, D, map2, false);
        int count = 0;
        Integer[]  sums1 = map1.keySet().toArray(new Integer[0]);
        while (!map1.isEmpty()) {
            Map.Entry<Integer, Integer> ab = map1.pollFirstEntry();
            int sum1 = ab.getKey();
            while (true) {
                Map.Entry<Integer, Integer> cd = map2.pollFirstEntry();
                if (cd == null) return count;

                int sum2 = cd.getKey();
                if (sum1 == sum2) {
                    count += ab.getValue() * cd.getValue();
                    break;
                }
                if (sum1 < sum2) {
                    map2.put(sum2, cd.getValue());
                    break;
                }
            }
        }
        return count;
    }

    // Hash Table + Binary Search
    // Time Limit Exceeded
    public int fourSumCount3(int[] A, int[] B, int[] C, int[] D) {
        Map<Integer, Integer> sumMap = new HashMap<>();
        for (int a : A) {
            for (int b : B) {
                sumMap.put(a + b, sumMap.getOrDefault(a + b, 0) + 1);
            }
        }
        Arrays.sort(D);
        int count = 0;
        int n = D.length;
        for (int c : C) {
            for (int sum : sumMap.keySet()) {
                int i = Arrays.binarySearch(D, -c - sum);
                if (i < 0) continue;

                int j = i;
                for (; j + 1 < n && D[j + 1] == D[i]; j++) {}
                for (; i > 0 && D[i - 1] == D[i]; i--) {}
                count += sumMap.get(sum) * (j - i + 1);
            }
        }
        return count;
    }

    private void countSum(int[] A, int[] B, Map<Integer, Integer> map, boolean positive) {
        for (int a : A) {
            for (int b : B) {
                int sum = positive ? a + b : -(a + b);
                map.put(sum, map.getOrDefault(sum, 0) + 1);
            }
        }
    }

    void test(int[] A, int[] B, int[] C, int[] D, int expected) {
        assertEquals(expected, fourSumCount(A, B, C, D));
        assertEquals(expected, fourSumCount2(A, B, C, D));
        assertEquals(expected, fourSumCount3(A, B, C, D));
    }

    @Test
    public void test() {
        test(new int[] {1, 2}, new int[] {-2, -1}, new int[] {-1, 2},
             new int[] {0, 2}, 2);
        test(new int[] {1, 2}, new int[] {-4, 3},
             new int[] {-1}, new int[] {3, -3, 3}, 3);
        test(new int[] {1, 3, 8, 9, 4, 2, 1},
             new int[] {-4, 3, 9, -8, 0, 2, -1},
             new int[] {2, 3, 9, 2, -10, 2, -1},
             new int[] {-4, 3, -3, -8, 3, -12, 11}, 79);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FourSum2");
    }
}
