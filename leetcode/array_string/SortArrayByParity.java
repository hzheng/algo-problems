import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC905: https://leetcode.com/problems/sort-array-by-parity/
//
// Given an array A of non-negative integers, return an array consisting of all 
// the even elements of A, followed by all the odd elements of A.
public class SortArrayByParity {
    // Two pass
    // beats %(13 ms for 285 tests)
    public int[] sortArrayByParity(int[] A) {
        int n = A.length;
        int[] res = new int[n];
        int index = 0;
        for (int a : A) {
            if (a % 2 == 0) {
                res[index++] = a;
            }
        }
        for (int a : A) {
            if (a % 2 == 1) {
                res[index++] = a;
            }
        }
        return res;
    }

    // Sort
    // beats %(57 ms for 285 tests)
    public int[] sortArrayByParity2(int[] A) {
        Integer[] B = new Integer[A.length];
        for (int i = 0; i < A.length; i++) {
            B[i] = A[i];
        }
        Arrays.sort(B, (a, b) -> Integer.compare(a % 2, b % 2));
        for (int i = 0; i < A.length; i++) {
            A[i] = B[i];
        }
        return A;
    }

    // Sort
    // beats %(104 ms for 285 tests)
    public int[] sortArrayByParity3(int[] A) {
        return Arrays.stream(A).boxed()
                     .sorted((a, b) -> Integer.compare(a % 2, b % 2))
                     .mapToInt(i -> i).toArray();
    }

    // In Place
    // beats %(15 ms for 285 tests)
    public int[] sortArrayByParity4(int[] A) {
        for (int i = 0, j = A.length - 1; i < j; ) {
            if (A[i] % 2 > A[j] % 2) {
                int tmp = A[i];
                A[i] = A[j];
                A[j] = tmp;
            }
            if (A[i] % 2 == 0) {
                i++;
            }
            if (A[j] % 2 == 1) {
                j--;
            }
        }
        return A;
    }

    // In Place
    // beats %(15 ms for 285 tests)
    public int[] sortArrayByParity5(int[] A) {
        for (int i = 0, j = 0; i < A.length; i++)
            if (A[i] % 2 == 0) {
                int tmp = A[j];
                A[j++] = A[i];
                A[i] = tmp;
            }
        return A;
    }

    // One pass
    // beats %(18 ms for 285 tests)
    public int[] sortArrayByParity6(int[] A) {
        int n = A.length;
        int[] res = new int[n];
        for (int cur = 0, i = 0, j = n - 1; i <= j; cur++) {
            if (A[cur] % 2 == 0) {
                res[i++] = A[cur];
            } else {
                res[j--] = A[cur];
            }
        }
        return res;
    }

    void test(int[] A, int[] expected, Function<int[], int[]> f) {
        int split = 0;
        for ( ; split < expected.length; split++) {
            if (expected[split] % 2 == 1) break;
        }
        int[] res = f.apply(A.clone());
        Arrays.sort(expected, 0, split);
        Arrays.sort(res, 0, split);
        Arrays.sort(expected, split, expected.length);
        Arrays.sort(res, split, expected.length);
        assertArrayEquals(expected, res);
    }

    void test(int[] A, int[] expected) {
        SortArrayByParity s = new SortArrayByParity();
        test(A, expected, s::sortArrayByParity);
        test(A, expected, s::sortArrayByParity2);
        test(A, expected, s::sortArrayByParity3);
        test(A, expected, s::sortArrayByParity4);
        test(A, expected, s::sortArrayByParity5);
        test(A, expected, s::sortArrayByParity6);
    }

    @Test
    public void test() {
        test(new int[] {3, 1, 2, 4}, new int[]{2, 4, 3, 1});
        test(new int[] {3, 0, 2, 4}, new int[]{0, 2, 4, 3});
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
