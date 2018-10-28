import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC930: https://leetcode.com/problems/binary-subarrays-with-sum/
//
// In an array A of 0s and 1s, how many non-empty subarrays have sum S?
public class BinarySubarraysWithSum {
    // time complexity: O(N), space complexity: O(1)
    // beats %(5 ms for 59 tests)
    public int numSubarraysWithSum(int[] A, int S) {
        int n = A.length;
        int res = 0;
        if (S == 0) {
            for (int i = 0; i < n; i++) {
                if (A[i] != 0) continue;

                int count = 1;
                for (; i + 1 < n && A[i + 1] == 0; count++, i++) {}
                res += count * (count + 1) / 2;
            }
            return res;
        }
        for (int i = 0, j = -1, sum = 0; i < n;) {
            if (sum == S) {
                int countI = 1;
                if (A[i] == 0) {
                    for (countI++, i++; i < n && A[i] == 0; countI++, i++) {}
                }
                int countJ = 1;
                for (; j + 1 < n && A[j + 1] == 0; countJ++, j++) {}
                res += countI * countJ;
            } 
            if (sum < S || sum == S && j < n - 1) {
                if (++j == n) break;
                sum += A[j];
            } else {
                sum -= A[i++];
            }
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats %(29 ms for 59 tests)
    public int numSubarraysWithSum2(int[] A, int S) {
        int res = 0;
        Map<Integer, Integer> count = new HashMap<>();
        count.put(0, 1);
        int sum = 0;
        for (int a : A) {
            sum += a;
            res += count.getOrDefault(sum - S, 0);
            count.put(sum, count.getOrDefault(sum, 0) + 1);
        }
        return res;
    }

    void test(int[] A, int S, int expected) {
        assertEquals(expected, numSubarraysWithSum(A, S));
        assertEquals(expected, numSubarraysWithSum2(A, S));
    }

    @Test
    public void test() {
        test(new int[] {1, 0, 1, 0, 1}, 2, 4);
        test(new int[] {0, 0, 0, 0, 0}, 0, 15);
        test(new int[] {0, 1, 1, 0, 0, 1, 0}, 2, 8);
        test(new int[] {0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0}, 3, 18);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
