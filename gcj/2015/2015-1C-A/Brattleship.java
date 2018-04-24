import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/4244486/dashboard#s=p0
// Round 1C 2015: Problem A - Brattleship
//
// The board for a game is a rectangular grid with R rows and C columns. At the
// start of the game, you will close your eyes, and you will keep them closed
// until the end of the game. Your little brother will take a single rectangular
// 1 x W ship and place it horizontally somewhere on the board. The ship must
// always fit entirely on the board, with each cell of the ship occupying
// exactly one of the grid's cells, and it can never be rotated. In each turn of
// the game, you name a cell on the board, and your little brother tells you
// whether that is a hit or a miss. Once you have named all of the cells
// occupied by the ship, the game is over, and your score is the number of turns
// taken. Your goal is to minimize your score.
// Although the ship is not supposed to be moved once it is placed, you know
// that your little brother plans to cheat by changing the location of the ship
// whenever he wants, as long as the ship remains horizontal and completely on
// the board, and the new location is consistent with all the information he has
// given so far. Not only do you know that your little brother will cheat, he
// knows that you know. If you both play optimally, what is the lowest score
// that you can guarantee you will achieve?
// Input
// The first line of the input gives the number of test cases, T. T lines
// follow, each with three space-separated integers R, C, and W: the number of
// rows and columns of the board, followed by the width of the ship.
// Output
// For each test case, output one line containing "Case #x: y", where x is the
// test case number and y is the minimum score you can guarantee.
// Limits
// 1 ≤ W ≤ C.
// Small dataset
// T = 55.
// R = 1.
// 1 ≤ C ≤ 10.
// Large dataset
// 1 ≤ T ≤ 100.
// 1 ≤ R ≤ 20.
// 1 ≤ C ≤ 20.
public class Brattleship {
    public static int play(int R, int C, int W) {
        int res = C / W * R;
        return res + W - ((C % W == 0) ? 1 : 0);
    }

    void test(int R, int C, int W, int expected) {
        assertEquals(expected, play(R, C, W));
    }

    @Test
    public void test() {
        test(1, 4, 2, 3);
        test(1, 7, 7, 7);
        test(2, 5, 1, 10);

        test(1, 4, 2, 3);
        test(1, 8, 2, 5);
        test(1, 10, 2, 6);
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
        int R = in.nextInt();
        int C = in.nextInt();
        int W = in.nextInt();
        out.println(play(R, C, W));
    }
}
