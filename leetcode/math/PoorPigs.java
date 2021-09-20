import org.junit.Test;

import static org.junit.Assert.*;

// LC458: https://leetcode.com/problems/poor-pigs/
//
// There are buckets buckets of liquid, where exactly one of the buckets is poisonous. To figure
// out which one is poisonous, you feed some number of (poor) pigs the liquid to see whether they
// will die or not. Unfortunately, you only have minutesToTest minutes to determine which bucket is
// poisonous.
//
// You can feed the pigs according to these steps:
// Choose some live pigs to feed.
// For each pig, choose which buckets to feed it. The pig will consume all the chosen buckets
// simultaneously and will take no time.
// Wait for minutesToDie minutes. You may not feed any other pigs during this time.
// After minutesToDie minutes have passed, any pigs that have been fed the poisonous bucket will
// die, and all others will survive.
// Repeat this process until you run out of time.
// Given buckets, minutesToDie, and minutesToTest, return the minimum number of pigs needed to
// figure out which bucket is poisonous within the allotted time.
//
// Constraints:
// 1 <= buckets <= 1000
// 1 <= minutesToDie <= minutesToTest <= 100
public class PoorPigs {
    // Dynamic Programming
    // beats 100.00%(0 ms for 6 tests)
    public int poorPigs(int buckets, int minutesToDie, int minutesToTest) {
        if (buckets < 2) {return 0;}

        int times = minutesToTest / minutesToDie;
        for (int pigs = 1; ; pigs++) {
            if (maxBucket(pigs, times) >= buckets) {return pigs;}
        }
    }

    private int maxBucket(int pigs, int times) {
        int[][] dp = new int[pigs + 1][times + 1];
        for (int i = 1; i <= times; i++) {
            dp[0][i] = 1;
        }
        for (int i = 1; i <= pigs; i++) {
            dp[i][1] = 1 << i; // 2 ^ i
        }
        for (int i = 1; i <= pigs; i++) {
            for (int j = 2; j <= times; j++) {
                for (int k = (1 << i) - 1; k >= 0; k--) {
                    dp[i][j] += dp[i - Integer.bitCount(k)][j - 1];
                }
                // by binomial theorem, dp[i][j] = (1 + j) ^ i
            }
        }
        return dp[pigs][times];
    }

    // method 1: binomial theorem
    //
    // method 2: N-ary representation
    // (t+1)-based number to encode the buckets. 
    // 0 - the pig survives in the end
    // 1 - the pig drinks in the 1st round and die.
    // ...
    // t - the pig drinks in the t-th round and die.
    //
    // method 3: N-dimensional coordinate
    // each pig control one dimension, each round correspond one line(row)
    // beats 100.00%(0 ms for 6 tests)
    public int poorPigs2(int buckets, int minutesToDie, int minutesToTest) {
        if (buckets < 2) {return 0;}

        int times = minutesToTest / minutesToDie;
        for (int pigs = 1; ; pigs++) {
            if (Math.pow(times + 1, pigs) >= buckets) {return pigs;}
        }
    }

    // beats 10.29%(1 ms for 6 tests)
    public int poorPigs3(int buckets, int minutesToDie, int minutesToTest) {
        int times = minutesToTest / minutesToDie + 1;
        return (int)Math.ceil(Math.log(buckets) / Math.log(times));
    }

    void test(int buckets, int minutesToDie, int minutesToTest, int expected) {
        assertEquals(expected, poorPigs(buckets, minutesToDie, minutesToTest));
        assertEquals(expected, poorPigs2(buckets, minutesToDie, minutesToTest));
        assertEquals(expected, poorPigs3(buckets, minutesToDie, minutesToTest));
    }

    @Test public void test() {
        test(1, 1, 1, 0);

        test(1000, 15, 60, 5);
        test(3125, 15, 60, 5);
        test(3126, 15, 60, 6);

        test(1000, 12, 60, 4);
        test(1296, 12, 60, 4);
        test(1297, 12, 60, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
