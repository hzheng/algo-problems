import java.util.*;
import static java.util.stream.IntStream.range;

import org.junit.Test;
import static org.junit.Assert.*;

import common.DisjointSet;

// LC839: https://leetcode.com/problems/similar-string-groups
//
// Two strings X and Y are similar if we can swap two letters (in different
// positions) of X, so that it equals Y.
// We are given a list A of unique strings.  Every string in A is an anagram of
// every other string in A.  How many groups are there?
//
// A.length <= 2000
// A[i].length <= 1000
// A.length * A[i].length <= 20000
// All words in A consist of lowercase letters only.
// All words in A have the same length and are anagrams of each other.
public class SimilarStringGroups {
    // Union Find
    // time complexity:  O(N ^ 2 * W), space complexity: O(N * W ^ 3)
    // beats %(217 ms for 58 tests)
    public int numSimilarGroups(String[] A) {
        int n = A.length;
        DisjointSet ds = new DisjointSet(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // if (ds.equals(i, j)) continue;

                if (isSimilar(A[i], A[j])) {
                    ds.union(i, j);
                }
            }
        }
        return ds.count();
    }

    private boolean isSimilar(String a, String b) {
        int diff = 0;
        for (int i = a.length() - 1; i >= 0; i--) {
            if (a.charAt(i) != b.charAt(i)) {
                if (++diff > 2) return false;
            }
        }
        return true;
    }

    // Union Find
    // time complexity:  O(N * W * min(N, W^2)), space complexity: O(N * W ^ 3)
    // beats %(486 ms for 58 tests)
    public int numSimilarGroups2(String[] A) {
        int n = A.length;
        int w = A[0].length();
        DisjointSet ds = new DisjointSet(n);
        if (n < w * w) {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (isSimilar(A[i], A[j])) {
                        ds.union(i, j);
                    }
                }
            }
        } else {
            Map<String, List<Integer> > nei = new HashMap<>();
            for (int i = 0; i < n; ++i) {
                char[] L = A[i].toCharArray();
                for (int j0 = 0; j0 < w; ++j0)
                    for (int j1 = j0 + 1; j1 < w; ++j1) {
                        swap(L, j0, j1);
                        nei.computeIfAbsent(
                            String.valueOf(L), x -> new ArrayList<>()).add(i);
                        swap(L, j0, j1);
                    }
            }
            for (String word : A) {
                int i1 = nei.get(word).get(0);
                for (int i2 : nei.get(word)) {
                    ds.union(i1, i2);
                }
            }
        }
        return ds.count();
    }

    private void swap(char[] A, int i, int j) {
        char tmp = A[i];
        A[i] = A[j];
        A[j] = tmp;
    }

    // Union Find
    // time complexity:  O(N ^ 2 * W), space complexity: O(N * W ^ 3)
    // beats %(387 ms for 58 tests)
    public int numSimilarGroups3(String[] A) {
        int n = A.length;
        DisjointSet ds = new DisjointSet(n);
        range(0, n).forEach(i -> range(i + 1, n)
                   .filter(j -> isSimilar(A[i], A[j]))
                   .forEach(j -> ds.union(i, j)));
        return ds.count();
    }

    // DFS + Recursion
    // time complexity:  O(N ^ 2 * W), space complexity: O(N * W ^ 3)
    // beats %(246 ms for 58 tests)
    public int numSimilarGroups4(String[] A) {
        int n = A.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] == null) continue;

            String str = A[i];
            A[i] = null;
            res++;
            dfs(A, str);
        }
        return res;
    }

    private void dfs(String[] arr, String str) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) continue;

            if (isSimilar(str, arr[i])) {
                String s = arr[i];
                arr[i] = null;
                dfs(arr, s);
            }
        }
    }

    void test(String[] A, int expected) {
        assertEquals(expected, numSimilarGroups(A));
        assertEquals(expected, numSimilarGroups2(A));
        assertEquals(expected, numSimilarGroups3(A));
        assertEquals(expected, numSimilarGroups4(A));
    }

    @Test
    public void test() {
        test(new String[] { "tars", "rats", "arts", "star" }, 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
