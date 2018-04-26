import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/4244486/dashboard#s=p1
// Round 1C 2015: Problem B - Typewriter Monkey
//
// Your has decided to use monkeys randomly typing at keyboards to write great
// works of literature. You are the supervisor for one monkey with a keyboard
// containing K keys, each of which is labeled with an uppercase English letter.
// The monkey will start with an empty string and repeat the following S times:
// choose a key from its keyboard uniformly at random and press it, adding a
// copy of that key's letter to the right end of the string. The final resulting
// string will have length S. You have a target word of length L that you are
// hoping the monkey will type. You plan to pay the monkey one banana for each
// instance of the target word that it types. When you go to inspect the monkey's
// work, you will bring along the minimum number of bananas that you need to
// ensure that you will always have enough bananas to pay the monkey. Then, you
// will pay the monkey one banana for each instance of the target word that it
// actually typed. You will keep the remaining bananas that you brought with you.
// What is the expected number of bananas that you will get to keep?
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow. Each consists of three lines. The first contains 3 space-separated
// positive integers: K, L, and S. The second contains a string of K uppercase
// English letters representing the monkey's keyboard. The third contains a
// string of L uppercase English letters representing the target word.
// Output
// For each test case, output one line containing "Case #x: y", where y is the
// expected number of bananas you will get to keep after paying the monkey.
// y will be considered correct if it is within an absolute or relative error of
// 10^-6 of the correct answer.
// Limits
// 1 ≤ T ≤ 100.
// Small dataset
// 1 ≤ K ≤ 7.
// 1 ≤ L ≤ S ≤ 7.
// Large dataset
// 1 ≤ K ≤ 100.
// 1 ≤ L ≤ S ≤ 100.
public class TypewriterMonkey {
    public static double type(String keyboard, String target, int repeat) {
        int[] freq = new int[26];
        for (char c : keyboard.toCharArray()) {
            freq[c - 'A']++;
        }
        double[] chance = new double[freq.length];
        for (char c : target.toCharArray()) {
            if (freq[c - 'A'] == 0) return 0;

            chance[c - 'A'] = (double)freq[c - 'A'] / keyboard.length();
        }
        int L = target.length();
        double p = 1;
        for (int i = 0; i < L; i++) {
            p *= chance[target.charAt(i) - 'A'];
        }
        double expected = p * (repeat - L + 1);

        int minShift = L;
        outer : for (int i = 1; i <= repeat - L; i++) {
            for (int j = 0; i + j < L; j++) {
                if (target.charAt(j) != target.charAt(i + j)) continue outer;
            }
            minShift = i;
            break;
        }
        double max = (repeat - L) / minShift + 1;
        return max - expected;
    }

    void test(String keyboard, String target, int repeat, double expected) {
        assertEquals(expected, type(keyboard, target, repeat), 1E-6);
    }

    @Test
    public void test() {
        test("BANANAS", "MONKEY", 6, 0);
        test("AA", "AAA", 4, 0);
        test("AB", "B", 2, 1);
        test("GOOGLE", "GO", 2, 0.8888889);
        test("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ROSENCRANTZ", 100, 9);
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
        int K = in.nextInt();
        int L = in.nextInt();
        int S = in.nextInt();
        String keyboard = in.next();
        String target = in.next();
        out.format("%.6f\n", type(keyboard, target, S));
    }
}
