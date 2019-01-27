import org.junit.Test;
import static org.junit.Assert.*;

// LC972: https://leetcode.com/problems/equal-rational-numbers/
//
// Given two strings S and T, each of which represents a non-negative rational
// number, return True if and only if they represent the same number. The
// strings may use parentheses to denote the repeating part of the rational 
// number.
// Note:
// Each part consists only of digits.
// The <IntegerPart> will not begin with 2 or more zeros.  
// 1 <= <IntegerPart>.length <= 4
// 0 <= <NonRepeatingPart>.length <= 4
// 1 <= <RepeatingPart>.length <= 4
public class EqualRational {
    // beats 100.00%(4 ms for 61 tests)
    public boolean isRationalEqual(String S, String T) {
        int[] p = getValue(S);
        int[] q = getValue(T);
        return p[2] == q[2] && p[1] * q[0] == p[0] * q[1];
    }

    static final int[] POWERS_OF_10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000,
                                       100000000, 1000000000};

    private int[] getValue(String S) {
        int[] res = {0, 1, 0}; // nominator, denominator, integer
        int dot = S.indexOf('.');
        if (dot < 0) {
            res[2] = Integer.valueOf(S);
            return res;
        }
        res[2] = Integer.valueOf(S.substring(0, dot));
        String decimal = S.substring(dot + 1);
        int paren = decimal.indexOf('(');
        if (paren < 0) {
            res[0] = decimal.isEmpty() ? 0 : Integer.valueOf(decimal);
            res[1] = POWERS_OF_10[decimal.length()];
            return res;
        }
        res[0] = (paren == 0) ? 0 : Integer.valueOf(decimal.substring(0, paren));
        res[1] = POWERS_OF_10[paren];
        int p = Integer.valueOf(decimal.substring(paren + 1, decimal.length() - 1));
        int q = POWERS_OF_10[decimal.length() - 2];
        int r = POWERS_OF_10[decimal.length() - paren - 2];
        add(res, new int[] {p * r, q * (r - 1)});
        if (res[0] == res[1]) {
            res[2]++;
            res[0] = 0;
            res[1] = 1;
        }
        return res;
    }

    private void add(int[] p, int[] q) {
        int a = p[0] * q[1] + p[1] * q[0];
        int b = p[1] * q[1];
        p[0] = a;
        p[1] = b;
    }

    // beats 60.00%(8 ms for 61 tests)
    public boolean isRationalEqual2(String S, String T) {
        return convert(S).equals(convert(T));
    }

    private Fraction convert(String S) {
        int state = 0; // whole, decimal, repeating
        Fraction res = new Fraction(0, 1);
        int decimailSize = 0;
        for (String part : S.split("[.()]")) {
            state++;
            if (part.isEmpty()) continue;

            long x = Long.valueOf(part);
            int sz = part.length();

            if (state == 1) { // whole
                res.add(new Fraction(x, 1));
            } else if (state == 2) { // decimal
                res.add(new Fraction(x, (long)Math.pow(10, sz)));
                decimailSize = sz;
            } else { // repeating
                long denom = (long)Math.pow(10, decimailSize);
                denom *= (long)(Math.pow(10, sz) - 1);
                res.add(new Fraction(x, denom));
            }
        }
        return res;
    }

    private static class Fraction {
        private long numerator, denominator;

        Fraction(long n, long d) {
            long g = gcd(n, d);
            numerator = n / g;
            denominator = d / g;
        }

        private void add(Fraction other) {
            long n = this.numerator * other.denominator + this.denominator * other.numerator;
            long d = this.denominator * other.denominator;
            long g = Fraction.gcd(n, d);
            numerator = n / g;
            denominator = d / g;
        }

        public boolean equals(Object other) {
            if (!(other instanceof Fraction)) return false;

            Fraction o = (Fraction)other;
            return numerator == o.numerator && denominator == o.denominator;
        }

        private static long gcd(long x, long y) {
            return x != 0 ? gcd(y % x, x) : y;
        }
    }

    // beats 60.00%(6 ms for 61 tests)
    public boolean isRationalEqual3(String S, String T) {
        return eval(S) == eval(T);
    }

    private double eval(String S) {
        int i = S.indexOf('(');
        if (i < 0) return Double.valueOf(S);

        String base = S.substring(0, i);
        String rep = S.substring(i + 1, S.length() - 1);
        for (int j = 0; j < 20; ++j) {
            base += rep;
        }
        return Double.valueOf(base);
    }

    private static final double[] ratios = {1.0, 1.0 / 9, 1.0 / 99, 1.0 / 999, 1.0 / 9999};

    // beats 100.00%(3 ms for 61 tests)
    public boolean isRationalEqual4(String S, String T) {
        return Math.abs(compute(S) - compute(T)) < 1e-9;
    }

    private double compute(String s) {
        int i = s.indexOf('(');
        if (i < 0) return Double.valueOf(s);

        double nonRepeatingVal = Double.valueOf(s.substring(0, i));
        int nonRepeatingLength = i - s.indexOf('.') - 1;
        int repeatingLen = s.length() - i - 2;
        int repeatingVal = Integer.parseInt(s.substring(i + 1, s.length() - 1));
        return nonRepeatingVal
               + repeatingVal * Math.pow(0.1, nonRepeatingLength) * ratios[repeatingLen];
    }

    void test(String S, String T, boolean expected) {
        assertEquals(expected, isRationalEqual(S, T));
        assertEquals(expected, isRationalEqual2(S, T));
        assertEquals(expected, isRationalEqual3(S, T));
        assertEquals(expected, isRationalEqual4(S, T));
    }

    @Test
    public void test() {
        test("0.(52)", "0.5(25)", true);
        test("0.1666(6)", "0.166(66)", true);
        test("0.9(9)", "1.", true);
        test("0.9(09)", "1.", false);
        test("0.9(90)", "1.", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
