import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/4304486/dashboard#s=p2
// Round 1A 2016: Problem C - BFFs
//
// You have N kids in your class, and each one has a different student ID number
// from 1 through N. Every kid in your class has a single best friend forever (BFF),
// and you know who that BFF is for each kid. BFFs are not necessarily reciprocal.
// Your lesson plan for tomorrow includes an activity in which the participants must sit
// in a circle. You want to make the activity as successful as possible by building the
// largest possible circle of kids such that each kid in the circle is sitting directly
// next to their BFF, either to the left or to the right.
// What is the greatest number of kids that can be in the circle?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each test case consists of two lines. The first line of a test case contains a single
// integer N, the total number of kids in the class. The second line of a test case contains
// N integers F1, F2, ..., FN, where Fi is the student ID number of the BFF of the kid with student ID i.
// Output
// For each test case, output one line containing "Case #x: y", where x is the test case number
//(starting from 1) and y is the maximum number of kids in the group that can be arranged in
// a circle such that each kid in the circle is sitting next to his or her BFF.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ Fi ≤ N, for all i.
// Fi ≠ i, for all i. (No kid is their own BFF.)
// Small dataset
// 3 ≤ N ≤ 10.
// Large dataset
// 3 ≤ N ≤ 1000.
public class BFFs {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    public static int largetstCircle(int[] friends) {
        int max = 0;
        final int N = 1000;
        int n = friends.length;
        int[] values = new int[n];
        Map<Integer, Integer> pairs = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (values[i] != 0) continue;

            List<Integer> path = new ArrayList<>();
            int cur = i;
            int value = 0;
            for (; (value = values[cur]) == 0; cur = friends[cur] - 1) {
                values[cur] = -1; // visiting
                path.add(cur);
            }
            int size = path.size();
            if (value > 0) { // distance to pairs is value - pair * N
                for (int j = size - 1; j >= 0; j--) {
                    values[path.get(j)] = ++value;
                }
                pairs.put(value / N, Math.max(pairs.get(value / N), value % N));
                continue;
            }
            int circlePos = (value < -1) ? size : path.indexOf(cur);
            int circle = (value < -1) ? -value : size - circlePos;
            if (value == -1) { // new circle found
                for (int j = circlePos; j < size; j++) {
                    values[path.get(j)] = -circle;
                    if (circle == 2) {
                        pairs.put(path.get(j), 0);
                    }
                }
                max = Math.max(max, circle);
            }
            if (circle == 2) {
                for (int j = circlePos - 1, k = 1 + N * cur; j >= 0; j--, k++) {
                    values[path.get(j)] = k; // keep track of distance from pairs
                }
                pairs.put(cur, Math.max(pairs.get(cur), circlePos));
            } else {
                for (int j = 0; j < circlePos; j++) {
                    values[path.get(j)] = -circle;
                }
            }
        }
        int sum = 0;
        for (int p : pairs.keySet()) {
            int len = pairs.get(p);
            if (len >= 0) {
                sum += len + pairs.put(friends[p] - 1, -1) + 2;
            }
        }
        return Math.max(max, sum);
    }

    public static int largetstCircle2(int[] friends) {
        int n = friends.length;
        int maxCircle = 0;
        int totalPairGroupLen = 0;
        int[] maxPairLens = new int[n];
        for (int i = 0; i < n; i++) {
            int[] path = new int[n];
            int len = 0;
            int cyclePos = i;
            for (; path[cyclePos] == 0; cyclePos = friends[cyclePos] - 1) {
                path[cyclePos] = ++len;
            }
            int circle = len - path[cyclePos] + 1;
            if (circle > 2) {
                maxCircle = Math.max(maxCircle, circle);
            } else if (len > maxPairLens[cyclePos]) {
                totalPairGroupLen += len - maxPairLens[cyclePos];
                maxPairLens[cyclePos] = len;
                if (maxPairLens[friends[cyclePos] - 1] == 0) {
                    maxPairLens[friends[cyclePos] - 1] = 2;
                }
            }
        }
        return Math.max(maxCircle, totalPairGroupLen);
    }

    void test(int[] friends, int expected) {
        assertEquals(expected, largetstCircle(friends));
        assertEquals(expected, largetstCircle2(friends));
    }

    @Test
    public void test() {
        test(new int[] {3, 3, 4, 3}, 3);
        test(new int[] {3, 3, 4, 1}, 3);
        test(new int[] {2, 3, 4, 1}, 4);
        test(new int[] {3, 4, 9, 1, 1, 8, 6, 5, 1, 7}, 3);
        test(new int[] {4, 1, 2, 1, 4, 3, 8, 2, 5, 8}, 7);
        test(new int[] {7, 8, 10, 10, 9, 2, 9, 6, 3, 3}, 6);
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
        int n = in.nextInt();
        int[] friends = new int[n];
        for (int i = 0; i < n; i++) {
            friends[i] = in.nextInt();
        }
        out.println(largetstCircle2(friends));
    }
}
