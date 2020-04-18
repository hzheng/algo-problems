import java.util.*;
import java.io.*;

// Qualification Round 2020: Problem A - Vestigium
//
// The trace of a square matrix is the sum of the values on the main diagonal. An N-by-N square matrix is a Latin square
// if each cell contains one of N different values, and no value is repeated within a row or a column. In this problem,
// we will deal only with "natural Latin squares" in which the N values are the integers between 1 and N.
// Given a matrix that contains only integers between 1 and N, we want to compute its trace and check whether it is a
// natural Latin square. To give some additional information, instead of simply telling us whether the matrix is a
// natural Latin square or not, compute the number of rows and the number of columns that contain repeated values.
// Input
// The first line of the input gives the number of test cases, T. T test cases follow. Each starts with a line
// containing a single integer N: the size of the matrix to explore. Then, N lines follow. The i-th of these lines
// contains N integers Mi,1, Mi,2 ..., Mi,N. Mi,j is the integer in the i-th row and j-th column of the matrix.
// Output
// For each test case, output one line containing Case #x: k r c, where x is the test case number (starting from 1), k
// is the trace of the matrix, r is the number of rows of the matrix that contain repeated elements, and c is the number
// of columns of the matrix that contain repeated elements.
// Limits
// Test set 1 (Visible Verdict)
// Time limit: 20 seconds per test set.
// Memory limit: 1GB.
// 1 ≤ T ≤ 100.
// 2 ≤ N ≤ 100.
// 1 ≤ Mi,j ≤ N, for all i, j.
public class Vestigium {
    // Hash Table
    public static int[] solve(int N, int[][] matrix) {
        int trace = 0;
        int repeatedRows = 0;
        int repeatedCols = 0;
        outer:
        for (int x = 0; x < N; x++) {
            int[] row = matrix[x];
            trace += row[x];
            boolean[] set = new boolean[N + 1];
            for (int y = 0; y < N; y++) {
                if (set[row[y]]) {
                    repeatedRows++;
                    continue outer;
                }

                set[row[y]] = true;
            }
        }
        outer:
        for (int y = 0; y < N; y++) {
            boolean[] set = new boolean[N + 1];
            for (int x = 0; x < N; x++) {
                if (set[matrix[x][y]]) {
                    repeatedCols++;
                    continue outer;
                }

                set[matrix[x][y]] = true;
            }
        }
        return new int[]{trace, repeatedRows, repeatedCols};
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            int N = in.nextInt();
            int[][] matrix = new int[N][N];
            for (int x = 0; x < N; x++) {
                int[] row = matrix[x];
                for (int y = 0; y < N; y++) {
                    row[y] = in.nextInt();
                }
            }
            out.format("Case #%d: ", i);
            int[] res = solve(N, matrix);
            out.println(res[0] + " " + res[1] + " " + res[2]);
        }
    }
}
