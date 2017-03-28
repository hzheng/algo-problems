import java.util.*;
import java.math.*;

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
outer:
        for (int i = 0;; i++) {
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

    private static Object getResult(int n, int j) {
        StringBuilder sb = new StringBuilder();
        for (List<String> coins : coinJam2(n, j)) {
            for (String coin : coins) {
                sb.append(coin).append(" ");
            }
            sb.append("\n");
        }
        return sb;
    }

    public static void main(String[] args) {
        if (System.getProperty("gcj.submit") == null) {
            org.junit.runner.JUnitCore.main("CoinJam");
            return;
        }

        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            System.out.format("Case #%d:%n", i);
            System.out.println(getResult(in.nextInt(), in.nextInt()));
        }
    }
}
