import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1366: https://leetcode.com/problems/rank-teams-by-votes/
//
// In a special ranking system, each voter gives a rank from highest to lowest to all teams
// participated in the competition. The ordering of teams is decided by who received the most
// position-one votes. If two or more teams tie in the first position, we consider the second
// position to resolve the conflict, if they tie again, we continue this process until the ties are
// resolved. If two or more teams are still tied after considering all positions, we rank them
// alphabetically based on their team letter.
// Given an array of strings votes which is the votes of all voters in the ranking systems. Sort all
// teams according to the ranking system described above.
// Return a string of all teams sorted by the ranking system.
// Constraints:
// 1 <= votes.length <= 1000
// 1 <= votes[i].length <= 26
// votes[i].length == votes[j].length for 0 <= i, j < votes.length.
// votes[i][j] is an English upper-case letter.
// All characters of votes[i] are unique.
// All the characters that occur in votes[0] also occur in votes[j] where 1 <= j < votes.length.
public class RankTeams {
    // Heap
    // time complexity: O(M(N+M*log(M))), space complexity: O(M^2)
    // 30 ms(6.92%), 40.3 MB(100%) for 34 tests
    public String rankTeams(String[] votes) {
        final int LEN = 26;
        int[][] scores = new int[LEN][LEN];
        for (String v : votes) {
            for (int i = 0; i < v.length(); i++) {
                scores[v.charAt(i) - 'A'][i]++;
            }
        }
        StringBuilder[] scoreStr = new StringBuilder[LEN];
        for (int i = 0; i < LEN; i++) {
            scoreStr[i] = new StringBuilder();
        }
        boolean[] set = new boolean[LEN];
        for (int i = 0; i < LEN; i++) {
            for (int j = 0; j < LEN; j++) {
                scoreStr[i].append(String.format("%4s", scores[i][j]));
                set[i] |= (scores[i][j] > 0);
            }
        }
        PriorityQueue<String[]> pq = new PriorityQueue<>((p, q) -> {
            int res = p[0].compareTo(q[0]);
            return (res != 0) ? -res : p[1].compareTo(q[1]);
        });
        for (int i = 0; i < LEN; i++) {
            if (set[i]) {
                pq.offer(new String[] {scoreStr[i].toString(), String.valueOf((char)('A' + i))});
            }
        }
        StringBuilder res = new StringBuilder();
        while (!pq.isEmpty()) {
            res.append(pq.poll()[1]);
        }
        return res.toString();
    }

    // Heap
    // time complexity: O(M(N+M*log(M))), space complexity: O(M^2)
    // 5 ms(82.79%), 39.1 MB(100%) for 34 tests
    public String rankTeams2(String[] votes) {
        final int LEN = 26;
        int[][] scores = new int[LEN][LEN];
        for (String v : votes) {
            for (int i = 0; i < v.length(); i++) {
                scores[v.charAt(i) - 'A'][i]++;
            }
        }
        boolean[] set = new boolean[LEN];
        for (int i = 0; i < LEN; i++) {
            for (int j = 0; j < LEN; j++) {
                set[i] |= (scores[i][j] > 0);
            }
        }
        PriorityQueue<Object[]> pq = new PriorityQueue<>((p, q) -> {
            int[] a = (int[])p[0];
            int[] b = (int[])q[0];
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) { return b[i] - a[i]; }
            }
            return Integer.compare((int)p[1], (int)q[1]);
        });
        for (int i = 0; i < LEN; i++) {
            if (set[i]) {
                pq.offer(new Object[] {scores[i], i});
            }
        }
        StringBuilder res = new StringBuilder();
        while (!pq.isEmpty()) {
            res.append((char)('A' + (int)pq.poll()[1]));
        }
        return res.toString();
    }

    // Map + Sort
    // time complexity: O(M(N+M*log(M))), space complexity: O(M^2)
    // 10 ms(54.90%), 39.6 MB(100%) for 34 tests
    public String rankTeams3(String[] votes) {
        Map<Character, int[]> map = new HashMap<>();
        int len = votes[0].length();
        for (String v : votes) {
            for (int i = 0; i < len; i++) {
                map.computeIfAbsent(v.charAt(i), x -> new int[len])[i]++;
            }
        }
        List<Character> candidates = new ArrayList<>(map.keySet());
        candidates.sort((a, b) -> {
            for (int i = 0; i < len; i++) {
                int v1 = map.get(a)[i];
                int v2 = map.get(b)[i];
                if (v1 != v2) { return v2 - v1; }
            }
            return a - b;
        });
        StringBuilder sb = new StringBuilder();
        for (char c : candidates) {
            sb.append(c);
        }
        return sb.toString();
    }

    // Map + Sort
    // time complexity: O(M(N+M*log(M))), space complexity: O(M^2)
    // 4 ms(96.92%), 39.1 MB(100%) for 34 tests
    public String rankTeams4(String[] votes) {
        final int LEN = 26;
        int[][] scores = new int[LEN][LEN + 1];
        for (char c : votes[0].toCharArray()) {
            scores[c - 'A'][LEN] = c;
        }
        for (String v : votes) {
            for (int i = 0; i < v.length(); i++) {
                scores[v.charAt(i) - 'A'][i]--; // reverse order
            }
        }
        Arrays.sort(scores, (p, q) -> {
            for (int i = 0; i <= LEN; i++) {
                if (p[i] != q[i]) { return p[i] - q[i]; }
            }
            return 1;
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < votes[0].length(); i++) {
            sb.append((char)scores[i][LEN]);
        }
        return sb.toString();
    }

    private void test(String[] votes, String expected) {
        assertEquals(expected, rankTeams(votes));
        assertEquals(expected, rankTeams2(votes));
        assertEquals(expected, rankTeams3(votes));
        assertEquals(expected, rankTeams4(votes));
    }

    @Test public void test() {
        test(new String[] {"ABC", "ACB", "ABC", "ACB", "ACB"}, "ACB");
        test(new String[] {"WXYZ", "XYZW"}, "XWYZ");
        test(new String[] {"ZMNAGUEDSJYLBOPHRQICWFXTVK"}, "ZMNAGUEDSJYLBOPHRQICWFXTVK");
        test(new String[] {"BCA", "CAB", "CBA", "ABC", "ACB", "BAC"}, "ABC");
        test(new String[] {"M", "M", "M", "M"}, "M");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
