import org.junit.Test;
import static org.junit.Assert.*;

// LC605: https://leetcode.com/problems/can-place-flowers/
//
// Suppose you have a long flowerbed in which some of the plots are planted and
// some are not. However, flowers cannot be planted in adjacent plots. Given a
// flowerbed (represented as an array containing 0 and 1, where 0 means empty
// and 1 means not empty), and a number n, return if n new flowers can be
// planted in it without violating the no-adjacent-flowers rule.
public class PlaceFlower {
    // beats 34.20%(13 ms for 123 tests)
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        for (int i = 0, prev = -2, left = n, len = flowerbed.length; i <= len;
             i++) {
            if (i == len || flowerbed[i] != 0) {
                if (i == len) {
                    i++;
                }
                if ((left -= Math.max((i - prev) / 2 - 1, 0)) <= 0) return true;

                prev = i;
            }
        }
        return false;
    }

    // beats 59.69%(12 ms for 123 tests)
    public boolean canPlaceFlowers2(int[] flowerbed, int n) {
        for (int i = 0, prev = -1, left = n, len = flowerbed.length; i < len;
             i++) {
            if (flowerbed[i] == 0 && i != prev
                && (i == 0 || flowerbed[i - 1] == 0)
                && (i == len - 1 || flowerbed[i + 1] == 0)) {
                if (--left <= 0) return true;

                prev = i + 1;
            }
        }
        return n == 0;
    }

    void test(int[] flowerbed, int n, boolean expected) {
        assertEquals(expected, canPlaceFlowers(flowerbed, n));
        assertEquals(expected, canPlaceFlowers2(flowerbed, n));
    }

    @Test
    public void test() {
        test(new int[] {1, 0, 0, 0, 1, 0, 0}, 2, true);
        test(new int[] {0, 0, 1, 0, 1}, 1, true);
        test(new int[] {1, 0, 0, 0, 0, 1}, 2, false);
        test(new int[] {1, 0, 0, 0, 1}, 1, true);
        test(new int[] {1, 0, 0, 0, 1}, 2, false);
        test(new int[] {1, 0, 1, 0, 1, 0, 1}, 0, true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().
        getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
