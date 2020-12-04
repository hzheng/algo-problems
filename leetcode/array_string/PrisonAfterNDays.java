import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC957: https://leetcode.com/problems/prison-cells-after-n-days/
//
// There are 8 prison cells in a row, and each cell is either occupied or vacant.
// Each day, whether the cell is occupied or vacant changes according to the following rules:
// If a cell has two adjacent neighbors that are both occupied or both vacant, then the cell becomes
// occupied. Otherwise, it becomes vacant.
// We describe the current state of the prison in the following way: cells[i] == 1 if the i-th cell
// is occupied, else cells[i] == 0.
// Given the initial state of the prison, return the state of the prison after N days.
//
// Note:
// cells.length == 8
// cells[i] is in {0, 1}
// 1 <= N <= 10^9
public class PrisonAfterNDays {
    // Bit Manipulation + Hash Table
    // time complexity: O(2^N), space complexity: O(2^N)
    // 1 ms(98.62%), 38.9 MB(89.60%) for 258 tests
    public int[] prisonAfterNDays(int[] cells, int N) {
        int v = 0;
        for (int cell : cells) {
            v <<= 1;
            v |= cell;
        }
        Map<Integer, Integer> vals = new HashMap<>();
        vals.put(v, 0);
        List<Integer> list = new ArrayList<>();
        list.add(v);
        for (int i = 1; i <= N; i++) {
            v = next(v, cells.length - 1);
            Integer last = vals.put(v, i);
            if (last != null) {
                int period = i - last;
                v = list.get(last + (N - last) % period);
                break;
            }
            list.add(v);
        }
        int[] res = new int[cells.length];
        for (int i = res.length - 1; i >= 0; v >>= 1, i--) {
            res[i] = v & 1;
        }
        return res;
    }

    private int next(int val, int bits) {
        int res = 0;
        for (int i = 1, prev = (val >> bits) & 1, cur, next; ; i++, res <<= 1, prev = cur) {
            cur = (val >> (bits - i)) & 1;
            next = (val >> (bits - i - 1)) & 1;
            res |= (1 - (prev ^ next));
            if (i == bits) { return res & ~1; }
        }
    }

    // Set
    // time complexity: O(2^N), space complexity: O(2^N)
    // 2 ms(82.20%), 39 MB(79.41%) for 258 tests
    public int[] prisonAfterNDays2(int[] cells, int N) {
        cells = cells.clone();
        Set<String> set = new HashSet<>();
        int i = 0;
        for (; i < N; i++) {
            nextDay(cells);
            if (!set.add(Arrays.toString(cells))) { break; }
        }
        if (i < N) {
            for (int j = 0, n = (N - 1) % i; j < n; j++) {
                nextDay(cells);
            }
        }
        return cells;
    }

    private void nextDay(int[] cells) {
        for (int i = 1, prev = cells[0], cur; i < cells.length - 1; i++, prev = cur) {
            cur = cells[i];
            cells[i] = 1 - (prev ^ cells[i + 1]);
        }
        cells[0] = cells[cells.length - 1] = 0;
    }

    // Hash Table
    // time complexity: O(2^N), space complexity: O(2^N)
    // 3 ms(61.87%), 39.2 MB(39.17%) for 258 tests
    public int[] prisonAfterNDays3(int[] cells, int N) {
        Map<String, Integer> visited = new HashMap<>();
        for (int i = N, n = cells.length; i > 0; ) {
            int[] cells2 = new int[n];
            visited.put(Arrays.toString(cells), i--);
            for (int j = 1; j < n - 1; j++) {
                cells2[j] = 1 - (cells[j - 1] ^ cells[j + 1]);
            }
            cells = cells2;
            Integer prev = visited.get(Arrays.toString(cells));
            if (prev != null) {
                i %= prev - i;
            }
        }
        return cells;
    }

    // time complexity: O(1), space complexity: O(1)
    // 3 ms(61.87%), 39.2 MB(39.17%) for 258 tests
    public int[] prisonAfterNDays4(int[] cells, int N) {
        for (int i = (N - 1) % 14 + 1, n = cells.length; i > 0; i--) { // 14 is always cycle
            int[] cells2 = new int[n];
            for (int j = 1; j < n - 1; j++) {
                cells2[j] = 1 - (cells[j - 1] ^ cells[j + 1]);
            }
            cells = cells2;
        }
        return cells;
    }

    private void test(int[] cells, int N, int[] expected) {
        assertArrayEquals(expected, prisonAfterNDays(cells, N));
        assertArrayEquals(expected, prisonAfterNDays2(cells, N));
        assertArrayEquals(expected, prisonAfterNDays3(cells, N));
        assertArrayEquals(expected, prisonAfterNDays4(cells, N));
    }

    @Test public void test() {
        test(new int[] {1, 1, 0, 1, 1, 0, 0, 0}, 1, new int[] {0, 0, 1, 0, 0, 0, 1, 0});
        test(new int[] {0, 1, 0, 1, 1, 0, 0, 1}, 7, new int[] {0, 0, 1, 1, 0, 0, 0, 0});
        test(new int[] {0, 0, 0, 1, 1, 0, 1, 0}, 574, new int[] {0, 0, 0, 1, 1, 0, 1, 0});
        test(new int[] {1, 0, 0, 1, 0, 0, 1, 0}, 1000000000, new int[] {0, 0, 1, 1, 1, 1, 1, 0});
        test(new int[] {1, 1, 0, 1, 1, 0, 1, 1}, 6, new int[] {0, 0, 1, 0, 0, 1, 0, 0});
        test(new int[] {1, 1, 0, 1, 0, 0, 1, 1}, 69999999, new int[] {0, 1, 1, 1, 0, 1, 0, 0});
        test(new int[] {0, 0, 0, 1, 0, 0, 1, 1}, 79999999, new int[] {0, 1, 1, 0, 1, 1, 1, 0});
        test(new int[] {0, 0, 0, 1, 0, 0, 0, 0}, 179999999, new int[] {0, 1, 0, 0, 0, 0, 0, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
