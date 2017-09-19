import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import common.Trie;

/**
 * Cracking the Coding Interview(5ed) Problem 17.14:
 * Given a dictionary, find the optimal way of "unconcatenating" a sequence of
 * words. "optimal" is defined to be the parsing which minimizes the number of
 * unrecognized sequences of characters.
 */
public class WordParser {
    static class Result {
        private int invalid = Integer.MAX_VALUE;
        private String parsed = "";

        public Result(int inv, String p) {
            invalid = inv;
            parsed = p;
        }

        public Result clone() {
            return new Result(invalid, parsed);
        }

        public static Result choose(Result r1, Result r2) {
            if (r1 == null) return r2;
            if (r2 == null) return r1;

            return r2.invalid < r1.invalid ? r2 : r1;
        }

        @Override
        public String toString() {
            return parsed + "(" + invalid + ")";
        }
    }

    private static Trie<Integer> dictionary = new Trie<Integer>();

    private static String sentence;

    public static void addWords(String ... words) {
        for (String word : words) {
            dictionary.insert(word, 1);
        }
    }

    public static Result parse(String str) {
        sentence = str;
        return parse(0, 0, new HashMap<Integer, Result>());
    }

    private static Result parse(int start, int end, Map<Integer, Result> cache) {
        if (end >= sentence.length()) {
            return new Result(end - start,
                              sentence.substring(start).toUpperCase());
        }

        if (cache.containsKey(start)) {
            return cache.get(start).clone();
        }

        String word = sentence.substring(start, end + 1);
        boolean validPartial = dictionary.contains(word, false);

        Result exactResult = parse(end + 1, end + 1, cache);
        if (validPartial && dictionary.contains(word, true)) {
            if (exactResult.parsed.length() > 0) {
                exactResult.parsed = " " + exactResult.parsed;
            }
            exactResult.parsed = word + exactResult.parsed;
        } else {
            exactResult.invalid += word.length();
            exactResult.parsed = word.toUpperCase() + " " + exactResult.parsed;
        }

        Result extendResult = null;
        if (validPartial) {
            extendResult = parse(start, end + 1, cache);
        }
        Result bestResult = Result.choose(exactResult, extendResult);
        cache.put(start, bestResult.clone());
        return bestResult;
    }

    @Before
    public void setUp() {
        addWords("as", "in", "the", "company", "is", "will", "of", "top", "not",
                 "does", "word", "gurus", "world", "surely", "attention",
                 "computer", "this", "however", "mean", "attract", "companies",
                 "for", "forever", //"every", <- this cause ambiguous
                 "one", "everyone",
                 "google");
    }

    private static String removePunctuations(String words) {
        return words.replaceAll("[,.!?]", "").toLowerCase();
    }

    private static String concat(String words) {
        char[] punctuation = {',', '"', '!', '.', '\'', '?', ','};
        return removePunctuations(words).replace(" ", "");
    }

    private void test(String words) {
        String concated = concat(words);
        String punctuationRemoved = removePunctuations(words);
        // System.out.println(concated);
        // System.out.println(punctuationRemoved);
        Result res = parse(concated);
        System.out.println(res);
        assertEquals(punctuationRemoved, res.parsed);
    }

    @Test
    public void test() {
        test("As one of the top companies in the world, Google " +
             "will surely attract the attention of computer gurus. " +
             "This does not, however, mean the company is for everyone.");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordParser");
    }
}
