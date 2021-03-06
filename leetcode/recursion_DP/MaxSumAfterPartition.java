import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1043: https://leetcode.com/problems/partition-array-for-maximum-sum/
//
// Given an integer array A, you partition the array into (contiguous) subarrays of length at most
// K.  After partitioning, each subarray has their values changed to become the maximum value of
// that subarray. Return the largest sum of the given array after partitioning.
// Note:
// 1 <= K <= A.length <= 500
// 0 <= A[i] <= 10^6
public class MaxSumAfterPartition {
    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N * K), space complexity: O(N)
    // 4 ms(100%),  36.5 MB(100%) for 51 tests
    public int maxSumAfterPartitioning(int[] A, int K) {
        int n = A.length;
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int k = 1, maxVal = 0, maxK = Math.min(i, K); k <= maxK; k++) {
                maxVal = Math.max(maxVal, A[i - k]);
                dp[i] = Math.max(dp[i], dp[i - k] + maxVal * k);
            }
        }
        return dp[n];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N * K), space complexity: O(N)
    // 3 ms(100%),  37.5 MB(100%) for 51 tests
    public int maxSumAfterPartitioning2(int[] A, int K) {
        return maxSum(A, K, A.length, new int[A.length + 1]);
    }

    private int maxSum(int[] A, int K, int index, int[] dp) {
        if (index == 0 || dp[index] > 0) {
            return dp[index];
        }

        int res = 0;
        for (int k = 1, maxVal = 0, maxK = Math.min(index, K); k <= maxK; k++) {
            maxVal = Math.max(maxVal, A[index - k]);
            res = Math.max(res, maxSum(A, K, index - k, dp) + maxVal * k);
        }
        return dp[index] = res;
    }

    void test(int[] A, int K, int expected) {
        assertEquals(expected, maxSumAfterPartitioning(A, K));
        assertEquals(expected, maxSumAfterPartitioning2(A, K));
    }

    @Test
    public void test() {
        test(new int[]{1, 15, 7, 9, 2, 5, 10}, 3, 84);
        test(new int[]{10, 9, 3, 2}, 2, 30);
        test(new int[]{828744, 238880, 451054, 55196, 540424, 716102, 231631, 133817, 659358,
                       193648, 512029, 546502, 904749, 690684, 728885, 8409, 498810, 612015, 304222,
                       775935, 756538, 221108, 402157, 21145, 31973, 549876, 696507, 848452, 771649,
                       936055, 804041, 754797, 336224, 981637, 715138, 372354, 494988, 539765,
                       735620, 651443, 564339, 757329, 745391, 808416, 943673, 569482, 619677,
                       414844, 936647, 974789, 315593, 833466, 866189, 925801, 284184, 583165,
                       734015, 424909, 870049, 485452, 584461, 740113, 394150, 931935, 300525,
                       574243, 721674, 86941, 632054, 67326, 103743, 805213, 57129, 366325, 385392,
                       491570, 717602, 744440, 976359, 768348, 533279, 88001, 372455, 977015,
                       975773, 305152, 513631, 855241, 179700, 451862, 181689, 811913, 187051,
                       805521, 908269, 529764, 674032, 423258, 569285, 597169, 931921, 683779,
                       86771, 190311, 815791, 401489, 354124, 681630, 418977, 213139, 43544, 42403,
                       640402, 652406, 936720, 337493},
             31, 112891215);
        test(new int[]{422751, 941229, 108772, 315862, 580313, 758846, 976938, 17165, 697713,
                       556686, 50821, 506408,
                       219990, 763698, 939437, 575934, 189426, 947487, 780033, 996818, 869582,
                       413299, 844371, 421126, 225416, 745520, 676892, 650514, 936391, 261259,
                       58508, 805516, 768771, 755181, 306278, 222420, 113346, 468380, 960007,
                       791074, 871437, 213145, 2765, 629344, 348873, 443733, 214218, 787104, 203848,
                       614899, 683477, 358667, 888628, 669669, 418412, 897974, 186240, 937397,
                       708274, 461085, 863649, 222822, 198155, 319588, 614338, 718290, 232317,
                       348771, 2089, 578781, 257309, 9374, 699869, 455331, 825678, 3052, 350758,
                       212713, 403358, 944804, 299603, 410479, 257857, 11459, 333295, 573829,
                       156532, 882806, 431795, 682417, 612662, 867359, 120400, 647712, 706614,
                       803112, 203799, 394708, 879274, 113053, 234033, 32075, 500246, 70579, 35847,
                       630129, 333942, 394724, 169968, 12311, 766156, 310932, 910101, 753768,
                       322048, 579689, 611362, 327168, 356643, 360770, 767977, 938424, 553516,
                       839599, 977492, 484173, 701412, 678385, 424482, 226224, 370680, 278995,
                       846469, 975681, 927553, 373177, 312658, 125051, 21027, 721060, 800371,
                       316559, 805172, 791989, 579039, 163549, 644579, 322759, 248427, 415107,
                       735132, 355414, 739764, 519656, 364085, 137858, 887381, 967943, 662065, 5693,
                       747822, 448386, 241081, 182172, 708997, 701185, 668813, 452878, 679201,
                       690462, 17349, 610068, 272572, 4332, 288177, 343412, 150124, 182702, 105347,
                       484754, 866604, 40975, 616671, 201805, 606974, 384454, 523443, 293594,
                       560984, 585540, 917614, 20030, 248862, 849871, 834032, 986510, 298266,
                       343876, 96882, 968677, 582852, 912490, 337844, 613183, 520825, 946865,
                       107168, 78455, 915189, 672052, 264027, 200533, 546337, 665937, 736202,
                       287825, 882745, 844936, 880398, 936646, 539338, 688972, 678124, 612469,
                       534133, 573790, 365549, 701522, 584166, 911085, 350560, 620122, 741226,
                       803135, 374183, 929627, 392532, 79372, 913140, 842121, 552610, 323061, 97294,
                       862743, 348471, 178508, 524604, 277930, 868784, 75443, 421500, 949997,
                       249319, 447604, 836659, 861822, 119490, 304285, 729391, 307549, 847353,
                       71823, 762410, 190289, 380976, 475142, 987168, 820773, 784262, 218141, 15442,
                       131778, 528447, 544432, 691811, 959453, 833840, 637882, 620806, 908630,
                       760879, 986106, 289961}, 117, 280445612);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
