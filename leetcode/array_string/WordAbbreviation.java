import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC527: https://leetcode.com/problems/word-abbreviation
//
// Given an array of n distinct non-empty strings, you need to generate minimal
// possible abbreviations for every word following rules below.
// Begin with the first character and then the number of characters abbreviated,
// which followed by the last character. If there are any conflict, that is more
// than one words share the same abbreviation, a longer prefix is used instead of
//  only the first character until making the map from word to abbreviation become unique.
// If the abbreviation doesn't make the word shorter, then keep it as original.
public class WordAbbreviation {
    // Hash Table + SortedSet
    // time complexity: O(N * log(N))
    // beats N/A(45 ms for 78 tests)
    public List<String> wordsAbbreviation(List<String> dict) {
        List<String> res = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        Map<String, Set<String> > revMap = new HashMap<>();
        for (String word : dict) {
            int len = word.length();
            if (len < 4) continue;

            String abbr = abbreviate(word, 1, len);
            map.put(word, abbr);
            Set<String> set = revMap.get(abbr);
            if (set == null) {
                revMap.put(abbr, set = new TreeSet<>());
            }
            set.add(word);
        }
        for (String abbr : revMap.keySet()) {
            Iterator<String> itr = revMap.get(abbr).iterator();
            String prev = itr.next();
            for (int len = prev.length(), prevDiff = 0, diff; itr.hasNext(); prevDiff = diff) {
                String cur = itr.next();
                diff = 1;
                for (int i = 1;; i++) {
                    if (prev.charAt(i) != cur.charAt(i)) {
                        diff = i + 1;
                        break;
                    }
                }
                if (diff > prevDiff) {
                    map.put(prev, abbreviate(prev, diff, len));
                }
                map.put(prev = cur, abbreviate(cur, diff, len));
            }
        }
        for (String word : dict) {
            res.add(word.length() < 4 ? word : map.get(word));
        }
        return res;
    }

    private String abbreviate(String word, int start, int len) {
        if (len - start > 2) {
            String abbr = word.substring(0, start) + (len - start - 1) + word.charAt(len - 1);
            if (abbr.length() < len) return abbr;
        }
        return word;
    }

    private String abbreviate(String word, int start) {
        return abbreviate(word, start, word.length());
    }

    // Hash Table + Heap
    // time complexity: O(N * log(N))
    // beats N/A(39 ms for 78 tests)
    public List<String> wordsAbbreviation2(List<String> dict) {
        PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
            public int compare(String a, String b) {
                int n = a.length();
                if (n != b.length()) return n - b.length();

                int diff = compareEnds(a, b, n);
                return diff != 0 ? diff : a.compareTo(b);
            }
        });
        for (String word : dict) {
            if (word.length() > 3) {
                pq.offer(word);
            }
        }
        List<String> res = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        String prev = "";
        for (int prevDiff = 1, diff; !pq.isEmpty(); prevDiff = diff) {
            String cur = pq.poll();
            diff = 1;
            if (conflict(prev, cur)) {
                for (int i = 1;; i++) {
                    if (prev.charAt(i) != cur.charAt(i)) {
                        diff = i + 1;
                        break;
                    }
                }
            }
            if (diff > prevDiff) {
                map.put(prev, abbreviate(prev, diff));
            }
            map.put(prev = cur, abbreviate(cur, diff));
        }
        for (String word : dict) {
            res.add(word.length() < 4 ? word : map.get(word));
        }
        return res;
    }

    private boolean conflict(String a, String b) {
        int len = a.length();
        return b.length() == len && compareEnds(a, b, len) == 0;
    }

    private int compareEnds(String a, String b, int n) {
        if (a.charAt(0) != b.charAt(0)) return a.charAt(0) - b.charAt(0);
        return (a.charAt(n - 1) == b.charAt(n - 1)) ? 0 : a.charAt(n - 1) - b.charAt(n - 1);
    }

    // Hash Table + Heap
    // time complexity: O(N * log(N))
    // beats N/A(39 ms for 78 tests)
    public List<String> wordsAbbreviation2_2(List<String> dict) {
        List<String> res = new ArrayList<>(dict);
        Map<String, Integer> indices = new HashMap<>();
        for (int i = dict.size() - 1; i >= 0; i--) {
            indices.put(res.get(i), i);
        }
        PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>() {
            public int compare(String a, String b) {
                int n = a.length();
                if (n != b.length()) return n - b.length();

                int diff = compareEnds(a, b, n);
                return diff != 0 ? diff : a.compareTo(b);
            }
        });
        for (String word : dict) {
            if (word.length() > 3) {
                pq.offer(word);
            }
        }
        String prev = "";
        for (int prevDiff = 1, diff; !pq.isEmpty(); prevDiff = diff) {
            String cur = pq.poll();
            diff = 1;
            if (conflict(prev, cur)) {
                for (int i = 1;; i++) {
                    if (prev.charAt(i) != cur.charAt(i)) {
                        diff = i + 1;
                        break;
                    }
                }
            }
            if (diff > prevDiff) {
                res.set(indices.get(prev), abbreviate(prev, diff));
            }
            res.set(indices.get(prev = cur), abbreviate(cur, diff));
        }
        return res;
    }

    // Brute-Force
    // time complexity: O(N ^ 2)
    // beats N/A(61 ms for 78 tests)
    public List<String> wordsAbbreviation3(List<String> dict) {
        int len = dict.size();
        String[] res = new String[len];
        int[] prefix = new int[len];
        for (int i = 0; i < len; i++) {
            prefix[i] = 1;
            res[i] = abbreviate(dict.get(i), 1);
        }
        for (int i = 0; i < len; i++) {
            while (true) {
                boolean duplicate = false;
                for (int j = i + 1; j < len; j++) {
                    if (res[j].equals(res[i])) {
                        res[j] = abbreviate(dict.get(j), ++prefix[j]);
                        duplicate = true;
                    }
                }
                if (duplicate) {
                    res[i] = abbreviate(dict.get(i), ++prefix[i]);
                } else break;
            }
        }
        return Arrays.asList(res);
    }

    void test(String[] dict, String[] expected) {
        List<String> res = Arrays.asList(expected);
        assertEquals(res, wordsAbbreviation(Arrays.asList(dict)));
        assertEquals(res, wordsAbbreviation2(Arrays.asList(dict)));
        assertEquals(res, wordsAbbreviation2_2(Arrays.asList(dict)));
        assertEquals(res, wordsAbbreviation3(Arrays.asList(dict)));
    }

    @Test
    public void test() {
        test(new String[] {"aa", "aaa"}, new String[] {"aa", "aaa"});
        test(new String[] {"abcdefg", "abccefg", "abcckkg"},
             new String[] {"abcd2g", "abccefg", "abcckkg"});
        test(new String[] {"like", "god", "internal", "me", "internet",
                           "interval", "intension", "face", "intrusion"},
             new String[] {"l2e", "god", "internal", "me", "i6t", "interval",
                           "inte4n", "f2e", "intr4n"});
        test(new String[] {"like", "god", "internal", "me", "internet", "interval",
                           "intension", "face", "intrusion", "intrution"},
             new String[] {"l2e", "god", "internal", "me", "i6t", "interval",
                           "inte4n", "f2e", "intrus2n", "intrut2n"});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordAbbreviation");
    }
}
