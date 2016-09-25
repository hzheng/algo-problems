import java.util.*;
import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

// LC043: https://leetcode.com/problems/multiply-strings/
//
// Given two non-negative numbers represented as strings, return multiplication.
public class StrMultiplier {
    // beats 14.48%(25 ms)
    public String multiply(String num1, String num2) {
        String n1 = num1;
        String n2 = num2;
        if (num1.length() < num2.length()) {
            n1 = num2;
            n2 = num1;
        }
        // make sure len(n1) >= len(n2)
        String product = "0";
        for (int i = n2.length() - 1, zeros = 0; i >= 0; i--, zeros++) {
            String partial = multiply(n1, n2.charAt(i), zeros);
            product = add(product, partial);
        }
        return product;
    }

    private String multiply(String num, char digit, int zeros) {
        int n = digit - '0';
        if (n == 0) return "0";

        String product = num;
        while (--n > 0) {
            product = add(product, num);
        }
        for (int i = zeros; i > 0; i--) { // can be optimized by 0's tables
            product += '0';
        }
        return product;
    }

    private String add(String n1, String n2) {
        int l1 = n1.length();
        int l2 = n2.length();
        int maxLen = Math.max(l1, l2);
        char[] sumStr = new char[maxLen + 1];
        int carry = 0;
        for (int i = maxLen, i1 = l1 - 1, i2 = l2 - 1; i > 0; i--, i1--, i2--) {
            int d1 = (i1 >= 0) ? n1.charAt(i1) - '0' : 0;
            int d2 = (i2 >= 0) ? n2.charAt(i2) - '0' : 0;
            int sum = d1 + d2 + carry;
            sumStr[i] = (char)('0' + (sum % 10));
            carry = sum / 10;
        }
        sumStr[0] = '1';
        return new String(sumStr, 1 - carry, maxLen + carry);
    }

    // http://www.jiuzhang.com/solutions/multiply-strings/
    // beats 20.55%(18 ms)
    public String multiply2(String num1, String num2) {
        int len1 = num1.length();
        int len2 = num2.length();
        int len = len1 + len2;
        int[] num = new int[len];
        for (int i = len1 - 1; i >= 0; i--) {
            int carry = 0;
            int j = len2 - 1;
            for (; j >= 0; j--) {
                int product = carry + num[i + j + 1]
                              + Character.getNumericValue(num1.charAt(i))
                              * Character.getNumericValue(num2.charAt(j));
                num[i + j + 1] = product % 10;
                carry = product / 10;
            }
            num[i + j + 1] = carry;
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < len - 1 && num[i] == 0; i++) {}
        while (i < len) {
            sb.append(num[i++]);
        }
        return sb.toString();
    }

    // Solution of Choice
    // beats 34.19%(31 ms)
    public String multiply3(String num1, String num2) {
        int len1 = num1.length();
        int len2 = num2.length();
        int[] product = new int[len1 + len2];
        for (int i = len1 - 1; i >= 0; i--) {
            for (int j = len2 - 1; j >= 0; j--) {
                int sum = (num1.charAt(i) - '0') * (num2.charAt(j) - '0')
                          + product[i + j + 1];
                product[i + j] += sum / 10;
                product[i + j + 1] = sum % 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int p : product) {
            if (sb.length() > 0 || p > 0) {
                sb.append(p);
            }
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, String, String> multiply, String name,
              String n1, String n2) {
        BigInteger expected = new BigInteger(n1).multiply(new BigInteger(n2));
        boolean benchmark = n1.length() > 9;
        long t1 = System.nanoTime();
        String product = multiply.apply(n1, n2);
        if (benchmark) {
            System.out.println("\n===" + name);
            System.out.format("%.3f ms\n", (System.nanoTime() - t1) * 1e-6);
        }
        assertEquals(expected.toString(), product);
    }

    void test(String n1, String n2) {
        StrMultiplier multiplier = new StrMultiplier();
        test(multiplier::multiply, "multiply", n1, n2);
        test(multiplier::multiply2, "multiply2", n1, n2);
        test(multiplier::multiply3, "multiply3", n1, n2);
    }

    @Test
    public void test1() {
        int M = 100;
        int N = 100;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                test("" + i, "" + j);
            }
        }
        test("0", "6615");
        test("1", "615");
    }

    @Test
    public void test2() {
        test("1009823743894390327452009111838772", "78427823784393403478926615");
        test("983210098237438948945748433894734789324233390327452009111838772",
             "874436789349239327842782378439340347892661834837432483243243435");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrMultiplier");
    }
}
