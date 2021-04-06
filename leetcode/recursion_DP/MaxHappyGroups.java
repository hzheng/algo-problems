import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1815: https://leetcode.com/problems/maximum-number-of-groups-getting-fresh-donuts/
//
// There is a donuts shop that bakes donuts in batches of batchSize. They have a rule where they
// must serve all of the donuts of a batch before serving any donuts of the next batch. You are
// given an integer batchSize and an integer array groups, where groups[i] denotes that there is a
// group of groups[i] customers that will visit the shop. Each customer will get exactly one donut.
// When a group visits the shop, all customers of the group must be served before serving any of the
// following groups. A group will be happy if they all get fresh donuts. That is, the first customer
// of the group does not receive a donut that was left over from the previous group.
// You can freely rearrange the ordering of the groups. Return the maximum possible number of happy
// groups after rearranging the groups.
//
// Constraints:
// 1 <= batchSize <= 9
// 1 <= groups.length <= 30
// 1 <= groups[i] <= 10^9
public class MaxHappyGroups {
    // Recursion + DFS + Backtracking + Dynamic Programming(Top-Down) + Hash Table
    // 2 ms(88.34%), 36.3 MB(96.32%) for 67 tests
    public int maxHappyGroups(int batchSize, int[] groups) {
        int[] grp = new int[batchSize];
        int res = preprocess(grp, batchSize, groups);
        return res + dfs(Arrays.stream(grp).boxed().collect(Collectors.toList()), 0,
                         new HashMap<>());
    }

    private int dfs(List<Integer> grp, int remain, Map<List<Integer>, Integer> dp) {
        Integer cache = dp.get(grp);
        if (cache != null) { return cache; }

        int res = 0;
        for (int i = 0, n = grp.size(); i < n; i++) {
            int count = grp.get(i);
            if (count == 0 || (grp.get(remain) > 0 && i != remain)) { continue; } // greedy match

            grp.set(i, count - 1);
            res = Math.max(res, ((remain == 0) ? 1 : 0) + dfs(grp, (n + remain - i) % n, dp));
            grp.set(i, count);
        }
        dp.put(grp, res);
        return res;
    }

    // Recursion + DFS + Backtracking + Dynamic Programming(Top-Down) + Hash Table
    // 2 ms(88.34%), 36.2 MB(96.93%) for 67 tests
    public int maxHappyGroups2(int batchSize, int[] groups) {
        int[] grp = new int[batchSize];
        int res = preprocess(grp, batchSize, groups);
        List<Integer> grpList = Arrays.stream(grp).boxed().collect(Collectors.toList());
        grpList.add(0); // add remaining
        return res + dfs(grpList, new HashMap<>());
    }

    private int dfs(List<Integer> grp, Map<List<Integer>, Integer> dp) {
        Integer cache = dp.get(grp);
        if (cache != null) { return cache; }

        int n = grp.size() - 1;
        int remain = grp.get(n);
        int res = 0;
        for (int i = 1; i < n; i++) {
            int count = grp.get(i);
            if (count == 0) { continue; }

            grp.set(i, count - 1);
            grp.set(n, (n + remain - i) % n);
            res = Math.max(res, ((remain == 0) ? 1 : 0) + dfs(grp, dp));
            grp.set(i, count);
            grp.set(n, remain);
        }
        dp.put(grp, res);
        return res;
    }

    private int preprocess(int[] grp, int batchSize, int[] groups) {
        int res = 0;
        for (int g : groups) {
            int mod = g % batchSize;
            if (mod == 0) {
                res++;
            } else {
                grp[mod]++;
            }
        }
        for (int i = 1; i < batchSize; i++) {
            int match = (i * 2 == batchSize) ? grp[i] / 2 : Math.min(grp[i], grp[batchSize - i]);
            res += match;
            grp[i] -= match;
            grp[batchSize - i] -= match;
        }
        return res;
    }

    // Dynamic Programming(Bottom-Up) + Hash Table
    // 81 ms(55.83%), 38.5 MB(76.69%) for 67 tests
    public int maxHappyGroups3(int batchSize, int[] groups) {
        int[] grp = new int[batchSize];
        for (int g : groups) {
            grp[g % batchSize]++;
        }
        int[] pow = new int[batchSize + 1];
        int max = 1;
        pow[0] = 1;
        for (int i = 0; i < batchSize; i++) {
            max *= (grp[i] + 1);
            pow[i + 1] = pow[i] * (grp[i] + 1);
        }
        int[] dp = new int[max];
        dp[0] = 1;
        int res = 0;
        for (int i = 1; i < max; i++) {
            int remain = 0;
            for (int x = i, j = 0; j < batchSize; j++) {
                int pick = x % (grp[j] + 1);
                x /= (grp[j] + 1);
                if (pick > 0) {
                    dp[i] = Math.max(dp[i], dp[i - pow[j]]);
                    remain = (remain + pick * j) % batchSize;
                }
            }
            res = Math.max(res, dp[i]);
            dp[i] += (remain == 0) ? 1 : 0;
        }
        return res;
    }

    private void test(int batchSize, int[] groups, int expected) {
        assertEquals(expected, maxHappyGroups(batchSize, groups));
        assertEquals(expected, maxHappyGroups2(batchSize, groups));
        assertEquals(expected, maxHappyGroups3(batchSize, groups));
    }

    @Test public void test() {
        test(3, new int[] {1, 2, 3, 4, 5, 6}, 4);
        test(4, new int[] {1, 3, 2, 5, 2, 2, 1, 6}, 4);
        test(6,
             new int[] {369205928, 981877451, 947462486, 899465743, 737778942, 573732515, 520226542,
                        824581298, 571789442, 251943251, 70139785, 778962318, 43379662, 90924712,
                        142825931, 182207697, 178834435, 978165687}, 10);
        test(7,
             new int[] {145326640, 622724151, 591814792, 827053040, 111964428, 344376875, 42023891,
                        436582274, 78590835, 408269112, 930041188, 846233596, 158192647, 889601516,
                        134236253, 366035866, 123146762}, 9);
        test(8,
             new int[] {8, 8, 4, 1, 6, 8, 6, 3, 7, 7, 2, 4, 1, 6, 7, 4, 1, 4, 2, 4, 4, 7, 6, 1, 5,
                        1, 3, 4, 1, 1}, 16);
        test(9,
             new int[] {3, 1, 3, 3, 5, 6, 1, 1, 9, 10, 3, 3, 3, 1, 1, 3, 3, 3, 19, 20, 1, 3, 3, 3,
                        3, 1, 1, 3, 3, 30}, 9);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
