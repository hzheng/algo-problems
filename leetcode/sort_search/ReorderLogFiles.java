import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC937: https://leetcode.com/problems/reorder-log-files/
//
// You have an array of logs.  Each log is a space delimited string of words.
// For each log, the first word in each log is an alphanumeric identifier. Then,
// either:
// Each word after the identifier will consist only of lowercase letters, or;
// Each word after the identifier will consist only of digits.
// We will call these two varieties of logs letter-logs and digit-logs.  It is 
// guaranteed that each log has at least one word after its identifier.
// Reorder the logs so that all of the letter-logs come before any digit-log.
//  The letter-logs are ordered lexicographically ignoring identifier, with the 
// identifier used in case of ties.  The digit-logs should be put in their 
// original order. Return the final order of the logs.
public class ReorderLogFiles {
    // Sort
    // beats %(66 ms for 61 tests)
    public String[] reorderLogFiles(String[] logs) {
        Arrays.sort(logs, (a, b) -> compare(a, b));
        return logs;
    }

    private int compare(String log1, String log2) {
        String[] keyVal1 = log1.split(" ", 2); // or use indexOf(" ")
        String[] keyVal2 = log2.split(" ", 2);
        boolean isDigit1 = Character.isDigit(keyVal1[1].charAt(0));
        boolean isDigit2 = Character.isDigit(keyVal2[1].charAt(0));
        if (!isDigit1 && !isDigit2) {
            int cmp = keyVal1[1].compareTo(keyVal2[1]);
            return (cmp != 0) ? cmp : keyVal1[0].compareTo(keyVal2[0]);
        }
        return isDigit1 ? (isDigit2 ? 0 : 1) : -1;
    }

    void test(String[] logs, String[] expected) {
        assertArrayEquals(expected, reorderLogFiles(logs));
    }

    @Test
    public void test() {
        test(new String[] {"a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo"},
             new String[] {"g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3 1", "zo4 4 7"});
        test(new String[] {"b b c", "a b c"}, new String[]{"a b c", "b b c"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
