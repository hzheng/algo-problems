import java.util.*;
import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/0000000000007764/dashboard
// Round 1B 2018: Problem A - Rounding Error
//
// You are asking a total of N people to tell you their favorite language. Each 
// person is free to name any language, some people have already responded, and
// you have gathered this information as a list of counts. You plan to publish 
// the results in a table listing each language and the percentage of people who
// picked it. You will round each percentage to the nearest integer, rounding up
// any percentage with a decimal part equal to or greater than 0.5. Sometimes 
// the rounded percentages do not add up to exactly 100. After you are done 
// surveying the remaining people, what is the largest value that the rounded 
// percentages could possibly add up to?
// Input
// The first line of the input gives the number of test cases, T. T test cases 
// follow; each consists of two lines. The first line consists of two integers N
// and L: the total number of people in the survey, and the total number of 
// different languages represented among the people who have already responded.
// The second line consists of L integers Ci; the i-th of these is the number of
// people who said that the i-th of the represented languages was their favorite.
// Output
// For each test case, output one line containing Case #x: y, where x is the 
// test case number and y is the largest value that the percentages could 
// possibly add up to, as described above.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ L < N.
// 1 ≤ Ci, for all i.
// The sum of all Ci values < N.
// Time limit: 10 seconds per test set.
// Memory limit: 1GB.
// Test set 1 (Visible)
// 2 ≤ N ≤ 25.
// Test set 2 (Visible)
// 2 ≤ N ≤ 250.
// Test set 3 (Hidden)
// 2 ≤ N ≤ 10^5.
public class RoundingError {
    // Heap + TreeSet
    // time complexity: O(N * log(N)), space complexity: O(N)
    public static int solve(int N, int[] C) {
        int remains = N;
        for (int c : C) {
            remains -= c;
        }
        NavigableSet<Integer> roundSet = new TreeSet<>();
        for (int i = 1; i < N; i++) {
            if (round(i, N)) {
                roundSet.add(i);
            }
        }
        int res = 0;
        for (int c : C) {
            res += (100.0 * c / N + 0.5);
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[1] - b[1];
            }
        });
        for (int c : C) {
            if (!round(c, N)) {
                addQueue(c, roundSet, pq);
            }
        }
        for (int i = 0; i < remains; i++) {
            addQueue(0, roundSet, pq);
        }
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            if (cur[1] > remains) break;

            remains -= cur[1];
            res += (100.0 * (cur[0] + cur[1]) / N + 0.5) - 100 * cur[0] / N;
        }
        return res + 100 * remains / N;
    }

    private static void addQueue(int c, NavigableSet<Integer> roundSet,
                                 PriorityQueue<int[]> pq) {
        Integer next = roundSet.higher(c);
        if (next != null) {
            pq.offer(new int[] { c, next - c });
        }
    }

    private static boolean round(int p, int q) {
        p *= 100;
        return p / (double) q - (p / q) >= 0.5;
    }

    public static void main(String[] args) {
        Scanner in =
            new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int N = in.nextInt();
        int L = in.nextInt();
        int[] C = new int[L];
        for (int i = 0; i < L; i++) {
            C[i] = in.nextInt();
        }
        out.println(solve(N, C));
    }
}
