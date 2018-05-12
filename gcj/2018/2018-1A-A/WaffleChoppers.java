import java.util.*;
import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/0000000000007883/dashboard
// Round 1A 2018: Problem A - Waffle Choppers
//
// A waffle is a grid of square cells with R rows and C columns. Each cell of 
// the waffle is either empty, or contains a single chocolate chip. A horizontal
// cut runs along the entire gridline between two of the rows; a vertical cut 
// runs along the entire gridline between two of the columns. One chef will make
// exactly H different horizontal cuts and another will make exactly V different
// vertical cuts. Each piece must have exactly the same number of chocolate 
// chips. Can you determine whether the chefs can accomplish this goal?
// Input
// The first line of the input gives the number of test cases, T; T test cases 
// follow. Each begins with one line containing four integers R, C, H, and V: 
// the number of rows and columns in the waffle, and the exact numbers of 
// horizontal and vertical cuts that the chefs must make. Then, there are R more
// lines of C characters each; the j-th character in the i-th of these lines 
// represents the cell in the i-th row and the j-th column of the waffle. Each 
// character is either @, which means the cell has a chocolate chip, or ., which
// means the cell is empty.
// Output
// For each test case, output one line containing Case #x: y, where x is the 
// test case number (starting from 1) and y is POSSIBLE if the chefs can 
// accomplish the goal as described above, or IMPOSSIBLE if they cannot.
// Limits
// 1 ≤ T ≤ 100.
// Time limit: 6 seconds per test set.
// Memory limit: 1GB.
// Test set 1 (Visible)
// 2 ≤ R ≤ 10.
// 2 ≤ C ≤ 10.
// H = 1.
// V = 1.
// Test set 2 (Hidden)
// 2 ≤ R ≤ 100.
// 2 ≤ C ≤ 100.
// 1 ≤ H < R.
// 1 ≤ V < C.
public class WaffleChoppers {
    // time complexity: O(R * C), space complexity: O(R * C)
    public static boolean solve(int H, int V, char[][] cakes, int R, int C) {
        int[][] count = new int[R + 1][C + 1];
        for (int i = 1; i <= R; i++) {
            for (int j = 1; j <= C; j++) {
                count[i][j] += count[i][j - 1] +
                               ((cakes[i - 1][j - 1] == '@') ? 1 : 0);
            }
        }
        for (int j = 1; j <= C; j++) {
            for (int i = 1; i <= R; i++) {
                count[i][j] += count[i - 1][j];
            }
        }
        if (count[R][C] % ((H + 1) * (V + 1)) != 0) return false;
        if (count[R][C] == 0) return true;

        int chips = (count[R][C] / ((H + 1) * (V + 1)));
        List<Integer> rows = new ArrayList<>();
        for (int i = 1; i <= R; i++) {
            if (count[i][C] == chips * (V + 1) * (rows.size() + 1)) {
                rows.add(i);
            }
        }
        if (rows.size() != H + 1) return false;

        List<Integer> cols = new ArrayList<>();
        for (int i = 1; i <= C; i++) {
            if (count[R][i] == chips * (H + 1) * (cols.size() + 1)) {
                cols.add(i);
            }
        }
        if (cols.size() != V + 1) return false;

        int i = 1;
        for (int row : rows) {
            int j = 1;
            for (int col : cols) {
                if (count[row][col] != chips * i * j) return false;
                j++;
            }
            i++;
        }
        return true;
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
        int C = in.nextInt();
        int H = in.nextInt();
        int V = in.nextInt();
        char[][] cakes = new char[R][C];
        for (int i = 0; i < R; i++) {
            cakes[i] = in.next().toCharArray();
        }
        out.println(solve(H, V, cakes, R, C) ? "POSSIBLE" : "IMPOSSIBLE");
    }
}
