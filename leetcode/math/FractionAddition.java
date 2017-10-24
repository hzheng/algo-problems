import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC592: https://leetcode.com/problems/fraction-addition-and-subtraction/
//
// Given a string representing an expression of fraction addition and subtraction,
// you need to return the calculation result in string format. The final result
// should be irreducible fraction.
// Note:
// The input string only contains '0' to '9', '/', '+' and '-'. So does the output.
// Each fraction (input and output) has format ±numerator/denominator. If the first
// input fraction or the output is positive, then '+' will be omitted.
// The input only contains valid irreducible fractions, where the numerator and denominator
// of each fraction will always be in the range [1,10]. If the denominator is 1, it means
// this fraction is actually an integer in a fraction format defined above.
// The number of given fractions will be in the range [1,10].
// The numerator and denominator of the final result are guaranteed to be valid and in the
// range of 32-bit int.
public class FractionAddition {
    // beats 45.22%(18 ms for 105 tests)
    public String fractionAddition(String expression) {
        Fraction sum = new Fraction("0/1");
        for (String fraction : expression.split("(?=[+-])")) {
            sum.add(new Fraction(fraction));
        }
        sum.reduce();
        return sum.p + "/" + sum.q;
    }

    private static int gcd(int a, int b) {
        if (a < b) return gcd(b, a);
        if (b == 0) return a;

        if ((a & 1) == 0) {
            if ((b & 1) == 0) return gcd(a >> 1, b >> 1) << 1;

            return gcd(a >> 1, b);
        }
        return ((b & 1) == 0) ? gcd(a, b >> 1) : gcd(a - b, b);
    }

    private int gcd2(int a, int b) {
        return (b == 0) ? a : gcd2(b, a % b);
    }

    private int gcd3(int a, int b) {
        while (b != 0) {
            int tmp = b;
            b = a % b;
            a = tmp;
        }
        return a;
    }

    private static class Fraction {
        int p;
        int q;

        Fraction(String str) {
            String[] fraction = str.split("/");
            p = Integer.valueOf(fraction[0]);
            q = Integer.valueOf(fraction[1]);
        }

        void add(Fraction other) {
            int tmp = p * other.q + q * other.p;
            q *= other.q;
            p = tmp;
        }

        void reduce() {
            int gcd = gcd(Math.abs(p), q);
            p /= gcd;
            q /= gcd;
        }
    }

    // beats 10.51%(84 ms for 105 tests)
    public String fractionAddition2(String expression) {
        Scanner scanner = new Scanner(expression).useDelimiter("/|(?=[+-])");
        int p = 0;
        int q = 1;
        for (int factor = 1; scanner.hasNext(); p /= factor, q /= factor) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            p = p * b + q * a;
            q *= b;
            factor = gcd3(Math.abs(p), q);
        }
        return p + "/" + q;
    }

    void test(String expression, String expected) {
        assertEquals(expected, fractionAddition(expression));
        assertEquals(expected, fractionAddition2(expression));
    }

    @Test
    public void test() {
        test("-1/2+1/2", "0/1");
        test("-1/2+1/2+1/3", "1/3");
        test("1/3-1/2", "-1/6");
        test("5/3+1/3", "2/1");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
