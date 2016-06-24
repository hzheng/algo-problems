import java.util.*;
import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/fraction-to-recurring-decimal/
//
// Given two integers representing the numerator and denominator of a fraction,
// return the fraction in string format. If the fractional part is repeating,
// enclose the repeating part in parentheses.
public class FractionToDecimal {
    // beats 1.24%(16 ms)
    public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) return "0";
        if (denominator == 0) return null;

        long longNum = numerator;
        long longDenom = denominator;
        if (longDenom < 0) {
            longDenom = -longDenom;
            longNum = -longNum;
        }
        String res = "";
        if (longNum < 0) {
            res = "-";
            longNum = -longNum;
        }
        res += longNum / longDenom;
        longNum %= longDenom;
        if (longNum == 0) return res;

        res += "."; // decimal part

        // reduce
        long factor = gcd(longNum, longDenom);
        longNum /= factor;
        longDenom /= factor;

        long reducedDenominator = longDenom;
        long factor2Count = 0;
        while ((reducedDenominator % 2) == 0) {
            factor2Count++;
            reducedDenominator /= 2;
        }

        long factor5Count = 0;
        while ((reducedDenominator % 5) == 0) {
            factor5Count++;
            reducedDenominator /= 5;
        }

        BigInteger nines;
        long recurCount = 0;
        BigInteger bigDenominator = BigInteger.valueOf(reducedDenominator);
        for (BigInteger tenPowers = BigInteger.TEN;; ) {
            recurCount++;
            nines = tenPowers.subtract(BigInteger.ONE);
            if (nines.mod(bigDenominator).equals(BigInteger.ZERO)) {
                break;
            }
            tenPowers = tenPowers.multiply(BigInteger.TEN);
        }

        BigInteger val = nines.divide(bigDenominator).multiply(
            BigInteger.valueOf(longNum));
        long factor10Count = Math.max(factor2Count, factor5Count);
        while (factor10Count > factor2Count++) {
            val = val.multiply(new BigInteger("2"));
        }
        while (factor10Count > factor5Count++) {
            val = val.multiply(new BigInteger("5"));
        }
        if (factor10Count > 0) {
            BigInteger nonRecur = val.divide(nines);
            // res += String.format("%0" + factor10Count + "d", nonRecur);
            String nonRecurStr = nonRecur.toString();
            for (long i = factor10Count - nonRecurStr.length(); i > 0; i--) {
                res += "0";
            }
            res += nonRecur;
        }
        BigInteger recur = val.mod(nines);
        if (recur.signum() > 0) {
            // res += String.format("(%0" + recurCount + "d)", recur);
            String recurStr = recur.toString();
            res += "(";
            for (long i = recurCount - recurStr.length(); i > 0; i--) {
                res += "0";
            }
            res += recurStr + ")";
        }
        return res;
    }

    private long gcd(long a, long b) {
        if (a < b) return gcd(b, a);
        if (b == 0) return a;

        if ((a & 1) == 0) {
            if ((b & 1) == 0) return gcd(a >> 1, b >> 1) << 1;

            return gcd(a >> 1, b);
        }

        return ((b & 1) == 0) ? gcd(a, b >> 1) : gcd(a - b, b);
    }

    // may overflow
    private long gcd2(long a, long b) {
        if (a < b) return gcd2(b, a);

        return (b == 0) ? a : gcd2(a - b, b);
    }

    void test(int numerator, int denominator, String expected) {
        assertEquals(expected, fractionToDecimal(numerator, denominator));
    }

    @Test
    public void test1() {
        test(2, 3, "0.(6)");
        test(1, 90, "0.0(1)");
        test(1, 99, "0.(01)");
        test(0, 2, "0");
        test(1, 2, "0.5");
        test(2, 1, "2");
        test(8, 7, "1.(142857)");
        test(87, 13, "6.(692307)");
        test(1, 17, "0.(0588235294117647)");
        test(837, 133, "6.(293233082706766917)");
        test(-1, -2147483648, "0.0000000004656612873077392578125");
        test(1, 214748364,
             "0.00(00000046566128904246274025165565405657758584833735916144162"
             + "10407079049971249140691940265491382276607238786694551954770654"
             + "27143370461252966751355553982241280310754777158628319049732085"
             + "50263973140209813193268378053860284588710533785486719703252314"
             + "41576896017703771657138212238021985583089238342230164789520817"
             + "95603341592860749337303449725)");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FractionToDecimal");
    }
}
