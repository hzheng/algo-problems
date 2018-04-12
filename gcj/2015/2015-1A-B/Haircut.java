import java.util.*;
import java.io.*;
import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

// https://codejam.withgoogle.com/codejam/contest/4224486/dashboard#s=p1
// Round 1A 2015: Problem B - Haircut
//
// You are waiting in a long line to get a haircut at a trendy barber shop. The 
// shop has B barbers on duty, and they are numbered 1 through B. It always 
// takes the kth barber exactly Mk minutes to cut a customer's hair, and a
// barber can only cut one customer's hair at a time. Once a barber finishes
// cutting hair, he is immediately free to help another customer.
// While the shop is open, the customer at the head of the queue always goes to
// the lowest-numbered barber who is available. When no barber is available, 
// that customer waits until at least one becomes available.
// You are the Nth person in line, and the shop has just opened. Which barber 
// will cut your hair?
// Input
// The first line of the input gives the number of test cases, T. T test cases 
// follow; each consists of two lines. The first contains two space-separated 
// integers B and N -- the number of barbers and your place in line. The 
// customer at the head of the line is number 1, the next one is number 2, and 
// so on. The second line contains M1, M2, ..., MB.
// Output
// For each test case, output one line containing "Case #x: y", where x is the
// test case number (starting from 1) and y is the number of the barber who will
// cut your hair.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ N ≤ 109.
// Small dataset
// 1 ≤ B ≤ 5.
// 1 ≤ Mk ≤ 25.
// Large dataset
// 1 ≤ B ≤ 1000.
// 1 ≤ Mk ≤ 100000.
public class Haircut {
    // Heap
    public static int haircut(int[] minutes, int place) {
        final int N = minutes.length;
        if (place <= N) return place;

        place -= N;
        PriorityQueue<long[]> pq = new PriorityQueue<>(new Comparator<long[]>() {
            public int compare(long[] a, long[] b) {
                if (a[1] != b[1]) return Long.compare(a[1], b[1]);
                return Long.compare(a[0], b[0]);
            }
        });
        int i = 0;
        double speed = 0;
        for (int min : minutes) {
            speed += 1.0 / min;
        }
        long time = (long)(place / speed) - 1;
        for (int min : minutes) {
            place -= (int)(time / min);
            pq.offer(new long[]{i++, (min - time % min)});
        }
        while (true) {
            long[] cur = pq.poll();
            if (--place == 0) return (int)(cur[0] + 1);

            cur[1] += minutes[(int)cur[0]];
            pq.offer(cur);
        }
    }

    // Only works for small dataset
    // Heap
    public static int haircut0(int[] minutes, int place) {
        final int N = minutes.length;
        int lcm = 1;
        for (int min : minutes) {
            lcm = min / gcd(min, lcm) * lcm;
        }
        int totalTimes = 0;
        for (int i = 0; i < N; i++) {
            totalTimes += lcm / minutes[i];
        }
        place %= totalTimes;
        if (place == 0) {
            place = totalTimes;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                if (a[1] != b[1]) return a[1] - b[1];
                return a[0] - b[0];
            }
        });
        int i = 0;
        for (int min : minutes) {
            pq.offer(new int[]{i++, 0});
        }
        while (true) {
            int[] cur = pq.poll();
            if (--place == 0) return cur[0] + 1;
            cur[1] += minutes[cur[0]];
            pq.offer(cur);
        }
    }

    private static int gcd(int a, int b) {
        if (a < b) return gcd(b, a);
        if (b == 0) return a;

        if ((a & 1) == 0) {
            if ((b & 1) == 0) return gcd(a >> 1, b >> 1) << 1;

            return gcd(a >> 1, b);
        }
        return ((b & 1) == 0) ? gcd(a, b >> 1) : gcd(a - b, b);
    }

    void test(int[] minutes, int place, int expected) {
        assertEquals(expected, haircut0(minutes, place));
        assertEquals(expected, haircut(minutes, place));
    }

    @Test
    public void test() {
        test(new int[] {2, 8, 4}, 7, 1);
        test(new int[] {10, 5}, 4, 1);
        test(new int[] {7, 7, 7}, 12, 3);
        test(new int[] {4, 2, 1}, 8, 1);
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
        int B = in.nextInt();
        int N = in.nextInt();
        int[] M = new int[B];
        for (int i = 0; i < B; i++) {
            M[i] = in.nextInt();
        }
        out.println(haircut(M, N));
    }
}
