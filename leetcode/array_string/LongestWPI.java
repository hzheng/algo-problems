import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1124: https://leetcode.com/problems/longest-well-performing-interval/
//
// We are given hours, a list of the number of hours worked per day for a given employee. A day is
// considered to be a tiring day if and only if the number of hours worked is (strictly) greater
// than 8. A well-performing interval is an interval of days for which the number of tiring days is
// strictly larger than the number of non-tiring days. Return the length of the longest
// well-performing interval.
//
// Constraints:
// 1 <= hours.length <= 10000
// 0 <= hours[i] <= 16
public class LongestWPI {
    // Solution of Choice
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 9 ms(54.15%), 39.4 MB(88.14%) for 98 tests
    public int longestWPI(int[] hours) {
        int n = hours.length;
        int res = 0;
        Map<Integer, Integer> found = new HashMap<>();
        for (int i = 0, sum = 0; i < n; i++) {
            sum += (hours[i] > 8) ? 1 : -1;
            if (sum > 0) {
                res = i + 1;
            } else {
                found.putIfAbsent(sum, i);
                int prev = found.getOrDefault(sum - 1, i);
                res = Math.max(res, i - prev);
            }
        }
        return res;
    }

    // Solution of Choice
    // Monotonic Stack (can be generalized to sum > some number)
    // time complexity: O(N), space complexity: O(N)
    // 6 ms(96.05%), 39.5 MB(79.84%) for 98 tests
    public int longestWPI2(int[] hours) {
        int n = hours.length;
        int[] sum = new int[n + 1];
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        for (int i = 0; i < n; i++) {
            int s = sum[i + 1] = sum[i] + ((hours[i] > 8) ? 1 : -1);
            if (sum[stack.peek()] > s) {
                stack.push(i + 1);
            }
        }
        int res = 0;
        for (int i = n; i >= 0; i--) {
            while (!stack.isEmpty() && sum[stack.peek()] < sum[i]) {
                res = Math.max(res, i - stack.pop());
            }
        }
        return res;
    }

    // Binary Search (can be generalized to sum > some number)
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 9 ms(53.33%), 39.8 MB(32.55%) for 98 tests
    public int longestWPI3(int[] hours) {
        int n = hours.length;
        int[] sum = new int[n + 1]; // suffix sum
        for (int i = n - 1; i >= 0; i--) {
            sum[i] = sum[i + 1] + (hours[i] > 8 ? 1 : -1);
        }
        int res = 0;
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (index.isEmpty() || sum[i] > sum[index.get(index.size() - 1)]) {
                index.add(i);
            }
            int j = binarySearch(sum, index, sum[i + 1] + 1);
            if (j < index.size()) {
                res = Math.max(res, i - index.get(j) + 1);
            }
        }
        return res;
    }

    private int binarySearch(int[] sum, List<Integer> index, int key) {
        int low = 0;
        for (int high = index.size() - 1; low <= high; ) {
            int mid = (low + high) >>> 1;
            if (sum[index.get(mid)] >= key) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private void test(int[] hours, int expected) {
        assertEquals(expected, longestWPI(hours));
        assertEquals(expected, longestWPI2(hours));
        assertEquals(expected, longestWPI3(hours));
    }

    @Test public void test() {
        test(new int[] {9, 9, 6, 0, 6, 6, 9}, 3);
        test(new int[] {9, 6, 6}, 1);
        test(new int[] {9, 9, 1, 2, 3, 4, 5, 6, 9, 9, 9, 9, 6, 7, 8}, 7);
        test(new int[] {9, 9, 9, 1, 2, 3, 4, 5, 6, 9, 9, 9, 9, 6, 7, 8}, 13);
        test(new int[] {9, 9, 9, 1, 2, 3, 4, 5, 6, 7, 8}, 5);
        test(new int[] {9, 9, 9}, 3);
        test(new int[] {9, 9, 6, 0, 6, 6, 9, 8, 7, 4, 3, 9, 2, 10, 9, 7}, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
