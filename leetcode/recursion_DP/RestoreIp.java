import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC093: https://leetcode.com/problems/restore-ip-addresses/
//
// Given a string containing only digits, restore it by returning all possible
// valid IP address combinations.
public class RestoreIp {
    // Backtracking
    // beats 58.01%(4 ms)
    public List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        restoreIp(s, new ArrayList<>(4), res);
        return res;
    }

    private void restoreIp(String s, List<String> cur, List<String> res) {
        int size = cur.size();
        int len = s.length();
        if (size == 4) {
            if (len == 0) {
                res.add(String.join(".", cur));
            }
            return;
        }
        if (len == 0) return;

        // try one digit
        char first = s.charAt(0);
        cur.add(String.valueOf(first));
        restoreIp(s.substring(1), cur, res);
        cur.remove(size);

        if (first == '0' || len == 1) return;
        // try two digits
        cur.add(s.substring(0, 2));
        restoreIp(s.substring(2), cur, res);
        cur.remove(size);

        if (len == 2) return;
        // try three digits
        char second = s.charAt(1);
        if (first > '2' || first == '2'
            && (second > '5' || second == '5' && s.charAt(2) > '5')) {
            return;
        }
        cur.add(s.substring(0, 3));
        restoreIp(s.substring(3), cur, res);
        cur.remove(size);
    }

    // Solution of Choice
    // Backtracking
    // beats 36.02%(5 ms)
    public List<String> restoreIpAddresses2(String s) {
        List<String> res = new ArrayList<>();
        restoreIp2(s, new String[4], 0, res);
        return res;
    }

    private void restoreIp2(String s, String[] cur, int size, List<String> res) {
        int len = s.length();
        if (size == 4) {
            if (len == 0) {
                res.add(String.join(".", cur));
            }
            return;
        }
        for (int i = 1; i < 4 && i <= len; i++) {
            String segment = s.substring(0, i);
            if ((i == 1 || segment.charAt(0) != '0')
                && (i < 3 || Integer.parseInt(segment) < 256)) {
                cur[size] = segment;
                restoreIp2(s.substring(i), cur, size + 1, res);
            }
        }
    }

    // Solution of Choice
    // Iteration
    // beats 85.59%(3 ms)
    public List<String> restoreIpAddresses3(String s) {
        List<String> res = new ArrayList<>();
        int len = s.length();
        char[] chars = s.toCharArray();
        for (int i = 1; i < 4 && i < len - 2; i++) {
            if (len - i > 9) continue;

            for (int j = i + 1; j < i + 4 && j < len - 1; j++) {
                if (len - j > 6) continue;

                for (int k = j + 1; k < j + 4 && k < len; k++) {
                    String s1 = validate(chars, 0, i);
                    if (s1 == null) continue;

                    String s2 = validate(chars, i, j);
                    if (s2 == null) continue;

                    String s3 = validate(chars, j, k);
                    if (s3 == null) continue;

                    String s4 = validate(chars, k, len);
                    if (s4 == null) continue;

                    res.add(s1 + "." + s2 + "." + s3 + "." + s4);
                }
            }
        }
        return res;
    }

    private String validate(char[] chars, int start, int end) {
        int len = end - start;
        if (len > 3 || len != 1 && chars[start] == '0') return null;

        String s = new String(chars, start, len);
        return Integer.parseInt(s) < 256 ? s : null;
    }

    void test(Function<String, List<String> > restore,
              String s, String ... expected) {
        String[] res = restore.apply(s).toArray(new String[0]);
        if (res.length == 0) {
            assertEquals(1, expected.length);
            assertEquals("", expected[0]);
            return;
        }

        Arrays.sort(res);
        Arrays.sort(expected);
        assertArrayEquals(expected, res);
    }

    void test(String s, String ... expected) {
        RestoreIp r = new RestoreIp();
        test(r::restoreIpAddresses, s, expected);
        test(r::restoreIpAddresses2, s, expected);
        test(r::restoreIpAddresses3, s, expected);
    }

    @Test
    public void test1() {
        test("0000", "0.0.0.0");
        test("00001", "");
        test("25525511135", "255.255.11.135", "255.255.111.35");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RestoreIp");
    }
}
