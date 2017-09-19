import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/4304486/dashboard#s=p0
// Round 1A 2016: Problem A - The Last Word
//
// On the game show The Last Word, the host begins a round by showing the contestant
// a string S of uppercase English letters. The contestant has a whiteboard which is
// initially blank. The host will then present the contestant with the letters of S,
// one by one, in the order in which they appear in S. When the host presents the
// first letter, the contestant writes it on the whiteboard; this counts as the first
// word in the game. After that, each time the host presents a letter, the contestant
// must write it at the beginning or the end of the word on the whiteboard before
// the host moves on to the next letter (or to the end of the game, if there are no more letters).
// The word is called the last word when the contestant finishes writing all of the
// letters from S, under the given rules. The contestant wins the game if their last
// word is the last of an alphabetically sorted list of all of the possible last words
// that could have been produced.
// You are the next contestant on this show, and the host has just showed you the string S.
// What's the winning last word that you should produce?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each consists of one line with a string S.
// Output
// For each test case, output one line containing Case #x: y, where x is the test
// case number and y is the winning last word, as described in the statement.
// Limits: 1 ≤ T ≤ 100.
// Small dataset
// 1 ≤ length of S ≤ 15.
// Large dataset
// 1 ≤ length of S ≤ 1000.
public class TheLastWord {
    // Deque
    public static String lastword(String s) {
        LinkedList<Character> deque = new LinkedList<>();
        deque.add(s.charAt(0));
        outer : for (int i = 1, len = s.length(); i < len; i++) {
            char c = s.charAt(i);
            char first = deque.peekFirst();
            if (c > first) {
                deque.offerFirst(c);
            } else if (c < first) {
                deque.offerLast(c);
            } else {
                for (int j = 1, size = deque.size(); j < size; j++) {
                    int c2 = deque.get(j);
                    if (c > c2) {
                        deque.offerFirst(c);
                        continue outer;
                    } else if (c < c2) {
                        deque.offerLast(c);
                        continue outer;
                    }
                }
                deque.offerFirst(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c : deque) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String lastword2(String s) {
        String res = "";
        for (char c : s.toCharArray()) {
            String a = c + res;
            String b = res + c;
            res = a.compareTo(b) > 0 ? a : b;
        }
        return res;
    }

    void test(String s, String expected) {
        assertEquals(expected, lastword(s));
        assertEquals(expected, lastword2(s));
    }

    @Test
    public void test() {
        test("CAB", "CAB");
        test("JAM", "MJA");
        test("CODE", "OCDE");
        test("ABAAB", "BBAAA");
        test("CABCBBABC", "CCCABBBAB");
        test("ABCABCABC", "CCCBAABAB");
        test("ZXCASDQWE", "ZXCASDQWE");
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
        out.println(lastword2(in.next()));
    }
}
