import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 9.5:
 * Write a method to compute all permutations of a string.
 */
public class StrPermute {
    public static List<String> permutate(String str) {
        if (str == null) return null;

        List<String> perms = new ArrayList<String>();
        if (str.length() == 0) {
            perms.add("");
            return perms;
        }

        char firstChar = str.charAt(0);
        String remainder = str.substring(1);
        for (String substr: permutate(remainder)) {
            for (int i = substr.length(); i >= 0; i--) {
                perms.add(insertChar(substr, firstChar, i));
            }
        }
        return perms;
    }

    private static String insertChar(String str, char c, int pos) {
        StringBuilder builder = new StringBuilder(str.substring(0, pos));
        builder.append(c);
        builder.append(str.substring(pos));
        return builder.toString();
    }

    public static List<String> permutate2(String str) {
        List<String> perms = new ArrayList<String>();
        permutate2("", str, perms);
        return perms;
    }

    private static void permutate2(String prefix, String str,
                                   List<String> perms) {
        int len = str.length();
        if (len == 0) {
            perms.add(prefix);
            return;
        }

        for (int i = 0; i < len; i++) {
            permutate2(prefix + str.charAt(i),
                       str.substring(0, i) + str.substring(i + 1, len), perms);
        }
    }

    private static int factorial(int n) {
        return (n == 0) ? 1 : n * factorial(n - 1);
    }

    private void test(String str, Function<String, List<String> > perm) {
        long t = System.nanoTime();
        List<String> perms = perm.apply(str);
        System.out.format("%.03f ms\n", (System.nanoTime() - t) * 1e-6);
        if (str.length() < 5) {
            for (String s : perms) {
                System.out.println(s);
            }
        }
        assertEquals(factorial(str.length()), perms.size());
    }

    @Test
    public void test() {
        String str = "123456";
        System.out.println("test permutate");
        test(str, StrPermute::permutate);
        System.out.println("test permutate2");
        test(str, StrPermute::permutate2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrPermute");
    }
}
