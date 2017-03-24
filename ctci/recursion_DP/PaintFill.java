import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.Collections;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 9.7:
 * Implement the "paint fill" function: given a screen (represented by a
 * 2D array of colors), a point, and a new color, fill in the surrounding area
 * until the color changes from the original color.
 */
public class PaintFill {
    public static void fill(int[][] screen, int x, int y, int newColor) {
        if (screen[y][x] != newColor) {
            fill(screen, x, y, screen[y][x], newColor);
        }
    }

    private static void fill(int[][] screen, int x, int y,
                             int oldColor, int newColor) {
        if (x < 0 || x >= screen[0].length
            || y < 0 || y >= screen.length) {
            return;
        }

        if (screen[y][x] == oldColor) {
            screen[y][x] = newColor;
            fill(screen, x - 1, y, oldColor, newColor);
            fill(screen, x + 1, y, oldColor, newColor);
            fill(screen, x, y - 1, oldColor, newColor);
            fill(screen, x, y + 1, oldColor, newColor);
        }
    }

    @Test
    public void test() {
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PaintFill");
    }
}
