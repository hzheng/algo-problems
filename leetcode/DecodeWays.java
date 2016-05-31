import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// A message containing letters from A-Z is being encoded to numbers using the
// following mapping: 'A' -> 1 'B' -> 2 ... 'Z' -> 26. Given an encoded message
// containing digits, determine the total number of ways to decode it.
public class DecodeWays {
    // beats 30.12%
    public int numDecodings(String s) {
        int len = s.length();
        if (len == 0) return 0;

        if (s.charAt(0) == '0') return 0;

        if (len == 1) return 1;

        int combineable = canCombine(s, 0);
        if (len == 2) {
            if (combineable > 0) return 2;
            return (combineable == 0 || s.charAt(1) != '0') ? 1 : 0;
        }

        if (combineable < 0) return numDecodings(s.substring(1));

        int count = numDecodings(s.substring(2));
        if (count == 0) return 0;

        if (combineable == 0) return count;

        count *= 2;
        combineable = canCombine(s, 1);
        if (combineable < 0) return count;

        if (len == 3) {
            count++;
        } else if (len > 3) {
            int more = numDecodings(s.substring(3));
            if (more == 0) return 0;

            count += more;
        }
        return count;
    }

    private int canCombine(String s, int index) {
        char c = s.charAt(index);
        if (c > '2') return -1;

        char c2 = s.charAt(index + 1);
        if (c2 == '0') return 0;

        if (c2 == '1' || c2 == '2') {
            if (index + 2 < s.length() && s.charAt(index + 2) == '0') return -1;

            return 1;
        }
        return  (c == '1' || c2 < '7') ? 1 : -1;
    }

    void test(String s, int expected) {
        assertEquals(expected, numDecodings(s));
    }

    @Test
    public void test1() {
        test("0", 0);
        test("90", 0);
        test("1980", 0);
        test("123", 3);
        test("198", 2);
        test("5198", 2);
        test("345198", 2);
        test("2112345198", 16);
        test("1210", 2);
        test("10", 1);
        test("120", 1);
        test("12", 2);
        test("123421", 6);
        test("123421123451", 24);
        test("12342112345198", 48);
        test("123421123451981", 48);
        test("12342112345198101", 48);
        test("123421123451981012", 96);
        test("1234211234519810123", 144);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DecodeWays");
    }
}
