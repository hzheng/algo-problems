import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC514: https://leetcode.com/problems/freedom-trail/
//
// In the video game Fallout 4, the quest "Road to Freedom" requires players to
// reach a metal dial called the "Freedom Trail Ring", and use the dial to spell
// a specific keyword in order to open the door.
// Given a string ring, which represents the code engraved on the outer ring and
// another string key, which represents the keyword needs to be spelled. You need
//  to find the minimum number of steps in order to spell all the characters in the keyword.
// Initially, the first character of the ring is aligned at 12:00 direction. You
// need to spell all the characters in the string key one by one by rotating the
// ring clockwise or anticlockwise to make each character of the string key aligned
//  at 12:00 direction and then by pressing the center button.
// At the stage of rotating the ring to spell the key character key[i]:
// You can rotate the ring clockwise or anticlockwise one place, which counts as 1 step.
// The final purpose of the rotation is to align one of the string ring's characters
//  at the 12:00 direction, where this character must equal to the character key[i].
// If the character key[i] has been aligned at the 12:00 direction, you need to press
// the center button to spell, which also counts as 1 step. After the pressing,
// you could begin to spell the next character in the key (next stage), otherwise,
// you've finished all the spelling.
// Note:
// Length of both ring and key will be in range 1 to 100.
// There are only lowercase letters in both strings and might be some duplicate characters in both strings.
// It's guaranteed that string key could always be spelled by rotating the string ring.
public class FreedomTrail {
    // DFS + Recursion + Memoization
    // beats 5.65%(240 ms for 302 tests)
    public int findRotateSteps(String ring, String key) {
        char[] ringChars = ring.toCharArray();
        char[] keyChars = key.toCharArray();
        int[][][] dist = distanceTable(ringChars, keyChars);
        int[][] memo = new int[ringChars.length][keyChars.length];
        for (int[] d : memo) {
            Arrays.fill(d, Integer.MAX_VALUE);
        }
        int[] res = new int[] {Integer.MAX_VALUE};
        dfs(dist, keyChars, 0, 0, 0, memo, res);
        return res[0] + keyChars.length;
    }

    private void dfs(int[][][] distTable, char[] key, int rIndex, int kIndex, int dist, int[][] memo, int[] res) {
        if (kIndex > 0) {
            if (memo[rIndex][kIndex - 1] <= dist) return;

            memo[rIndex][kIndex - 1] = dist;
            if (kIndex == key.length) {
                res[0] = Math.min(res[0], dist);
                return;
            }
        }
        int[] d = distTable[rIndex][key[kIndex] - 'a'];
        int len = distTable.length;
        dfs(distTable, key, (rIndex + d[0]) % len, kIndex + 1, dist + d[0], memo, res);
        dfs(distTable, key, (rIndex + len - d[1]) % len, kIndex + 1, dist + d[1], memo, res);
    }

    private int[][][] distanceTable(char[] ring, char[] key) {
        int rLen = ring.length;
        int kLen = key.length;
        int[][][] dist = new int[rLen][26][2];
        Set<Character> chars = new HashSet<>();
        for (char c : key) {
            chars.add(c);
        }
        for (int i = 0; i < rLen; i++) {
            for (char c : chars) {
                for (int j = i, count = 0;; j = (j + 1) % rLen, count++) {
                    if (ring[j] == c) {
                        dist[i][c - 'a'][0] = count;
                        break;
                    }
                }
            }
            for (char c : chars) {
                for (int j = i, count = 0;; j = (j + rLen - 1) % rLen, count++) {
                    if (ring[j] == c) {
                        dist[i][c - 'a'][1] = count;
                        break;
                    }
                }
            }
        }
        return dist;
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(K * R ^ 2), space complexity: O(K * R)
    // beats 84.18%(50 ms for 302 tests)
    public int findRotateSteps2(String ring, String key) {
        char[] ringChars = ring.toCharArray();
        char[] keyChars = key.toCharArray();
        int rLen = ringChars.length;
        int kLen = keyChars.length;
        int[][] dp = new int[kLen + 1][rLen];
        Arrays.fill(dp[0], Integer.MAX_VALUE);
        dp[0][0] = 0;
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < kLen; i++) {
            for (int j = 0; j < rLen; j++) {
                dp[i + 1][j] = Integer.MAX_VALUE;
                if (ringChars[j] != keyChars[i]) continue;

                for (int k = 0; k < rLen; k++) {
                    if (dp[i][k] == Integer.MAX_VALUE) continue;

                    int d = Math.abs(j - k);
                    dp[i + 1][j] = Math.min(dp[i + 1][j], dp[i][k] + Math.min(d, rLen - d));
                }
                if (i + 1 == kLen) {
                    res = Math.min(res, dp[kLen][j]);
                }
            }
        }
        return res + kLen;
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(K * R ^ 2), space complexity: O(R)
    // beats 67.80%(66 ms for 302 tests)
    public int findRotateSteps3(String ring, String key) {
        int n = ring.length();
        int[] dp = new int[n];
        int[] buf = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        for (char c : key.toCharArray()) {
            for (int i = 0; i < n; i++) {
                buf[i] = Integer.MAX_VALUE / 2;
                if (ring.charAt(i) != c) continue;

                for (int j = 0; j < n; j++) {
                    int d = Math.abs(i - j);
                    buf[i] = Math.min(buf[i], dp[j] + Math.min(d, n - d));
                }
            }
            System.arraycopy(buf, 0, dp, 0, n);
        }
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            res = Math.min(res, dp[i]);
        }
        return res + key.length();
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(K * R ^ 2), space complexity: O(K * R)
    // beats 33.33%(102 ms for 302 tests)
    public int findRotateSteps4(String ring, String key) {
        char[] ringChars = ring.toCharArray();
        char[] keyChars = key.toCharArray();
        int rLen = ringChars.length;
        int kLen = keyChars.length;
        int[][] dp = new int[kLen + 1][rLen];
        for (int[] d : dp) {
            Arrays.fill(d, Integer.MAX_VALUE / 2);
        }
        dp[0][0] = 0;
        @SuppressWarnings("unchecked")
        List<Integer>[] positions = new List[26];
        for (int i = 0; i < rLen; i++) {
            if (positions[ringChars[i] - 'a'] == null) {
                positions[ringChars[i] - 'a'] = new ArrayList<>();
            }
            positions[ringChars[i] - 'a'].add(i);
        }
        for (int i = 0; i < kLen; i++) {
            for (int j = 0; j < rLen; j++) {
                for (int k : positions[keyChars[i] - 'a']) {
                    int d = Math.abs(j - k);
                    dp[i + 1][k] = Math.min(dp[i + 1][k], dp[i][j] + Math.min(d, rLen - d));
                }
            }
        }
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < rLen; i++) {
            res = Math.min(res, dp[kLen][i]);
        }
        return res + kLen;
    }

    void test(String ring, String key, int expected) {
        assertEquals(expected, findRotateSteps(ring, key));
        assertEquals(expected, findRotateSteps2(ring, key));
        assertEquals(expected, findRotateSteps3(ring, key));
        assertEquals(expected, findRotateSteps4(ring, key));
    }

    @Test
    public void test() {
        test("rau", "aru", 6);
        test("godding", "gd", 4);
        test("xrrakuulnczywjs", "jrlucwzakzussrlckyjjsuwkuarnaluxnyzcnrxxwruyr", 204);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FreedomTrail");
    }
}
