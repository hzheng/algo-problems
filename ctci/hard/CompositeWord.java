import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.7:
 * Given a list of words, find the longest word made of other words in the list.
 */
public class CompositeWord {

    public static String longestComposite(String[] words) {
        Map<String, Boolean> wordMap = new HashMap<String, Boolean>();
        for (String w : words) {
            wordMap.put(w, true);
        }
        Map<String, Boolean> composites = new HashMap<String, Boolean>();

        int len = 0;
        String longestWord = "";
        for (String w : words) {
            if (decomposable(w, wordMap, composites)) {
                if (len < w.length()) {
                    len = w.length();
                    longestWord = w;
                }
            }
        }
        return longestWord;
    }

    private static boolean decomposable(String word, Map<String, Boolean> wordMap,
                                        Map<String, Boolean> composites) {
        if (composites.containsKey(word)) return composites.get(word);

        for (int i = 1; i < word.length(); i++) {
            String prefix = word.substring(0, i);
            if (!wordMap.containsKey(prefix)) continue;

            String postfix = word.substring(i);
            if (wordMap.containsKey(postfix)
                || decomposable(postfix, wordMap, composites)) {
                composites.put(word, true);
                return true;
            }
        }
        composites.put(word, false);
        return false;
    }

    // form the book
    // compare to longestComposite, this method creates only one hash table,
    // and will stop when one of the longest composite word is found.
    // but it has to sort the array first, which needs extra time and
    // also changes the orinigal array.
    public static String longestComposite2(String arr[]) {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        for (String str : arr) {
            map.put(str, true);
        }
        Arrays.sort(arr, (o1, o2) -> o2.length() - o1.length());
        for (String s : arr) {
            if (canBuildWord(s, true, map)) {
                return s;
            }
        }
        return "";
    }

    public static boolean canBuildWord(String str, boolean isOriginalWord,
                                       Map<String, Boolean> map) {
        if (map.containsKey(str) && !isOriginalWord) {
            return map.get(str);
        }
        for (int i = 1; i < str.length(); i++) {
            String left = str.substring(0, i);
            String right = str.substring(i);
            if (map.containsKey(left) && map.get(left) == true &&
                canBuildWord(right, false, map)) {
                return true;
            }
        }
        map.put(str, false);
        return false;
    }

    private String test(Function<String[], String> longest, String name,
                        String[] words) {
        long t1 = System.nanoTime();
        String word = longest.apply(words);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return word;
    }

    private void test(String[] words) {
        String w1 = test(CompositeWord::longestComposite,
                         "longestComposite", words);
        String w2 = test(CompositeWord::longestComposite2,
                         "longestComposite2", words);
        System.out.println(w1 + " | " + w2);
        assertEquals(w1.length(), w2.length());
    }

    @Test
    public void test1() {
        test(new String[] {"a", "heart","awake", "wake", "beat", "air", "boat",
                           "airboat", "airborne", "heartbeat"});
    }

    @Test
    public void test2() throws Exception {
        String[] words = Files.readAllLines(
            Paths.get("/usr/share/dict/words")).toArray(new String[0]);
        test(words);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CompositeWord");
    }
}
