import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/patching-array/
//
// Given a sorted positive integer array nums and an integer n, add/patch
// elements to the array such that any number in range [1, n] inclusive can be
// formed by the sum of some elements in the array. Return the minimum number of
// patches required.
public class PatchArray {
    // Backtracking
    // Time Limit Exceeded
    public int minPatches(int[] nums, int n) {
        SortedSet<Integer> unfinished = new TreeSet<>();
        for (int i = 1; i <= n; i++) {
            unfinished.add(i);
        }

        List<Integer> finished = new ArrayList<>();
        combine(nums, 0, nums.length, unfinished, finished);
        return patch(unfinished, finished);
    }

    private int patch(SortedSet<Integer> unfinished, List<Integer> finished) {
        int left = unfinished.size();
        if (left < 2) return left;

        int first = unfinished.first();
        int size = finished.size();
        int min = left;
        Set<Integer> removed = new HashSet<>();
        for (int i = 1; i <= first && min > 0; i++) {
            add(i, unfinished, finished, removed);
            min = Math.min(min, patch(unfinished, finished));
            // backtracking
            finished.subList(size, finished.size()).clear();
            for (int x : removed) {
                unfinished.add(x);
            }
            removed.clear();
        }
        return min + 1;
    }

    private void add(int num, Set<Integer> unfinished, List<Integer> finished,
                     Set<Integer> removed) {
        int size = finished.size();
        for (int i = 0; i < size; i++) {
            int combined = finished.get(i) + num;
            if (unfinished.remove(combined) && removed != null) {
                removed.add(combined);
            }
            finished.add(combined);
        }
        if (unfinished.remove(num) && removed != null) {
            removed.add(num);
        }
        finished.add(num);
    }

    private void combine(int[] nums, int start, int end,
                         Set<Integer> unfinished, List<Integer> finished) {
        if (start >= end) return;

        combine(nums, start + 1, end, unfinished, finished);
        add(nums[start], unfinished, finished, null);
    }

    // Greedy
    // Time Limit Exceeded
    public int minPatches2(int[] nums, int n) {
        SortedSet<Integer> unfinished = new TreeSet<>();
        for (int i = 1; i <= n; i++) {
            unfinished.add(i);
        }

        List<Integer> finished = new ArrayList<>();
        combine(nums, 0, nums.length, unfinished, finished);
        for (int patches = 0;; patches++) {
            int left = unfinished.size();
            if (left < 2) return patches + left;

            add(unfinished.first(), unfinished, finished, null);
        }
    }

    // Greedy
    // Time Limit Exceeded
    public int minPatches3(int[] nums, int n) {
        int maxSum = 0;
        List<Integer> base = new ArrayList<>();
        for (int num : nums) {
            maxSum += num;
            base.add(num);
        }

        int patches = 0;
        for (;; patches++) {  // try to shrink n
            int half = (n >> 1) + (n & 1); // (n + 1) / 2 may overflow
            if (half <= maxSum) break;

            n -= half;
        }

        if (n <= 1) return patches + ((n == 0 || base.get(0) == 1) ? 0 : 1);

        SortedSet<Integer> unfinished = new TreeSet<>();
        for (int i = 1; i <= n; i++) {// TODO: if n is too big, should slice it
            unfinished.add(i);
        }

        // TODO: keep invariant: maxSum >= n / 2
        while (!unfinished.isEmpty()) {
            int min = unfinished.first();
            if (combine(base, unfinished, min)) continue;

            if (unfinished.isEmpty()) return patches;

            min = unfinished.first();
            int insertPos = Collections.binarySearch(base, min);
            base.add(-insertPos - 1, min);
            patches++;
            maxSum += min;
            unfinished.subSet(0, min * 2).clear();
            if (unfinished.isEmpty()) return patches;

            int max = unfinished.last();
            if (false && maxSum >= max) { // try to shrink max unfinished(can be omitted)
                int sum = 0;
                for (int index = base.size() - 1; index >= 0; index--) {
                    int s = sum + base.get(index);
                    if (s < max) {
                        sum = s;
                        continue;
                    }
                    if (max - sum >= min) break;

                    unfinished.remove(max);
                    maxSum -= check(unfinished, base);
                    if (unfinished.isEmpty()) return patches;
                    // reset
                    max = unfinished.last();
                    sum = 0;
                    index = base.size();
                }
            }
        }
        return patches;
    }

    private boolean combine(List<Integer> base, SortedSet<Integer> unfinished,
                            int target) {
        if (unfinished.isEmpty()) return false;

        int n = Math.min(base.size(), 30);
        int max = target * 2 - 1;
        boolean changed = false;
        for (int k = 1; k <= n; k++) {
            for (int v = (1 << k) - 1; v < (1 << n); ) {
                int sum = 0;
                for (int i = 0, mask = 1; i < n; i++, mask <<= 1) {
                    if ((v & mask) != 0) {
                        sum += base.get(i);
                    }
                }
                if (unfinished.isEmpty()) return changed;

                if (sum > Math.min(max, unfinished.last())) break;

                changed |= unfinished.remove(sum);
                int t = v | (v - 1);
                v = (t + 1) | (((~t & -~t) - 1) >> (trailingZero(v) + 1));
            }
        }
        return changed;
    }

    private int check(SortedSet<Integer> unfinished, List<Integer> base) {
        int deductedSum = 0;
        while (!unfinished.isEmpty()) {
            int lastBase = base.size() - 1;
            int lastVal = base.get(lastBase);
            int max = unfinished.last();
            if (lastVal < max) break;

            deductedSum += lastBase;
            base.remove(lastBase);
            if (lastVal == max) {
                unfinished.remove(max);
                if (unfinished.isEmpty()) break;
            }
        }
        return deductedSum;
    }

    private int trailingZero(int n) {
        int count = 0;
        for (int x = (n ^ (n - 1)) >> 1; x > 0; count++, x >>= 1) {}
        return count;
    }

    private int highestBit(int n) {
        n |= (n >>  1);
        n |= (n >>  2);
        n |= (n >>  4);
        n |= (n >>  8);
        n |= (n >> 16);
        return n - (n >> 1);
    }

    private int bits(int n) {
        int count = 0;
        for (int i = n; i > 0; i >>= 1) {
            count++;
        }
        return count;
    }

    // WRONG algorithm(it not power of 2 sequence)
    public int minPatches0(int[] nums, int n) {
        if (n < 1) return 0;

        int end = nums.length - 1;
        // remove big numbers, which are uncecessary
        for (;; end--, n--) {
            end = Arrays.binarySearch(nums, 0, end + 1, n);
            if (end < 0) {
                end = -end - 2;
                break;
            }
        }

        int bitCount = bits(n);
        if (end < 0) return bitCount;

        Set<Integer> patchedPowerOf2 = new HashSet<>();
        List<Integer> numList = new ArrayList<>();
        int maxPowerOf2 = 1 << (bitCount - 1);
        for (int i = 0, powerOf2 = 1; i <= end || powerOf2 <= maxPowerOf2; ) {
            if (i > end || powerOf2 < nums[i]) {
                patchedPowerOf2.add(powerOf2);
                powerOf2 <<= 1;
            } else if (powerOf2 > maxPowerOf2 || powerOf2 > nums[i]) {
                numList.add(nums[i++]);
            } else { // powerOf2 == nums[i]
                powerOf2 <<= 1;
                i++;
            }
        }

        for (int i = 2; i <= numList.size(); i++) {
            if (tryCombine(patchedPowerOf2, numList, i)
                && (numList.size() == 0 || patchedPowerOf2.isEmpty())) {
                return patchedPowerOf2.size();
            }
        }
        int maxSum = 0;
        for (int num : numList) {
            maxSum += num;
        }
        // try to remove redudant patches with the help from numList...
        for (int size = numList.size(), extra = 0; maxPowerOf2 > 0 && maxSum > 0;
             maxPowerOf2 >>= 1) {
            if (!patchedPowerOf2.contains(maxPowerOf2)
                || maxSum + extra + maxPowerOf2 <= n // too small: sum(1+...+powerOf2 / 2) = powerOf2-1
                || maxPowerOf2 < numList.get(size - 1) // too big ?
                ) {
                extra += 2 * maxPowerOf2 - n - 1;
                n = maxPowerOf2 - 1; // reduce to lower level
            } else {
                int s = extra;
                // maxSum += extra;
                for (; s <= n - maxPowerOf2 && size > 0; size--) {
                    s += numList.get(size - 1);
                    maxSum -= numList.get(size - 1);
                }
                extra = n - (s + 1);
                // n -= (s + 1);
                n = maxPowerOf2 - 1; // reduce to lower level
                patchedPowerOf2.remove(maxPowerOf2);
            }
        }
        return patchedPowerOf2.size();
    }

    private boolean tryCombine(Set<Integer> patchedPowerOf2,
                               List<Integer> numList, int k) {
        int n = numList.size();
        if (k > n) return false;

        for (int v = (1 << k) - 1; v < (1 << n); ) {
            int sum = 0;
            for (int i = 0, mask = 1; i < n; i++, mask <<= 1) {
                if ((v & mask) != 0) {
                    sum += numList.get(i);
                }
            }
            if (patchedPowerOf2.remove(sum)) {
                for (int i = 0, mask = 1; i < n; i++, mask <<= 1) {
                    if ((v & mask) != 0) {
                        numList.set(i, 0);
                    }
                }
                for (int i = 0; i < k; i++) {
                    numList.remove((Integer)0);
                }
                tryCombine(patchedPowerOf2, numList, k);
                return true;
            }

            int t = v | (v - 1);
            v = (t + 1) | (((~t & -~t) - 1) >> (trailingZero(v) + 1));
        }
        return false;
    }

    // greedy
    // time complexity: O(N), space complexity: O(1)
    // beats 12.62%(1 ms)
    public int minPatches4(int[] nums, int n) {
        int cur = 0;
        int patches = 0;
        for (long target = 1; target <= n; ) {
            if (cur < nums.length && nums[cur] <= target) {
                target += nums[cur++];
            } else {
                patches++; // patching the target
                target <<= 1; // old target contains prefix sum
            }
        }
        return patches;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], Integer, Integer> minPatches, String name,
              int expected, int[] nums, int n) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)minPatches.apply(nums, n));
        if (n > 100) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int expected, int[] nums, int n) {
        PatchArray p = new PatchArray();
        if (n < 30) {
            test(p::minPatches, "minPatches", expected, nums, n);
        }
        if (n < 60) {
            test(p::minPatches2, "minPatches2", expected, nums, n);
        }
        if (nums.length < 100) {
            test(p::minPatches3, "minPatches3", expected, nums, n);
        }
        test(p::minPatches4, "minPatches4", expected, nums, n);
    }

    @Test
    public void test1() {
        test(5, new int[] {1, 8, 10}, 104);
        test(1, new int[] {1, 3}, 6);
        test(2, new int[] {1, 5}, 8);
        test(2, new int[] {1, 5, 10}, 9);
        test(0, new int[] {1, 2, 2}, 5);
        test(0, new int[] {1, 2, 1, 3}, 7);
        test(2, new int[] {1, 5, 10}, 20);
        test(2, new int[] {1, 3, 8, 10}, 26);
        test(3, new int[] {1, 3, 8, 10}, 38);
        test(3, new int[] {1, 3, 8, 10}, 46);
        test(4, new int[] {1, 3, 8, 10}, 96);
        test(4, new int[] {1, 8, 10}, 103);
        test(4, new int[] {1, 8, 10}, 63);
        test(7, new int[] {1, 3, 8, 10}, 960);
        test(11, new int[] {1, 3, 8, 10}, 9600);
        test(14, new int[] {1, 3, 8, 10}, 96000);
        test(17, new int[] {1, 3, 8, 10}, 960000);
        test(29, new int[] {1, 2, 3}, 2147483647);
        test(28, new int[] {1, 2, 31, 33}, 2147483647);
        test(2, new int[] {1, 5, 6, 12, 13, 14, 16, 20, 21, 22, 23, 24, 25, 36,
                           40, 41, 43, 44, 45, 46, 47, 49, 51, 53, 58, 59, 64,
                           66, 66, 68, 72, 77,77,80,83,83,86,88,89}, 84);
    }

    @Test
    public void test2() {
        test(1,  new int[] {1, 3, 4, 5, 8, 10, 15, 19, 28, 28, 31, 33, 38, 41,
                            42, 44, 46, 47, 51, 57, 58, 60, 60, 62, 65, 65, 68,
                            71, 73, 73, 75, 76, 80, 83, 83, 84, 92, 95, 100, 100},
             66);
        test(2,  new int[] {4, 6, 8, 13, 22, 24, 25, 34, 37, 40, 42, 44, 50,
                            51, 54, 56, 56, 56, 57, 57, 59, 64, 71, 73, 77, 79,
                            82, 83, 89, 97},  90);
        test(4,  new int[] {9, 10, 20, 29, 34, 34, 35, 35, 37, 39, 41, 42, 43,
                            47, 59, 60, 65, 69, 74, 78, 81, 81, 85, 88, 89, 97,
                            100, 100},  85);
        test(5,  new int[] {2, 3, 3, 4, 6, 8, 8, 10, 10, 10, 12, 13, 13, 14, 15,
                            15, 16, 17, 19, 20, 20, 21, 21, 21, 23, 23, 24, 25,
                            26, 27, 27, 28, 28, 29, 29, 30, 30, 31, 31, 32, 32,
                            32, 36, 36, 38, 41, 41, 41, 43, 44, 46, 46, 46, 48,
                            48, 49, 50, 51, 51, 52, 52, 53, 54, 55, 56, 56, 58,
                            58, 58, 59, 60, 60, 60, 61, 63, 63, 66, 66, 70, 70,
                            73, 74, 74, 75, 78, 80, 81, 83, 85, 87, 87, 89, 89,
                            89, 90, 90, 92, 92, 96, 98},  60844);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PatchArray");
    }
}
