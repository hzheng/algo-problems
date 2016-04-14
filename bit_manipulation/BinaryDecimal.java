import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 5.2:
 * Given a real number between 0 and 1 that is passed in as a double, print the
 * binary representation. If the number cannot be represented accurately in
 * binary with at most 32 characters, print "ERROR."
 */
public class BinaryDecimal {
    public static int PRECISION = 32;

    public static String binaryRep(double d) {
        if (d >= 1 || d <= 0) return "ERROR";

        byte[] str = new byte[PRECISION];
        str[0] = '.';
        int i = 1;
        for ( ; d > 0 && i < PRECISION; i++) {
            d *= 2;
            if (d >= 1) {
                str[i] = '1';
                d--;
            } else {
                str[i] = '0';
            }
        }

        if (d > 0) {
            return "ERROR: " + new String(str);
        }

        return new String(str, 0, i);
    }

    public static String binaryRep2(double num) {
        if (num >= 1 || num <= 0) return "ERROR";

        StringBuilder binary = new StringBuilder(".");
        while (num > 0) {
            if (binary.length() >= PRECISION) {
                return "ERROR: " + binary;
             }

             double r = num * 2;
             if (r >= 1) {
                 binary.append(1);
                 num = r - 1;
             } else {
                 binary.append(0);
                 num = r;
             }
         }
         return binary.toString();
     }

    void test(double n, String expected) {
        assertEquals(expected, binaryRep(n));
        assertEquals(expected, binaryRep2(n));
    }

    @Test
    public void test1() {
        test(.5, ".1");
        test(.25, ".01");
        test(.125, ".001");
        test(.34375, ".01011");
        test(.3515625, ".0101101");
        test(.35156257, "ERROR: .0101101000000000000000010010110");
        test(0.1, "ERROR: .0001100110011001100110011001100");
        test(0.2, "ERROR: .0011001100110011001100110011001");
    }

    public void benchmark(Function<Double, String> binRep, double d) {
        final int N = 10000000;
        long t = System.currentTimeMillis();
        String result = "";
        for (int i = 0; i < N; i++) {
            result = binRep.apply(d);
        }
        System.out.format("%d ms\n", System.currentTimeMillis() - t);
    }

    public void benchmark(double d) {
        System.out.println("\ndecimal: " + d);
        System.out.print("benchmark binaryRep ...");
        benchmark(BinaryDecimal::binaryRep, d);
        System.out.print("benchmark binaryRep2...");
        benchmark(BinaryDecimal::binaryRep2, d);
    }

    @Test
    public void benchmark() {
        // binaryRep loses when double can be represented accurately,
        // wins otherwise.
        benchmark(.25); // binaryRep2 wins
        benchmark(.2); // binaryRep wins
        benchmark(.3515625); // binaryRep2 wins
        benchmark(.35156257); // binaryRep wins
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BinaryDecimal");
    }
}
