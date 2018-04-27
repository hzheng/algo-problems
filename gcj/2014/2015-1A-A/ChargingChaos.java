import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/2984486/dashboard#s=p0
// Round 1A 2014: Problem A - Charging Chaos
//
// Shota has just moved into his newly built farmhouse, but it turns out that
// the outlets haven't been configured correctly for all of his devices. Shota
// owns a large number of smartphones and laptops, and even owns a tablet for
// his favorite cow Wagyu to use. In total, he owns N different devices. They
// each require a different electric flow to charge. Similarly, each outlet in
// the house outputs a specific electric flow. An electric flow can be
// represented by a string of 0s and 1s of length L. Shota would like to be able
// to charge all N of his devices at the same time. Coincidentally, there are
// exactly N outlets in his new house. In order to configure the electric flow
// from the outlets, there is a master control panel with L switches. The ith
// switch flips the ith bit of the electric flow from each outlet in the house.
// For example, if the electric flow from the outlets is:
// Outlet 0: 10
// Outlet 1: 01
// Outlet 2: 11
// Then flipping the second switch will reconfigure the electric flow to:
// Outlet 0: 11
// Outlet 1: 00
// Outlet 2: 10
// Misaki has been hired by Shota to help him solve this problem. She has
// measured the electric flows from the outlets in the house, and noticed that
// they are all different. Decide if it is possible for Shota to charge all of
// his devices at the same time, and if it is possible, figure out the minimum
// number of switches that needs to be flipped.
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow. Each test case consists of three lines. The first line contains two
// space-separated integers N and L. The second line contains N space-separated
// strings of length L, representing the initial electric flow from the outlets.
// The third line also contains N space-separated strings of length L,
// representing the electric flow required by Shota's devices.
// Output
// For each test case, output one line containing "Case #x: y", where x is the
// case number (starting from 1) and y is the minimum number of switches to be
// flipped in order for Shota to charge all his devices. If it is impossible, y
// should be the string "NOT POSSIBLE" (without the quotes).
// Limits
// 1 ≤ T ≤ 100.
// No two outlets will be producing the same electric flow, initially.
// No two devices will require the same electric flow.
// Small dataset
// 1 ≤ N ≤ 10.
// 2 ≤ L ≤ 10.
// Large dataset
// 1 ≤ N ≤ 150.
// 10 ≤ L ≤ 40.
public class ChargingChaos {
    // Bit Manipulation
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(N)
    public static int minSwitches(String[] initial, String[] required) {
        int N = initial.length;
        long[] source = new long[N];
        for (int i = 0; i < N; i++) {
            source[i] = Long.valueOf(initial[i], 2);
        }
        Arrays.sort(source);
        long[] target = new long[N];
        for (int i = 0; i < N; i++) {
            target[i] = Long.valueOf(required[i], 2);
        }
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            long flips = source[0] ^ target[i];
            long[] tgt = target.clone();
            for (int j = 0; j < N; j++) {
                tgt[j] ^= flips;
            }
            Arrays.sort(tgt);
            if (Arrays.equals(source, tgt)) {
                res = Math.min(res, Long.bitCount(flips));
            }
        }
        return (res == Integer.MAX_VALUE) ? -1 : res;
    }

    void test(String[] initial, String[] required, int expected) {
        assertEquals(expected, minSwitches(initial, required));
    }

    @Test
    public void test() {
        test(new String[] {"01", "11", "10"},
             new String[] {"11", "00", "10"}, 1);
        test(new String[] {"101", "111"}, new String[] {"010", "001"}, -1);
        test(new String[] {"01", "10"}, new String[] {"10", "01"}, 0);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz =
                new Object(){}.getClass().getEnclosingClass().getSimpleName();
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
        int N = in.nextInt();
        int L = in.nextInt();
        String[] initial = new String[N];
        String[] required = new String[N];
        for (int i = 0; i < N; i++) {
            initial[i] = in.next();
        }
        for (int i = 0; i < N; i++) {
            required[i] = in.next();
        }
        int res = minSwitches(initial, required);
        out.println(res >= 0 ? ("" + res) : "NOT POSSIBLE");
    }
}
