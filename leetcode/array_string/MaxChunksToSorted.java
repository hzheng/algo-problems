import org.junit.Test;
import static org.junit.Assert.*;

// LC769: https://leetcode.com/problems/max-chunks-to-make-sorted
//
// Given an array arr that is a permutation of [0, 1, ..., arr.length - 1], we
// split the array into some number of partitions, and individually sort each
// chunk.  After concatenating them, the result equals the sorted array.
// What is the most number of chunks we could have made?
public class MaxChunksToSorted {
    // time complexity: O(N), space complexity: O(1)
    // beats %(4 ms for 52 tests)
    public int maxChunksToSorted(int[] arr) {
        int res = 0;
        int n = arr.length;
        int min = n - 1;
        int max = n - 1;
        for (int i = n - 1, count = 0; i >= 0; i--) {
            min = Math.min(min, arr[i]);
            if (count++ == max - min) {
                res++;
                max = min - 1;
                count = 0;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(4 ms for 52 tests)
    public int maxChunksToSorted2(int[] arr) {
        int res = 0;
        for (int i = 0, max = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
            if (max == i) {
                res++;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(4 ms for 52 tests)
    public int maxChunksToSorted3(int[] arr) {
        int res = 0;
        outer : for (int i = 0, max = 0; i < arr.length; i++) {
            for (int j = max; j <= i; j++) {
                if (arr[j] > i) continue outer;
            }
            max = i;
            res++;
        }
        return res;
    }

    void test(int[] arr, int expected) {
        assertEquals(expected, maxChunksToSorted(arr));
        assertEquals(expected, maxChunksToSorted2(arr));
        assertEquals(expected, maxChunksToSorted3(arr));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 0, 3}, 2);
        test(new int[] {1, 4, 3, 6, 0, 7, 8, 2, 5}, 1);
        test(new int[] {1, 0, 4, 3, 2, 7, 6, 5}, 3);
        test(new int[] {4, 3, 2, 1, 0}, 1);
        test(new int[] {1, 0, 2, 3, 4}, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
