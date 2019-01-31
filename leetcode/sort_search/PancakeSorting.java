import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC969: https://leetcode.com/problems/pancake-sorting/
//
// Given an array A, we can perform a pancake flip: We choose some positive 
// integer k <= A.length, then reverse the order of the first k elements of A.
// We want to perform zero or more pancake flips (doing them one after another 
// in succession) to sort the array A.
// Return the k-values corresponding to a sequence of pancake flips that sort A.
// Any valid answer that sorts the array within 10 * A.length flips is correct.
public class PancakeSorting {
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 97.73%(8 ms for 215 tests)
    public List<Integer> pancakeSort(int[] A) {
        List<Integer> res = new ArrayList<>();
        for (int i = A.length - 1; i > 0; i--) {
            int target = i + 1;
            if (A[i] == target) continue;
            for (int j = i - 1;; j--) {
                if (A[j] == target) {
                    flip(A, j);
                    res.add(j + 1);
                    break;
                }
            }
            flip(A, i);
            res.add(i + 1);
        }
        return res;
    }

    private void flip(int[] A, int k) {
        for (int i = 0, j = k; i < j; i++, j--) {
            int tmp = A[i];
            A[i] = A[j];
            A[j] = tmp;
        }
    }

    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 97.73%(8 ms for 215 tests)
    public List<Integer> pancakeSort2(int[] A) {
        List<Integer> res = new ArrayList<>();
        for (int i = A.length, j; i > 0; i--) {
            for (j = 0; A[j] != i; j++) {}
            flip(A, j);
            res.add(j + 1);
            flip(A, i - 1);
            res.add(i);
        }
        return res;
    }

    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 13.64%(51 ms for 215 tests)
    public List<Integer> pancakeSort3(int[] A) {
        List<Integer> res = new ArrayList<>();
        int n = A.length;
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i + 1;
        }
        Arrays.sort(indices, (i, j) -> A[j - 1] - A[i - 1]);
        for (int i : indices) {
            for (int flip : res) {
                if (i <= flip) {
                    i = flip + 1 - i;
                }
            }
            res.add(i);
            res.add(n--);
        }
        return res;
    }

    void test(int[] A, Function<int[], List<Integer>> pancakeSort) {
        int[] copy = A.clone();
        for (int f : pancakeSort.apply(A)) {
            flip(copy, f - 1);
        }
        for (int i = 1; i < A.length; i++) {
            assertTrue(copy[i] >= copy[i - 1]);
        }
    }

    void test(int[] A) {
        PancakeSorting p = new PancakeSorting();
        test(A.clone(), p::pancakeSort);
        test(A.clone(), p::pancakeSort2);
        test(A.clone(), p::pancakeSort3);
    }

    @Test
    public void test() {
        test(new int[] {3, 2, 4, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
