import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC691: https://leetcode.com/problems/stickers-to-spell-word/description/
//
// Given N different types of stickers. Each sticker has a lowercase English
// word on it. You would like to spell out the given target string by cutting
// individual letters from your collection of stickers and rearranging them.
// You can use each sticker more than once if you want, and you have infinite
// quantities of each sticker. What is the minimum number of stickers that you
// need to spell out the target? If the task is impossible, return -1.
// Note:
// stickers has length in the range [1, 50].
// stickers consists of lowercase English words(without apostrophes).
// target has length in the range [1, 15], and consists of lowercase letters.
public class MinStickers {
    // Recursion + DFS(+ Backtracking)
    // beats 2.10%(976 ms for 100 tests)
    public int minStickers(String[] stickers, String target) {
        int[] tgt = new int[26];
        for (char c : target.toCharArray()) {
            tgt[c - 'a']++;
        }
        int n = stickers.length;
        int[][] srcs = new int[n][26];
        for (int i = 0; i < n; i++) {
            for (char c : stickers[i].toCharArray()) {
                srcs[i][c - 'a']++;
            }
        }
        for (int i = 0; i < 26; i++) {
            if (tgt[i] == 0) continue;

            boolean supplied = false;
            for (int[] src : srcs) {
                if (src[i] > 0) {
                    supplied = true;
                    break;
                }
            }
            if (!supplied) return -1;
        }
        int[] res = new int[] {Integer.MAX_VALUE};
        spell(srcs, tgt, 0, 0, res);
        return res[0];
    }

    private int[] apply(int[] src, int[] tgt) {
        int[] applied = null;
        for (int i = 0; i < 26; i++) {
            if (tgt[i] > 0 && src[i] > 0) {
                if (applied == null) {
                    tgt = (applied = tgt.clone());
                }
                tgt[i] -= Math.min(tgt[i], src[i]);
            }
        }
        return applied;
    }

    private void spell(int[][] srcs, int[] tgt, int start, int chosen,
                       int[] min) {
        if (chosen >= min[0]) return; // prune

        boolean finished = true;
        for (int i : tgt) {
            if (i != 0) {
                finished = false;
                break;
            }
        }
        if (finished) {
            min[0] = chosen;
            return;
        }
        if (start == srcs.length) return;

        int[] applied = apply(srcs[start], tgt);
        if (applied != null) {
            spell(srcs, applied, start, chosen + 1, min); // clone to avoid undo
        }
        spell(srcs, tgt, start + 1, chosen, min); // simply skip the current src
    }

    // Dynamic Programming(Bottom-Up) + Bit Manipulation
    // time complexity: O(2 ^ T * S * T), space complexity: O(2 ^ T)
    // where S is the total number of letters in all stickers
    // beats 77.33%(133 ms for 100 tests)
    public int minStickers2(String[] stickers, String target) {
        int n = target.length();
        int N = 1 << n;
        int[] dp = new int[N];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 0; i < N; i++) {
            if (dp[i] == Integer.MAX_VALUE) continue;

            for (String sticker : stickers) {
                int set = i; // try to extend a given set by a given sticker
                for (char c : sticker.toCharArray()) {
                    for (int j = 0; j < n; j++) {
                        if (target.charAt(j) == c && ((set >> j) & 1) == 0) {
                            set |= (1 << j);
                            break;
                        }
                    }
                }
                dp[set] = Math.min(dp[set], dp[i] + 1);
            }
        }
        return (dp[N - 1] != Integer.MAX_VALUE) ? dp[N - 1] : -1;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // beats 82.40%(70 ms for 100 tests)
    public int minStickers3(String[] stickers, String target) {
        int n = stickers.length;
        int[][] srcs = new int[n][26];
        for (int i = 0; i < n; i++) {
            for (char c : stickers[i].toCharArray()) {
                srcs[i][c - 'a']++;
            }
        }
        Map<String, Integer> dp = new HashMap<>();
        dp.put("", 0);
        int res = minSticker3(srcs, target, dp);
        return res != Integer.MAX_VALUE ? res : -1;
    }

    private int minSticker3(int[][] srcs, String target,
                            Map<String, Integer> dp) {
        if (dp.containsKey(target)) return dp.get(target);

        int res = Integer.MAX_VALUE;
        int[] tgt = new int[26];
        for (char c : target.toCharArray()) {
            tgt[c - 'a']++;
        }
        for (int[] src : srcs) {
            if (src[target.charAt(0) - 'a'] == 0) continue; // optimization

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                for (int j = 0; j < Math.max(0, tgt[i] - src[i]); j++) {
                    sb.append((char)('a' + i));
                }
            }
            int preRes = minSticker3(srcs, sb.toString(), dp);
            if (preRes != Integer.MAX_VALUE) {
                res = Math.min(res, 1 + preRes);
            }
        }
        dp.put(target, res);
        return res;
    }

    // BFS + Queue + Set
    // beats 32.74%(423 ms for 100 tests)
    public int minStickers4(String[] stickers, String target) {
        int n = stickers.length;
        int[][] srcs = new int[n][26];
        for (int i = 0; i < stickers.length; ++i) {
            for (char c : stickers[i].toCharArray()) {
                srcs[i][c - 'a']++;
            }
        }
        int[] tgt = new int[27];
        for (char c : target.toCharArray()) {
            tgt[c - 'a']++;
        }
        String key = toKey(tgt);
        if (key.isEmpty()) return 0;

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(tgt);
        Set<String> visited = new HashSet<>();
        visited.add(key);
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int[] src : srcs) {
                int[] next = cur.clone();
                for (int i = 0; i < 26; i++) {
                    next[i] = Math.max(next[i] - src[i], 0);
                }
                next[26]++;
                String nextKey = toKey(next);
                if (nextKey.isEmpty()) return next[26];

                if (visited.add(nextKey)) {
                    queue.offer(next);
                }
            }
        }
        return -1;
    }

    private String toKey(int[] tgt) {
        StringBuilder sb = new StringBuilder();
        boolean empty = true;
        for (int i = 0; i < 26; ++i) {
            int n = tgt[i];
            sb.append(n).append(" ");
            empty &= (n == 0);
        }
        return empty ? "" : sb.toString();
    }

    // BFS + Queue + Set
    // beats 12.74%(593 ms for 100 tests)
    public int minStickers4_2(String[] stickers, String target) {
        int n = stickers.length;
        int[][] srcs = new int[n][26];
        for (int i = 0; i < stickers.length; i++) {
            for (char c : stickers[i].toCharArray()) {
                srcs[i][c - 'a']++;
            }
        }
        Integer[] tgt = new Integer[27];
        Arrays.fill(tgt, 0);
        for (char c : target.toCharArray()) {
            tgt[c - 'a']++;
        }
        List<Integer> tgtList = Arrays.asList(tgt); // for sake of List "equals"
        if (finished(tgtList)) return 0;

        Queue<List<Integer> > queue = new LinkedList<>();
        queue.offer(tgtList);
        Set<List<Integer> > visited = new HashSet<>();
        visited.add(tgtList);
        while (!queue.isEmpty()) {
            List<Integer> cur = queue.poll();
            for (int[] src : srcs) {
                List<Integer> next = null;
                for (int i = 0; i < 26; i++) {
                    int count = cur.get(i);
                    if (count > 0 && src[i] > 0) {
                        if (next == null) {
                            next = new ArrayList<>(cur);
                        }
                        next.set(i, Math.max(count - src[i], 0));
                    }
                }
                if (next == null) continue;

                next.set(26, next.get(26) + 1);
                if (finished(next)) return next.get(26);

                if (visited.add(next)) {
                    queue.offer(next);
                }
            }
        }
        return -1;
    }

    private boolean finished(List<Integer> tgt) {
        for (int i = tgt.size() - 2; i >= 0; i--) {
            if (tgt.get(i) > 0) return false;
        }
        return true;
    }

    void test(String[] stickers, String target, int expected) {
        assertEquals(expected, minStickers(stickers, target));
        assertEquals(expected, minStickers2(stickers, target));
        assertEquals(expected, minStickers3(stickers, target));
        assertEquals(expected, minStickers4(stickers, target));
        assertEquals(expected, minStickers4_2(stickers, target));
    }

    @Test
    public void test() {
        test(new String[] {"with", "example", "science"}, "", 0);
        test(new String[] {"with", "example", "science"}, "thehat", 3);
        test(new String[] {"notice", "possible"}, "basicbasic", -1);
        test(new String[] {
            "heart", "seven", "consider", "just", "less", "back", "an", "four",
            "cost", "kill", "skin", "happen", "depend", "broad", "caught",
            "fast", "fig", "way", "under", "print", "white", "war", "sent",
            "locate", "be", "noise", "door", "get", "burn", "quite", "eight",
            "press", "eye", "wave", "bread", "wont", "short", "cow", "plain",
            "who", "well", "drive", "fact", "chief", "store", "night",
            "operate", "page", "south", "once"
        }, "simpleexample", -1);
        test (new String[] {
            "heavy", "claim", "period", "son", "brought", "as", "street",
            "slip", "pass", "dear", "lie", "flower", "support", "sky",
            "tiny", "add", "much", "call", "change", "smell", "body", "begin",
            "knew", "triangle", "see", "syllable", "symbol", "safe", "gas",
            "free", "quite", "blood", "broke", "half", "sing", "month", "those",
            "enemy", "stone", "shop", "oh", "life", "quiet", "face", "try",
            "seat", "near", "continue", "root", "bone"
        }, "solveside", 4);
        test(new String[] {
            "gone", "dont", "bell", "simple", "colony", "mine", "carry",
            "sleep", "village", "ready", "ground", "sell", "use", "lead",
            "doctor", "stretch", "less", "except", "long", "why", "indicate",
            "live", "animal", "blow", "inch", "got", "include", "hope", "real",
            "then", "string", "degree", "syllable", "blue", "stop", "job",
            "key", "class", "he", "valley", "did", "country", "space", "heat",
            "collect", "truck", "mother", "problem", "toward", "my"
        }, "bringmethod", 4);
        test(new String[] {
            "catch", "right", "raise", "weight", "less", "direct", "crop",
            "gentle", "shape", "sea", "dear", "cut", "eye", "spoke", "want",
            "floor", "three", "next", "answer", "got", "wait", "type", "bird",
            "hope", "engine", "dry", "star", "big", "class", "after", "job",
            "fell", "five", "surprise", "foot", "come", "day", "care", "fly",
            "made", "noun", "meat", "wish", "region", "chance", "against",
            "war", "warm", "decimal", "mean"
        }, "drinkinvent", 5);

    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
