import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

import org.junit.Test;

/**
 * Cracking the Coding Interview(5ed) Problem 11.2:
 * Sort strings s.t all the anagrams are next to each other.
 */
public class AnagramSort {
    public static void anagramSort(String[] words) {
        Map<String, List<String> > map = new HashMap<String, List<String> >();
        for (String w : words) {
            String sorted = sortChars(w);
            if (!map.containsKey(sorted)) {
                map.put(sorted, new ArrayList<String>());
            }
            map.get(sorted).add(w);
        }
        int i = 0;
        for (String k : map.keySet()) {
            for (String s : map.get(k)) {
                words[i++] = s;
            }
        }
    }

    private static String sortChars(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String (chars);
    }

    // From the book
    static class AnagramComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            return sortChars(s1).compareTo(sortChars(s2));
        }
    }

    public static void anagramSort2(String[] words) {
        Arrays.sort(words, new AnagramComparator());
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    private void test(Function<String[]> sort, String name, String[] words) {
        words = words.clone();
        long t1 = System.nanoTime();
        sort.apply(words);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        System.out.println(Arrays.toString(words));
    }

    @Test
    public void test1() {
        String[] words = {"apple", "banana", "carrot", "ele", "duck", "papel",
                          "tarroc", "cudk", "eel", "lee", "abc", "cab"};
        test(AnagramSort::anagramSort, "anagramSort", words);
        test(AnagramSort::anagramSort, "anagramSort2", words);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AnagramSort");
    }
}
