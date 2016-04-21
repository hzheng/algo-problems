import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 11.1:
 * Given two sorted arrays, A and B, where A has a large enough buffer at the
 * end to hold B. Write a method to merge B into A in sorted order.
 */
public class ArrayMerge {
    public static void merge(int[] a, int[] b, int lastA, int lastB) {
        int aEnd = lastA - 1;
        int bEnd = lastB - 1;
        int mergedEnd = aEnd + bEnd + 1;
        while (bEnd >= 0) {
            while (aEnd >= 0 && (a[aEnd] > b[bEnd])) {
                a[mergedEnd--] = a[aEnd--];
            }
            while (bEnd >= 0 && (aEnd < 0 || a[aEnd] <= b[bEnd])) {
                a[mergedEnd--] = b[bEnd--];
            }
        }
    }

    // from the book
    public static void merge2(int[] a, int[] b, int lastA, int lastB) {
        int indexMerged = lastB + lastA - 1;
        int indexA = lastA - 1;
        int indexB = lastB - 1;

        /* Merge a and b, starting from the last element in each */
        while (indexA >= 0 && indexB >= 0) {
            if (a[indexA] > b[indexB]) { /* end of a is bigger than end of b */
                a[indexMerged--] = a[indexA--]; // copy element
            } else {
                a[indexMerged--] = b[indexB--]; // copy element
            }
        }

        /* Copy remaining elements from b into place */
        while (indexB >= 0) {
            a[indexMerged--] = b[indexB--];
        }
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public void apply(A a, B b, C c, D d);
    }

    private void test(int[] a, int[] b,
                      Function<int[], int[], Integer, Integer> merge) {
        int[] aa = new int[a.length + b.length];
        System.arraycopy(a, 0, aa, 0, a.length);
        merge.apply(aa, b, a.length, b.length);
        System.out.println(Arrays.toString(aa));
    }

    @Test
    public void test1() {
        int[] a = {1, 3, 4, 5, 6, 8, 10};
        int[] b = {4, 7, 9, 10, 12};
        test(a, b, ArrayMerge::merge);
        test(a, b, ArrayMerge::merge2);
    }

    private int[] test(int[] a, int[] b, String name,
                        Function<int[], int[], Integer, Integer> merge) {
        Arrays.sort(a);
        Arrays.sort(b);
        int[] aa = new int[a.length + b.length];
        System.arraycopy(a, 0, aa, 0, a.length);
        long t1 = System.nanoTime();
        merge.apply(aa, b, a.length, b.length);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        if (aa.length < 20) {
             System.out.println(Arrays.toString(aa));
         }
        return aa;
    }

    private void randArray(int[] a, int[] b, int n) {
        for (int i = 0; i < a.length; i++) {
            a[i] = ThreadLocalRandom.current().nextInt(0, n);
        }
        for (int i = 0; i < b.length; i++) {
            b[i] = ThreadLocalRandom.current().nextInt(0, n);
        }
    }

    @Test
    public void test2() {
        int a[] = new int[100000];
        int b[] = new int[80000];
        for (int i = 0; i < 10; ++i) {
            randArray(a, b, 100);
            int[] sorted1 = test(a, b, "merge", ArrayMerge::merge);
            int[] sorted2 = test(a, b, "merge2", ArrayMerge::merge2);
            assertArrayEquals(sorted1, sorted2);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ArrayMerge");
    }
}
