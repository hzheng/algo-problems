import org.junit.Test;
import static org.junit.Assert.*;

// LC495: https://leetcode.com/problems/teemo-attacking/
//
// A hero called Teemo and his attacking can make his enemy Ashe be in poisoned
// condition. Now, given the Teemo's attacking ascending time series towards Ashe
// and the poisoning time duration per Teemo's attacking, you need to output the
// total time that Ashe is in poisoned condition.
// You may assume that Teemo attacks at the very beginning of a specific time
// point, and makes Ashe be in poisoned condition immediately.
public class TeemoAttacking {
    // beats 73.72%(9 ms for 39 tests)
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        int n = timeSeries.length;
        if (n == 0) return 0;

        int total = 0;
        for (int i = 1; i < n; i++) {
            total += Math.min(timeSeries[i] - timeSeries[i - 1], duration);
        }
        return total + duration;
    }

    void test(int[] timeSeries, int duration, int expected) {
        assertEquals(expected, findPoisonedDuration(timeSeries, duration));
    }

    @Test
    public void test() {
        test(new int[] {4}, 5, 5);
        test(new int[] {1, 4}, 2, 4);
        test(new int[] {1, 2}, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TeemoAttacking");
    }
}
