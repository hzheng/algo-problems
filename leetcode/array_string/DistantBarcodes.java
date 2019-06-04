import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1054: https://leetcode.com/problems/distant-barcodes/
//
// In a warehouse, there is a row of barcodes, where the i-th barcode is barcodes[i].
// Rearrange the barcodes so that no two adjacent barcodes are equal.  You may return any answer,
// and it is guaranteed an answer exists.
// Note:
// 1 <= barcodes.length <= 10000
// 1 <= barcodes[i] <= 10000
public class DistantBarcodes {
    // Heap
    // time complexity: O(N + M * log(M)) (M is count of distinct values), space complexity: O(N)
    // 46 ms(67.64%), 39.6 MB(100%) for 57 tests
    public int[] rearrangeBarcodes(int[] barcodes) {
        int N = 10002;
        int[] count = new int[N];
        for (int b : barcodes) {
            count[b]++;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        for (int i = 0; i < N; i++) {
            if (count[i] > 0) {
                pq.offer(new int[]{count[i], i});
            }
        }
        int[] res = new int[barcodes.length];
        for (int i = 0; !pq.isEmpty(); ) {
            int[] first = pq.poll();
            res[i++] = first[1];
            if (pq.isEmpty()) {
                break;
            }
            int[] second = pq.poll();
            res[i++] = second[1];
            if (--first[0] > 0) {
                pq.offer(first);
            }
            if (--second[0] > 0) {
                pq.offer(second);
            }
        }
        return res;
    }

    // Heap
    // time complexity: O(N + M * log(M)) (M is count of distinct values), space complexity: O(N)
    // 42 ms(71.32%), 39.6 MB(100%) for 57 tests
    public int[] rearrangeBarcodes2(int[] barcodes) {
        int N = 10002;
        int[] count = new int[N];
        for (int b : barcodes) {
            count[b]++;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        for (int i = 0; i < N; i++) {
            if (count[i] > 0) {
                pq.offer(new int[]{count[i], i});
            }
        }
        int n = barcodes.length;
        int[] res = new int[n];
        for (int i = 0; !pq.isEmpty(); ) {
            int[] head = pq.poll();
            for (int j = head[0]; j > 0; j--) {
                res[i] = head[1];
                if ((i += 2) >= n) {
                    i = 1;
                }
            }
        }
        return res;
    }

    // Max
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(100.00%), 39.6 MB(100%) for 57 tests
    public int[] rearrangeBarcodes3(int[] barcodes) {
        int N = 10002;
        int maxIndex = 0;
        int[] count = new int[N];
        for (int b : barcodes) {
            if (++count[b] >= count[maxIndex]) {
                maxIndex = b;
            }
        }
        int n = barcodes.length;
        int[] res = new int[n];
        for (int i = 0, j = 0; i < N; i++) {
            for (int code = (i == 0) ? maxIndex : i; count[code]-- > 0; ) {
                res[j] = code;
                if ((j += 2) >= n) {
                    j = 1;
                }
            }
        }
        return res;
    }

    void test(int[] barcodes) {
        check(barcodes.clone(), rearrangeBarcodes(barcodes));
        check(barcodes.clone(), rearrangeBarcodes2(barcodes));
        check(barcodes.clone(), rearrangeBarcodes3(barcodes));
    }

    void check(int[] before, int[] after) {
        for (int i = 1; i < after.length; i++) {
            assertNotEquals(after[i - 1], after[i]);
        }
        Arrays.sort(before);
        Arrays.sort(after);
        assertArrayEquals(before, after);
    }

    @Test
    public void test() {
        test(new int[]{1, 1, 1, 2, 2, 2});
        test(new int[]{1, 1, 1, 1, 2, 2, 3, 3});
        test(new int[]{1, 7, 9, 1, 2, 4, 5, 4, 5, 8, 2, 1, 2, 6, 1, 1, 2, 2, 3, 3, 8, 3});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
