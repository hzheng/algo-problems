import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/3274486/dashboard#s=p1
// Round 1C 2017: Problem B - Parenting Partnering
//
// Cameron and Jamie are life partners and have recently become parents!
// They are establishing a daily routine and need to decide who will be the main
// person in charge of the baby at each given time. Each of them will be in
// charge for exactly 12 hours per day. They have other activities that they
// want to do on their own. Cameron has AC of these and Jamie has AJ. These
// activities always take place at the same times each day. None of Cameron's
// activities overlap with Jamie's activities, so at least one of the parents
// will always be free to take care of the baby. They want to come up with a
// daily baby care schedule such that:
// Scheduled baby time must not interfere with a scheduled activity.
// Each of Cameron and Jamie must have exactly 720 minutes assigned to them.
// The number of times the person in charge of the baby changes from one partner
// to the other — must be as small as possible.
// Given Cameron's and Jamie's lists of activities, and the restrictions above,
// what is the minimum possible number of exchanges in a daily schedule?
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow. Each test case starts with a line containing two integers AC and AJ,
// the number of activities that Cameron and Jamie have, respectively.
// Then, AC + AJ lines follow. The first AC of these lines contain two integers
// Ci and Di each. The i-th of Cameron's activities starts exactly Ci minutes
// after the start of the day at midnight and ends exactly Di minutes after the
// start of the day at midnight (taking exactly Di - Ci minutes). The last AJ of
// these lines contain two integers Ji and Ki each, representing the starting
// and ending time of one of Jamie's activities, in minutes counting from the
// start of the day at midnight (same format as Cameron's). No activity spans
// two days, and no two activities overlap (except that one might end exactly as
// another starts, but an exchange can still occur at that time).
// Output
// For each test case, output one line containing Case #x: y, where x is the
// test case number (starting from 1) and y the minimum possible number of
// exchanges, as described in the statement.
// Limits
// 1 ≤ T ≤ 100.
// 0 ≤ Ci < Di ≤ 24 × 60, for all i.
// 0 ≤ Ji < Ki ≤ 24 × 60, for all i.
// Any two of the intervals of {[Ci, Di) for all i} union {[Ji, Ki) for all i}
// have an empty intersection.
// sum of {Di - Ci for all i} ≤ 720.
// sum of {Ki - Ji for all i} ≤ 720.
// Small dataset
// 0 ≤ AC ≤ 2.
// 0 ≤ AJ ≤ 2.
// 1 ≤ AC + AJ ≤ 2.
// Large dataset
// 0 ≤ AC ≤ 100.
// 0 ≤ AJ ≤ 100.
// 1 ≤ AC + AJ ≤ 200.
public class ParentingPartnering {
    // Heap + SortedSet
    // time complexity: O(N * log(N))
    public static int minExchange(int[][] acts1, int[][] acts2) {
        final int TOTAL = 12 * 60;
        int[] unallocated = {TOTAL, TOTAL};
        NavigableSet<Interval> intervals = new TreeSet<>();
        for (int[] interval : acts1) {
            int start = interval[0];
            int end = interval[1];
            unallocated[0] -= (end - start);
            intervals.add(new Interval(start, end, 0));
        }
        for (int[] interval : acts2) {
            int start = interval[0];
            int end = interval[1];
            unallocated[1] -= (end - start);
            intervals.add(new Interval(start, end, 1));
        }
        int size = intervals.size();
        if (size <= 1) return size + 1;

        Interval first = intervals.pollFirst();
        // add a virtual last interval(wrapped first), whose end is negative
        intervals.add(new Interval(TOTAL * 2 + first.start, -1, first.kind));
        @SuppressWarnings("unchecked")
        PriorityQueue<Integer>[] slices = new PriorityQueue[] {
            new PriorityQueue<>(), new PriorityQueue<>()
        };
        int res = 0;
        for (Interval prev = first, cur; prev.end >= 0; prev = cur, res++) {
            cur = intervals.pollFirst();
            if (prev.kind == cur.kind) {
                slices[cur.kind].offer(cur.start - prev.end);
                res++;
            }
        }
        for (int i = 0; i < 2; i++) {
            for (PriorityQueue<Integer> q = slices[i]; !q.isEmpty(); res -= 2) {
                if ((unallocated[i] -= q.poll()) < 0) break;
            }
        }
        return res;
    }

    private static class Interval implements Comparable<Interval> {
        int start;
        int end;
        int kind;

        Interval(int start, int end, int kind) {
            this.start = start;
            this.end = end;
            this.kind = kind;
        }

        public int compareTo(Interval other) {
            return start - other.start;
        }
    }

    // Heap + SortedSet
    // time complexity: O(N * log(N))
    public static int minExchange2(int[][] acts1, int[][] acts2) {
        final int TOTAL = 12 * 60;
        int[] unallocated = {TOTAL, TOTAL};
        NavigableSet<int[]> intervals = new TreeSet<>((a, b) -> a[0] - b[0]);
        for (int[] interval : acts1) {
            int start = interval[0];
            int end = interval[1];
            unallocated[0] -= (end - start);
            intervals.add(new int[] {start, end});
        }
        for (int[] interval : acts2) {
            int start = interval[0];
            int end = interval[1];
            unallocated[1] -= (end - start);
            intervals.add(new int[] {start, -end});
        }
        int size = intervals.size();
        if (size <= 1) return size + 1;

        int[] first = intervals.pollFirst();
        int[] last = new int[] {TOTAL * 2 + first[0],
                                (TOTAL * 4) * (first[1] > 0 ? 1 : -1)};
        intervals.add(last);
        @SuppressWarnings("unchecked")
        PriorityQueue<Integer>[] slices = new PriorityQueue[] {
            new PriorityQueue<>(), new PriorityQueue<>()
        };
        int res = 0;
        for (int[] prev = first, cur; prev[1] != last[1]; prev = cur, res++) {
            cur = intervals.pollFirst();
            if ((prev[1] > 0) ^ (cur[1] < 0)) {
                slices[cur[1] > 0 ? 0 : 1].offer(cur[0] - Math.abs(prev[1]));
                res++;
            }
        }
        for (int i = 0; i < 2; i++) {
            for (PriorityQueue<Integer> q = slices[i]; !q.isEmpty(); res -= 2) {
                if ((unallocated[i] -= q.poll()) < 0) break;
            }
        }
        return res;
    }

    void test(int[][] acts1, int[][] acts2, int expected) {
        assertEquals(expected, minExchange(acts1, acts2));
        assertEquals(expected, minExchange2(acts1, acts2));
    }

    @Test
    public void test() {
        test(new int[][] {{900, 1260}, {180, 540}}, new int[][] {}, 4);
        test(new int[][] {{540, 600}}, new int[][] {{840, 900}}, 2);
        test(new int[][] {{1439, 1440}}, new int[][] {{0, 1}}, 2);
        test(new int[][] {{0, 1}, {1439, 1440}},
             new int[][] {{1438, 1439}, {1, 2}}, 4);
        test(new int[][] {{0, 10}, {1420, 1440}, {90, 100}},
             new int[][] {{550, 600}, {900, 950}, {100, 150}, {1050, 1400}}, 6);
        test(new int[][] {{893, 1105}, {1105, 1440}}, new int[][] {}, 2);
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz =
                new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n", clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }

        in = new Scanner(new File(args[0]));
        if (args.length > 1) {
            out = new PrintStream(args[1]);
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int N1 = in.nextInt();
        int N2 = in.nextInt();
        int[][] acts1 = new int[N1][2];
        int[][] acts2 = new int[N2][2];
        for (int i = 0; i < N1; i++) {
            acts1[i] = new int[] {in.nextInt(), in.nextInt()};
        }
        for (int i = 0; i < N2; i++) {
            acts2[i] = new int[] {in.nextInt(), in.nextInt()};
        }
        out.printf("%d%n", minExchange2(acts1, acts2));
    }
}
