import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a digit string, return all possible letter combinations that the number
// could represent.
public class LetterCombinations {
    private final String[][] LETTERS = {
        {" "}, {""}, {"a", "b", "c"}, {"d", "e", "f"}, {"g", "h", "i"},
        {"j", "k", "l"}, {"m", "n", "o"}, {"p", "q", "r", "s"},
        {"t", "u", "v"}, {"w", "x", "y", "z"}};

    // beats 46.02%
    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.isEmpty()) return Collections.emptyList();

        LinkedList<String[]> letterList = new LinkedList<>();
        for (char c : digits.toCharArray()) {
            int index = c - '0';
            if (index < 2 || index > 9) return Collections.emptyList();

            letterList.add(LETTERS[index]);
        }
        return Arrays.asList(combine(letterList));
    }

    private String[] combine(LinkedList<String[]> strsList) {
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

    void test(String digits, String[] expected) {
        String[] res = letterCombinations(digits).stream()
                       .toArray(String[]::new);
        assertArrayEquals(expected, res);
    }

    @Test
    public void test1() {
        test("12", new String[] {});
        test("23", new String[] {"ad", "ae", "af", "bd", "be", "bf",
                                 "cd", "ce", "cf"});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LetterCombinations");
    }
}
