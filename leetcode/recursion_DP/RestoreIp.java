import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string containing only digits, restore it by returning all possible
// valid IP address combinations.
public class RestoreIp {
    // beats 54.78%
    public List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        restoreIp(s, new ArrayList<String>(), res);
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

        // try two digits
        if (first == '0' || len == 1) return;

        cur.add(s.substring(0, 2));
        restoreIp(s.substring(2), cur, res);
        cur.remove(size);

        // try three digits
        if (len == 2) return;

        char second = s.charAt(1);
        if (first > '2' || first == '2'
            && (second > '5' || second == '5' && s.charAt(2) > '5')) {
            return;
        }

        cur.add(s.substring(0, 3));
        restoreIp(s.substring(3), cur, res);
        cur.remove(size);
    }

    // beats 21.91%
    public List<String> restoreIpAddresses2(String s) {
        List<String> res = new ArrayList<>();
        restoreIp2(s, new ArrayList<String>(), res);
        return res;
    }

    private void restoreIp2(String s, List<String> cur, List<String> res) {
        int size = cur.size();
        int len = s.length();
        if (size == 4) {
            if (len == 0) {
                res.add(String.join(".", cur));
            }
            return;
        }

        for (int i = 1; i < 4 && i <= len; i++) {
            String segment = s.substring(0, i);
            if (!isValid(segment)) continue;

            cur.add(segment);
            restoreIp2(s.substring(i), cur, res);
            cur.remove(size);
        }
    }

    private boolean isValid(String s) {
        if (s.charAt(0) == '0') {
            return s.length() == 1;
        }

        return Integer.parseInt(s) < 256;
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
