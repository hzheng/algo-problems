import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1307: https://leetcode.com/problems/verbal-arithmetic-puzzle/
//
// Given an equation, represented by words on left side and the result on right side.
// You need to check if the equation is solvable under the following rules:
// Each character is decoded as one digit (0 - 9).
// Every pair of different characters they must map to different digits.
// Each words[i] and result are decoded as one number without leading zeros.
// Sum of numbers on left side (words) will equal to the number on right side (result).
// Return True if the equation is solvable otherwise return False.
//
// Constraints:
// 2 <= words.length <= 5
// 1 <= words[i].length, result.length <= 7
// words[i], result contain only uppercase English letters.
// The number of different characters used in the expression is at most 10.
public class VerbalArithmeticPuzzle {
    // DFS + Recursion + Backtracking
    // time complexity: O(N!), space complexity: O(N)
    // 700 ms(34.72%), 36.5 MB(90.28%) for 32 tests
    public boolean isSolvable(String[] words, String result) {
        Map<Character, Integer> map = new HashMap<>();
        Set<Character> nonZeros = new HashSet<>();
        int n = words.length;
        String[] allWords = Arrays.copyOf(words, n + 1);
        allWords[n] = result;
        for (int i = 0; i <= n; i++) {
            String word = allWords[i];
            for (int len = word.length(), j = len - 1, base = 1; j >= 0; j--, base *= 10) {
                char c = word.charAt(j);
                if (j == 0 && len > 1) {
                    nonZeros.add(c);
                }
                map.put(c, map.getOrDefault(c, 0) + base * (i == n ? -1 : 1));
            }
        }
        int index = 0;
        char[] chars = new char[map.size()];
        for (Character c : map.keySet()) {
            chars[index++] = c;
        }
        return dfs(chars, nonZeros, map, new boolean[10], 0, 0);
    }

    public boolean dfs(char[] chars, Set<Character> nonZeros, Map<Character, Integer> map,
                       boolean[] visited, int pos, int diff) {
        if (pos == chars.length) { return diff == 0; }

        char cur = chars[pos];
        int coefficient = map.get(cur);
        for (int i = 0; i < visited.length; i++) {
            if (visited[i] || i == 0 && nonZeros.contains(cur)) { continue; }

            visited[i] = true;
            if (dfs(chars, nonZeros, map, visited, pos + 1, diff + coefficient * i)) {
                return true;
            }
            visited[i] = false;
        }
        return false;
    }

    private void test(String[] words, String result, boolean expected) {
        assertEquals(expected, isSolvable(words, result));
    }

    @Test public void test() {
        test(new String[] {"A", "B"}, "A", true);
        test(new String[] {"SEND", "MORE"}, "MONEY", true);
        test(new String[] {"SIX", "SEVEN", "SEVEN"}, "TWENTY", true);
        test(new String[] {"THIS", "IS", "TOO"}, "FUNNY", true);
        test(new String[] {"LEET", "CODE"}, "POINT", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
