import org.junit.Test;
import static org.junit.Assert.*;

// LC521: https://leetcode.com/problems/longest-uncommon-subsequence-i/
//
// Given a group of two strings, you need to find the longest uncommon subsequence of
// this group of two strings. The longest uncommon subsequence is defined as the
// longest subsequence of one of these strings and this subsequence should not be any
// subsequence of the other strings.
public class LongestUncommonSubsequence {
    // beats N/A(5 ms for 41 tests)
    public int findLUSlength(String a, String b) {
        return a.equals(b) ? -1 : Math.max(a.length(), b.length());
    }

    void test(String a, String b, int expected) {
        assertEquals(expected, findLUSlength(a, b));
    }

    @Test
    public void test() {
        test("aaa", "aaa", -1);
        test("aba", "cdc", 3);
        test("aba", "adc", 3);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
