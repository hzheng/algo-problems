import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC819: https://leetcode.com/problems/most-common-word/
//
// Given a paragraph and a list of banned words, return the most frequent word
// that is not in the list of banned words.  It is guaranteed there is at least
// one word that isn't banned, and that the answer is unique.
// Words in the list of banned words are given in lowercase, and free of
// punctuation.  Words in the paragraph are not case sensitive.  The answer is
// in lowercase.
// Note:
// 1 <= paragraph.length <= 1000.
// 1 <= banned.length <= 100.
// 1 <= banned[i].length <= 10.
// Different words in paragraph are always separated by a space.
// There are no hyphens or hyphenated words.
// Words only consist of letters, never apostrophes or other punctuation symbols.
public class MostCommonWord {
    // beats %(37 ms for 46 tests)
    public String mostCommonWord(String paragraph, String[] banned) {
        Set<String> banSet = new HashSet<>();
        for (String word : banned) {
            banSet.add(word);
        }
        Map<String, Integer> count = new HashMap<>();
        for (String word : paragraph.toLowerCase().split("[\\pP\\s]+")) {
        // for (String word : paragraph.toLowerCase().split("[\\p{Punct}\\s]+")) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }
        int max = 0;
        String res = null;
        for (String word : count.keySet()) {
            int cur = count.get(word);
            if (cur > max && !banSet.contains(word)) {
                max = cur;
                res = word;
            }
        }
        return res;
    }

    // beats %(23 ms for 46 tests)
    public String mostCommonWord2(String paragraph, String[] banned) {
        Map<String, Integer> count = new HashMap<>();
        for (String word : paragraph.toLowerCase().split("[!?',;. ]+")) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }
        for (String word : banned) {
            count.remove(word);
        }
        int max = 0;
        String res = null;
        for (String word : count.keySet()) {
            int cur = count.get(word);
            if (cur > max) {
                max = cur;
                res = word;
            }
        }
        return res;
    }

    // beats %(17 ms for 46 tests)
    public String mostCommonWord3(String paragraph, String[] banned) {
        paragraph += ".";
        Set<String> banSet = new HashSet<>();
        for (String word : banned) {
            banSet.add(word);
        }
        Map<String, Integer> count = new HashMap<>();
        String res = "";
        int max = 0;
        StringBuilder buf = new StringBuilder();
        for (char c : paragraph.toCharArray()) {
            if (Character.isLetter(c)) {
                buf.append(Character.toLowerCase(c));
            } else if (buf.length() > 0) {
                String word = buf.toString();
                if (!banSet.contains(word)) {
                    int freq = count.getOrDefault(word, 0) + 1;
                    count.put(word, freq);
                    if (freq > max) {
                        res = word;
                        max = count.get(word);
                    }
                }
                buf = new StringBuilder();
            }
        }
        return res;
    }

    void test(String paragraph, String[] banned, String expected) {
        assertEquals(expected, mostCommonWord(paragraph, banned));
        assertEquals(expected, mostCommonWord2(paragraph, banned));
        assertEquals(expected, mostCommonWord3(paragraph, banned));
    }

    @Test
    public void test() {
        test("Bob hit a ball, the hit BALL flew far after it was hit.",
             new String[] {"hit"}, "ball");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
