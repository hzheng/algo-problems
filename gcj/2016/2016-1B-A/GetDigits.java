import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/11254486/dashboard#s=p0
// Round 1B 2016: Problem A -  Getting the Digits
//
// Get digits from string.
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each consists of one line with a string S of uppercase English letters.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case
// number and y is a string of digits.
// Limits
// 1 ≤ T ≤ 100.
// A unique answer is guaranteed to exist.
// Small dataset
// 3 ≤ length of S ≤ 20.
// Large dataset
// 3 ≤ length of S ≤ 2000.
public class GetDigits {
    private static final String[] DIGITS = {
        "ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX",
        "SEVEN", "EIGHT", "NINE"
    };

    private static int[][] NUM_CHARS = {{0, 'Z'}, {6, 'X'},  {7, 'S'}, {5, 'V'}, {4, 'U'},
                                        {2, 'W'}, {3, 'R'}, {8, 'G'}, {1, 'O'}, {9, 'I'}};
    // Or: zERO, SIx, EIgHT, TwO, FOuR, fIVE, SEvEN, tHREE, NiNE, oNE.

    public static String getDigits(String s) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'A']++;
        }
        StringBuilder sb = new StringBuilder();
        for (int[] num : NUM_CHARS) {
            int digit = num[0];
            int count = freq[num[1] - 'A'];
            for (int i = count; i > 0; i--) {
                sb.append(digit);
            }
            for (char c : DIGITS[digit].toCharArray()) {
                freq[c - 'A'] -= count;
            }
        }
        char[] chars = sb.toString().toCharArray();
        Arrays.sort(chars);
        return String.valueOf(chars);
    }

    public static String getDigits2(String s) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'A']++;
        }
        int[] res = new int[10];
        int total = 0;
        for (int[] num : NUM_CHARS) {
            int digit = num[0];
            int count = res[digit] = freq[num[1] - 'A'];
            for (char c : DIGITS[digit].toCharArray()) {
                freq[c - 'A'] -= count;
            }
            total += count;
        }
        char[] chars = new char[total];
        for (int i = 0, j = 0; i < 10; i++) {
            for (int count = res[i]; count > 0; count--) {
                chars[j++] = (char)('0' + i);
            }
        }
        return String.valueOf(chars);
    }

    void test(String s, String expected) {
        assertEquals(expected, getDigits(s));
        assertEquals(expected, getDigits2(s));
    }

    @Test
    public void test() {
        test("OZONETOWER", "012");
        test("WEIGHFOXTOURIST", "2468");
        test("OURNEONFOE", "114");
        test("ETHER", "3");
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
        out.println(getDigits2(in.next()));
    }
}
