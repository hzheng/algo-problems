import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC288: https://leetcode.com/problems/unique-word-abbreviation/
//
// An abbreviation of a word follows the form <first letter><number><last letter>.
// Assume you have a dictionary and given a word, find whether its abbreviation
// is unique in the dictionary. A word's abbreviation is unique if no other word
// from the dictionary has the same abbreviation.
public class ValidWordAbbr {
    interface IValidWordAbbr {
        public boolean isUnique(String word);
    }

    // Hash Table + Set
    // beats 57.65%(240 ms for 54 tests)
    static class ValidWordAbbr1 implements IValidWordAbbr {
        private Map<String, Integer> map = new HashMap<>();
        private Set<String> dict = new HashSet<>();

        public ValidWordAbbr1(String[] dictionary) {
            for (String word : dictionary) {
                if (dict.add(word)) {
                    String key = getKey(word);
                    map.put(key, map.getOrDefault(key, 0) + 1);
                }
            }
        }

        public boolean isUnique(String word) {
            int count = map.getOrDefault(getKey(word), 0);
            return count == 0 || count == 1 && dict.contains(word);
        }

        private String getKey(String word) {
            int len = word.length();
            String key = word;
            if (len > 2) {
                key = key.charAt(0) + Integer.toString(len - 2) + key.charAt(len - 1);
            }
            return key;
        }
    }

    // Hash Table + Set
    // beats 84.17%(216 ms for 54 tests)
    static class ValidWordAbbr2 implements IValidWordAbbr {
        private int[][][] map;
        private Set<String> dict = new HashSet<>();

        public ValidWordAbbr2(String[] dictionary) {
            int maxLen = 0;
            for (String word : dictionary) {
                maxLen = Math.max(maxLen, word.length());
            }
            map = new int[26][26][Math.max(0, maxLen - 2)];
            for (String word : dictionary) {
                if (dict.add(word)) {
                    int len = word.length();
                    if (len > 2) {
                        map[word.charAt(0) - 'a'][word.charAt(len - 1) - 'a'][len - 3]++;
                    }
                }
            }
        }

        public boolean isUnique(String word) {
            int len = word.length();
            if (len <= 2 || len >= map[0][0].length + 3) return true;

            int count = map[word.charAt(0) - 'a'][word.charAt(len - 1) - 'a'][len - 3];
            return count == 0 || count == 1 && dict.contains(word);
        }
    }

    // Hash Table
    // beats 80.82%(220 ms for 54 tests)
    static class ValidWordAbbr3 implements IValidWordAbbr {
        private Map<String, String> map = new HashMap<>();

        public ValidWordAbbr3(String[] dictionary) {
            for (String word : dictionary) {
                String key = getKey(word);
                String prev = map.get(key);
                if (prev == null) {
                    map.put(key, word);
                } else if (!prev.equals(word)) {
                    map.put(key, "");
                }
            }
        }

        public boolean isUnique(String word) {
            String val = map.get(getKey(word));
            return val == null || val.equals(word);
        }

        private String getKey(String word) {
            int len = word.length();
            String key = word;
            if (len > 2) {
                key = key.charAt(0) + Integer.toString(len - 2) + key.charAt(len - 1);
            }
            return key;
        }
    }

    void test(IValidWordAbbr obj, String[] uniques, String[] nonUniques) {
        for (String word : uniques) {
            assertTrue(word + " should be unique", obj.isUnique(word));
        }
        for (String word : nonUniques) {
            assertFalse(word + " should not be unique", obj.isUnique(word));
        }
    }

    private void test(String className, String[] dictionary, String[] uniques, String[] nonUniques) {
        try {
            Class<?> clazz = Class.forName("ValidWordAbbr$" + className);
            Constructor<?> ctor = clazz.getConstructor(String[].class);
            test((IValidWordAbbr)ctor.newInstance(new Object[]{dictionary}), uniques, nonUniques);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test(String[] dictionary, String[] uniques, String[] nonUniques) {
        test("ValidWordAbbr1", dictionary, uniques, nonUniques);
        test("ValidWordAbbr2", dictionary, uniques, nonUniques);
        test("ValidWordAbbr3", dictionary, uniques, nonUniques);
    }

    @Test
    public void test() {
        test(new String[] {"aba", "a"}, new String[] {"abba"}, new String[] {"aca"});
        test(new String[] {"a", "a"}, new String[] {"a", ""}, new String[] {});
        test(new String[] {"hello"}, new String[] {"hello"}, new String[] {});
        test(new String[] {"deer", "door", "cake", "card"},
             new String[] {"cart", "make"},
             new String[] {"dear", "cane", "door"});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidWordAbbr");
    }
}
