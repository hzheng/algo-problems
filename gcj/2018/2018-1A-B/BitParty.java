import java.util.*;
import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/0000000000007883/dashboard/000000000002fff6
// Round 1A 2018: Problem B - Bit Party
//
// R robot shoppers buy B "bits" supplies. The supermarket has C cashiers who 
// can scan customers' purchases. The i-th cashier will: accept a maximum of Mi
// items per customer; take Si seconds to scan each item; spend a further Pi 
// seconds handling payment and packaging up the bits. That is, a customer who 
// brings N bits to the i-th cashier will spend a total of Si × N + Pi seconds
// interacting with that cashier. You will distribute the bits among the robots.
// For each robot with at least one bit, you will choose a different single 
// cashier. (Two robots cannot use the same cashier, and a robot cannot use more 
// than one cashier.) The robots all start interacting with their cashiers at 
// time 0. Once a robot finishes interacting with its cashier, it cannot be 
// given more bits and cannot interact with other cashiers. What is the earliest
// time at which all of the robots can finish interacting with their cashiers?
// Input
// The first line of the input gives the number of test cases, T. T test cases 
// follow. Each begins with one line with three integers R, B, and C: the 
// numbers of robot shoppers, bits, and cashiers. Then, there are C more lines.
// The i-th of these represents the i-th cashier, and it has three integers Mi,
// Si, and Pi: the maximum number of bits, scan time per bit, payment/packaging 
// time for that cashier, as described above.
// Output
// For each test case, output one line containing Case #x: y, where x is the 
// test case number and y is the earliest time at which all robots can finish 
// interacting with their cashiers.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ Mi ≤ 10^9, for all i.
// 1 ≤ Si ≤ 10^9, for all i.
// 1 ≤ Pi ≤ 10^9, for all i.
// The sum of the R largest values of Mi ≥ B. 
// Time limit: 15 seconds per test set.
// Memory limit: 1GB.
// Test set 1 (Visible)
// 1 ≤ R ≤ C ≤ 5.
// 1 ≤ B ≤ 20.
// Test set 2 (Hidden)
// 1 ≤ R ≤ C ≤ 1000.
// 1 ≤ B ≤ 10^9.
public class BitParty {
    // Binary Search
    // time complexity: O(C * log(C) * log(max(S)*B + max(P)))
    public static long solve(int R, int B, int[][] cashiers) {
        long low = 1;
        for (long high = (long) Math.pow(10, 18) * 2; low < high; ) {
            long mid = (low + high) >>> 1;
            if (ok(R, B, cashiers, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private static boolean ok(int R, int B, int[][] cashiers, long t) {
        Arrays.sort(cashiers,
                    (a, b) -> Long.valueOf(mBits(b, t)).compareTo(mBits(a, t)));
        long total = 0;
        for (int i = 0; i < R; i++) {
            if ((total += mBits(cashiers[i], t)) >= B) return true;
        }
        return false;
    }

    private static long mBits(int[] cashier, long t) {
        return Math.min(cashier[0], Math.max(0, (t - cashier[2]) / cashier[1]));
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
        int R = in.nextInt();
        int B = in.nextInt();
        int C = in.nextInt();
        int[][] cashiers = new int[C][3];
        for (int i = 0; i < C; i++) {
            cashiers[i][0] = in.nextInt();
            cashiers[i][1] = in.nextInt();
            cashiers[i][2] = in.nextInt();
        }
        out.println(solve(R, B, cashiers));
    }
}
