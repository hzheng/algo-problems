import org.junit.Test;
import static org.junit.Assert.*;

// LC875: https://leetcode.com/problems/koko-eating-bananas/
//
// There are N piles of bananas, the i-th pile has piles[i] bananas. The guards
// have gone and will come back in H hours. Koko can decide her bananas-per-hour
// eating speed of K. Each hour, she chooses some pile of bananas, and eats K
// bananas from that pile.  If the pile has less than K bananas, she eats all of
// them instead, and won't eat any more bananas during this hour. Koko likes to
// eat slowly, but still wants to finish eating all the bananas before the
// guards come back. Return the minimum integer K such that she can eat all the
// bananas within H hours.
public class MinEatingSpeed {
    // Binary Search
    // beats 96.25%(13 ms for 113 tests)
    public int minEatingSpeed(int[] piles, int H) {
        int low = 1;
        int high = 1000_000_000;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (canFinish(piles, H, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private boolean canFinish(int[] piles, int H, int k) {
        int res = 0;
        for (int p : piles) {
            // res += (int)Math.ceil(p / (double)k);
            res += (p - 1) / k + 1;
        }
        return res <= H;
    }

    void test(int[] piles, int H, int expected) {
        assertEquals(expected, minEatingSpeed(piles, H));
    }

    @Test
    public void test() {
        test(new int[] { 3, 6, 7, 11 }, 8, 4);
        test(new int[] { 30, 11, 23, 4, 20 }, 5, 30);
        test(new int[] { 30, 11, 23, 4, 20 }, 6, 23);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
