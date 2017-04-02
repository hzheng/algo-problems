import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC522: https://leetcode.com/problems/longest-uncommon-subsequence-ii/
//
// Given a list of strings, you need to find the longest uncommon subsequence among them.
// The longest uncommon subsequence is defined as the longest subsequence of one of these
// strings and this subsequence should not be any subsequence of the other strings.
public class LongestUncommonSubsequence2 {
    // Sort + Set
    // time complexity: O(N ^ 2 * S), space complexity: O(N)
    // beats N/A(11 ms for 98 tests)
    public int findLUSlength(String[] strs) {
        Set<String> duplicates = new HashSet<>();
        Arrays.sort(strs, new Comparator<String>() {
            public int compare(String a, String b) {
                return a.length() != b.length() ? b.length() - a.length() : a.compareTo(b);
            }
        });
        outer : for (int i = 0, n = strs.length; i < n; i++) {
            String s = strs[i];
            if (i + 1 == n || !s.equals(strs[i + 1])) {
                for (String t : duplicates) {
                    if (isSubseq(s, t)) continue outer;
                }
                return s.length();
            }
            duplicates.add(s);
        }
        return -1;
    }

    private boolean isSubseq(String a, String b) {
        int m = a.length();
        int n = b.length();
        int i = 0;
        for (int j = 0; i < m && j < n; j++) {
            if (a.charAt(i) == b.charAt(j)) {
                i++;
            }
        }
        return i == m;
    }

    // time complexity: O(N ^ 2 * S), space complexity: O(1)
    // beats N/A(10 ms for 98 tests)
    public int findLUSlength2(String[] strs) {
        int res = -1;
        for (int i = 0, j, n = strs.length; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (i != j && isSubseq(strs[i], strs[j])) break;
            }
            if (j == n) {
                res = Math.max(res, strs[i].length());
            }
        }
        return res;
    }

    void test(String[] strs, int expected) {
        assertEquals(expected, findLUSlength(strs));
        assertEquals(expected, findLUSlength2(strs));
    }

    @Test
    public void test() {
        test(new String[] {"aabbcc", "aabbcc", "cb", "abc"}, 2);
        test(new String[] {"aabbcc", "aabbcc", "c", "e"}, 1);
        test(new String[] {"aaa", "aaa", "aa"}, -1);
        test(new String[] {"aba", "cdc", "eae"}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
