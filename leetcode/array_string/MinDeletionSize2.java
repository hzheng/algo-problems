import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC955: https://leetcode.com/problems/delete-columns-to-make-sorted-ii/
//
// We are given an array A of N lowercase letter strings, all of the same length. Now, we may choose
// any set of deletion indices, and for each string, we delete all the characters in those indices.
// For example, if we have an array A = ["abcdef","uvwxyz"] and deletion indices {0, 2, 3}, then the
// final array after deletions is ["bef","vyz"]. Suppose we chose a set of deletion indices D such
// that after deletions, the final array has its elements in lexicographic order
// (A[0] <= A[1] <= A[2] ... <= A[A.length - 1]). Return the minimum possible value of D.length.
//
// Note:
// 1 <= A.length <= 100
// 1 <= A[i].length <= 100
public class MinDeletionSize2 {
    // Greedy + Set
    // time complexity: O(N*M), space complexity: O(N)
    // 1 ms(91.72%), 38.8 MB(32.41%) for 145 tests
    public int minDeletionSize(String[] A) {
        int n = A.length;
        int m = A[0].length();
        int[] inOrder = new int[n - 1];
        int res = 0;
        outer:
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (inOrder[j] == 1) { continue; }

                int cmp = A[j + 1].charAt(i) - A[j].charAt(i);
                if (cmp > 0) {
                    inOrder[j] = -1;
                } else if (cmp < 0) {
                    res++;
                    for (int k = n - 2; k >= 0; k--) {
                        inOrder[k] = Math.max(inOrder[k], 0);
                    }
                    continue outer;
                }
            }
            for (int k = n - 2; k >= 0; k--) {
                inOrder[k] = Math.abs(inOrder[k]);
            }
        }
        return res;
    }

    // Greedy + Set
    // time complexity: O(N*M), space complexity: O(N)
    // 1 ms(91.72%), 38.6 MB(53.10%) for 145 tests
    public int minDeletionSize2(String[] A) {
        int n = A.length;
        int m = A[0].length();
        boolean[] inOrder = new boolean[n - 1];
        int res = 0;
        outer:
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (!inOrder[j] && A[j].charAt(i) > A[j + 1].charAt(i)) {
                    res++;
                    continue outer;
                }
            }
            for (int j = 0; j < n - 1; j++) {
                inOrder[j] |= (A[j].charAt(i) < A[j + 1].charAt(i));
            }
        }
        return res;
    }

    // Brute Force
    // time complexity: O(N*M), space complexity: O(N)
    // 1 ms(91.72%), 38.4 MB(75.86%) for 145 tests
    public int minDeletionSize3(String[] A) {
        Set<Integer> deletedCols = new HashSet<>();
        int n = A.length;
        int m = A[0].length();
        outer:
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (deletedCols.contains(j)) { continue; }

                int cmp = A[i - 1].charAt(j) - A[i].charAt(j);
                if (cmp == 0) { continue; }

                if (cmp > 0) {
                    deletedCols.add(j);
                    i = 0; // restart
                }
                continue outer;
            }
        }
        return deletedCols.size();
    }

    // Brute Force
    // time complexity: O(N*M^2), space complexity: O(N)
    // 18 ms(10.34%), 39.3 MB(20.69%) for 145 tests
    public int minDeletionSize4(String[] A) {
        int n = A.length;
        int m = A[0].length();
        String[] str = new String[n];
        Arrays.fill(str, "");
        for (int j = 0; j < m; j++) {
            boolean outOfOrder = false;
            for (int i = 0; i < n; i++) {
                str[i] += A[i].charAt(j);
            }
            for (int i = 1; i < n; i++) {
                if (str[i].compareTo(str[i - 1]) < 0) {
                    outOfOrder = true;
                    break;
                }
            }
            if (outOfOrder) {
                for (int i = 0; i < n; i++) {
                    str[i] = str[i].substring(0, str[i].length() - 1);
                }
            }
        }
        return m - str[0].length();
    }

    private void test(String[] A, int expected) {
        assertEquals(expected, minDeletionSize(A));
        assertEquals(expected, minDeletionSize2(A));
        assertEquals(expected, minDeletionSize3(A));
        assertEquals(expected, minDeletionSize4(A));
    }

    @Test public void test() {
        test(new String[] {"xga", "xfb", "yfa"}, 1);
        test(new String[] {"ca", "bb", "ac"}, 1);
        test(new String[] {"xc", "yb", "za"}, 0);
        test(new String[] {"zyx", "wvu", "tsr"}, 3);
        test(new String[] {"hpwunsqg", "euzujrtm", "fbovfpii", "piexjjfc"}, 5);
        test(new String[] {"epdzz", "vjoxh"}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
