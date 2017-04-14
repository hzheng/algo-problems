import java.util.*;
import java.util.stream.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/3264486/dashboard#s=p3
// Qualification Round 2017: Problem D - Fashion Show
//
// Each cell in the grid can be empty or can contain '+'(1 point), 'x'(1 point)
// or 'o'(2 points). Whenever any two models share a row or column, at least one
// of the two must be a +. Whenever any two models share a diagonal of the grid,
// at least one of the two must be an x.
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each test case begins with one line with two integers N and M, as described above.
// Then, M more lines follow; the i-th of these lines has a +, x, or o character
// and two integers Ri and Ci(the position of the model). The rows of the grid are
// numbered 1 through N, from top to bottom. The columns of the grid are numbered 1
// through N, from left to right.
// Output
// For each test case, first output one line containing Case #x: y z, where x is the
// test case number, y is the number of style points earned in your arrangement, and
// z is the total number of models you have added and/or substituted in. Then, for
// each model that you have added or substituted in, output exactly one line in
// exactly the same format described in the Input section, where the character is
// the type of the model that you have added or substituted in.
// If there are multiple valid answers, you may output any one of them.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ N ≤ 100.
// 1 ≤ Ci ≤ N, for all i.
// 0 ≤ M ≤ N ^ 2.
// It is guaranteed that the set of pre-placed models follows the rules.
// Small dataset
// Ri = 1, for all i.
// Large dataset
// 1 ≤ Ri ≤ N, for all i.
public class FashionShow {
    // Greedy + Sort
    public static Result display(int N, List<int[]> models) {
        List<int[]> rooks = new ArrayList<>();
        List<int[]> bishops = new ArrayList<>();
        for (int[] model : models) {
            int type = model[0];
            int[] pos = new int[] {model[1] - 1, model[2] - 1};
            if (type == 'x' || type == 'o') {
                rooks.add(pos);
            }
            if (type == '+' || type == 'o') {
                bishops.add(pos);
            }
        }
        Result res = new Result(N);
        displayRooks(rooks, res);
        displayBishops(bishops, res);
        res.summarize();
        return res;
    }

    private static class Result {
        public int score;
        public List<String> changes = new ArrayList<>();
        public int N;
        private Map<Integer, Integer> map = new HashMap<>();

        public Result(int N) {
            this.N = N;
        }

        public void change(int row, int col, int type) {
            int index = row * N + col;
            int oldType = map.getOrDefault(index, 0);
            map.put(index, (oldType == 0 || oldType == type) ? type : 'o');
            score++;
        }

        public void summarize() {
            for (int i : map.keySet()) {
                if (map.get(i) < 0) continue;

                changes.add(String.format("%c %d %d", map.get(i), i / N + 1, i % N + 1));
            }
        }
    }

    private static void displayRooks(List<int[]> rooks, Result res) {
        boolean[] usedRows = new boolean[res.N];
        boolean[] usedCols = new boolean[res.N];
        for (int[] pos : rooks) {
            usedRows[pos[0]] = true;
            usedCols[pos[1]] = true;
            res.change(pos[0], pos[1], -1);
        }
        for (int i = 0; i < res.N; i++) {
            if (usedRows[i]) continue;

            for (int j = 0; j < res.N; j++) {
                if (!usedCols[j]) {
                    usedCols[j] = true;
                    res.change(i, j, 'x');
                    break;
                }
            }
        }
    }

    private static void displayBishops(List<int[]> bishops, Result res) {
        int N = res.N;
        boolean[] usedSums = new boolean[2 * N - 1];
        boolean[] usedDiffs = new boolean[usedSums.length];
        int[] sumCount = new int[usedSums.length];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sumCount[i + j]++;
            }
        }
        for (int[] pos : bishops) {
            sumCount[pos[0] + pos[1]]--;
            usedSums[pos[0] + pos[1]] = true;
            usedDiffs[pos[0] - pos[1] + N - 1] = true;
            res.change(pos[0], pos[1], -1);
        }
        Integer[] sums = IntStream.range(0, sumCount.length).boxed().toArray(Integer[]::new);
        Arrays.sort(sums, new Comparator<Integer>() {
            public int compare(Integer a, Integer b) { return sumCount[a] - sumCount[b]; }
        });
        for (int sum : sums) {
            if (usedSums[sum]) continue;

            for (int i = 0; i < N; i++) {
                int j = sum - i;
                if (j >= 0 && j < N && !usedDiffs[i - j + N - 1]) {
                    usedSums[sum] = true;
                    usedDiffs[i - j + N - 1] = true;
                    res.change(i, j, '+');
                    break;
                }
            }
        }
    }

    void test(int N, int[][] models, int expected) {
        assertEquals(expected, display(N, Arrays.asList(models)).score);
    }

    @Test
    public void test() {
        test(2, new int[][] {}, 4);
        test(1, new int[][] {{'o', 1, 1}}, 2);
        test(3, new int[][] {{'+', 2, 3}, {'+', 2, 1}, {'x', 3, 1}, {'+', 2, 2}}, 6);
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
        int N = in.nextInt();
        int M = in.nextInt();
        List<int[]> models = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            models.add(new int[] {in.next().charAt(0), in.nextInt(), in.nextInt()});
        }
        Result res = display(N, models);
        out.println(res.score + " " + res.changes.size());
        for (String change : res.changes) {
            out.println(change);
        }
    }
}
