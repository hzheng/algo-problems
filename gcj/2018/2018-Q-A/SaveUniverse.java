import java.util.*;
import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/00000000000000cb/dashboard
// Qualification Round 2018: Problem A - Saving The Universe Again
//
// An alien robot starts off with a beam with a strength of 1, and it will run a
// program that is a series of instructions, which will be executed one at a 
// time, in left to right order. Each instruction is of one of the following two 
// types: C (for "charge"): Double the beam's strength. S (for "shoot"): Shoot 
// the beam, doing damage equal to the beam's current strength.
// The only way the President can hack is by swapping two adjacent instructions. 
// What is this smallest possible number of hacks which will ensure that the 
// program does no more than D total damage, if it is possible to do so?
// Input
// The first line of the input gives the number of test cases, T. T test cases 
// follow. Each consists of one line containing an integer D and a string P: the
//  maximum total damage our shield can withstand, and the robot's program.
// Output
// For each test case, output one line containing Case #x: y, where x is the 
// test case number and y is either the minimum number of hacks needed to 
// accomplish the goal, or IMPOSSIBLE if it is not possible.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ D ≤ 109.
// 2 ≤ length of P ≤ 30.
// Every character in P is either C or S.
// Time limit: 20 seconds per test set.
// Memory limit: 1GB.
// Test set 1 (Visible)
// The robot's program contains either zero or one C characters.
// Test set 2 (Hidden)
// No additional restrictions to the Limits section.
public class SaveUniverse {
    // Stack
    public static int solve(int D, String P) {
        char[] p = P.toCharArray();
        if (evaluate(p) <= D) return 0;

        ArrayDeque<Integer> chargeStack = new ArrayDeque<>();
        int n = p.length;
        for (int i = 0, shoots = 0; i < n; i++) {
            if (p[i] == 'S') {
                if (++shoots > D) return -1;
            } else {
                chargeStack.push(i);
            }
        }
        for (int end = n, hacks = 0; !chargeStack.isEmpty(); end--) {
            for (int i = chargeStack.pop(); i + 1 < end; i++) {
                swap(p, i, i + 1);
                hacks++;
                if (evaluate(p) <= D) return hacks;
            }
        }
        return -1;
    }

    private static void swap(char[] p, int a, int b) {
        char tmp = p[a];
        p[a] = p[b];
        p[b] = tmp;
    }

    private static int evaluate(char[] p) {
        int res = 0;
        for (int i = 0, power = 1; i < p.length; i++) {
            if (p[i] == 'C') {
                power <<= 1;
            } else {
                res += power;
            }
        }
        return res;
    }

    // String
    public static int solve2(int D, String P) {
        char[] p = P.toCharArray();
        for (int hacks = 0; ; hacks++) {
            if (evaluate(p) <= D) return hacks;

            int i = String.valueOf(p).lastIndexOf("CS");
            if (i < 0) return -1;

            swap(p, i, i + 1);
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
        int res = solve2(in.nextInt(), in.next());
        out.println(res >= 0 ? String.valueOf(res) : "IMPOSSIBLE");
    }
}
