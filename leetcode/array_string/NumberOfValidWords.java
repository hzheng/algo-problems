import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1178: https://leetcode.com/problems/number-of-valid-words-for-each-puzzle/
//
// With respect to a given puzzle string, a word is valid if both the following conditions are
// satisfied:
// word contains the first letter of puzzle.
// For each letter in word, that letter is in puzzle.
// For example, if the puzzle is "abcdefg", then valid words are "faced", "cabbage", and "baggage";
// while invalid words are "beefed" (doesn't include "a") and "based" (includes "s" which isn't in
// the puzzle). Return an array answer, where answer[i] is the number of words in the given word
// list words that are valid with respect to the puzzle puzzles[i].
//
// Constraints:
// 1 <= words.length <= 10^5
// 4 <= words[i].length <= 50
// 1 <= puzzles.length <= 10^4
// puzzles[i].length == 7
// words[i][j], puzzles[i][j] are English lowercase letters.
// Each puzzles[i] doesn't contain repeated characters.
public class NumberOfValidWords {
    // Bit Manipulation
    // time complexity: O(W*P+SUM(WORD)), space complexity: O(W+P)
    // 2334 ms(5.95%), 50.7 MB(95.24%) for 10 tests
    public List<Integer> findNumOfValidWords(String[] words, String[] puzzles) {
        int[] sets = new int[words.length];
        int i = 0;
        for (String word : words) {
            sets[i++] = getSet(word.toCharArray());
        }
        List<Integer> res = new ArrayList<>();
        for (String puzzle : puzzles) {
            res.add(validWords(puzzle.toCharArray(), sets));
        }
        return res;
    }

    private int getSet(char[] s) {
        int set = 0;
        for (char c : s) {
            set |= 1 << (c - 'a');
        }
        return set;
    }

    private int validWords(char[] puzzle, int[] sets) {
        int res = 0;
        int puzzleSet = getSet(puzzle);
        int firstChar = 1 << (puzzle[0] - 'a');
        for (int set : sets) {
            if ((set & firstChar) == 0) { continue; }

            res += (puzzleSet == (puzzleSet | set)) ? 1 : 0;
        }
        return res;
    }

    // Bit Manipulation + Hash Table
    // time complexity: O(W*P+SUM(WORD)), space complexity: O(W+P)
    // 1964 ms(5.95%), 64.2 MB(26.19%) for 10 tests
    public List<Integer> findNumOfValidWords2(String[] words, String[] puzzles) {
        Map<Integer, List<Integer>> memo = new HashMap<>();
        for (String word : words) {
            int set = 0;
            for (char c : word.toCharArray()) {
                set |= (1 << (c - 'a'));
            }
            for (int i = 0; i < 26; i++) {
                if ((set & (1 << i)) != 0) {
                    memo.computeIfAbsent(i, a -> new ArrayList<>()).add(set);
                }
            }
        }
        List<Integer> res = new ArrayList<>();
        for (String puzzle : puzzles) {
            int puzzleSet = 0;
            for (char c : puzzle.toCharArray()) {
                puzzleSet |= (1 << (c - 'a'));
            }
            int c = puzzle.charAt(0) - 'a';
            int count = 0;
            for (int set : memo.getOrDefault(c, Collections.emptyList())) {
                count += (puzzleSet == (puzzleSet | set)) ? 1 : 0;
            }
            res.add(count);
        }
        return res;
    }

    // Solution of Choice
    // Bit Manipulation + Hash Table
    // time complexity: O(P+SUM(WORD)), space complexity: O(W+P)
    // 62 ms(96.43%), 54.1 MB(85.71%) for 10 tests
    public List<Integer> findNumOfValidWords3(String[] words, String[] puzzles) {
        Map<Integer, Integer> map = new HashMap<>();
        for (String word : words) {
            int set = 0;
            for (char c : word.toCharArray()) {
                set |= 1 << (c - 'a');
            }
            map.put(set, map.getOrDefault(set, 0) + 1);
        }
        List<Integer> res = new ArrayList<>();
        for (String puzzle : puzzles) {
            int set = 0;
            for (char c : puzzle.toCharArray()) {
                set |= 1 << (c - 'a');
            }
            int count = 0;
            int firstChar = 1 << (puzzle.charAt(0) - 'a');
            for (int subset = set; subset > 0; subset = (subset - 1) & set) { // iterate all subsets
                if ((subset & firstChar) != 0) {
                    count += map.getOrDefault(subset, 0);
                }
            }
            res.add(count);
        }
        return res;
    }

    private void test(String[] words, String[] puzzles, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, findNumOfValidWords(words, puzzles));
        assertEquals(expectedList, findNumOfValidWords2(words, puzzles));
        assertEquals(expectedList, findNumOfValidWords3(words, puzzles));
    }

    @Test public void test() {
        test(new String[] {"aaaa", "asas", "able", "ability", "actt", "actor", "access"},
             new String[] {"aboveyz", "abrodyz", "abslute", "absoryz", "actresz", "gaswxyz"},
             new Integer[] {1, 1, 3, 2, 4, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
