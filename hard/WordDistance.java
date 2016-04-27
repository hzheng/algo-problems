import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.5:
 * You have a large text file containing words. Given any two words, find the
 * shortest distance (in terms of number of words) between them in the file.
 * How if the operation will be repeated many times for the same file?
 */
public class WordDistance {
    public static int distance(String[] words, String w1, String w2) {
        int lastW1 = -1;
        int lastW2 = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.equals(w1)) {
                lastW1 = i;
                if (lastW2 >= 0) {
                    int distance = lastW1 - lastW2;
                    minDistance = Math.min(minDistance, distance);
                }
            } else if (word.equals(w2)) {
                lastW2 = i;
                if (lastW1 >= 0) {
                    int distance = lastW2 - lastW1;
                    minDistance = Math.min(minDistance, distance);
                }
            }
        }
        return minDistance;
    }

    // cache
    private static Map<String, List<Integer> > cache;

    public static int distance2(String[] words, String w1, String w2) {
        preprocess(words);
        try {
            return distance(cache.get(w1), cache.get(w2));
        } catch (Exception e) {
            return -1;
        }
    }

    private static int distance(List<Integer> list1, List<Integer> list2) {
        int min = Integer.MAX_VALUE;
        int index1 = 0;
        int index2 = 0;
        while (true) {
            int pos1 = list1.get(index1);
            int pos2 = list2.get(index2);
            if (pos2 > pos1) {
                min = Math.min(min, pos2 - pos1);
                if (++index1 >= list1.size()) break;
            } else {
                min = Math.min(min, pos1 - pos2);
                if (++index2 >= list2.size()) break;
            }
        }
        return min;
    }

    private static void preprocess(String[] words) {
        if (cache != null) return;

        cache = new HashMap<String, List<Integer> >();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (cache.containsKey(word)) {
                List<Integer> list = cache.get(word);
                list.add(i);
            } else {
                List<Integer> locations = new ArrayList<Integer>();
                locations.add(i);
                cache.put(word, locations);
            }
        }
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    private int test(Function<String[], String, String, Integer> distance,
                     String name, String text, String w1, String w2) {
        String[] words = text.split("[\\W]");
        words = Arrays.stream(words).filter(
            w -> !w.trim().isEmpty()).toArray(String[]::new);
        long t1 = System.nanoTime();
        int d = distance.apply(words, w1, w2);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return d;
    }

    private void test(String text, String[] words) {
        for (int i = 0; i < words.length; i += 2) {
            int d1 = test(WordDistance::distance, "distance",
                          text, words[i], words[i + 1]);
            int d2 = test(WordDistance::distance2, "distance2",
                          text, words[i], words[i + 1]);
            assertEquals(d1, d2);
        }
    }

    @Test
    public void test1() {
        test("this is a test sentence, which is used to test " +
             " word distance a new phrase that is just perfect",
             new String[] {"this", "is", "is", "to", "is", "test",
                           "a", "distance", "test", "phrase",
                           "is", "perfect", "this", "new"});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordDistance");
    }
}
