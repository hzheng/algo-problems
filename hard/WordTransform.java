import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.function.Function;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.10:
 * Given two words of equal length that are in a dictionary, write a method to
 * transform one word into another word by changing only one letter at a time.
 * The new word you get in each step must be in the dictionary.
 */
public class WordTransform {
    private static Set<String> dict;

    public static void setDict(Set<String> dictionary) {
        dict = dictionary;
    }

    public static List<String> transformBug(String w1, String w2) {
        if (dict == null || w1 == null || w2 == null) return null;

        if (w1.length() != w2.length()) return null;

        w1 = w1.toLowerCase();
        w2 = w2.toLowerCase();
        if (!dict.contains(w1) || !dict.contains(w2)) return null;

        return doTransform(w1, w2);
    }

    private static List<String> doTransform(String w1, String w2) {
        List<String> path = new ArrayList<String>();
        if (w1.equals(w2)) return path;

        for (int i = 0; i < w1.length(); i++) {
            if (w1.charAt(i) == w2.charAt(i)) continue;

            // BUG: this is not the only way to transform!
            String w = w1.substring(0, i) + w2.charAt(i) + w1.substring(i + 1);
            if (dict.contains(w)) {
                List<String> testPath = doTransform(w, w2);
                if (testPath != null) {
                    path.add(w);
                    path.addAll(testPath);
                    return path;
                }
            }
        }
        return null;
    }

    // from the book
    public static List<String> transform(String startWord, String stopWord) {
        startWord = startWord.toLowerCase();
        stopWord = stopWord.toLowerCase();
        Queue<String> actionQueue = new LinkedList<String>();
        Set<String> visitedSet = new HashSet<String>();
        Map<String, String> backtrackMap = new TreeMap<String, String>();

        actionQueue.add(startWord);
        visitedSet.add(startWord);

        while (!actionQueue.isEmpty()) {
            String w = actionQueue.poll();
            // For each possible word v from w with one edit operation
            for (String v : getOneEditWords(w)) {
                if (v.equals(stopWord)) { // Found our word!
                    LinkedList<String> list = new LinkedList<String>();
                    list.add(v);
                    while (w != null) {
                        list.add(0, w);
                        w = backtrackMap.get(w);
                    }
                    return list;
                }

                if (dict.contains(v) && !visitedSet.contains(v)) {
                    actionQueue.add(v);
                    visitedSet.add(v);     // mark visited
                    backtrackMap.put(v, w);
                }
            }
        }
        return null;
    }

    private static Set<String> getOneEditWords(String word) {
        Set<String> words = new TreeSet<String>();
        for (int i = 0; i < word.length(); i++) {
            char[] wordArray = word.toCharArray();
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != word.charAt(i)) {
                    wordArray[i] = c;
                    words.add(new String(wordArray));
                }
            }
        }
        return words;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private void test(Function<String, String, List<String> > transform,
                      String name, String w1, String w2) {
        System.out.print(name + ":" + w1 + "->" + w2 + " = ");
        List<String> path = transform.apply(w1, w2);
        System.out.println(path);
    }

    private void test(String w1, String w2) {
        test(WordTransform::transformBug, "transformBug", w1, w2);
        test(WordTransform::transform, "transform", w1, w2);
    }

    @Test
    public void test2() throws Exception {
        setDict(new HashSet<String>(Files.readAllLines(
                                        Paths.get("/usr/share/dict/words"))));
        test("tree", "flat");
        test("read", "leek");
        test("lack", "good");
        test("stock", "hello");
        test("slack", "solid");
        test("slack", "cloth");
        test("stream", "belief");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordTransform");
    }
}
