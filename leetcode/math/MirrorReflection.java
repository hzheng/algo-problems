import org.junit.Test;
import static org.junit.Assert.*;

// LC858: https://leetcode.com/problems/mirror-reflection/
//
// There is a special square room with mirrors on each of the four walls.  
// Except for the southwest corner, there are receptors on each of the remaining
// corners, numbered 0, 1, and 2(counter-clockwise). The square room has walls 
// of length p, and a laser ray from the southwest corner first meets the east 
// wall at a distance q from the 0th receptor. Return the number of the receptor
// that the ray meets first.  (It is guaranteed that the ray will meet a 
// receptor eventually.)
// Note:
// 1 <= p <= 1000
// 0 <= q <= p
public class MirrorReflection {
    // time complexity: O(log(p)), space complexity: O(1)
    // beats %(6 ms for 69 tests)
    public int mirrorReflection(int p, int q) {
        for (; p % 2 == 0 && q % 2 == 0; p /= 2, q /= 2) {}
        if (p % 2 == 0) return 2;
        if (q % 2 == 0) return 0;
        return 1;
    }

    // time complexity: O(log(p)), space complexity: O(1)
    // beats %(6 ms for 69 tests)
    public int mirrorReflection2(int p, int q) {
        for (; p % 2 == 0 && q % 2 == 0; p /= 2, q /= 2) {}
        return 1 - p % 2 + q % 2;
    }

    // time complexity: O(log(p)), space complexity: O(1)
    // beats %(6 ms for 69 tests)
    public int mirrorReflection3(int p, int q) {
        int factor = gcd(p, q);
        p /= factor;
        p %= 2;
        q /= factor;
        q %= 2;
        if (p == 1 && q == 1) return 1;
        return p == 1 ? 0 : 2;
    }

    private int gcd(int a, int b) {
        return (a == 0) ? b : gcd(b % a, a);
    }

    void test(int p, int q, int expected) {
        assertEquals(expected, mirrorReflection(p, q));
        assertEquals(expected, mirrorReflection2(p, q));
        assertEquals(expected, mirrorReflection3(p, q));
    }

    @Test
    public void test() {
        test(2, 1, 2);
        test(5, 2, 0);
        test(5, 3, 1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
