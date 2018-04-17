import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/4314486/dashboard#s=p0
// Round 1C 2016: Problem A - Senate Evacuation
//
// A senate room needs to be evacuated. There are some senators in the senate
// room, each of whom belongs to of one of N political parties. Those parties
// are named after the first N (uppercase) letters of the English alphabet.
// The emergency door is wide enough for up to two senators, so in each step of
// the evacuation, you may choose to remove either 1 or 2 senators from the room.
// The senate rules indicate the senators in the room may vote on any bill at
// any time, even in the middle of an evacuation! So, the senators must be
// evacuated in a way that ensures that no party ever has an absolute majority.
// Can you construct an evacuation plan?
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow. Each test case consists of two lines. The first line contains a
// single integer N, the number of parties. The second line contains N integers,
// P1, P2, ..., PN, where Pi represents the number of senators of the party
// named after the i-th letter of the alphabet.
// Output
// For each test case, output one line containing Case #x: y, where x is the
// test case number (starting from 1) and y is the evacuation plan. The plan
// must be a space-separated list of instructions, in the order in which they
// are to be carried out, where each instruction is either one or two
// characters, representing the parties of the senators to evacuate in each step.
// It is guaranteed that at least one valid evacuation plan will exist. If
// multiple evacuation plans are valid, you may output any of them.
// Limits
// 1 ≤ T ≤ 50.
// No party will have an absolute majority before the start of the evacuation.
// 1 ≤ Pi ≤ 1000, for all i.
// Small dataset
// 2 ≤ N ≤ 3.
// sum of all Pi ≤ 9.
// Large dataset
// 2 ≤ N ≤ 26.
// sum of all Pi ≤ 1000.
public class SenateEvacuation {
    public static String evacuate(int[] P) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        int N = P.length;
        for (int i = 0; i < N; i++) {
            pq.offer(new int[] {i, P[i]});
        }
        StringBuilder res = new StringBuilder();
        while (true) {
            int[] first = pq.poll();
            if (--first[1] > 0) {
                pq.offer(first);
            }
            if (res.length() > 0) {
                res.append(" ");
            }
            res.append((char)(first[0] + 'A'));
            
            int[] second = pq.poll();
            if (--second[1] > 0) {
                pq.offer(second);
            }
            int size = pq.size();
            if (size == 1) { // avoid remaining exactly 1
                second[1]++;
                pq.offer(second);
            } else {
                res.append((char)(second[0] + 'A'));
                if (size == 0) return res.toString();
            }
        }
    }

    public static String evacuate2(int[] P) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        int N = P.length;
        for (int i = 0; i < N; i++) {
            pq.offer(new int[] {i, P[i]});
        }
        StringBuilder res = new StringBuilder();
        while (true) {
            int size = pq.size();
            if (size == 0) return res.toString();

            if (res.length() > 0) {
                res.append(" ");
            }
            for (int i = (size == 2) ? 2 : 1; i > 0; i--) {
                int[] cur = pq.poll();
                if (--cur[1] > 0) {
                    pq.offer(cur);
                }
                res.append((char)(cur[0] + 'A'));
            }
        }
    }

    void test(int[] P, String expected) {
        assertEquals(expected, evacuate(P));
    }

    @Test
    public void test() {
        test(new int[] {2, 2}, "AB AB");
        test(new int[] {3, 2, 2}, "AC AB A CB");
        test(new int[] {1, 1, 2}, "CA CB");
        test(new int[] {2, 3, 1}, "BA BA BC");
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
        int[] P = new int[N];
        for (int i = 0; i < N; i++) {
            P[i] = in.nextInt();
        }
        out.println(evacuate(P));
    }
}
