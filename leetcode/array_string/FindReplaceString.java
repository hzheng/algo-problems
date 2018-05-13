import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC833: https://leetcode.com/problems/find-and-replace-in-string/
//
// To string S, we will perform some replacement operations that replace groups
// of letters with new ones. Each replacement operation has 3 parameters: a
// starting index i, a source word x and a target word y.  The rule is that if x
// starts at position i in the original string S, then we will replace that
// occurrence of x with y.  If not, we do nothing. All these operations occur
// simultaneously. There won't be any overlap in replacement.
public class FindReplaceString {
    // TreeMap
    // beats %(12 ms for 52 tests)
    public String findReplaceString(String S, int[] indexes, String[] sources,
                                    String[] targets) {
        StringBuilder sb = new StringBuilder();
        NavigableMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < indexes.length; i++) {
            if (S.indexOf(sources[i], indexes[i]) == indexes[i]) {
                map.put(indexes[i], i);
            }
        }
        int i = 0;
        for (int j : map.keySet()) {
            sb.append(S.substring(i, j));
            int index = map.get(j);
            sb.append(targets[index]);
            i = j + sources[index].length();
        }
        sb.append(S.substring(i));
        return sb.toString();
    }

    // TreeMap
    // beats %(16 ms for 52 tests)
    public String findReplaceString2(String S, int[] indexes, String[] sources,
                                     String[] targets) {
        NavigableMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < indexes.length; i++) { 
            map.put(indexes[i], i);
        }
        StringBuilder sb = new StringBuilder(S);
        for (int i : map.descendingKeySet()) {
            int j = map.get(i);
            int index = indexes[j];
            if (S.substring(index).startsWith(sources[j])) {
                sb.replace(index, index + sources[j].length(), targets[j]);
            }
        } 
        return sb.toString();
    }

    // beats %(6 ms for 52 tests)
    public String findReplaceString3(String S, int[] indexes, String[] sources,
                                     String[] targets) {
        int n = S.length();
        int[] match = new int[n];
        Arrays.fill(match, -1);
        for (int i = 0; i < indexes.length; i++) {
            int j = indexes[i];
            if (S.substring(j, j + sources[i].length()).equals(sources[i])) {
                match[j] = i;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ) {
            int index = match[i];
            if (index >= 0) {
                sb.append(targets[index]);
                i += sources[index].length();
            } else {
                sb.append(S.charAt(i++));
            }
        }
        return sb.toString();
    }

    void test(String S, int[] indexes, String[] sources, String[] targets,
              String expected) {
        assertEquals(expected, findReplaceString(S, indexes, sources, targets));
        assertEquals(expected,
                     findReplaceString2(S, indexes, sources, targets));
        assertEquals(expected,
                     findReplaceString3(S, indexes, sources, targets));
    }

    @Test
    public void test() {
        test("abcd", new int[] { 0, 2 }, new String[] { "a", "cd" },
             new String[] { "eee", "ffff" }, "eeebffff");
        test("abcd", new int[] { 0, 2 }, new String[] { "ab", "ec" },
             new String[] { "eee", "ffff" }, "eeecd");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
