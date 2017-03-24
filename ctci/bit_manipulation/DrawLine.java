import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 5.8:
 * A monochrome screen is stored as a single array of bytes, allowing 8
 * consecutive pixels to be stored in one byte. The screen has width w, where w
 * is divisible by 8. The height of the screen can be derived from the length
 * of the array and the width. Implement a function draws a horizontal line.
 */
public class DrawLine {
    public static void drawHorizontalLine(byte[] screen, int width,
                                          int x1, int x2, int y) {
        if (x1 > x2) { // make sure x1 <= x2
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }

        int startBitOffset = x1 % 8;
        int startFullByte = x1 / 8;
        if (startBitOffset > 0) {
            startFullByte++;
        }

        int endBitOffset = x2 % 8;
        int endFullByte = x2 / 8;
        if (endBitOffset != 7) {
            endFullByte--;
        }

        int yByteBase = width / 8 * y;
        int yStart = yByteBase + startFullByte;
        int yEnd = yByteBase + endFullByte;
        // full bytes
        for (int i = yStart; i <= yEnd; i++) {
            screen[i] = (byte)0xff;
        }
        // fix edges
        byte startMask = (byte)(0xFF >> startBitOffset);
        byte endMask = (byte)~(0xFF >> (endBitOffset + 1));
        if (x1 / 8 == x2 / 8) { // x1 and x2 are in the same byte
            screen[yByteBase + x1 / 8] |= (startMask & endMask);
        } else {
            if (startBitOffset != 0) {
                screen[yStart - 1] |= startMask;
            }
            if (endBitOffset != 7) {
                screen[yEnd + 1] |= endMask;
            }
        }
    }

    public static void printScreen(byte[] screen, int width) {
        int wByte = width / 8;
        int height = screen.length / wByte;
        for (int y = 0; y < height; y++) {
            for (int xByte = 0; xByte < wByte; xByte++) {
                byte b = screen[wByte * y + xByte];
                for (int i = 7; i >= 0; i--) {
                    System.out.print((b >> i) & 1);
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // org.junit.runner.JUnitCore.main("DrawLine");
        int width = 64;
        int height = 10;
        byte[] screen = new byte[width * height / 8];
        screen[1] = 13;
        drawHorizontalLine(screen, width, 8, 16, 1);
        drawHorizontalLine(screen, width, 1, 7, 2);
        drawHorizontalLine(screen, width, 0, 10, 3);
        drawHorizontalLine(screen, width, 0, 7, 4);
        drawHorizontalLine(screen, width, 1, width - 2, height - 1);
        printScreen(screen, width);
    }
}
