import java.util.*;
import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/0000000000007883/dashboard/000000000002fff7
// Round 1A 2018: Problem C - Edgy Baking
//
// There are N rectangle cookies, we need the sum of the perimeters of all the 
// cookies were as close as possible to P millimeters. For each cookie, we can 
// leave it as is, or make a single straight cut to separate it into two halves 
// with equal area. such a cut must necessarily go through the center of the 
// cookie. The 2 new cookies created in this way cannot themselves be cut again.
// What is the closest we can come to P without exceeding it?
// Input
// The first line of the input gives the number of test cases, T. T test cases 
// follow. Each begins with one line with two integers N and P: the number of 
// cookies, and the desired perimeter sum, respectively. Then, N lines follow.
// The i-th of these has 2 Wi and Hi: the width and height of the i-th cookie.
// Output
// For each test case, output one line containing Case #x: y, where x is the
// test case number and y is a real number: the largest possible sum of the 
// perimeters of all cookies that does not exceed P. 
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ N ≤ 100.
// 1 ≤ Wi ≤ 250, for all i.
// 1 ≤ Hi ≤ 250, for all i.
// P ≥ 2 × the sum of (Wi + Hi) over all i. (P is at least as large as the sum 
// of the perimeters of all cookies before any cuts are made.)
// P ≤ 10^8.
// Time limit: 15 seconds per test set.
// Memory limit: 1GB.
// Test set 1 (Visible)
// Wi = Wj, for all i and j.
// Hi = Hj, for all i and j.
// (All of the provided cookies have the same dimensions.)
// Test set 2 (Hidden)
// No additional limits beyond the general ones.
public class EdgyBaking {
    private static final double EPSILON = 1E-8;

    // Dynamic Programming
    // time complexity: O(N * min(P', MIN_TOTAL))
    // space complexity: O(N * min(P', MIN_TOTAL))
    public static double solve(int[][] C, int P) {
        int p = 0;
        double maxTotal = 0;
        double minTotal = 0;
        for (int[] c : C) {
            int w = c[0];
            int l = c[1];
            p += (w + l) * 2;
            maxTotal += Math.sqrt(w * w + l * l) * 2;
            minTotal += Math.min(w, l) * 2;
        }
        int increase = P - p; // i.e. P'
        if (increase == 0) return p;

        if (maxTotal <= increase) return p + maxTotal;
        if (minTotal <= increase) return P;

        int N = C.length;
        double[][] minSum = new double[N + 1][increase + 1];
        double[][] maxSum = new double[N + 1][increase + 1];
        for (int i = 1; i <= N; i++) {
            for (int j = 0; j <= increase; j++) {
                minSum[i][j] = minSum[i - 1][j];
                maxSum[i][j] = maxSum[i - 1][j];
                int w = C[i - 1][0];
                int l = C[i - 1][1];
                int min = Math.min(w, l) * 2;
                if (min > j) continue; // too big, skip

                double maxS = maxSum[i - 1][j - min] +
                              Math.sqrt(w * w + l * l) * 2;
                double minS = Math.min(maxS, j);
                if (minS > minSum[i][j]
                    || minS == minSum[i][j] && maxS > maxSum[i][j]) {
                    minSum[i][j] = minS;
                    maxSum[i][j] = maxS;
                }
            }
        }
        return Math.min(p + maxSum[N][increase], P);
    }

    // Interval Overlap + Sort
    // time complexity: O(N * log(P')), space complexity: O(N * log(P'))
    public static double solve2(int[][] C, int P) {
        int p = 0;
        for (int[] c : C) {
            p += (c[0] + c[1]) * 2;
        }
        int increase = P - p; // i.e. P'
        List<double[]> intervals = new ArrayList<>();
        intervals.add(new double[2]);
        for (int[] c : C) {
            double l = Math.min(c[0], c[1]) * 2;
            double r = Math.sqrt(c[0] * c[0] + c[1] * c[1]) * 2;
            for (int i = 0, n = intervals.size(); i < n; i++) {
                double nl = intervals.get(i)[0] + l;
                if (nl > increase) continue;

                double nr = intervals.get(i)[1] + r;
                if (nr >= increase) return P;

                intervals.add(new double[] { nl, nr });
            }
            intervals.sort((a, b) -> a[0] == b[0]
                           ? Double.compare(a[1], b[1]) 
                           : Double.compare(a[0], b[0]));
            for (int i = 1, n = intervals.size(); i < n; i++) {
                if (intervals.get(i - 1)[1] >= intervals.get(i)[0]) {
                    intervals.get(i - 1)[1] = intervals.get(i)[1];
                    intervals.remove(i--);
                    n--;
                }
            }
        }
        return p + intervals.get(intervals.size() - 1)[1];
    }

    // passed on Test set 1 but failed on Test set 2
    public static double solve0(int[][] C, int P) {
        int N = C.length;
        Cookie[] cookies = new Cookie[N];
        int i = 0;
        double p = 0;
        for (int[] d : C) {
            Cookie cookie = cookies[i++] = new Cookie(d[0], d[1]);
            p += cookie.p;
        }
        if (p >= P - EPSILON) return p;

        double more = P - p;
        double max = 0;
        double min = Double.MAX_VALUE;
        PriorityQueue<Cookie> pq = new PriorityQueue<>(
            new Comparator<Cookie>() {
            public int compare(Cookie a, Cookie b) {
                if (a.max != b.max) return Double.compare(b.max, a.max);
                return Double.compare(a.min, b.min);
            }
        });
        for (Cookie cookie : cookies) {
            max += cookie.max;
            min = Math.min(min, cookie.min);
            if (cookie.min > more) continue;

            pq.offer(cookie);
        }
        if (max <= more) return p + max;
        if (pq.isEmpty()) return p;

        double fix = 0;
        while (!pq.isEmpty()) {
            Cookie cur = pq.poll();
            if (more >= cur.max) {
                more -= cur.max;
                fix += cur.max - cur.min;
                p += cur.max;
            } else if (more < cur.min) {
                if (cur.min - more <= fix) return P;
            } else {
                return P;
            }
        }
        return p;
    }

    private static class Cookie {
        int min;
        double max;
        int p;

        Cookie(int w, int l) {
            min = Math.min(w, l) * 2;
            max = Math.sqrt(w * w + l * l) * 2;
            p = (w + l) * 2;
        }
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
        int P = in.nextInt();
        int[][] cookies = new int[N][2];
        for (int i = 0; i < N; i++) {
            cookies[i] = new int[] { in.nextInt(), in.nextInt() };
        }
        double res = solve2(cookies, P);
        out.format("%.6f\n", res);
    }
}
