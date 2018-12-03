import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC949: https://leetcode.com/contest/weekly-contest-113/problems/largest-time-for-given-digits/
//
// Given an array of 4 digits, return the largest 24 hour time that can be made.
// The smallest 24 hour time is 00:00, and the largest is 23:59.  Starting from 
// 00:00, a time is larger if more time has elapsed since midnight.
// Return the answer as a string of length 5.  If no valid time can be made,
// return an empty string.
public class LargestTimeFromDigits {
    // Recursion
    // beats %(11 ms for 172 tests)
    public String largestTimeFromDigits(int[] A) {
        int max = -1;
        Integer[] res = null;
        for (Integer[] num : permute(new int[] {0, 1, 2, 3})) {
            int cur = calc(A, num);
            if (cur > max) {
                max = cur;
                res = num;
            }
        }
        if (res == null) return "";
        return "" + A[res[0]] + A[res[1]] + ":" + A[res[2]] + A[res[3]];
    }

    private List<Integer[]> permute(int[] nums) {
        List<Integer[]> res = new ArrayList<>();
        permute(res, new ArrayList<>(), nums);
        return res;
    }

    private void permute(List<Integer[]> res, List<Integer> cur, int[] nums) {
        int n = nums.length;
        if (cur.size() == n) {
            res.add(cur.toArray(new Integer[4]));
            return;
        }

        for (int i = 0; i < n; i++) {
            if (cur.contains(nums[i])) continue;

            cur.add(nums[i]);
            permute(res, cur, nums);
            cur.remove(cur.size() - 1);
        }
    }

    private int calc(int[] A, Integer[] a) {
        int hour = A[a[0]] * 10 + A[a[1]];
        int min = A[a[2]] * 10 + A[a[3]];
        return (hour > 23 || min >= 60) ? -1 : hour * 60 + min;
    }

    // beats %(23 ms for 172 tests)
    public String largestTimeFromDigits2(int[] A) {
        int res = -1;
        int[] perm = new int[] {0, 1, 2, 3};
        while (true) {
            int hour = A[perm[0]] * 10 + A[perm[1]];
            int min = A[perm[2]] * 10 + A[perm[3]];
            if (hour < 24 && min < 60) {
                res = Math.max(res, hour * 60 + min);
            }
            if (!nextPermutation(perm)) break;
        }
        return res >= 0 ? String.format("%02d:%02d", res / 60, res % 60) : "";
    }

    private boolean nextPermutation(int[] nums) {
        int len = nums.length;
        int i = len - 1;
        for (; i > 0 && nums[i - 1] >= nums[i]; i--) {
        }
        if (i == 0) return false;

        int j = len - 1;
        for (; nums[j] <= nums[i - 1]; j--) {} // or binary search
        swap(nums, i - 1, j);
        for (j = len - 1; j > i; i++, j--) { // reverse from i to the end
            swap(nums, i, j);
        }
        return true;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // beats %(14 ms for 172 tests)
    public String largestTimeFromDigits3(int[] A) {
        int res = -1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == i) continue;

                for (int k = 0; k < 4; k++) {
                    if (k == i || k == j) continue;

                    int l = 6 - i - j - k;
                    int hour = 10 * A[i] + A[j];
                    int min = 10 * A[k] + A[l];
                    if (hour < 24 && min < 60) {
                        res = Math.max(res, hour * 60 + min);
                    }
                }
            }
        }
        return res >= 0 ? String.format("%02d:%02d", res / 60, res % 60) : "";
    }

    // BFS + Queue
    // beats %(9 ms for 172 tests)
    public String largestTimeFromDigits4(int[] A) {
        Queue<String> queue = new LinkedList<>();
        queue.offer("");
        for (int a : A) {
            for (int size = queue.size(); size > 0; size--) {
                String s = queue.poll();
                for (int i = s.length(); i >= 0; i--) {
                    queue.offer(s.substring(0, i) + a + s.substring(i));
                }
            }
        }
        String res = "";
        for (String s : queue) {
            s = s.substring(0, 2) + ":" + s.substring(2);
            if (s.charAt(3) < '6' && s.compareTo("24:00") < 0 && s.compareTo(res) > 0) {
                res = s;
            }
        }
        return res;
    }

    // beats %(35 ms for 172 tests)
    public String largestTimeFromDigits5(int[] A) {
        int[] freq = new int[10];
        for (int a : A) {
            freq[a]++;
        }
        for (int hour = 23; hour >= 0; hour--) {
            for (int min = 59; min >= 0; min--) {
                int[] f = new int[10];
                for (int i = hour, j = 0; j < 2; i /= 10, j++) {
                    f[i % 10]++;
                }
                for (int i = min, j = 0; j < 2; i /= 10, j++) {
                    f[i % 10]++;
                }
                if (Arrays.equals(freq, f)) return String.format("%02d:%02d", hour, min);
            }
        }
        return "";
    }

    void test(int[] A, String expected) {
        assertEquals(expected, largestTimeFromDigits(A));
        assertEquals(expected, largestTimeFromDigits2(A));
        assertEquals(expected, largestTimeFromDigits3(A));
        assertEquals(expected, largestTimeFromDigits4(A));
        assertEquals(expected, largestTimeFromDigits5(A));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3, 4}, "23:41");
        test(new int[] {5, 5, 5, 5}, "");
        test(new int[] {0, 0, 0, 0}, "00:00");
        test(new int[] {5, 1, 3, 0}, "15:30");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
