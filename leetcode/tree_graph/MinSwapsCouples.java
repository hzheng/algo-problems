import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC765: https://leetcode.com/problems/couples-holding-hands/
//
// N couples sit in 2N seats arranged in a row and want to hold hands. We want to know the minimum
// number of swaps so that every couple is sitting side by side. A swap consists of choosing any two
// people, then they stand up and switch seats. The people and seats are represented by an integer
// from 0 to 2N-1, the couples are numbered in order, the first couple being (0, 1), the second
// couple being (2, 3), and so on with the last couple being (2N-2, 2N-1). The couples' initial
// seating is given by row[i] being the value of the one who's initially sitting in the i-th seat.
//
// Note:
// len(row) is even and in the range of [4, 60].
// row is guaranteed to be a permutation of 0...len(row)-1.
public class MinSwapsCouples {
    // SortedSet
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 3 ms(6.77%), 36.5 MB(56.97%) for 56 tests
    public int minSwapsCouples(int[] row) {
        NavigableSet<Integer> set = new TreeSet<>();
        int n = row.length / 2;
        for (int i = 0; i < n; i++) {
            int first = row[2 * i];
            int second = row[2 * i + 1];
            int tmp = Math.max(first, second);
            first = Math.min(first, second);
            second = tmp;
            if (second - first != 1 || first % 2 == 1) {
                set.add(first / 2 * n + second / 2);
            }
        }
        int res = 0;
        for (; !set.isEmpty(); res++) {
            int first = set.pollFirst();
            if (set.isEmpty() || set.first() / n != first / n) {
                continue;
            }
            int second = set.pollFirst();
            int swap = first % n * n + second % n;
            if (set.remove(swap)) {
                res++;
            } else {
                set.add(swap);
            }
        }
        return res;
    }

    // Union Find
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 36.3 MB(74.70%) for 56 tests
    public int minSwapsCouples2(int[] row) {
        int n = row.length / 2;
        int[] id = new int[n + 1];
        Arrays.fill(id, -1);
        id[n] = n; // last element stores count
        for (int i = 0; i < n; i++) {
            union(id, row[2 * i] / 2, row[2 * i + 1] / 2);
        }
        return n - id[n];
    }

    private boolean union(int[] id, int x, int y) {
        int px = root(id, x);
        int py = root(id, y);
        if (px == py) { return false; }

        if (id[py] < id[px]) {
            int tmp = px;
            px = py;
            py = tmp;
        }
        id[px] += id[py];
        id[py] = px;
        id[id.length - 1]--;
        return true;
    }

    private int root(int[] id, int x) {
        int parent = x;
        for (; id[parent] >= 0; parent = id[parent]) {}
        return parent;
    }

    // Greedy + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 36.5 MB(39.24%) for 56 tests
    public int minSwapsCouples3(int[] row) {
        Map<Integer, Integer> map = new HashMap<>();
        int n = row.length / 2;
        for (int i = 0; i < row.length; i++) {
            map.put(row[i], i);
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            int first = row[2 * i];
            int second = row[2 * i + 1];
            if (Math.abs(first - second) != 1 || Math.min(first, second) % 2 != 0) {
                swap(map, row, 2 * i + 1, map.get(first + 1 - (first % 2 * 2)));
                res++;
            }
        }
        return res;
    }

    private void swap(Map<Integer, Integer> map, int[] row, int i, int j) {
        int tmp = row[i];
        map.put(row[i] = row[j], i);
        map.put(row[j] = tmp, j);
    }

    // Greedy + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 36.2 MB(85.86%) for 56 tests
    public int minSwapsCouples4(int[] row) {
        int res = 0;
        int n = row.length;
        int[] pair = new int[n];
        int[] pos = new int[n];
        for (int i = 0; i < n; i++) {
            pair[i] = i + 1 - i % 2 * 2;
            pos[row[i]] = i;
        }
        for (int i = 0; i < n / 2; i++) {
            int first = row[2 * i];
            int second = row[2 * i + 1];
            if (Math.abs(first - second) != 1 || Math.min(first, second) % 2 != 0) {
                int tgt = pos[pair[first]];
                swap(pos, second, row[tgt]);
                swap(row, 2 * i + 1, tgt);
                res++;
            }
        }
        return res;
    }

    // Greedy + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 36.6 MB(39.24%) for 56 tests
    public int minSwapsCouples5(int[] row) {
        int res = 0;
        int n = row.length;
        int[] pair = new int[n];
        int[] pos = new int[n];
        for (int i = 0; i < n; i++) {
            pair[i] = i + 1 - i % 2 * 2;
            pos[row[i]] = i;
        }
        for (int i = 0; i < n; i++) {
            for (int j = pair[pos[pair[row[i]]]]; i != j; j = pair[pos[pair[row[i]]]], res++) {
                swap(row, i, j);
                swap(pos, row[i], row[j]);
            }
        }
        return res;
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private void test(int[] row, int expected) {
        assertEquals(expected, minSwapsCouples(row));
        assertEquals(expected, minSwapsCouples2(row));
        assertEquals(expected, minSwapsCouples3(row.clone()));
        assertEquals(expected, minSwapsCouples4(row.clone()));
        assertEquals(expected, minSwapsCouples5(row.clone()));
    }

    @Test public void test() {
        test(new int[] {0, 2, 1, 3}, 1);
        test(new int[] {3, 2, 0, 1}, 0);
        test(new int[] {1, 4, 0, 5, 8, 7, 6, 3, 2, 9}, 3);
        test(new int[] {1, 9, 7, 0, 2, 15, 5, 13, 4, 3, 11, 12, 6, 8, 14, 10}, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
