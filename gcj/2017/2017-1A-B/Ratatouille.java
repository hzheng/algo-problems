import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/5304486/dashboard#s=p1
// Round 1A 2017: Problem B - Ratatouille
//
// Each ingredient packages package contains some amount of one ingredient; different
// packages may have different amounts even if they contain the same ingredient. You ordered
// the same number of packages of each ingredient. You would like to use these packages to
// form as many kits as possible. A kit consists of exactly one package of each ingredient,
// and a label with the integer number of servings of ratatouille that the kit makes. Each
// package must contain between 90 and 110 percent (inclusive) of the amount of that ingredient
// that is actually needed to make the number of servings on the kit's label.
// Input
// The first line of the input gives the number of test cases, T. T test cases follow. Each case
// consists of the following:
// One line with two integers N: the number of ingredients, and P, the number of packages of
// each ingredient. One line with N integers Ri. The i-th of these represents the number of
// grams of the i-th ingredient needed to make one serving of ratatouille. N more lines of P
// integers each. The j-th value on the i-th of these lines, Qij, represents the quantity, in the
// j-th package of the i-th ingredient.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number and
// y is the maximum number of kits you can produce, as described above.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ Ri ≤ 106, for all i.
// 1 ≤ Qij ≤ 106, for all i and j.
// Small dataset
// 1 ≤ N ≤ 2.
// 1 ≤ P ≤ 8.
// Large dataset
// 1 ≤ N ≤ 50.
// 1 ≤ P ≤ 50.
// N × P ≤ 1000.
public class Ratatouille {
    // Sort + Greedy
    public static int maxKits(int[] ingredients, int[][] packages) {
        for (int[] pkg : packages) {
            Arrays.sort(pkg);
        }
        int N = packages.length;
        int P = packages[0].length;
        int[] pkgStart = new int[N];
        int[] lastPkgStart = new int[N];
        int kits = 0;
        for (int weight : packages[0]) {
            int minServe = (weight * 10 - 1) / (11 * ingredients[0]) + 1;
            int maxServe = weight * 10 / (9 * ingredients[0]);
            serveLoop : for (int serve = minServe; serve <= maxServe; serve++) {
                ingredientLoop : for (int i = 1; i < N; i++) {
                    double minWeight = serve * ingredients[i] * 0.9;
                    double maxWeight = serve * ingredients[i] * 1.1;
                    for (int j = pkgStart[i]; j < P; j++) {
                        if (packages[i][j] >= minWeight && packages[i][j] <= maxWeight) {
                            pkgStart[i] = j + 1;
                            continue ingredientLoop;
                        }
                    }
                    System.arraycopy(lastPkgStart, 0, pkgStart, 0, N);
                    continue serveLoop;
                }
                System.arraycopy(pkgStart, 0, lastPkgStart, 0, N);
                kits++;
                break;
            }
        }
        return kits;
    }

    public static int maxKits2(int[] ingredients, int[][] packages) {
        for (int[] pkg : packages) {
            Arrays.sort(pkg);
        }
        int N = packages.length;
        int P = packages[0].length;
        int[] pkgStart = new int[N];
        for (int kits = 0;; ) {
            int maxLeft = 0;
            int minRight = Integer.MAX_VALUE;
            int minMaxIndex = 0;
            for (int i = 0; i < N; i++) {
                if (pkgStart[i] >= P) return kits;

                int weight = packages[i][pkgStart[i]];
                int minServe = (weight * 10 - 1) / (11 * ingredients[i]) + 1;
                int maxServe = weight * 10 / (9 * ingredients[i]);
                maxLeft = Math.max(maxLeft, minServe);
                if (maxServe < minRight) {
                    minMaxIndex = i;
                    minRight = maxServe;
                }
            }
            if (maxLeft > minRight) {
                pkgStart[minMaxIndex]++;
            } else { // has intersection
                kits++;
                for (int i = 0; i < N; i++) {
                    pkgStart[i]++;
                }
            }
        }
    }

    void test(int[] ingredients, int[][] packages, int expected) {
        assertEquals(expected, maxKits(ingredients, packages));
        assertEquals(expected, maxKits2(ingredients, packages));
    }

    @Test
    public void test() {
        test(new int[] {500, 300}, new int[][] {{900}, {660}}, 1);
        test(new int[] {500, 300}, new int[][] {{1500}, {809}}, 0);
        test(new int[] {50, 100}, new int[][] {{450, 449}, {1100, 1101}}, 1);
        test(new int[] {70, 80, 90},
             new int[][] {{1260, 1500, 700}, {800, 1440, 1600}, {1700, 1620, 900}}, 3);
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
        int N = in.nextInt();
        int P = in.nextInt();
        int[] ingredients = new int[N];
        for (int i = 0; i < N; i++) {
            ingredients[i] = in.nextInt();
        }
        int[][] packages = new int[N][P];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < P; j++) {
                packages[i][j] = in.nextInt();
            }
        }
        out.println(maxKits2(ingredients, packages));
    }
}
