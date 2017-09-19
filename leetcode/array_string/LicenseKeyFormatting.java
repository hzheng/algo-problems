import org.junit.Test;
import static org.junit.Assert.*;

// LC482: https://leetcode.com/problems/license-key-formatting/
//
// Now you are given a string S, which represents a software license key which
// we would like to format. The string S is composed of alphanumerical characters
// and dashes. The dashes split the alphanumerical characters within the string
// into groups. (i.e. if there are M dashes, the string is split into M+1 groups).
// The dashes in the given string are possibly misplaced.
// We want each group of characters to be of length K (except for possibly the
// first group, which could be shorter, but still must contain at least one
// character). To satisfy this requirement, we will reinsert dashes.
// Additionally, all the lower case letters in the string must be converted to upper case.
public class LicenseKeyFormatting {
    // beats 72.31%(22 ms for 35 tests)
    public String licenseKeyFormatting(String S, int K) {
        S = S.replace("-", "");
        if (S.isEmpty()) return "";

        int groups = (int)Math.ceil((double)S.length() / K);
        int len = S.length() + groups - 1;
        char[] chars = new char[len];
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int j = 0;
        for (int k = S.length() % K; k > 0; k--) {
            chars[i++] = Character.toUpperCase(S.charAt(j++));
        }
        if (i > 0) {
            groups--;
        }
        for (int k = 0; k < groups; k++) {
            if (i > 0) {
                chars[i++] = '-';
            }
            for (int loop = K; loop > 0; loop--) {
                chars[i++] = Character.toUpperCase(S.charAt(j++));
            }
        }
        return new String(chars);
    }

    // beats 38.04%(32 ms for 35 tests)
    public String licenseKeyFormatting2(String S, int K) {
        StringBuilder sb = new StringBuilder();
        for (int i = S.length() - 1; i >= 0; i--) {
            if (S.charAt(i) != '-') {
                sb.append(sb.length() % (K + 1) == K ? '-' : "")
                .append(Character.toUpperCase(S.charAt(i)));
            }
        }
        return sb.reverse().toString();
    }

    void test(String S, int K, String expected) {
        assertEquals(expected, licenseKeyFormatting(S, K));
        assertEquals(expected, licenseKeyFormatting2(S, K));
    }

    @Test
    public void test() {
        test("---", 3, "");
        test("2-4A0r7-4k", 4, "24A0-R74K");
        test("2-4A0r7-4k", 3, "24-A0R-74K");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LicenseKeyFormatting");
    }
}
