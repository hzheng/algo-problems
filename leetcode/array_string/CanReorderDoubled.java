import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC954: https://leetcode.com/problems/array-of-doubled-pairs/
//
// Given an array of integers A with even length, return true if and only if it is possible to
// reorder it such that A[2 * i + 1] = 2 * A[2 * i] for every 0 <= i < len(A) / 2.
//
// Constraints:
// 0 <= A.length <= 3 * 10^4
// A.length is even.
// -10^5 <= A[i] <= 10^5
public class CanReorderDoubled {
    // Hash Table
    // time complexity: O(N^2), space complexity: O(N)
    // 15 ms(97.61%), 46.4 MB(72.70%) for 97 tests
    public boolean canReorderDoubled(int[] A) {
        Map<Integer, Integer> count = new HashMap<>();
        int zeros = 0;
        for (int a : A) {
            if (a == 0) {
                zeros++;
            } else {
                count.put(a, count.getOrDefault(a, 0) + 1);
            }
        }
        if ((zeros & 1) != 0) {return false; }

        while (!count.isEmpty()) {
            Set<Integer> toBeRemoved = new HashSet<>();
            int commonPowerOf2 = Integer.MAX_VALUE;
            for (int num : count.keySet()) {
                if ((num & 1) == 0) {
                    commonPowerOf2 = Math.min(commonPowerOf2, num & -num);
                    continue;
                }

                int doubleNum = num << 1;
                Integer doubleCount = count.get(doubleNum);
                if (doubleCount == null || (doubleCount -= count.get(num)) < 0) { return false; }

                toBeRemoved.add(num);
                if (doubleCount == 0) {
                    toBeRemoved.add(doubleNum);
                }
                count.put(doubleNum, doubleCount);
            }
            if (toBeRemoved.isEmpty()) {
                Map<Integer, Integer> nextCount = new HashMap<>();
                for (int num : count.keySet()) {
                    nextCount.put(num / commonPowerOf2, count.get(num));
                }
                count = nextCount;
            } else {
                count.keySet().removeIf(toBeRemoved::contains);
            }
        }
        return true;
    }

    // Hash Table + Sort + Greedy
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 85 ms(23.89%), 46.7 MB(62.46%) for 97 tests
    public boolean canReorderDoubled2(int[] A) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int a : A) {
            count.put(a, count.getOrDefault(a, 0) + 1);
        }
        Integer[] B = new Integer[A.length];
        for (int i = 0; i < A.length; i++) {
            B[i] = A[i];
        }
        Arrays.sort(B, Comparator.comparingInt(Math::abs));
        for (int b : B) {
            if (count.get(b) == 0) { continue; }
            if (count.getOrDefault(2 * b, 0) <= 0) { return false; }

            count.put(b, count.get(b) - 1);
            count.put(2 * b, count.get(2 * b) - 1);
        }
        return true;
    }

    // SortedMap + Greedy
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 32 ms(82.25%), 47.5 MB(20.82%) for 97 tests
    public boolean canReorderDoubled3(int[] A) {
        SortedMap<Integer, Integer> count = new TreeMap<>();
        for (int a : A) {
            count.put(a, count.getOrDefault(a, 0) + 1);
        }
        for (int num : count.keySet()) {
            int cnt = count.get(num);
            if (cnt == 0) { continue; }

            if (num < 0 && num % 2 != 0) { return false; }

            int pair = (num < 0) ? num / 2 : num * 2;
            if (cnt > count.getOrDefault(pair, 0)) { return false; }

            count.put(pair, count.get(pair) - cnt);
        }
        return true;
    }

    // Heap + Greedy
    // time complexity: O(N^2), space complexity: O(N)
    // 722 ms(5.46%), 46.8 MB(55.97%) for 97 tests
    public boolean canReorderDoubled4(int[] A) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(Math::abs));
        for (int a : A) {
            pq.offer(a);
        }
        while (!pq.isEmpty()) {
            int num = pq.poll();
            if (!pq.remove(num * 2)) { return false; }
        }
        return true;
    }

    private void test(int[] A, boolean expected) {
        assertEquals(expected, canReorderDoubled(A));
        assertEquals(expected, canReorderDoubled2(A));
        assertEquals(expected, canReorderDoubled3(A));
        assertEquals(expected, canReorderDoubled4(A));
    }

    @Test public void test() {
        test(new int[] {-6,-3}, true);
        test(new int[] {4, -2, 2, -4}, true);
        test(new int[] {3, 1, 3, 6}, false);
        test(new int[] {2, 1, 2, 6}, false);
        test(new int[] {1, 2, 4, 16, 8, 4}, false);
        test(new int[] {2, 1, 2, 1, 1, 1, 2, 2}, true);
        test(new int[] {0, 0}, true);
        test(new int[] {0, 0, 0, 1}, false);
        test(new int[] {-2, 4}, false);
        test(new int[] {-32, 64, 384, 40000, 80000, 128, -64, 32}, false);
        test(new int[] {256, 768, -32, 64, 384, 40000, 80000, 128, -64, 32}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
