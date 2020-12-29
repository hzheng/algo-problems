import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1089: https://leetcode.com/problems/duplicate-zeros/
//
// Given a fixed length array arr of integers, duplicate each occurrence of zero, shifting the
// remaining elements to the right. Note that elements beyond the length of the original array are
// not written. Do the above modifications to the input array in place, do not return anything from
// your function.
//
// Note:
// 1 <= arr.length <= 10000
// 0 <= arr[i] <= 9
public class DuplicateZeros {
    // Queue
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(61.21%), 40.3 MB(10.16%) for 30 tests
    public void duplicateZeros(int[] arr) {
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0, n = arr.length; i < n; i++) {
            int a = arr[i];
            queue.offer(a);
            arr[i] = queue.poll();
            if (a == 0) {
                queue.offer(0);
            }
        }
    }

    // time complexity: O(N), space complexity: O(1)
    // 1 ms(89.47%), 39 MB(81.83%) for 30 tests
    public void duplicateZeros2(int[] arr) {
        int n = arr.length;
        int i = 0;
        int j = n - 1;
        int zeros = 0;
        for (; i + zeros < n; i++) {
            zeros += (arr[i] == 0) ? 1 : 0;
        }
        if (arr[i - 1] == 0 && i + zeros == n) {
            arr[j--] = 0;
        }
        for (i--; i >= 0 && i < j; i--, j--) {
            arr[j] = arr[i];
            if (i > 0 && arr[i - 1] == 0) {
                arr[--j] = 0;
            }
        }
    }

    // time complexity: O(N), space complexity: O(1)
    // 1 ms(89.47%), 38.9 MB(95.72%) for 30 tests
    public void duplicateZeros3(int[] arr) {
        int n = arr.length;
        int i = 0;
        int zeros = 0;
        for (; i + zeros < n; i++) {
            zeros += (arr[i] == 0) ? 1 : 0;
        }
        for (i--; zeros > 0; i--) {
            if (i + zeros < n) {
                arr[i + zeros] = arr[i];
            }
            if (arr[i] == 0) {
                arr[i + --zeros] = 0;
            }
        }
    }

    interface Function<T> {
        void apply(T t);
    }

    private void test(Function<int[]> duplicateZeros, int[] arr, int[] expected) {
        int[] clone = arr.clone();
        duplicateZeros.apply(clone);
        assertArrayEquals(expected, clone);

    }

    private void test(int[] arr, int[] expected) {
        DuplicateZeros d = new DuplicateZeros();
        test(d::duplicateZeros, arr, expected);
        test(d::duplicateZeros2, arr, expected);
        test(d::duplicateZeros3, arr, expected);
    }

    @Test public void test() {
        test(new int[] {1, 0, 0, 2, 3, 1, 0, 4, 0}, new int[] {1, 0, 0, 0, 0, 2, 3, 1, 0});
        test(new int[] {1, 0, 0, 2, 3, 0, 4, 5, 0}, new int[] {1, 0, 0, 0, 0, 2, 3, 0, 0});
        test(new int[] {1, 0, 2, 3, 0, 4, 5, 0}, new int[] {1, 0, 0, 2, 3, 0, 0, 4});
        test(new int[] {1, 2, 3}, new int[] {1, 2, 3});
        test(new int[] {0, 0, 3}, new int[] {0, 0, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
