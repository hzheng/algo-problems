import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.6:
 * Find the smallest one million numbers in one billion numbers. Assume that the
 * computer memory can hold all one billion numbers.
 */
public class Smallest {
    private static int rand(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static int partition(int[] a, int left, int right, int pivot) {
        while (true) {
            for (; left <= right; left++) {
                if (a[left] > pivot) break;
            }

            for (; left <= right; right--) {
                if (a[right] <= pivot) break;
            }

            if (left > right) return left - 1;

            // swap
            int tmp = a[left];
            a[left] = a[right];
            a[right] = tmp;
        }
    }

    private static int getRank(int[] a, int rank, int left, int right) {
        int pivot = a[rand(left, right)];
        int leftEnd = partition(a, left, right, pivot);
        int leftSize = leftEnd - left + 1;
        // FIXME: if a contains duplicate numbers, may cause infinite recursion
        if (leftSize == rank) { // (leftSize == rank + 1) {
            return max(a, left, leftEnd);
        }
        if (leftSize > rank) {
            return getRank(a, rank, left, leftEnd);
        }
        return getRank(a, rank - leftSize, leftEnd + 1, right);
    }

    public static int getRank(int[] a, int rank) {
        return getRank(a, rank, 0, a.length - 1);
    }

    private static int max(int[] a, int left, int right) {
        int max = Integer.MIN_VALUE;
        for (int i = left; i <= right; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }
        return max;
    }

    public static int[] smallest(int[] a, int rank) {
        int critical = getRank(a, rank);
        int[] result = new int[rank];
        for (int i = 0, j = 0; j < rank; i++) {
            if (a[i] <= critical) {
                result[j++] = a[i];
            }
        }
        return result;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private void test(Function<int[], Integer, Integer> getRank,
                      String name, int[] a, int[] rank, int[] expected) {
        // long t1 = System.nanoTime();
        for (int i = 0; i < rank.length; i++) {
            assertEquals(expected[i], (int)getRank.apply(a.clone(), rank[i]));
        }
    }

    private void test(int[] a, int[] rank, int[] expected) {
        test(Smallest::getRank, "getRank", a, rank, expected);
    }

    @Test
    public void test1() {
        test(new int[] {9, 7, 22, 1, 3, 19, 6, 10, 24},
             new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 6, 7, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Smallest");
    }
}
