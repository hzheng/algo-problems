import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC658: https://leetcode.com/problems/find-k-closest-elements/
//
// Given a sorted array, two integers k and x, find the k closest elements to x 
// in the array. The result should also be sorted in ascending order. If there 
// is a tie, the smaller elements are always preferred.
public class FindClosestElements {
    // Binary Search + Sort
    // time complexity: O(log(N) + k), space complexity: O(k)
    // beats 32.03%(17 ms for 55 tests)
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        List<Integer> res = new ArrayList<>();
        int j = Arrays.binarySearch(arr, x);
        if (j < 0) {
            j = -j - 1;
        }
        for (int n = arr.length, i = j - 1; k > 0; k--) {
            if (i < 0 || j < n && Math.abs(x - arr[i]) > Math.abs(x - arr[j])) {
                res.add(arr[j++]);
            } else {
                res.add(arr[i--]);
            }
        }
        Collections.sort(res);
        return res;
    }

    // Binary Search + Sort
    // time complexity: O(log(N) + k), space complexity: O(k)
    // beats 39.47%(13 ms for 55 tests)
    public List<Integer> findClosestElements2(int[] arr, int k, int x) {
        int index = Arrays.binarySearch(arr, x);
        if (index < 0) {
            index = -index - 1;
        }
        int low = Math.max(0, index - k - 1);
        int high = Math.min(arr.length - 1, index + k - 1);
        while (high - low >= k) {
            if (low < 0 || (x - arr[low]) <= (arr[high] - x)) {
                high--;
            } else if (high > arr.length - 1 || (x - arr[low]) > (arr[high] - x)) {
                low++;
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = low; i <= high; i++) {
            res.add(arr[i]);
        }
        return res;
    }


    // Binary Search + Sort
    // time complexity: O(log(N - k) + k), space complexity: O(k)
    // beats 79.93%(8 ms for 55 tests)
    public List<Integer> findClosestElements3(int[] arr, int k, int x) {
        int low = 0;
        for (int high = arr.length - k; low < high; ) {
            int mid = (low + high) >>> 1;
            if (x - arr[mid] > arr[mid + k] - x) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = low; i < low + k; i++) {
            res.add(arr[i]);
        }
        return res;
    }

    // Heap + Sort
    // time complexity: O(N * log(k)), space complexity: O(k)
    // beats 10.30%(54 ms for 55 tests)
    public List<Integer> findClosestElements4(int[] arr, int k, int x) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k, new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                int diff = Math.abs(b - x) - Math.abs(a - x);
                return (diff != 0) ? diff : (b - a);
            }
        });
        for (int a : arr) {
            pq.offer(a);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        List<Integer> res = new ArrayList<>();
        res.addAll(pq);
        Collections.sort(res);
        return res;
    }

    void test(int[] arr, int k, int x, Integer[] expected) {
        assertEquals(Arrays.asList(expected), findClosestElements(arr, k, x));
        assertEquals(Arrays.asList(expected), findClosestElements2(arr, k, x));
        assertEquals(Arrays.asList(expected), findClosestElements3(arr, k, x));
        assertEquals(Arrays.asList(expected), findClosestElements4(arr, k, x));
    }

    @Test
    public void test() {
        test(new int[]{1}, 1, 1, new Integer[]{1});
        test(new int[]{1, 2, 3}, 2, 2, new Integer[]{1, 2});
        test(new int[]{1, 2, 3, 4, 5}, 4, 3, new Integer[]{1, 2, 3, 4});
        test(new int[]{1, 2, 3, 4, 5}, 4, -1, new Integer[]{1, 2, 3, 4});
        test(new int[]{-13, -8, -2, 0, 1, 2, 2, 4, 7}, 6, -3, 
             new Integer[]{-8, -2, 0, 1, 2, 2});
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
