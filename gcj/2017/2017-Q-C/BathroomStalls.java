import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/3264486/dashboard#s=p2
// Qualification Round 2017: Problem C - Tidy Numbers
//
// A certain bathroom has N + 2 stalls in a single row; the stalls on the left and right
// ends are permanently occupied by the bathroom guards. The other N stalls are for users.
// Whenever someone enters the bathroom,  they follow deterministic rules: For each empty
// stall S, they compute two values LS and RS, each of which is the number of empty stalls
// between S and the closest occupied stall to the left or right, respectively. Then they
// consider the set of stalls with the farthest closest neighbor, if there are more than
// one such stall, they choose the one among those where max(LS, RS) is maximal. If
// there are still multiple tied stalls, they choose the leftmost stall among those.
// K people are about to enter the bathroom; each one will choose their stall before the
// next arrives. Nobody will ever leave. When the last person chooses their stall S,
// what will the values of max(LS, RS) and min(LS, RS) be?
// Input
// The first line of the input gives the number of test cases, T. T lines follow. Each
// line describes a test case with two integers N and K, as described above.
// Output
// For each test case, output one line containing Case #x: y z, where x is the test case
// number, y is max(LS, RS), and z is min(LS, RS).
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ K ≤ N.
// Small dataset 1
// 1 ≤ N ≤ 1000.
// Small dataset 2
// 1 ≤ N ≤ 10 ^ 6.
// Large dataset
// 1 ≤ N ≤ 10 ^ 18.
public class BathroomStalls {
    public static String emptyStalls(long N, long K) {
        int level = (int)(Math.log(K + 1) / Math.log(2));
        // or : int level = (int)Math.ceil(Math.log(K + 1) / Math.log(2) - 1e-8);
        if ((1L << level) <= K) {
            level++;
        }
        long min = N; // min(LS, RS) in second-to-last level
        for (int i = level - 1; i > 0; i--) {
            min = (min - 1) >> 1;
        }
        long max = ((min << (level - 1)) <= N - K) ? min + 1 : min;
        return (max / 2) + " " + ((max - 1) / 2);
    }

    // SortedMap
    public static String emptyStalls2(long N, long K) {
        SortedMap<Long, Long> map = new TreeMap<>();
        map.put(N, 1L);
        for (long left = K;; ) {
            long x = map.lastKey();
            long n = map.get(x);
            long x1 = (x - 1) >> 1;
            long x0 = x - 1 - x1;
            if ((left -= n) <= 0) return x0 + " " + x1;

            map.remove(x);
            map.put(x0, map.getOrDefault(x0, 0L) + n);
            map.put(x1, map.getOrDefault(x1, 0L) + n);
        }
    }

    void test(long n, long k, String expected) {
        assertEquals(expected, emptyStalls(n, k));
        assertEquals(expected, emptyStalls2(n, k));
    }

    @Test
    public void test() {
        test(3, 1, "1 1");
        test(4, 1, "2 1");
        test(6, 3, "1 0");
        test(8, 6, "0 0");
        test(8, 7, "0 0");
        test(9, 8, "0 0");
        test(7, 3, "1 1");
        test(4, 2, "1 0");
        test(5, 2, "1 0");
        test(6, 2, "1 1");
        test(1000, 1000, "0 0");
        test(1000, 1, "500 499");
        test(500000000000000000L, 144115188075855872L, "1 1");
        test(500000000000000000L, 288230376151711743L, "1 0");
        test(650701937524199729L, 556692397629582053L, "0 0");
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n", clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }
        try {
            in = new Scanner(new File(args[0]));
            if (args.length > 1) {
                out = new PrintStream(args[1]);
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        out.println(emptyStalls(in.nextLong(), in.nextLong()));
    }
}
