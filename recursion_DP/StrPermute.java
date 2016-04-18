import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.Collections;

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

    private static int factorial(int n) {
        return (n == 0) ? 1 : n * factorial(n - 1);
    }

    private void test(String str, Function<String, List<String>> perm) {
        List<String> perms = perm.apply(str);
        for (String s : perms) {
            System.out.println(s);
        }
        assertEquals(factorial(str.length()), perms.size());
    }

    @Test
    public void test() {
        System.out.println("test permutate");
        test("abcd", StrPermute::permutate);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrPermute");
    }
}
