import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

// LC451: https://leetcode.com/problems/sort-characters-by-frequency/
//
// Given a string, sort it in decreasing order based on the frequency of characters.
public class FrequencySort {
    // Counting Sort + Hash Table
    // time complexity: O(N)
    // beats 99.61%(8 ms for 35 tests)
    public String frequencySort(String s) {
        int[] freqs = new int[256];
        for (char c : s.toCharArray()) {
            freqs[c]++;
        }
        int len = s.length();
        int[] count = new int[len + 1];
        for (int c = 0; c < 256; c++) {
            if (freqs[c] > 0) {
                count[freqs[c]]++;
            }
        }
        for (int i = 0, total = 0; i <= len; i++) {
            int oldCount = count[i];
            count[i] = total;
            total += oldCount;
        }
        char[] sorted = new char[len];
        int n = -1;
        for (char c = 0; c < 256; c++) {
            if (freqs[c] > 0) {
                sorted[count[freqs[c]]++] = c;
                n++;
            }
        }
        char[] buf = new char[len];
        for (int i = 0; n >= 0; n--) {
            for (int j = freqs[sorted[n]]; j > 0; j--) {
                buf[i++] = sorted[n];
            }
        }
        return new String(buf);
    }

    // Bucket Sort + Hash Table
    // time complexity: O(N)
    // beats 100%(7 ms for 35 tests)
    public String frequencySort2(String s) {
        // beats 72.02%(32 ms for 35 tests) if use Map instead of array
        // Map<Character, Integer> map = new HashMap<>();
        // for (char c : s.toCharArray()) {
        //     map.put(c, map.getOrDefault(c, 0) + 1);
        // }
        int[] freqs = new int[256];
        for (char c : s.toCharArray()) {
            freqs[c]++;
        }
        // List<Character>[] bucket = new List[s.length() + 1];
        StringBuilder[] bucket = new StringBuilder[s.length() + 1];
        // for (char key : map.keySet()) {
        //     int freq = map.get(key);
        for (char c = 0; c < freqs.length; c++) {
            int freq = freqs[c];
            if (freq == 0) continue;

            if (bucket[freq] == null) {
                // bucket[freq] = new ArrayList<>();
                bucket[freq] = new StringBuilder();
            }
            bucket[freq].append(c);
        }
        char[] buf = new char[s.length()];
        for (int i = 0, pos = bucket.length - 1; pos >= 0; pos--) {
            if (bucket[pos] != null) {
                for (char num : bucket[pos].toString().toCharArray()) {
                    for (int j = freqs[num]; j > 0; j--) {
                        buf[i++] = num;
                    }
                }
            }
        }
        return new String(buf);
    }

    // SortedMap + Hash Table
    // time complexity: O(N * log(N))
    // beats 92.80%(13 ms for 35 tests)
    public String frequencySort3(String s) {
        int[] freqs = new int[256];
        for (char c : s.toCharArray()) {
            freqs[c]++;
        }
        NavigableMap<Integer, List<Character> > map = new TreeMap<>();
        for (char c = 0; c < freqs.length; c++) {
            if (freqs[c] > 0) {
                if (!map.containsKey(freqs[c])) {
                    map.put(freqs[c], new LinkedList<Character>());
                }
                map.get(freqs[c]).add(c);
            }
        }
        char[] buf = new char[s.length()];
        for (int i = 0; !map.isEmpty(); ) {
            Map.Entry<Integer, List<Character> > entry = map.pollLastEntry();
            for (Character c : entry.getValue()) {
                for (int j = entry.getKey(); j > 0; j--) {
                    buf[i++] = c;
                }
            }
        }
        return new String(buf);
    }

    // Heap + Hash Table
    // time complexity: O(N * log(N))
    // beats 53.03%(46 ms for 35 tests)
    public String frequencySort4(String s) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : s.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        PriorityQueue<Map.Entry<Character, Integer> > pq = new PriorityQueue<>(
            new Comparator<Map.Entry<Character, Integer> >() {
            public int compare(Map.Entry<Character, Integer> a,
                               Map.Entry<Character, Integer> b) {
                return b.getValue() - a.getValue();
            }
        });
        pq.addAll(map.entrySet());
        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            Map.Entry<Character, Integer> entry = pq.poll();
            for (int i = entry.getValue(); i > 0; i--) {
                sb.append(entry.getKey());
            }
        }
        return sb.toString();
    }

    void test(String s, String ... expected) {
        assertThat(Arrays.asList(expected), hasItem(frequencySort(s)));
        assertThat(Arrays.asList(expected), hasItem(frequencySort2(s)));
        assertThat(Arrays.asList(expected), hasItem(frequencySort3(s)));
        assertThat(Arrays.asList(expected), hasItem(frequencySort4(s)));
    }

    @Test
    public void test1() {
        test("tree", "eert", "eetr");
        test("cccaaa", "cccaaa", "aaaccc");
        test("Aabb", "bbAa", "bbaA");
        test("Aabccddcbddd", "dddddcccbbAa", "dddddcccbbaA");

    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FrequencySort");
    }
}
