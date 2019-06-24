import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC692: https://leetcode.com/problems/top-k-frequent-words/
//
// Given a non-empty list of words, return the k most frequent elements.
// Your answer should be sorted by frequency from highest to lowest. If two words have the same
// frequency, then the word with the lower alphabetical order comes first.
// Note:
// You may assume k is always valid, 1 ≤ k ≤ number of unique elements.
// Input words contain only lowercase letters.
public class TopKFrequentWords {
    // Hash Table + Heap
    // time complexity: O(N * log(K)), space complexity: O(N)
    // 35 ms(55.46%), 39.5 MB(69.13%) for 110 tests
    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> count = new HashMap<>();
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
                k + 1, Map.Entry.<String, Integer>comparingByValue()
                .thenComparing(Map.Entry.<String, Integer>comparingByKey().reversed()));
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            pq.offer(entry);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        List<String> res = new ArrayList<>();
        while (!pq.isEmpty()) {
            res.add(0, pq.poll().getKey());
        }
        return res;
    }

    // Hash Table + Heap
    // time complexity: O(N * log(K)), space complexity: O(N)
    // 36 ms(51.53%), 38.3 MB(87.41%) for 110 tests
    public List<String> topKFrequent2(String[] words, int k) {
        Map<String, Integer> count = new HashMap();
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }
        PriorityQueue<String> heap = new PriorityQueue<>(
                (w1, w2) -> count.get(w1).equals(count.get(w2)) ?
                            w2.compareTo(w1) : count.get(w1) - count.get(w2));
        for (String word : count.keySet()) {
            heap.offer(word);
            if (heap.size() > k) {
                heap.poll();
            }
        }
        List<String> res = new ArrayList();
        while (!heap.isEmpty()) {
            res.add(heap.poll());
        }
        Collections.reverse(res);
        return res;
    }

    // Hash Table + Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 36 ms(51.53%), 38.1 MB(90.39%) for 110 tests
    public List<String> topKFrequent3(String[] words, int k) {
        Map<String, Integer> count = new HashMap();
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }
        List<String> candidates = new ArrayList(count.keySet());
        Collections.sort(candidates, (w1, w2) -> count.get(w1).equals(count.get(w2)) ?
                                                 w1.compareTo(w2) : count.get(w2) - count.get(w1));
        return candidates.subList(0, k);
    }

    // Hash Table + Trie
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(99.89%), 39.4 MB(74.02%) for 110 tests
    public List<String> topKFrequent4(String[] words, int k) {
        Map<String, Integer> count = new HashMap<>();
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }
        TrieNode[] trie = new TrieNode[words.length + 1];
        for (String word : count.keySet()) {
            int freq = count.get(word);
            if (trie[freq] == null) {
                trie[freq] = new TrieNode();
            }
            trie[freq].add(word);
        }
        List<String> list = new LinkedList<>();
        for (int i = trie.length - 1; i > 0 && list.size() < k; i--) {
            if (trie[i] != null) {
                trie[i].get(list, k);
            }
        }
        return list;
    }

    private static class TrieNode {
        private TrieNode[] children = new TrieNode[26];
        private String word;

        public void add(String word) {
            TrieNode cur = this;
            for (char c : word.toCharArray()) {
                if (cur.children[c - 'a'] == null) {
                    cur.children[c - 'a'] = new TrieNode();
                }
                cur = cur.children[c - 'a'];
            }
            cur.word = word;
        }

        public void get(List<String> list, int k) {
            if (word != null) {
                list.add(word);
            }
            if (list.size() < k) {
                for (TrieNode child : children) {
                    if (child != null) {
                        child.get(list, k);
                    }
                }
            }
        }
    }

    void test(String[] words, int k, String[] expected) {
        assertEquals(Arrays.asList(expected), topKFrequent(words, k));
        assertEquals(Arrays.asList(expected), topKFrequent2(words, k));
        assertEquals(Arrays.asList(expected), topKFrequent3(words, k));
        assertEquals(Arrays.asList(expected), topKFrequent4(words, k));
    }

    @Test
    public void test() {
        test(new String[]{"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"}, 4,
             new String[]{"the", "is", "sunny", "day"});
        test(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 1,
             new String[]{"i"});
        test(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 2,
             new String[]{"i", "love"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
