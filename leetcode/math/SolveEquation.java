import org.junit.Test;
import static org.junit.Assert.*;

// LC640: https://leetcode.com/problems/solve-the-equation/
//
// Solve a given equation and return the value of x in the form of string
// "x=#value". The equation contains only '+', '-' operation, the variable x and
// its coefficient. If there is no solution, return "No solution".
// If there are infinite solutions, return "Infinite solutions".
// If there is exactly one solution for the equation, we ensure that the value
// of x is an integer.
public class SolveEquation {
    // beats 45.17%(10 ms for 52 tests)
    public String solveEquation(String equation) {
        String[] sides = equation.split("=");
        int[] left = parse(sides[0]);
        int[] right = parse(sides[1]);
        int m = left[0] - right[0];
        int n = -left[1] + right[1];
        if (m != 0) return "x=" + (n / m);

        return n == 0 ? "Infinite solutions" : "No solution";
    }

    private int[] parse(String side) {
        String[] terms = side.split("(?=[+-])");
        int[] res = new int[2];
        for (String term : terms) {
            int withX = term.endsWith("x") ? 1 : 0;
            int len = term.length() - withX;
            String coefficient = term.substring(0, len);
            int m = 1;
            if (len == 1) {
                if (coefficient.equals("-")) {
                    m = -1;
                } else if (!coefficient.equals("+")) {
                    m = Integer.valueOf(coefficient);
                }
            } else if (len > 0) {
                m = Integer.valueOf(coefficient);
            }
            res[1 - withX] += m;
        }
        return res;
    }

    // beats 45.17%(10 ms for 52 tests)
    // beats 25.20%(11 ms for 52 tests)
    public String solveEquation2(String equation) {
        String[] sides = equation.split("=");
        int[] left = parse2(sides[0]);
        int[] right = parse2(sides[1]);
        int m = left[0] - right[0];
        int n = -left[1] + right[1];
        if (m != 0) return "x=" + (n / m);

        return n == 0 ? "Infinite solutions" : "No solution";
    }

    private int[] parse2(String side) {
        String[] terms = side.split("(?=[+-])");
        int[] res = new int[2];
        for (String term : terms) {
            if (term.equals("x") || term.equals(("+x"))) {
                res[0]++;
            } else if (term.equals("-x")) {
                res[0]--;
            } else if (term.contains("x")) {
                res[0] += Integer.valueOf(term.substring(0, term.length() - 1));
            } else {
                res[1] += Integer.valueOf(term);
            }
        }
        return res;
    }

    void test(String equation, String expected) {
        assertEquals(expected, solveEquation(equation));
        assertEquals(expected, solveEquation2(equation));
    }

    @Test
    public void test() {
        test("x+5-3+x=6+x-2", "x=2");
        test("x=x", "Infinite solutions");
        test("2x+3x-6x=x+2", "x=-1");
        test("x=x+2", "No solution");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
