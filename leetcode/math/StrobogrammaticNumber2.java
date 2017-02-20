import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC247: https://leetcode.com/problems/strobogrammatic-number-ii/
//
// A strobogrammatic number is a number that looks the same when rotated 180 degrees.
// Find all strobogrammatic numbers that are of length = n.
public class StrobogrammaticNumber2 {
    private static final char[] PAIR_MAP = {'0', '1', 0, 0, 0, 0, '9', 0, '8', '6'};
    private static final char[] SYM_CHARS = {'0', '1', '8'};
    private static final char[] VALID_CHARS = {'0', '1', '6', '8', '9'};

    // Recursion + Backtracking
    // beats 78.33%(19 ms for 13 tests)
    public List<String> findStrobogrammatic(int n) {
        List<String> res = new ArrayList<>();
        generate(n, new StringBuilder(), res);
        return res;
    }

    private void generate(int n, StringBuilder sb, List<String> res) {
        int len = sb.length();
        if (n <= 0) {
            if (sb.charAt(0) == '0' && (len > 1 || n == 0)) return;

            for (int i = len + n - 1; i >= 0; i--) {
                sb.append(PAIR_MAP[sb.charAt(i) - '0']);
            }
            res.add(new String(sb));
            return;
        }
        for (char c : (n == 1 ? SYM_CHARS : VALID_CHARS)) {
            // if (len == 0 && c == '0' && n != 1) continue;

            sb.append(c);
            generate(n - 2, sb, res);
            sb.setLength(len);
        }
    }

    // Recursion
    // beats 98.73%(4 ms for 13 tests)
    public List<String> findStrobogrammatic2(int n) {
        List<String> res = new ArrayList<>();
        generate((n - 1) / 2, n, new char[n], res);
        return res;
    }

    private void generate(int index, int n, char[] buf, List<String> res) {
        if (index < 0) {
            res.add(new String(buf));
            return;
        }
        for (char c : (index * 2 == (n - 1) ? SYM_CHARS : VALID_CHARS)) {
            if (index == 0 && c == '0' && n != 1) continue;

            buf[index] = c;
            buf[n - 1 - index] = PAIR_MAP[c - '0'];
            generate(index - 1, n, buf, res);
        }
    }

    // Recursion
    // beats 52.26%(23 ms for 13 tests)
    public List<String> findStrobogrammatic3(int n) {
        return generate(n, n);
    }

    private List<String> generate(int n, int m) {
        if (n == 0) return new ArrayList<>(Arrays.asList(""));
        if (n == 1) return new ArrayList<>(Arrays.asList("0", "1", "8"));

        List<String> list = generate(n - 2, m);
        List<String> res = new ArrayList<>();
        for (String s : list) {
            if (n != m) {
                res.add("0" + s + "0");
            }
            res.add("1" + s + "1");
            res.add("6" + s + "9");
            res.add("8" + s + "8");
            res.add("9" + s + "6");
        }
        return res;
    }

    // beats 24.22%(28 ms for 13 tests)
    public List<String> findStrobogrammatic4(int n) {
        List<String> res = new ArrayList<>(
            n % 2 == 0 ? Arrays.asList("") : Arrays.asList("0", "1", "8"));
        for (int i = n; i > 1; i -= 2) {
            List<String> cur = new ArrayList<>();
            for (String s : res) {
                if (i > 3) {
                    cur.add("0" + s + "0");
                }
                cur.add("1" + s + "1");
                cur.add("6" + s + "9");
                cur.add("8" + s + "8");
                cur.add("9" + s + "6");
            }
            res = cur;
        }
        return res;
    }

    void test(Function<Integer, List<String> > findStrobogrammatic,
              int n, String ... expected) {
        List<String> res = findStrobogrammatic.apply(n);
        Collections.sort(res);
        assertArrayEquals(expected, res.toArray(new String[0]));
    }

    void test(int n, String ... expected) {
        StrobogrammaticNumber2 s = new StrobogrammaticNumber2();
        test(s::findStrobogrammatic, n, expected);
        test(s::findStrobogrammatic2, n, expected);
        test(s::findStrobogrammatic3, n, expected);
        test(s::findStrobogrammatic4, n, expected);
    }

    @Test
    public void test() {
        test(1, "0", "1", "8");
        test(2, "11", "69", "88", "96");
        test(3, "101", "111", "181", "609", "619", "689", "808", "818", "888",
             "906", "916", "986");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrobogrammaticNumber2");
    }
}
