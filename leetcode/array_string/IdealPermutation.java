import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC775: https://leetcode.com/problems/global-and-local-inversions/
//
// We have some permutation A of [0, 1, ..., N - 1], where N is the length of A.
// The number of (global) inversions is the number of i < j with 0 <= i < j < N
// and A[i] > A[j]. The number of local inversions is the number of i with
// 0 <= i < N and A[i] > A[i+1].
// Return true if and only if the number of global inversions is equal to the
// number of local inversions.
public class IdealPermutation {
    // SortedSet
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats %(632 ms for 208 tests)
    public boolean isIdealPermutation(int[] A) {
        int n = A.length;
        int localInversion = 0;
        for (int i = 1; i < n; i++) {
            if (A[i] < A[i - 1]) {
                localInversion++;
            }
        }
        int globalInversion = 0;
        SortedSet<Integer> set = new TreeSet<>();
        for (int a : A) {
            globalInversion += set.tailSet(a).size();
            set.add(a);
        }
        return localInversion == globalInversion;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(21 ms for 208 tests)
    public boolean isIdealPermutation2(int[] A) {
        for (int i = 0; i < A.length; i++) {
            if (A[i] == i) continue;

            if ((A[i] != i + 1) || (A[i + 1] != i)) return false;

            i++;
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(20 ms for 208 tests)
    public boolean isIdealPermutation2_2(int[] A) {
        for (int i = 0; i < A.length; ++i) {
            if (Math.abs(A[i] - i) > 1) return false;
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(23 ms for 208 tests)
    public boolean isIdealPermutation3(int[] A) {
        for (int i = 1, max = 0; i < A.length; i++) {
            if (A[i] < max) return false;

            max = Math.max(max, A[i - 1]);
        }
        return true;
    }

    // Binary Indexed Tree
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats %(33 ms for 208 tests)
    public boolean isIdealPermutation4(int[] A) {
        int n = A.length;
        int[] bit = new int[n + 1];
        int inversions = 0;
        for (int i = n - 1; i >= 0; i--) {
            inversions += sum(bit, A[i]);
            add(bit, A[i]);
        }
        for (int i = 0; i < n - 1; i++) {
            if (A[i] > A[i + 1]) {
                inversions--;
            }
        }
        return inversions == 0;
    }

    private int sum(int[] bit, int i) {
        int sum = 0;
        for (int j = i; j > 0; j -= (j & - j)) {
            sum += bit[j];
        }
        return sum;
    }

    private void add(int[] bit, int i) {
        for (int j = i + 1; j < bit.length; j += (j & - j)) {
            bit[j]++;
        }
    }

    void test(int[] A, boolean expected) {
        assertEquals(expected, isIdealPermutation(A));
        assertEquals(expected, isIdealPermutation2(A));
        assertEquals(expected, isIdealPermutation2_2(A));
        assertEquals(expected, isIdealPermutation3(A));
        assertEquals(expected, isIdealPermutation4(A));
    }

    @Test
    public void test() {
        test(new int[] {1, 0, 2}, true);
        test(new int[] {1, 2, 0}, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
