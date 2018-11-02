import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC017: https://leetcode.com/problems/letter-combinations-of-a-phone-number/
//
// Given a digit string, return all possible letter combinations that the number
// could represent.
public class LetterCombinations {
    private final String[][] LETTERS = {{" "}, {""}, {"a", "b", "c"}, {"d", "e", "f"},
                                        {"g", "h", "i"}, {"j", "k", "l"}, {"m", "n", "o"},
                                        {"p", "q", "r", "s"}, {"t", "u", "v"},
                                        {"w", "x", "y", "z"}};

    // beats 47.29%(1 ms)
    public List<String> letterCombinations(String digits) {
        if (digits.isEmpty()) return Collections.emptyList();

        Queue<String[]> letterList = new LinkedList<>();
        for (char c : digits.toCharArray()) {
            int index = c - '0';
            if (index < 2 || index > 9) return Collections.emptyList();

            letterList.add(LETTERS[index]);
        }
        return Arrays.asList(combine(letterList));
    }

    private String[] combine(Queue<String[]> strsList) {
        if (strsList.isEmpty()) return new String[0];

        String[] combinations = strsList.poll();
        while (true) {
            String[] strs = strsList.poll();
            if (strs == null) return combinations;

            combinations = combine(combinations, strs);
        }
    }

    private String[] combine(String[] s1, String[] s2) {
        int len1 = s1.length;
        if (len1 == 0) return s2;

        int len2 = s2.length;
        if (len2 == 0) return s1;

        String[] combinations = new String[len1 * len2];
        for (int i = 0, k = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                combinations[k++] = s1[i] + s2[j];
            }
        }
        return combinations;
    }

    // BFS + Queue
    // beats 11.68%(3 ms)
    private final char[][] LETTERS2 = {{}, {}, {'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'},
                                       {'j', 'k', 'l'}, {'m', 'n', 'o'}, {'p', 'q', 'r', 's'},
                                       {'t', 'u', 'v'}, {'w', 'x', 'y', 'z'}};

    public List<String> letterCombinations2(String digits) {
        if (digits.isEmpty()) return Collections.emptyList();

        LinkedList<String> res = new LinkedList<>();
        res.add("");
        char[] digitChars = digits.toCharArray();
        for (int i = 0; i < digitChars.length; i++) {
            int num = Character.getNumericValue(digitChars[i]);
            // if (num < 2) return Collections.emptyList();

            while (res.peek().length() == i) {
                String prefix = res.poll();
                for (char c : LETTERS2[num]) {
                    res.add(prefix + c);
                }
            }
        }
        return res;
    }

    // BFS + Queue
    // beats 44.96%(3 ms for 25 tests)
    public List<String> letterCombinations2_2(String digits) {
        LinkedList<String> res = new LinkedList<String>();
        if (digits.isEmpty()) return res;

        res.offer("");
        for (int n = digits.length(); res.peek().length() != n;) {
            String prefix = res.poll();
            for (char c : LETTERS2[digits.charAt(prefix.length()) - '0']) {
                res.offer(prefix + c);
            }
        }
        return res;
    }

    // DFS/Backtracking + Recursion
    // beats 11.68%(3 ms)
    public List<String> letterCombinations3(String digits) {
        if (digits.isEmpty()) return Collections.emptyList();

        List<String> res = new ArrayList<>();
        combine3(0, digits.toCharArray(), new StringBuilder(), res);
        return res;
    }

    private void combine3(int start, char[] digits, StringBuilder sb, List<String> res) {
        if (start == digits.length) {
            res.add(sb.toString());
            return;
        }

        int len = sb.length();
        for (char c : LETTERS2[digits[start] - '0']) {
            sb.append(c);
            combine3(start + 1, digits, sb, res);
            sb.setLength(len); // or: sb.deleteCharAt(len);
        }
    }

    // Solution of Choice
    // DFS + Recursion
    // beats 100.00%(1 ms for 25 tests)
    public List<String> letterCombinations4(String digits) {
        List<String> res = new ArrayList<>();
        if (digits.isEmpty()) return res;

        combine(digits.toCharArray(), 0, new char[digits.length()], res);
        return res;
    }

    private void combine(char[] digits, int start, char[] buffer, List<String> res) {
        if (start == digits.length) {
            res.add(String.valueOf(buffer));
            return;
        }
        for (char c : LETTERS2[digits[start] - '0']) {
            buffer[start] = c;
            combine(digits, start + 1, buffer, res);
        }
    }

    void test(Function<String, List<String>> letterCombinations, String digits, String[] expected) {
        String[] res = letterCombinations.apply(digits).stream().toArray(String[]::new);
        assertArrayEquals(expected, res);
    }

    void test(String digits, String[] expected) {
        LetterCombinations l = new LetterCombinations();
        test(l::letterCombinations, digits, expected);
        test(l::letterCombinations2, digits, expected);
        test(l::letterCombinations2_2, digits, expected);
        test(l::letterCombinations3, digits, expected);
        test(l::letterCombinations4, digits, expected);
    }

    @Test
    public void test1() {
        test("", new String[] {});
        // test("12", new String[] {});
        test("23", new String[] {"ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
