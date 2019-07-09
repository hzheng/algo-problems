import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1093: https://leetcode.com/problems/statistics-from-a-large-sample/
//
// We sampled integers between 0 and 255, and stored the results in an array count:  count[k] is the
// number of integers we sampled equal to k. Return the minimum, maximum, mean, median, and mode of
// the sample respectively, as an array of floating point numbers. The mode is unique.
public class SampleStats {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 38 MB(100%) for 46 tests
    public double[] sampleStats(int[] count) {
        double min = -1;
        double max = 0;
        int mode = 0;
        double sum = 0;
        int total = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] > 0) {
                if (min < 0) {
                    min = i;
                }
                max = i;
                sum += count[i] * i;
                total += count[i];
                if (count[i] > count[mode]) {
                    mode = i;
                }
            }
        }
        double median = 0;
        boolean isOdd = (total % 2 == 1);
        int mid = (total - 1) / 2;
        int total2 = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 0) {
                continue;
            }
            total2 += count[i];
            if (total2 >= mid) {
                if (isOdd || total2 > mid + 1) {
                    median = i;
                } else {
                    int j = i;
                    while (count[++j] == 0) {}
                    median = (i + j) / 2.0;
                }
                break;
            }
        }
        return new double[]{min, max, sum / total, median, mode};
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 38.1 MB(100%) for 46 tests
    public double[] sampleStats2(int[] count) {
        double min = -1;
        double max = 0;
        int mode = 0;
        double sum = 0;
        int total = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] > 0) {
                if (min < 0) {
                    min = i;
                }
                max = i;
                sum += count[i] * i;
                total += count[i];
                if (count[i] > count[mode]) {
                    mode = i;
                }
            }
        }
        double median = 0;
        int m1 = (total + 1) / 2; // m1-th items
        int m2 = total / 2 + 1; // m2-th items
        for (int i = 0, cnt = 0; i < count.length; cnt += count[i++]) {
            if (cnt < m1 && cnt + count[i] >= m1) { // find m1-th item.
                median += i / 2.0;
            }
            if (cnt < m2 && cnt + count[i] >= m2) { // find m2-th item.
                median += i / 2.0;
            }
        }
        return new double[]{min, max, sum / total, median, mode};
    }

    // Binary Search
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(76.80%), 38.2 MB(100%) for 46 tests
    public double[] sampleStats3(int[] count) {
        double min = -1;
        double max = 0;
        int mode = 0;
        double sum = 0;
        int total = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] > 0) {
                if (min < 0) {
                    min = i;
                }
                max = i;
                sum += count[i] * i;
                total += count[i];
                if (count[i] > count[mode]) {
                    mode = i;
                }
            }
        }
        double m1 = (total - 1) / 2 + 0.01; // m1-th items
        double m2 = total / 2 + 0.01; // m2-th items
        double[] cumulativeSum = new double[count.length];
        cumulativeSum[0] = count[0];
        for (int i = 1; i < count.length; i++) {
            cumulativeSum[i] += cumulativeSum[i - 1] + count[i];
        }
        int median1 = -Arrays.binarySearch(cumulativeSum, m1) - 1;
        int median2 = -Arrays.binarySearch(cumulativeSum, m2) - 1;
        return new double[]{min, max, sum / total, (median1 + median2) / 2.0, mode};
    }

    void test(int[] count, double[] expected) {
        assertArrayEquals(expected, sampleStats(count), 1E-5);
        assertArrayEquals(expected, sampleStats2(count), 1E-5);
        assertArrayEquals(expected, sampleStats3(count), 1E-5);
    }

    @Test
    public void test() {
        test(new int[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0},
             new double[]{1.00000, 1.00000, 1.00000, 1.00000, 1.00000});
        test(new int[]{0, 1, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0},
             new double[]{1.00000, 3.00000, 2.37500, 2.50000, 3.00000});
        test(new int[]{0, 4, 3, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
             new double[]{1.00000, 4.00000, 2.18182, 2.00000, 1.00000});
        test(new int[]{2725123, 2529890, 2612115, 3807943, 3002363, 3107290, 2767526, 981092,
                       896521, 2576757, 2808163, 3315813, 2004022, 2516900, 607052, 1203189,
                       2907162, 1849193, 1486120, 743035, 3621726, 3366475, 639843, 3836904, 462733,
                       2614577, 1881392, 85099, 709390, 3534613, 360309, 404975, 715871, 2258745,
                       1682843, 3725079, 564127, 1893839, 2793387, 2236577, 522108, 1183512, 859756,
                       3431566, 907265, 1272267, 2261055, 2234764, 1901434, 3023329, 863353,
                       2140290, 2221702, 623198, 955635, 304443, 282157, 3133971, 1985993, 1113476,
                       2092502, 2896781, 1245030, 2681380, 2286852, 3423914, 3549428, 2720176,
                       2832468, 3608887, 174642, 1437770, 1545228, 650920, 2357584, 3037465,
                       3674038, 2450617, 578392, 622803, 3206006, 3685232, 2687252, 1001246,
                       3865843, 2755767, 184888, 2543886, 2567950, 1755006, 249516, 3241670,
                       1422728, 809805, 955992, 415481, 26094, 2757283, 995334, 3713918, 2772540,
                       2719728, 1204666, 1590541, 2962447, 779517, 1322374, 1675147, 3146304,
                       2412486, 902468, 259007, 3161334, 1735554, 2623893, 1863961, 520352, 167827,
                       3654335, 3492218, 1449347, 1460253, 983079, 1135, 208617, 969433, 2669769,
                       284741, 1002734, 3694338, 2567646, 3042965, 3186843, 906766, 2755956,
                       2075889, 1241484, 3790012, 2037406, 2776032, 1123633, 2537866, 3028339,
                       3375304, 1621954, 2299012, 1518828, 1380554, 2083623, 3521053, 1291275,
                       180303, 1344232, 2122185, 2519290, 832389, 1711223, 2828198, 2747583, 789884,
                       2116590, 2294299, 1038729, 1996529, 600580, 184130, 3044375, 261274, 3041086,
                       3473202, 2318793, 2967147, 2506188, 127448, 290011, 3868450, 1659949,
                       3662189, 1720152, 25266, 1126602, 1015878, 2635566, 619797, 2898869, 3470795,
                       2226675, 2348104, 2914940, 1907109, 604482, 2574752, 1841777, 880254, 616721,
                       3786049, 2278898, 3797514, 1328854, 1881493, 1802018, 3034791, 3615171,
                       400080, 2277949, 221689, 1021253, 544372, 3101480, 1155691, 3730276, 1827138,
                       3621214, 2348383, 2305429, 313820, 36481, 2581470, 2794393, 902504, 2589859,
                       740480, 2387513, 2716342, 1914543, 3219912, 1865333, 2388350, 3525289,
                       3758988, 961406, 1539328, 448809, 1326527, 1339048, 2924378, 2715811, 376047,
                       3642811, 2973602, 389167, 1026011, 3633833, 2848596, 3353421, 1426817,
                       219995, 1503946, 2311246, 2618861, 1497325, 3758762, 2115273, 3238053,
                       2419849, 2545790},
             new double[]{0.0, 255.0, 128.64187, 131.0, 175.0});

    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
