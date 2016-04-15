import java.util.function.Function;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import static org.junit.Assert.*;

import bit_manipulation.BitInteger;

/**
 * Cracking the Coding Interview(5ed) Problem 5.7:
 * An array A contains all the integers from 0 through n, except for one number
 * which is missing. We cannot access an entire integer in A with a single
 * operation. The elements of A are represented in binary, and the only
 * operation we can use to access them is "fetch the jth bit of A[i]" which
 * takes constant time. Find the missing integer. Can you do it in 0(n) time?
 */
public class MissingInt {
    public static int findMissing(List<BitInteger> array) {
        int missing = 0;
        List<BitInteger> buffer = new LinkedList<BitInteger>(array);
        for (int i = 0; buffer.size() > 0; i++) {
            missing |= (findMissing(buffer, i)) << i;
        }
        return missing;
    }

    // each full column should be 2 ^ (col + 1) 0's followed by
    // that many 1's alternatively
    // since we halve list each time, the 0's and 1's difference
    // should be either 1 or 0
    private static int findMissing(List<BitInteger> array, int col) {
        int zeros = 0;
        int ones = 0;
        for (BitInteger i : array) {
            if (i.get(col) == 0) {
                zeros++;
            } else {
                ones++;
            }
        }
        switch (zeros - ones) {
        case 2: case 1:     // one missing, zeros are all good
            remove(array, col, 0);
            return 1;
        case 0: case -1:      // zero missing, ones are all good
            remove(array, col, 1);
            return 0;
        default:
            assert false : (zeros - ones);
        }
        return 0;
    }

    private static void remove(List<BitInteger> array, int col, int bit) {
        Iterator<BitInteger> iter = array.iterator();
        while (iter.hasNext()) {
            if (iter.next().get(col) == bit) {
                iter.remove();
            }
        }
    }

    public static int findMissingRecursive(List<BitInteger> array) {
        return findMissingRecursive(array, 0);
    }

    // adapt from the book
    private static int findMissingRecursive(List<BitInteger> input, int col) {
        if (col >= BitInteger.INTEGER_SIZE) return 0;

        List<BitInteger> oneBits = new ArrayList<BitInteger>(input.size() / 2);
        List<BitInteger> zeroBits = new ArrayList<BitInteger>(input.size() / 2);
        for (BitInteger t : input) {
            if (t.get(col) == 0) {
                zeroBits.add(t);
            } else {
                oneBits.add(t);
            }
        }
        if (zeroBits.size() <= oneBits.size()) {
            int v = findMissingRecursive(zeroBits, col + 1);
            return (v << 1) | 0;
        } else {
            int v = findMissingRecursive(oneBits, col + 1);
            return (v << 1) | 1;
        }
    }

    private List<BitInteger> initArray(int n, int missing) {
        List<BitInteger> array = new ArrayList<BitInteger>();

        for (int i = 1; i <= n; i++) {
            array.add(new BitInteger(i == missing ? 0 : i));
        }

        Collections.shuffle(array);
        return array;
    }

    private void test(int n, int missing) {
        List<BitInteger> array = initArray(n, missing);
        assertEquals(missing, findMissing(array));
        assertEquals(missing, findMissingRecursive(array));
    }

    @Test
    public void test1() {
        test(1, 1);
        test(4, 4);
        test(5, 4);
        test(10, 3);
        test(100, 1);
        test(100, 10);
        test(100, 99);
        test(100, 100);
        test(1024, 1024);
        test(1000, 708);
    }

    void benchmark(Function<List<BitInteger>, Integer> findMiss,
                   int n, int missing) {
        List<BitInteger> array = initArray(n, missing);
        long t = System.nanoTime();
        int result = findMiss.apply(array);
        System.out.format("%.0f ms\n", (System.nanoTime() - t) * 1e-6);
        assertEquals(missing, result);
    }

    void benchmark(int n, int missing) {
        System.out.print("benchmark findMissing...");
        benchmark(MissingInt::findMissing, n, missing);
        System.out.print("benchmark findMissingRecursive...");
        benchmark(MissingInt::findMissingRecursive, n, missing);
    }

    @Test
    public void benchmark() {
        benchmark(1234567, 307108);
        benchmark(12345678, 4507108);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MissingInt");
    }
}
