import org.junit.Test;
import static org.junit.Assert.*;

// LC831: https://leetcode.com/problems/masking-personal-information/
//
// Given a string S, which represents either an email address or a phone number.
// We mask this personal information according to the following rules:
// 1. Email address:
// We define a name to be a string of length ≥ 2 consisting of only lowercase
// letters a-z or uppercase letters A-Z. An email address starts with a name,
// followed by the symbol '@', followed by a name, followed by the dot '.' and
// followed by a name. All email addresses are valid and in the format of
// "name1@name2.name3". To mask an email, all names must be converted to
// lowercase and all letters between the first and last letter of the first name
// must be replaced by 5 asterisks '*'.
// 2. Phone number:
// A phone number consists of only the digits 0-9 or the characters from the set
// {'+', '-', '(', ')', ' '}. Assume a phone number contains 10 to 13 digits.
// The last 10 digits make up the local number, while the digits before those
// make up the country code. Note that the country code is optional. We want to
// expose only the last 4 digits and mask all other digits.
// The local number should be formatted and masked as "***-***-1111", where 1
// represents the exposed digits. To mask a phone number with country code like
// "+111 111 111 1111", we write it in the form "+***-***-***-1111".  The '+'
// sign and the first '-' sign before the local number should only exist if
// there is a country code.
public class MaskPII {
    // beats %(9 ms for 66 tests)
    public String maskPII(String S) {
        int atPos = S.indexOf('@');
        if (atPos >= 0) {
            return (S.substring(0, 1) + "*****" + S.substring(atPos - 1))
                   .toLowerCase();
        }
        // phone
        String digits = S.replaceAll("\\D+", "");
        String local = "***-***-" + digits.substring(digits.length() - 4);
        if (digits.length() <= 10) return local;

        String country = "+";
        for (int i = digits.length() - 10; i > 0; i--) {
            country += "*";
        }
        return country + "-" + local;
    }

    private static final String[] COUNTRY = {"", "+*-", "+**-", "+***-"};

    // beats %(9 ms for 66 tests)
    public String maskPII2(String S) {
        int atPos = S.indexOf('@');
        if (atPos >= 0) {
            return (S.substring(0, 1) + "*****" + S.substring(atPos - 1))
                   .toLowerCase();
        }
        // phone
        S = S.replaceAll("[^0-9]", "");
        return COUNTRY[S.length() - 10] + "***-***-"
               + S.substring(S.length() - 4);
    }

    void test(String S, String expected) {
        assertEquals(expected, maskPII(S));
        assertEquals(expected, maskPII2(S));
    }

    @Test
    public void test() {
        test("LeetCode@LeetCode.com", "l*****e@leetcode.com");
        test("AB@qq.com", "a*****b@qq.com");
        test("1(234)567-890", "***-***-7890");
        test("86-(10)12345678", "+**-***-***-5678");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
