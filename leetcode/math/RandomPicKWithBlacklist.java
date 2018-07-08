import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC864: https://leetcode.com/problems/random-pick-with-blacklist/
//
// Given a blacklist B containing unique integers from [0, N), write a function
// to return a uniform random integer from [0, N) which is NOT in B.
// Optimize it such that it minimizes the call to system’s Math.random().
public class RandomPicKWithBlacklist {
    static interface IRandomPicKWithBlacklist {
        public int pick();
    }

    // Hash Table
    // beats 96.30%(121 ms for 67 tests)
    static class Solution implements IRandomPicKWithBlacklist {
        private Random rand = new Random();
        private Map<Integer, Integer> map = new HashMap<>();
        private int max = 0;

        public Solution(int N, int[] blacklist) {
            max = N - blacklist.length;
            for (int b : blacklist) {
                map.put(b, -1);
            }
            for (int b : blacklist) {
                if (b >= max) continue;

                for (; map.containsKey(N - 1); N--) {}
                map.put(b, --N);
            }
        }
        
        public int pick() {
            int i = rand.nextInt(max);
            return map.getOrDefault(i, i);
        }
    }

    void test1(IRandomPicKWithBlacklist obj) {
        for (int i = 0; i < 10; i++) {
            System.out.println(obj.pick());
        }
    }

    @Test
    public void test() {
        test1(new Solution(18, new int[]{0, 2, 4, 6, 8, 10, 12, 14, 16}));
        test1(new Solution(8, new int[]{1, 3, 5}));
        test1(new Solution(3, new int[]{0}));
        test1(new Solution(3, new int[]{1}));
        test1(new Solution(3, new int[]{}));
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
