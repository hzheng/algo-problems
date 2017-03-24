import java.util.Arrays;
import java.util.stream.IntStream;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.3:
 * Randomly generate a set of m integers from an array of size n.
 */
public class Picker {
    /**
     * random number from min(inclusive) to max(exclusive).
     */
    private static int ran(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    // if we're allowed to rearrange the input array
    // proof: given any item, it's probability of unselected should be:
    // (1-1/n)(1-1/(n-1))(1-1/(n-2))...(1-1/(n-m+1))=1-m/n
    public static int[] pick(int[] a, int m) {
        if (m < 1) return new int[0];

        int n = a.length;
        for (int i = 0; i < m; i++) {
            int chosen = ran(i, n);
            int tmp = a[i];
            a[i] = a[chosen];
            a[chosen] = tmp;
        }
        return Arrays.copyOf(a, m);
    }

    // from the book
    /**
     * random number from min(inclusive) to max(inclusive).
     */
    private static int rand(int lower, int higher) {
        return lower + (int)(Math.random() * (higher - lower + 1));
    }

    public static int[] pickRecursive(int[] original, int m) {
        return pickRecursive(original, m, original.length - 1);
    }

    // first pull a random set of size m from the first n-1 elements. Then,we
    // just need to decide if array[n] should be inserted into our subset. To do
    // this is, pick a random number k from 0 through n. If k < m, then insert
    // into result. This will both "fairly" insert it into the subset and fairly
    // remove a random element from the subset.
    private static int[] pickRecursive(int[] original, int m, int i) {
        if (i + 1 == m) {
            return Arrays.copyOf(original, m);
        }

        if (i + 1 < m) return null;
        // assert (i + 1 > m)
        int[] subset = pickRecursive(original, m, i - 1);
        int k = rand(0, i);
        if (k < m) {
            subset[k] = original[i];
        }
        return subset;
    }

    public static int[] pick2(int[] original, int m) {
        int[] subset = new int[m];
        /* Fill in subset array with first part of original array */
        for (int i = 0; i < m; i++) {
            subset[i] = original[i];
        }
        /* Go through rest of original array. */
        for (int i = m; i < original.length; i++) {
            int k = rand(0, i);
            if (k < m) {
                subset[k] = original[i];
            }
        }
        return subset;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private void test(Function<int[], Integer, int[]> pick, String name,
                      int[] a, int m) {
        long t1 = System.nanoTime();
        a = Arrays.copyOf(a, a.length);
        int[] subset = pick.apply(a, m);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        System.out.println(Arrays.toString(subset));
    }

    private void test(int n, int m) {
        int[] a = IntStream.range(1, n).toArray();
        test(Picker::pick, "pick", a, m);
        test(Picker::pickRecursive, "pickRecursive", a, m);
        test(Picker::pick2, "pick2", a, m);
    }

    @Test
    public void test1() {
        test(10, 4);
        test(100, 10);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Picker");
    }
}
