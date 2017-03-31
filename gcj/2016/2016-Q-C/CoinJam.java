import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/6254486/dashboard#s=p2
// Qualification Round 2016: Problem C - Coin Jam
//
// A jamcoin is a string of N ≥ 2 digits with the following properties:
// Every digit is either 0 or 1. The first digit is 1 and the last digit is 1.
// If you interpret the string in any base between 2 and 10, inclusive, the resulting
// number is not prime.
// We hear that there may be communities that use jamcoins as a form of currency.
// When sending someone a jamcoin, it is polite to prove that the jamcoin is legitimate
// by including a nontrivial divisor of that jamcoin's interpretation in each base from 2 to 10.
// For convenience, these divisors must be expressed in base 10.
// Can you produce J different jamcoins of length N, along with proof that they are legitimate?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow;
// each consists of one line with two integers N and J.
// Output
// For each test case, output J+1 lines. The first line must consist of only Case #x:,
// where x is the test case number. Each of the last J lines must consist of a jamcoin
// of length N followed by nine integers. The i-th of those nine integers (counting
// starting from 1) must be a nontrivial divisor of the jamcoin when the jamcoin
// is interpreted in base i+1.
// All of these jamcoins must be different. You cannot submit the same jamcoin in
// two different lines, even if you use a different set of divisors each time.
// Limits
// T = 1. (There will be only one test case.)
// It is guaranteed that at least J distinct jamcoins of length N exist.
// Small dataset
// N = 16.
// J = 50.
// Large dataset
// N = 32.
// J = 500.
public class CoinJam {
    public static List<List<String> > coinJam(int n, int j) {
        List<List<String> > res = new ArrayList<>();
        int count = 0;
        int m = (n <= 16) ? n : n / 2;
        char[] buf = new char[m];
        buf[0] = buf[m - 1] = '1';
        outer : for (int i = 0;; i++) {
            for (int k = 1, mask = 1 << (m - 3); mask > 0; k++, mask >>= 1) {
                buf[k] = (i & mask) == 0 ? '0' : '1';
            }
            String num = String.valueOf(buf);
            List<String> coins = new LinkedList<>();
            for (int base = 10; base > 1; base--) {
                int factor = getFactor(num, base);
                if (factor < 2) continue outer;

                coins.add(0, String.valueOf(factor));
            }
            coins.add(0, m == n ? num : ((n % 2 == 0) ? num : num + "0") + num);
            res.add(coins);
            if (++count == j) return res;
        }
    }

    private static int getFactor(String s, int base) {
        long val = Long.parseLong(s, base);
        for (long i = 2; i * i <= val; i++) {
            if (val % i == 0) return (int)i;
        }
        return 0;
    }

    public static List<List<String> > coinJam2(int n, int j) {
        char[] buf = new char[n];
        int m = n / 2;
        buf[0] = buf[m - 1] = buf[m] = buf[n - 1] = '1';
        List<List<String> > res = new ArrayList<>();
        for (int i = 0, count = 0; count < j; i++, count++) {
            for (int k = n - 2, mask = 1; k > m; k--, mask <<= 1) {
                buf[k] = buf[k - m] = (i & mask) == 0 ? '0' : '1';
            }
            List<String> coins = new ArrayList<>();
            res.add(coins);
            coins.add(String.valueOf(buf));
            String factor = String.valueOf(buf, m, m);
            for (int base = 2; base <= 10; base++) {
                // coins.add(new BigInteger(factor, base).toString());
                coins.add(Long.valueOf(factor, base).toString());
            }
        }
        return res;
    }

    public static List<List<String> > coinJam3(int n, int j) {
        List<List<String> > res = new ArrayList<>();
        for (int i = 0; i < j; i++) {
            List<String> coins = new ArrayList<>();
            res.add(coins);
            String mask = Integer.toBinaryString(i);
            while (mask.length() + 2 < n / 2) {
                mask = "0" + mask;
            }
            String factor = String.format("1%s1", mask);
            coins.add(factor + factor);
            for (int base = 2; base <= 10; base++) {
                coins.add(Long.valueOf(factor, base).toString());
            }
        }
        return res;
    }

    public static List<List<String> > coinJam4(int n, int j) {
        char[] buf = new char[n];
        Arrays.fill(buf, '0');
        buf[n / 2 - 1] = buf[n - 1] = '1';
        BigInteger[] factors = new BigInteger[9];
        String[] factorStr = new String[9];
        String factor = new String(buf);
        for (int i = 0; i <= 8; i++) {
            factors[i] = new BigInteger(factor, i + 2);
            factorStr[i] = factors[i].toString();
        }
        buf[n / 2 - 1] = buf[n - 1] = '0';
        buf[n / 2] = '1';
        BigInteger base = new BigInteger(new String(buf), 2);
        BigInteger factor2 = factors[0];
        List<List<String> > res = new ArrayList<>();
        for (int i = 1, count = 0; count < j; i += 2, count++) {
            List<String> coins = new ArrayList<>();
            res.add(coins);
            BigInteger product = base.add(BigInteger.valueOf(i)).multiply(factor2);
            coins.add(product.toString(2));
            for (String s : factorStr) {
                coins.add(s);
            }
        }
        return res;
    }

    public static List<List<String> > coinJam5(int n, int J) {
        List<List<String> > res = new ArrayList<>();
        for (int i = 0; i < n - 10; i++) {
            for (int j = 0; j < n - 10 - i; j++) {
                for (int k = 0; k < n - 10 - i - j; k++) {
                    int l = n - 10 - i - j - k;
                    List<String> coins = new ArrayList<>();
                    res.add(coins);
                    coins.add(format(n, i, j, k, l));
                    coins.add("3 2 5 2 7 2 3 2 11");
                    if (--J == 0) return res;
                }
            }
        }
        return res;
    }

    private static String format(int n, int i, int j, int k, int l) {
        String template = String.format("11%%0%dd11%%0%dd11%%0%dd11%%0%dd11", i + 1, j + 1, k + 1, l + 1);
        return String.format(template, 0, 0, 0, 0).replace("10", "1"); // remove extra 0's
        // Or:
        // String template = String.format("11%%%ds11%%%ds11%%%ds11%%%ds11", i + 1, j + 1, k + 1, l + 1);
        // return String.format(template, "0", "0", "0", "0").replace(" ", "0").replace("10", "1");
        //
        // Python:
        // return "11{}11{}11{}11{}11".format("0" * i, "0" * j, "0" * k, "0" * l));
    }

    private static String format2(int n, int i, int j, int k, int l) {
        char[] buf = new char[n];
        Arrays.fill(buf, '0');
        buf[0] = buf[1] = '1';
        buf[i + 2] = buf[i + 3] = '1';
        buf[i + j + 4] = buf[i + j + 5] = '1';
        buf[i + j + k + 6] = buf[i + j + k + 7] = '1';
        buf[i + j + k + l + 8] = buf[i + j + k + l + 9] = '1';
        return String.valueOf(buf);
    }

    void test(int n, int j) {
        for (List<String> coins : coinJam2(n, j)) {
            System.out.println(coins);
        }
    }

    @Test
    public void test() {
        test(6, 2);
        test(16, 50);
    }

    private static Scanner in = new Scanner(System.in);
    private static PrintStream out = System.out;

    public static void main(String[] args) {
        if (System.getProperty("gcj.submit") == null) {
            org.junit.runner.JUnitCore.main("CoinJam");
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d:%n", i);
            printResult(in.nextInt(), in.nextInt());
        }
    }

    private static void printResult(int n, int j) {
        for (List<String> coins : coinJam2(n, j)) {
            coins.forEach(i -> out.print(i + " "));
            out.println();
        }
    }
}
