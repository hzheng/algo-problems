import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/8294486/dashboard#s=p1
// Round 1B 2017: Problem B - Stable Neigh-bors
//
// Each of unicorns has either one or two of the following kinds of hairs in its mane. The
// color of a mane depends on exactly which sorts of colored hairs it contains:
// A mane with only one color of hair appears to be that color. A mane with only blue
// hairs is blue. A mane with red and yellow hairs appears orange. A mane with yellow
// and blue hairs appears green. A mane with red and blue hairs appears violet.
// You have R, O, Y, G, B, and V unicorns with red, orange, yellow, green, blue, and violet
// manes, respectively. You have a circular stable with N stalls, arranged in a ring such
// that each stall borders two other stalls. You would like to put exactly one of your unicorns
// in each of these stalls. No unicorn can be next to another unicorn that shares at least one
// of the hair colors in its mane.
// Is it possible to place all of your unicorns? If so, provide any one arrangement.
// Input
// The first line of the input gives the number of test cases, T. T test cases follow. Each
// consists of one line with seven integers: N, R, O, Y, G, B, and V.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number
// and y is IMPOSSIBLE if it is impossible to place all the unicorns, or a string of N characters
// representing the placements of unicorns in stalls, starting at a point of your choice and
// reading clockwise around the circle.
// If multiple arrangements are possible, you may print any of them.
// Limits
// 1 ≤ T ≤ 100.
// 3 ≤ N ≤ 1000.
// R + O + Y + G + B + V = N.
// 0 ≤ Z for each Z in {R, O, Y, G, B, V}.
// Small dataset
// O = G = V = 0. (Each unicorn has only one hair color in its mane.)
// Large dataset
// No restrictions beyond the general limits.
public class StableNeighbors {
    public static String place(int N, int R, int O, int Y, int G, int B, int V) {
        StringBuilder sb = new StringBuilder();
        if (!checkCompound(N, R, G, "RG", sb) || !checkCompound(N, Y, V, "YV", sb)
            || !checkCompound(N, B, O, "BO", sb)) return sb.toString();

        R -= G;
        Y -= V;
        B -= O;
        int total = N - 2 * G - 2 * V - 2 * O;
        if (R * 2 > total || Y * 2 > total || B * 2 > total) return "";

        if (R >= Y && R >= B) {
            for (int i = B + Y - R; i > 0; i--) {
                sb.append("RYB");
            }
            for (int i = R - B; i > 0; i--) {
                sb.append("RY");
            }
            for (int i = R - Y; i > 0; i--) {
                sb.append("RB");
            }
        } else if (Y >= R && Y >= B) {
            for (int i = R + B - Y; i > 0; i--) {
                sb.append("YRB");
            }
            for (int i = Y - R; i > 0; i--) {
                sb.append("YB");
            }
            for (int i = Y - B; i > 0; i--) {
                sb.append("YR");
            }
        } else if (B >= R && B >= Y) {
            for (int i = R + Y - B; i > 0; i--) {
                sb.append("BRY");
            }
            for (int i = B - R; i > 0; i--) {
                sb.append("BY");
            }
            for (int i = B - Y; i > 0; i--) {
                sb.append("BR");
            }
        }
        return addCompound(sb, G, V, O);
    }

    private static boolean checkCompound(int N, int single, int composite, String colors,
                                         StringBuilder sb) {
        if (single < composite) return false;

        if (single == composite && single > 0) {
            if (single + composite != N) return false;

            for (int i = 0; i < single; i++) {
                sb.append(colors);
            }
            return false;
        }
        return true;
    }

    private static String addCompound(StringBuilder sb, int G, int V, int O) {
        int pos = sb.indexOf("R");
        for (int i = 0; i < G; i++) {
            sb.insert(pos, "RG");
        }
        pos = sb.indexOf("Y");
        for (int i = 0; i < V; i++) {
            sb.insert(pos, "YV");
        }
        pos = sb.indexOf("B");
        for (int i = 0; i < O; i++) {
            sb.insert(pos, "BO");
        }
        return sb.toString();
    }

    public static String place2(int N, int R, int O, int Y, int G, int B, int V) {
        StringBuilder sb = new StringBuilder();
        if (!checkCompound(N, R, G, "RG", sb) || !checkCompound(N, Y, V, "YV", sb)
            || !checkCompound(N, B, O, "BO", sb)) return sb.toString();

        R -= G;
        Y -= V;
        B -= O;
        int total = N - 2 * G - 2 * V - 2 * O;
        if (R * 2 > total || Y * 2 > total || B * 2 > total) return "";

        return addCompound(new StringBuilder(placeSingle(total, R, Y, B)), G, V, O);
    }

    private static String placeSingle(int N, int R, int Y, int B) {
        if (R >= Y && R >= B) return placeSingle(N, "RYB", new int[] {R, Y, B});
        if (Y >= R && Y >= B) return placeSingle(N, "YBR", new int[] {Y, B, R});
        return placeSingle(N, "BRY", new int[] {B, R, Y});
    }

    private static String placeSingle(int N, String colors, int[] count) {
        char[] s = new char[N];
        int i = 0;
        int j = 0;
        for (char c : colors.toCharArray()) {
            i = fill(s, i, c, count[j++]);
        }
        return String.valueOf(s);
    }

    private static int fill(char[] s, int start, char color, int count) {
        int i = start;
        for (; i < s.length && count > 0; i += 2, count--) {
            s[i] = color;
        }
        if (count == 0) return i;

        for (i = 1; i < s.length && count > 0; i += 2, count--) {
            s[i] = color;
        }
        return i;
    }

    void test(int N, int R, int O, int Y, int G, int B, int V, String expected) {
        assertEquals(expected, place(N, R, O, Y, G, B, V));
        assertEquals(expected, place2(N, R, O, Y, G, B, V));
    }

    @Test
    public void test() {
        // test(6, 2, 0, 1, 1, 2, 0, "YBRGRB");
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
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
        String res = place2(in.nextInt(), in.nextInt(), in.nextInt(),
                            in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
        out.println(res.isEmpty() ? "IMPOSSIBLE" : res);
    }
}
