import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1610: https://leetcode.com/problems/maximum-number-of-visible-points/
//
// You are given an array points, an integer angle, and your location, where location = [posx, posy]
// and points[i] = [xi, yi] both denote integral coordinates on the X-Y plane. Initially, you are
// facing directly east from your position. You cannot move from your position, but you can rotate.
// In other words, posx and posy cannot be changed. Your field of view in degrees is represented by
// angle, determining how wide you can see from any given view direction. Let d be the amount in
// degrees that you rotate counterclockwise. Then, your field of view is the inclusive range of
// angles [d - angle/2, d + angle/2].
// You can see some set of points if, for each point, the angle formed by the point, your position,
// and the immediate east direction from your position is in your field of view.
// There can be multiple points at one coordinate. There may be points at your location, and you can
// always see these points regardless of your rotation. Points do not obstruct your vision to other
// points.
// Return the maximum number of points you can see.
// Constraints:
// 1 <= points.length <= 10^5
// points[i].length == 2
// location.length == 2
// 0 <= angle < 360
// 0 <= posx, posy, xi, yi <= 10^9
public class VisiblePoints {
    // Sort + Sliding Window
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 110 ms(18.18%), 81.9 MB(100%) for 120 tests
    public int visiblePoints(List<List<Integer>> points, int angle, List<Integer> location) {
        List<Double> angles = new ArrayList<>();
        int duplicate = 0;
        for (List<Integer> p : points) {
            int dx = p.get(0) - location.get(0);
            int dy = p.get(1) - location.get(1);
            if (dx == 0 && dy == 0) {
                duplicate++;
            } else {
                angles.add(Math.toDegrees(Math.atan2(dy, dx)));
            }
        }
        Collections.sort(angles);
        int n = angles.size();
        for (int i = 0; i < n; i++) {
            angles.add(angles.get(i) + 360);
        }
        int res = 0;
        for (int i = 0, j = n; j < n * 2; j++) {
            for (; angles.get(j) - angles.get(i) > angle; i++) {}
            res = Math.max(res, j - i + 1);
        }
        return res + duplicate;
    }

    void test(Integer[][] pointArray, int angle, Integer[] locationArray, int expected) {
        List<List<Integer>> points = Utils.toList(pointArray);
        List<Integer> location = Arrays.asList(locationArray);
        assertEquals(expected, visiblePoints(points, angle, location));
    }

    @Test public void test() {
        test(new Integer[][] {{2, 1}, {2, 2}, {3, 3}}, 90, new Integer[] {1, 1}, 3);
        test(new Integer[][] {{2, 1}, {2, 2}, {3, 4}, {1, 1}}, 90, new Integer[] {1, 1}, 4);
        test(new Integer[][] {{1, 0}, {2, 1}}, 13, new Integer[] {1, 1}, 1);
        test(new Integer[][] {{0, 0}, {0, 2}}, 90, new Integer[] {1, 1}, 2);
        test(new Integer[][] {{198768142, 325231488}, {730653894, 526879029},
                              {482566443, 124650516}, {301750308, 786306795},
                              {428637509, 388444545}, {824139468, 560868920}, {46101719, 541790947},
                              {33117389, 306138652}, {379129552, 739264502}, {632078701, 382510936},
                              {648669937, 641406148}, {402494603, 290848535},
                              {681757446, 651339050}, {755146968, 328108553}, {856055968, 54585842},
                              {642810790, 781285498}, {624780623, 839525682},
                              {225552068, 597591380}, {941428680, 575243295},
                              {904246597, 409781914}, {133988308, 424694994},
                              {263860625, 642729245}, {725256971, 428462957}, {951188673, 24284291},
                              {65878467, 597579989}, {423910337, 760218568}, {375233659, 465709839},
                              {39079416, 44449206}, {76488044, 376497238}, {768046853, 401132958},
                              {862857872, 713625310}, {834212457, 613684155},
                              {145940546, 414657761}, {438671565, 895069996}, {486059332, 78047139},
                              {539611528, 236860222}, {328891159, 833572609},
                              {561041358, 896191043}, {469734995, 511499580},
                              {868786087, 593647615}, {502014973, 630219398},
                              {834825976, 939531210}, {232809706, 831430339},
                              {446916520, 518080962}, {516594877, 208057152},
                              {851130172, 768268153}, {665228968, 134767900}, {347594646, 46036486},
                              {675842115, 24895986}, {877430373, 353382710}, {167579980, 47992154},
                              {125351210, 769215749}, {438404131, 569154245},
                              {604952972, 128325995}, {304627075, 646626043},
                              {651998767, 527382342}, {121415369, 22955171}, {46278664, 726969424},
                              {650197837, 730272955}, {326340006, 424213045},
                              {242071539, 679004233}, {208227275, 449583956},
                              {688763276, 330569373}, {657221239, 659946024},
                              {760680906, 398786962}, {695186876, 163719246},
                              {416909447, 908414565}, {59247263, 674732497}, {396812330, 607544608},
                              {752069054, 728117920}}, 86, new Integer[] {136181398, 475556834},
             45);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
