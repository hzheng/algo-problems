import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC832: https://leetcode.com/problems/flipping-an-image/
//
// Given a binary matrix A, we flip the image horizontally, then invert it, and
// return the resulting image. To flip an image horizontally means that each row
// of the image is reversed. To invert an image means that each 0 is replaced by
// 1, and each 1 is replaced by 0.
public class FlipImage {
    // beats %(7 ms for 82 tests)
    public int[][] flipAndInvertImage(int[][] A) {
        for (int[] a : A) {
            for (int i = 0, j = a.length - 1; i <= j; i++, j--) {
                int tmp = a[i];
                a[i] = a[j] ^ 1;
                a[j] = tmp ^ 1;
            }
        }
        return A;
    }

    // beats %(7 ms for 82 tests)
    public int[][] flipAndInvertImage2(int[][] A) {
        for (int[] a : A) {
            for (int i = 0, j = a.length - 1; i <= j; i++, j--) {
                if (a[i] == a[j]) {
                    a[i] = (a[j] ^= 1);
                }
            }
        }
        return A;
    }

    void test(int[][] A, int[][] expected) {
        assertArrayEquals(expected, flipAndInvertImage(Utils.clone(A)));
        assertArrayEquals(expected, flipAndInvertImage2(Utils.clone(A)));
    }

    @Test
    public void test() {
        test(new int[][] { { 1, 1, 0 }, { 1, 0, 1 }, { 0, 0, 0 } },
             new int[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 1, 1, 1 } });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
