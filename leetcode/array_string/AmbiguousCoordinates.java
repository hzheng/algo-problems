import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC816: https://leetcode.com/problems/ambiguous-coordinates/
//
// We had some 2-dimensional coordinates, like "(1, 3)" or "(2, 0.5)".  Then, we
// removed all commas, decimal points, and spaces, and ended up with the string
// S.  Return a list of strings representing all possibilities for what our
// original coordinates could have been. Original representation never had
// extraneous zeroes or any other number that can be represented with less
// digits.  Also, a decimal point within a number never occurs without at least
// one digit occuring before it, so we never started with numbers like ".1".
public class AmbiguousCoordinates {
    // beats %(30 ms for 346 tests)
    public List<String> ambiguousCoordinates(String S) {
        List<String> res = new ArrayList<>();
        for (int i = 2, n = S.length(); i < n - 1; i++) {
            List<String> xCoordinates = coordinates(S.substring(1, i));
            if (xCoordinates.isEmpty()) continue;

            List<String> yCoordinates = coordinates(S.substring(i, n - 1));
            for (String x : xCoordinates) {
                for (String y : yCoordinates) {
                    res.add("(" + x + ", " + y + ")");
                }
            }
        }
        return res;
    }

    private List<String> coordinates(String s) {
        int len = s.length();
        List<String> res = new ArrayList<>();
        if (len == 1) {
            res.add(s);
            return res;
        }
        if (s.charAt(len - 1) == '0') {
            if (s.charAt(0) != '0') {
                res.add(s);
            }
            return res;
        }
        if (s.charAt(0) == '0') {
            res.add("0." + s.substring(1));
            return res;
        }
        res.add(s);
        for (int i = 1; i < len; i++) {
            res.add(s.substring(0, i) + "." + s.substring(i));
        }
        return res;
    }

    // beats %(34 ms for 346 tests)
    public List<String> ambiguousCoordinates2(String S) {
        List<String> res = new ArrayList();
        for (int i = 2, n = S.length(); i < n - 1; i++) {
            for (String x : coordinates(S, 1, i)) {
                for (String y : coordinates(S, i, n - 1)) {
                    res.add("(" + x  + ", " + y + ")");
                }
            }
        }
        return res;
    }

    private List<String> coordinates(String S, int start, int end) {
        List<String> res = new ArrayList();
        for (int i = 1; i <= end - start; i++) {
            String left = S.substring(start, start + i);
            String right = S.substring(start + i, end);
            if ((left.charAt(0) != '0' || left.equals("0"))
                && !right.endsWith("0")) {
                res.add(left + (i < end - start ? "." : "") + right);
            }
        }
        return res;
    }

    void test(String S, String[] expected) {
        Set<String> expectedSet = new HashSet<>();
        for (String e : expected) {
            expectedSet.add(e);
        }
        assertEquals(expectedSet, new HashSet<>(ambiguousCoordinates(S)));
        assertEquals(expectedSet, new HashSet<>(ambiguousCoordinates2(S)));
    }

    @Test
    public void test() {
        test("(100)", new String[] {"(10, 0)"});
        test ("(0123)", new String[] {
            "(0, 123)", "(0, 12.3)", "(0, 1.23)", "(0.1, 23)", "(0.1, 2.3)",
            "(0.12, 3)"
        });
        test ("(123)", new String[] {
            "(1, 23)", "(12, 3)", "(1.2, 3)", "(1, 2.3)"
        });
        test ("(00011)", new String[] { "(0.001, 1)", "(0, 0.011)"});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
