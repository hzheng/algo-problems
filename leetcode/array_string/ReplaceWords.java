import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC648: https://leetcode.com/problems/replace-words/
//
// We have a concept called root, which can be followed by some other words to
// form another longer word - let's call this word successor. Given a dictionary
// consisting of many roots and a sentence. You need to replace all the
// successor in the sentence with the root forming it. If a successor has many
// roots can form it, replace it with the root with the shortest length.
// You need to output the sentence after the replacement.
public class ReplaceWords {
    // Trie
    // time complexity: O(N), space complexity: O(N)
    // beats 90.54%(17 ms for 124 tests)
    public String replaceWords(List<String> dict, String sentence) {
        TrieNode root = new TrieNode();
        for (String word : dict) {
            root.insert(word);
        }
        String[] words = sentence.split(" "); // or: sentence.split("\\s+")
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            words[i] = root.search(word);
        }
        return String.join(" ", words);
    }

    private static class TrieNode {
        private TrieNode[] children = new TrieNode[26];

        private String word;

        public void insert(String word) {
            TrieNode cur = this;
            for (char c : word.toCharArray()) {
                TrieNode child = cur.children[c - 'a'];
                if (child == null) {
                    child = cur.children[c - 'a'] = new TrieNode();
                }
                cur = child;
            }
            cur.word = word;
        }

        public String search(String word) {
            TrieNode cur = this;
            for (char c : word.toCharArray()) {
                cur = cur.children[c - 'a'];
                if (cur == null) break;

                if (cur.word != null) return cur.word;
            }
            return word;
        }
    }

    // Set
    // time complexity: O(sum(word[i] ^ 2)), space complexity: O(N)
    // beats 21.66%(224 ms for 124 tests)
    public String replaceWords2(List<String> roots, String sentence) {
        Set<String> rootSet = new HashSet<>();
        rootSet.addAll(roots);
        StringBuilder res = new StringBuilder();
        for (String word : sentence.split("\\s+")) {
            String prefix = "";
            for (int i = 1; i <= word.length(); i++) {
                prefix = word.substring(0, i);
                if (rootSet.contains(prefix)) break;
            }
            if (res.length() > 0) {
                res.append(" ");
            }
            res.append(prefix);
        }
        return res.toString();
    }

    void test(String[] dict, String sentence, String expected) {
        assertEquals(expected, replaceWords(Arrays.asList(dict), sentence));
        assertEquals(expected, replaceWords2(Arrays.asList(dict), sentence));
    }

    @Test
    public void test() {
        test(new String[] { "cat", "bat", "rat" },
             "the cattle was rattled by the battery",
             "the cat was rat by the bat");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
