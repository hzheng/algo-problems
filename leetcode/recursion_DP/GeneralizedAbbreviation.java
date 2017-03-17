import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC320: https://leetcode.com/problems/generalized-abbreviation
//
// Write a function to generate the generalized abbreviations of a word.
public class GeneralizedAbbreviation {
    // DFS + Recursion + Backtracking
    // beats 84.12%(17 ms for 49 tests)
    public List<String> generateAbbreviations(String word) {
        List<String> res = new ArrayList<>();
        dfs(word.toCharArray(), 0, new StringBuilder(), res);
        return res;
    }

    private void dfs(char[] word, int cur, StringBuilder sb, List<String> res) {
        int n = word.length;
        if (cur == n) {
            res.add(sb.toString());
            return;
        }

        int len = sb.length();
        sb.append(word[cur]);
        dfs(word, cur + 1, sb, res);
        if (len > 0 && Character.isDigit(sb.charAt(len - 1))) return;

        for (int i = 1; i <= (n - cur); i++) {
            sb.setLength(len);
            sb.append(i);
            dfs(word, cur + i, sb, res);
        }
    }

    // DFS + Recursion
    // beats 74.97%(19 ms for 49 tests)
    public List<String> generateAbbreviations2(String word) {
        List<String> res = new ArrayList<>();
        dfs2(word.toCharArray(), 0, "", 0, res);
        return res;
    }

    private void dfs2(char[] word, int cur, String buf, int count, List<String> res) {
        if (cur == word.length) {
            res.add(count == 0 ? buf : buf + count);
            return;
        }
        dfs2(word, cur + 1, buf, count + 1, res);
        dfs2(word, cur + 1, buf + (count > 0 ? count : "") + word[cur], 0, res);
    }

    // DFS + Recursion + Backtracking
    // beats 99.01%(13 ms for 49 tests)
    public List<String> generateAbbreviations2_2(String word){
        List<String> res = new ArrayList<String>();
        dfs2(word.toCharArray(), 0, new StringBuilder(), 0, res);
        return res;
    }

    private void dfs2(char[] word, int cur, StringBuilder buf, int count, List<String> res) {
        if (cur == word.length) {
            res.add((count == 0 ? buf : buf.append(count)).toString());
            return;
        }
        int len = buf.length();
        dfs2(word, cur + 1, buf, count + 1, res);
        buf.setLength(len);
        (count == 0 ? buf : buf.append(count)).append(word[cur]);
        dfs2(word, cur + 1, buf, 0, res);
    }

    // Recursion + Divide & Conquer
    // beats 6.94%(86 ms for 49 tests)
    public List<String> generateAbbreviations3(String word) {
        List<String> res = new ArrayList<>();
        int len = word.length();
        res.add(len == 0 ? "" : String.valueOf(len));
        for (int i = 0; i < len; i++)
            for (String right : generateAbbreviations3(word.substring(i + 1))) {
                String leftNum = i > 0 ? String.valueOf(i) : "";
                res.add(leftNum + word.charAt(i) + right);
            }
        return res;
    }

    // Bit Manipulation
    // beats 26.90%(32 ms for 49 tests)
    public List<String> generateAbbreviations4(String word) {
        List<String> res = new ArrayList<>();
        char[] cs = word.toCharArray();
        int n = cs.length;
        for (int flag = (1 << n) - 1; flag >= 0; flag--) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0, mask = 1;; i++, mask <<= 1) {
                int nums = 0;
                for (; i < n && (flag & mask) == 0; i++, mask <<= 1, nums++) {}
                if (nums > 0) {
                    sb.append(nums);
                }
                if (i == n) break;

                sb.append(cs[i]);
            }
            res.add(sb.toString());
        }
        return res;
    }

    private void test(Function<String, List<String> > generateAbbreviations,
                      String word, String[] expected) {
        List<String> expectedList = Arrays.asList(expected);
        Collections.sort(expectedList);
        List<String> res = generateAbbreviations.apply(word);
        Collections.sort(res);
        assertEquals(expectedList, res);
    }

    private void test(String word, String[] expected) {
        GeneralizedAbbreviation g = new GeneralizedAbbreviation();
        test(g::generateAbbreviations, word, expected);
        test(g::generateAbbreviations2, word, expected);
        test(g::generateAbbreviations2_2, word, expected);
        test(g::generateAbbreviations3, word, expected);
        test(g::generateAbbreviations4, word, expected);
    }

    @Test
    public void test() {
        test("word", new String[] {
            "word", "1ord", "w1rd", "wo1d", "wor1", "2rd",
            "w2d", "wo2", "1o1d", "1or1", "w1r1", "1o2", "2r1", "3d", "w3", "4"
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GeneralizedAbbreviation");
    }
}
