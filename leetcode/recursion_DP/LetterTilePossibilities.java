import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1079: https://leetcode.com/problems/letter-tile-possibilities/
//
// You have a set of tiles, where each tile has one letter tiles[i] printed on it.  Return the
// number of possible non-empty sequences of letters you can make.
// Note:
// 1 <= tiles.length <= 7
// tiles consists of uppercase English letters.
public class LetterTilePossibilities {
    // Recursion + DFS + Backtracking
    // 1 ms(98.79%), 34 MB(100%) for 86 tests
    public int numTilePossibilities(String tiles) {
        int[] count = new int[26];
        for (char c : tiles.toCharArray()) {
            count[c - 'A']++;
        }
        return dfs(count);
    }

    private int dfs(int[] letters) {
        int sum = 0;
        for (int i = 0; i < 26; i++) {
            if (letters[i] > 0) {
                letters[i]--;
                sum += dfs(letters) + 1;
                letters[i]++;
            }
        }
        return sum;
    }

    // Recursion + DFS + Backtracking
    // 12 ms(56.50%), 36.4 MB(100%) for 86 tests
    public int numTilePossibilities2(String tiles) {
        Set<String> possibleSet = new HashSet<>();
        dfs(tiles.toCharArray(), 0, possibleSet);
        return possibleSet.size();
    }

    private void dfs(char[] chars, int cur, Set<String> possibleSet) {
        if (cur > chars.length) {
            return;
        }
        if (cur > 0) {
            possibleSet.add(String.valueOf(chars, 0, cur));
        }
        for (int i = cur; i < chars.length; i++) {
            swap(chars, cur, i);
            dfs(chars, cur + 1, possibleSet);
            swap(chars, cur, i);
        }
    }

    private void swap(char[] chars, int i, int j) {
        char tmp = chars[i];
        chars[i] = chars[j];
        chars[j] = tmp;
    }

    // Recursion + DFS + Bit Manipulation
    // 18 ms(46.25%), 37.4 MB(100%) for 86 tests
    public int numTilePossibilities3(String tiles) {
        char[] chars = tiles.toCharArray();
        Set<String> possibleSet = new HashSet<>();
        dfs(chars, 0, "", possibleSet);
        return possibleSet.size() - 1;
    }

    private void dfs(char[] chars, int mask, String cur, Set<String> possibleSet) {
        possibleSet.add(cur);
        for (int i = 0; i < chars.length; i++) {
            if ((mask << ~i) >= 0) {
                dfs(chars, mask | (1 << i), cur + chars[i], possibleSet);
            }
        }
    }

    void test(String tiles, int expected) {
        assertEquals(expected, numTilePossibilities(tiles));
        assertEquals(expected, numTilePossibilities2(tiles));
        assertEquals(expected, numTilePossibilities3(tiles));
    }

    @Test
    public void test() {
        test("AAB", 8);
        test("AAABBC", 188);
        test("FDEAFAA", 1265);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
