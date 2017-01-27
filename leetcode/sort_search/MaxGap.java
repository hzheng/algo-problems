import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.function.Function;

// LC164: https://leetcode.com/problems/maximum-gap/
//
// Given an unsorted array, find the maximum difference between the successive
// elements in its sorted form. Try to solve it in linear time/space.
// Return 0 if the array contains less than 2 elements.
// Assume all elements in the array are non-negative integers and fit in the
// 32-bit signed integer range.
public class MaxGap {
    // Solution of Choice
    // Bucket Sort
    // time complexity: O(N), space complexity: O(N)
    // beats 83.93%(4 ms for 17 tests)
    public int maximumGap(int[] nums) {
        int n = nums.length;
        if (n < 2) return 0;

        int min = nums[0];
        int max = min;
        for (int num : nums) {
            if (num < min) {
                min = num;
            } else if (num > max) {
                max = num;
            }
        }

        int bucketSize = Math.max((max - min) / n, 1);
        int bucketCount = (max - min) / bucketSize + 1;
        int[][] buckets = new int[bucketCount][];
        for (int num : nums) {
            int index = (num - min) / bucketSize;
            if (buckets[index] == null) {
                buckets[index] = new int[] {num, num};
            } else {
                int[] range = buckets[index];
                range[0] = Math.min(range[0], num);
                range[1] = Math.max(range[1], num);
            }
        }
        int lastBound = -1;
        int maxGap = 0;
        // max gap must exist in different buckets, since at least one *middle*
        // bucket is empty(buckets number >= n + 1)
        for (int[] range : buckets) {
            if (range != null) {
                if (lastBound >= 0) {
                    maxGap = Math.max(maxGap, range[0] - lastBound);
                }
                lastBound = range[1];
            }
        }
        return maxGap;
    }

    // Radix Sort (changed input)
    // time complexity: O(N), space complexity: O(N)
    // beats 1.10% (72 ms)
    public int maximumGap2(int[] nums) {
        SortedMap<Integer, List<Integer> > map = new TreeMap<>();
        final int maxIndex = 10;
        final int radix = 10;
        int power = 1;
        for (int index = 1; index <= maxIndex; power *= radix, index++) {
            for (int num : nums) {
                int key = num / power;
                if (!map.containsKey(key)) {
                    List<Integer> list = new LinkedList<>();
                    map.put(key, list);
                }
                map.get(key).add(num);
            }
            int i = 0;
            for (List<Integer> list : map.values()) {
                for (int num : list) {
                    nums[i++] = num;
                }
            }
            map.clear();
        }

        int maxGap = 0;
        for (int i = 1; i < nums.length; i++) {
            maxGap = Math.max(maxGap, nums[i] - nums[i - 1]);
        }
        return maxGap;
    }

    // Radix Sort + Counting Sort(changed input)
    // time complexity: O(N), space complexity: O(N)
    // beats 96.41% (3 ms for 17 tests)
    public int maximumGap3(int[] nums) {
        int n = nums.length;
        if (n < 2) return 0;

        final int maxIndex = (1 << 4); // 16-radix
        int[] buffer = new int[n];
        for (int shift = 0; shift < 32; shift += 4) {
            int[] count = new int[maxIndex];
            for (int num : nums) {
                count[(num >> shift) & 0xF]++;
            }
            for (int i = 1; i < maxIndex; i++) {
                count[i] += count[i - 1];
            }
            for (int i = n - 1; i >= 0; i--) {
                buffer[--count[(nums[i] >> shift) & 0xF]] = nums[i];
            }
            System.arraycopy(buffer, 0, nums, 0, n);
        }

        int maxGap = 0;
        for (int i = 1; i < n; i++) {
            maxGap = Math.max(maxGap, buffer[i] - buffer[i - 1]);
        }
        return maxGap;
    }

    void test(Function<int[], Integer> maxGap, int expected, int ... nums) {
        assertEquals(expected, (int)maxGap.apply(nums.clone()));
    }

    void test(int expected, int ... nums) {
        MaxGap m = new MaxGap();
        test(m::maximumGap, expected, nums);
        test(m::maximumGap2, expected, nums);
        test(m::maximumGap3, expected, nums);
    }

    @Test
    public void test1() {
        test(81403346, 601408776,63967816,431363697,242509930,15970592,60284088,228037800,147629558,220782926,55455864,456541040,106650540,17290078,52153098,103139530,294196042,16568100,426864152,61916064,657788565,166159446,1741650,101791800,28206276,6223796,524849590,125389882,84399672,153834912,164568204,1866165,283209696,560993994,16266096,219635658,9188983,485969304,782013650,120332636,44659356,444517408,36369045,47370708,18542592,98802990,137690000,124889895,56062800,265421676,309417680,4634176,801661539,510541206,258227892,398938089,47255754,152260962,409663140,102847688,45756553,377936600,269498,375738702,263761134,53797945,329493948,224442208,508336845,189507850,40944620,127879560,119629476,186894520,62409156,693721503,4289916,523899936,28955240,266488028,20356650,40769391,483694272,97988044,84102,67246047,310688630,41288643,58965588,42881432,152159462,94786355,174917835,119224652,525034376,261516,274800528,62643819,23613832,8397240,797832131,855155367,337066320,26341480,61932200,20661075,515542796,390337500,522552030,43538516,150800550,116747540,152989123,488640056,700610304,233604,344277340,21439176,9397864,16365822,73027584,453041413,197374275,157735188,15273822,187081152,379611084,865005504,223099767,80478651,377729400,186738219,34738263,16634072,112791343,99631856,119364960,477106486,583953920,624509809,188442472,294181256,213023715,146645884,149530380,497592753,132170327,72770643,126683010,405141255,590214306,26670714,95582385,162080790,231120099,8946432,204967980,592849110,54120698,375915096,602145859,5346440,226337825,425156369,653591624,578483360,572410800,32290700,381384563,149939976,183225375,155695620,38307636,457513760,97085778,75200576,8068176,221650296,556889418,252495726,895020231,19932465,156334887,191383314,348432526,368701264,14315598,148936587,279419435,237325542,252587218,322929504,26331343,355297676,600420786,652017765,51673622,159015675);
        test(97, 100, 3, 2, 1);
        test(0, 2, 2, 2, 2);
        test(3, 3, 2, 1, 5, 7, 6, 9, 12, 14);
        test(5, 2, 5, 7, 10, 8, 13, 21, 10, 18);
        test(2901, 15252, 16764, 27963, 7817, 26155, 20757, 3478, 22602, 20404,
             6739, 16790, 10588, 16521, 6644, 20880, 15632, 27078, 25463, 20124,
             15728, 30042, 16604, 17223, 4388, 23646, 32683, 23688, 12439, 30630,
             3895, 7926, 22101, 32406, 21540, 31799, 3768, 26679, 21799, 23740);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxGap");
    }
}
