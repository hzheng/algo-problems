import java.util.*;

public class LongestNoRepeatSubstring {
    // beats 30.1%
    public int lengthOfLongestSubstring(String s) {
        int maxLen = 0;
        int start = 0;
        int strLen = s.length();
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < strLen; i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)) {
                maxLen = Math.max(maxLen, i - start);
                int dupIndex = map.get(c);
                for (int j = start; j < dupIndex; j++) {
                    map.remove(s.charAt(j));
                }
                start = dupIndex + 1;
            }
            map.put(c, i);
        }
        return Math.max(strLen - start, maxLen);
    }

    // beats 56%
    public int lengthOfLongestSubstring2(String s) {
        int maxLen = 0;
        int start = 0;
        int strLen = s.length();
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < strLen; i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)) {
                int dupIndex = map.get(c);
                if (dupIndex >= start) {
                    maxLen = Math.max(maxLen, i - start);
                    start = dupIndex + 1;
                }
            }
            map.put(c, i);
        }
        return Math.max(strLen - start, maxLen);
    }

    // from the answer 1
    public int lengthOfLongestSubstring3(String s) {
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int ans = 0, i = 0, j = 0;
        while (i < n && j < n) {
            // try to extend the range [i, j]
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j++));
                ans = Math.max(ans, j - i);
            }
            else {
                set.remove(s.charAt(i++));
            }
        }
        return ans;
    }

    // from the answer 2
    public int lengthOfLongestSubstring4(String s) {
        int n = s.length(), ans = 0;
        Map<Character, Integer> map = new HashMap<>(); // current index of character
        // try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; ++j) {
            if (map.containsKey(s.charAt(j))) {
                i = Math.max(map.get(s.charAt(j)), i);
            }
            ans = Math.max(ans, j - i + 1);
            map.put(s.charAt(j), j + 1);
        }
        return ans;
    }

    // from the answer 3
    public int lengthOfLongestSubstring5(String s) {
        int n = s.length(), ans = 0;
        int[] index = new int[128]; // current index of character
        // try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; ++j) {
            i = Math.max(index[s.charAt(j)], i);
            ans = Math.max(ans, j - i + 1);
            index[s.charAt(j)] = j + 1;
        }
        return ans;
    }
}
