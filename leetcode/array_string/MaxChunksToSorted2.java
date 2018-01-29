import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC768: https://leetcode.com/problems/max-chunks-to-make-sorted-ii/
//
// Given an array arr of integers(not necessarily distinct), we split the array
// into some number of "chunks" (partitions), and individually sort each chunk.
// After concatenating them, the result equals the sorted array.
// What is the most number of chunks we could have made?
public class MaxChunksToSorted2 {
    // Hash Table + Sort
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats %(94 ms for 139 tests)
    public int maxChunksToSorted(int[] arr) {
        int n = arr.length;
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        int[] order = new int[n];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int k = arr[i];
            int last = map.getOrDefault(k, -1);
            if (last >= 0) {
                last++;
            } else {
                for (int j = 0; j < n; j++) {
                    if (k == sorted[j]) {
                        last = j;
                        break;
                    }
                }
            }
            order[i] = last;
            map.put(k, last);
        }
        return maxDistinctChunksToSorted(order);
    }

    private int maxDistinctChunksToSorted(int[] arr) {
        int res = 0;
        for (int i = 0, max = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
            if (max == i) {
                res++;
            }
        }
        return res;
    }

    // Hash Table + Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats %(27 ms for 139 tests)
    public int maxChunksToSorted2(int[] arr) {
        Map<Integer, Integer> count = new HashMap<>();
        int res = 0;
        int diff = 0;
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        for (int i = 0; i < arr.length; i++) {
            int x = arr[i];
            int cnt = count.getOrDefault(x, 0) + 1;
            count.put(x,  cnt);
            if (cnt == 0) {
                diff--;
            } else if (cnt == 1) {
                diff++;
            }
            int y = sorted[i];
            cnt = count.getOrDefault(y, 0) - 1;
            count.put(y, cnt);
            if (cnt == -1) {
                diff++;
            } else if (cnt == 0) {
                diff--;
            }
            if (diff == 0) {
                res++;
            }
        }
        return res;
    }

    // Hash Table + Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats %(38 ms for 139 tests)
    public int maxChunksToSorted3(int[] arr) {
        Map<Integer, Integer> countMap = new HashMap<>();
        List<Pair> countList = new ArrayList<>();
        for (int x : arr) {
            countMap.put(x, countMap.getOrDefault(x, 0) + 1);
            countList.add(new Pair(x, countMap.get(x)));
        }
        List<Pair> expect = new ArrayList<>(countList);
        Collections.sort(expect);
        int res = 0;
        Pair max = countList.get(0);
        for (int i = 0; i < arr.length; ++i) {
            Pair x = countList.get(i);
            Pair y = expect.get(i);
            if (x.compareTo(max) > 0) {
                max = x;
            }
            if (max.compareTo(y) == 0) {
                res++;
            }
        }
        return res;
    }

    class Pair implements Comparable<Pair> {
        int val, count;
        Pair(int v, int c) {
            val = v;
            count = c;
        }

        public int compareTo(Pair that) {
            return this.val != that.val
                   ? this.val - that.val : this.count - that.count;
        }
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats %(44 ms for 139 tests)
    public int maxChunksToSorted4(int[] arr) {
        int n = arr.length;
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = (long)arr[i] << 32 | i;
        }
        long[] sorted = Arrays.copyOf(a, n);
        Arrays.sort(sorted);
        int[] order = new int[n];
        for (int i = 0; i < n; i++) {
            order[i] = Arrays.binarySearch(sorted, a[i]);
        }
        int res = 0;
        outer : for (int i = 0, max = 0; i < n; i++) {
            for (int j = max; j <= i; j++) {
                if (order[j] > i) continue outer;
            }
            max = i;
            res++;
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats %(15 ms for 139 tests)
    public int maxChunksToSorted5(int[] arr) {
        int n = arr.length;
        int[] maxLeft = new int[n];
        int[] minRight = new int[n];
        maxLeft[0] = arr[0];
        for (int i = 1; i < n; i++) {
            maxLeft[i] = Math.max(maxLeft[i - 1], arr[i]);
        }
        minRight[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            minRight[i] = Math.min(minRight[i + 1], arr[i]);
        }
        int res = 1;
        for (int i = 0; i < n - 1; i++) {
            if (maxLeft[i] <= minRight[i + 1]) {
                res++;
            }
        }
        return res;
    }

    void test(int[] arr, int expected) {
        assertEquals(expected, maxChunksToSorted(arr));
        assertEquals(expected, maxChunksToSorted2(arr));
        assertEquals(expected, maxChunksToSorted3(arr));
        assertEquals(expected, maxChunksToSorted4(arr));
        assertEquals(expected, maxChunksToSorted5(arr));
    }

    @Test
    public void test() {
        test(new int[] {5, 4, 3, 2, 1}, 1);
        test(new int[] {2, 1, 3, 4, 4}, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
