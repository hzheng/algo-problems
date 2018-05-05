import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/2994486/dashboard#s=p0
// Round 1B 2014: Problem A - The Repeater
//
// Fegla and Omar invented a 2 player game. Fegla writes down N strings. Omar's
// task is to make all the strings identical, if possible, using the minimum
// number of actions of the following two types: Select any character in any of
// the strings and repeat it. Select any two adjacent and identical characters
// in any of the strings, and delete one of them.
// Help Omar to win this game by writing a program to find if it is possible to
// make the given strings identical, and to find the minimum number of moves if
// it is possible.
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow. Each test case starts with a line containing an integer N which is
// the number of strings. Followed by N lines, each line contains a non-empty
// string (each string will consist of lower case English characters only).
// Output
// For each test case, output one line containing "Case #x: y", where x is the
// test case number (starting from 1) and y is the minimum number of moves to
// make the strings identical. If there is no possible way to make all strings
// identical, print "Fegla Won" (quotes for clarity).
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ length of each string ≤ 100.
// Small dataset
// N = 2.
// Large dataset
// 2 ≤ N ≤ 100.
public class Repeater {
    public static int repeat(String[] S) {
        int N = S.length;
        StringBuilder base = new StringBuilder();
        int i = 0;
        char prev = 0;
        for (char c : S[0].toCharArray()) {
            i++;
            if (c != prev) {
                base.append(c);
            }
            prev = c;
        }
        int res = 0;
        int[] offsets = new int[N];
        for (char c : base.toString().toCharArray()) {
            int[] counts = new int[N];
            i = 0;
            for (String s : S) {
                for (; offsets[i] < s.length(); offsets[i]++, counts[i]++) {
                    if (s.charAt(offsets[i]) != c) break;
                }
                if (counts[i++] == 0) return -1;
            }
            res += minRepeat(counts);
        }
        i = 0;
        for (int offset : offsets) {
            if (offset != S[i++].length()) return -1;
        }
        return res;
    }

    private static int minRepeat(int[] counts) {
        Arrays.sort(counts);
        int res = 0;
        for (int i = 0, j = counts.length - 1; i < j; i++, j--) {
            res += counts[j] - counts[i];
        }
        return res;
    }

    private static int minRepeat0(int[] counts) { // inefficient
        Map<Integer, Integer> freq = new HashMap<>();
        for (int c : counts) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        int min = Integer.MAX_VALUE;
        for (int c : freq.keySet()) {
            int v = 0;
            for (int c2 : freq.keySet()) {
                v += Math.abs(c2 - c) * freq.get(c2);
            }
            min = Math.min(min, v);
        }
        return min;
    }

    void test(String[] S, int expected) {
        assertEquals(expected, repeat(S));
    }

    @Test
    public void test() {
        test(new String[] {"mmaw", "maw"}, 1);
        test(new String[] {"gcj", "cj"}, -1);
        test(new String[] {"aaabbb", "ab", "aabb"}, 4);
        test(new String[] {"abc", "abc"}, 0);
        test(new String[] {"aabc", "abbc", "abcc"}, 3);

        test(new String[] {"fffpppxxrrrrjllfwyzhhuhhhhhhhnccccauuaaaaaahhtzgeop"
                           + "hoomjjj",
                           "fppxxrjlllfffwyyzhhuuuuuuuuuhhnccccccauuaahttttzzz"
                           + "geophhhomjjj"}, 38);
        test(new String[] {"krjlf", "krjlfs"}, -1);
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
        String[] S = new String[N];
        for (int i = 0; i < N; i++) {
            S[i] = in.next();
        }
        int res = repeat(S);
        out.println(res >= 0 ? ("" + res) : "Fegla Won");
    }
}
