import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.Utils;

// LC1801: https://leetcode.com/problems/number-of-orders-in-the-backlog/
//
// You are given a 2D integer array orders, where each orders[i] = [pricei, amounti, orderTypei]
// denotes that amounti orders have been placed of type orderTypei at the price pricei. The
// orderTypei is:
// 0 if it is a batch of buy orders, or
// 1 if it is a batch of sell orders.
// Note that orders[i] represents a batch of amounti independent orders with the same price and
// order type. All orders represented by orders[i] will be placed before all orders represented by
// orders[i+1] for all valid i.
// There is a backlog that consists of orders that have not been executed. The backlog is initially
// empty. When an order is placed, the following happens:
// If the order is a buy order, you look at the sell order with the smallest price in the backlog.
// If that sell order's price is smaller than or equal to the current buy order's price, they will
// match and be executed, and that sell order will be removed from the backlog. Else, the buy order
// is added to the backlog.
// Vice versa, if the order is a sell order, you look at the buy order with the largest price in the
// backlog. If that buy order's price is larger than or equal to the current sell order's price,
// they will match and be executed, and that buy order will be removed from the backlog. Else, the
// sell order is added to the backlog.
// Return the total amount of orders in the backlog after placing all the orders from the input.
// Since this number can be large, return it modulo 10^9 + 7.
//
// Constraints:
// 1 <= orders.length <= 10^5
// orders[i].length == 3
// 1 <= pricei, amounti <= 10^9
// orderTypei is either 0 or 1.
public class NumberOfBacklogOrders {
    private static final int MOD = 1_000_000_007;

    // SortedSet
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 35 ms(%), 87.7 MB(%) for 60 tests
    public int getNumberOfBacklogOrders(int[][] orders) {
        TreeSet[] trades =
                {new TreeSet<int[]>((a, b) -> (a[0] == b[0]) ? b[2] - a[2] : a[0] - b[0]),
                 new TreeSet<int[]>((a, b) -> (a[0] == b[0]) ? a[2] - b[2] : b[0] - a[0])};
        for (int i = 0, n = orders.length; i < n; i++) {
            int[] order = orders[i];
            int type = order[2];
            TreeSet<int[]> trade = trades[type];
            for (int sign = type * 2 - 1;
                 !trade.isEmpty() && (trade.first()[0] - order[0]) * sign >= 0; ) {
                int[] first = trade.pollFirst();
                int deal = Math.min(order[1], first[1]);
                order[1] -= deal;
                if ((first[1] -= deal) > 0) {
                    trade.add(first);
                    break;
                }
            }
            if (order[1] > 0) {
                ((TreeSet<int[]>)(trades[1 - type])).add(new int[] {order[0], order[1], i});
            }
        }
        int res = 0;
        for (TreeSet<int[]> trade : trades) {
            for (int[] t : trade) {
                res = (res + t[1]) % MOD;
            }
        }
        return res;
    }

    // Heap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 37 ms(%), 82.4 MB(%) for 60 tests
    public int getNumberOfBacklogOrders2(int[][] orders) {
        PriorityQueue[] trades =
                {new PriorityQueue<int[]>((a, b) -> (a[0] == b[0]) ? b[2] - a[2] : a[0] - b[0]),
                 new PriorityQueue<int[]>((a, b) -> (a[0] == b[0]) ? a[2] - b[2] : b[0] - a[0])};
        for (int i = 0, n = orders.length; i < n; i++) {
            int[] order = orders[i];
            int type = order[2];
            PriorityQueue<int[]> trade = trades[type];
            for (int sign = type * 2 - 1;
                 !trade.isEmpty() && (trade.peek()[0] - order[0]) * sign >= 0; ) {
                int[] first = trade.poll();
                int deal = Math.min(order[1], first[1]);
                order[1] -= deal;
                if ((first[1] -= deal) > 0) {
                    trade.offer(first);
                    break;
                }
            }
            if (order[1] > 0) {
                ((PriorityQueue<int[]>)(trades[1 - type])).offer(new int[] {order[0], order[1], i});
            }
        }
        int res = 0;
        for (PriorityQueue<int[]> trade : trades) {
            for (int[] t : trade) {
                res = (res + t[1]) % MOD;
            }
        }
        return res;
    }

    // Heap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 47 ms(%), 82.4 MB(%) for 60 tests
    public int getNumberOfBacklogOrders3(int[][] orders) {
        PriorityQueue<int[]> buy = new PriorityQueue<>(Comparator.comparingInt(a -> -a[0]));
        PriorityQueue<int[]> sell = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        for (int[] order : orders) {
            if (order[2] == 0) {
                buy.offer(order);
            } else {
                sell.offer(order);
            }
            while (!buy.isEmpty() && !sell.isEmpty()) {
                int[] buyHead = buy.peek();
                int[] sellHead = sell.peek();
                if (buyHead[0] < sellHead[0]) { break; }

                int deal = Math.min(buyHead[1], sellHead[1]);
                buyHead[1] -= deal;
                sellHead[1] -= deal;
                if (buyHead[1] == 0) {
                    buy.poll();
                } else if (sellHead[1] == 0) {
                    sell.poll();
                }
            }
        }
        int res = 0;
        for (int[] order : buy) {
            res = (res + order[1]) % MOD;
        }
        for (int[] order : sell) {
            res = (res + order[1]) % MOD;
        }
        return res;
    }

    private void test(int[][] orders, int expected) {
        assertEquals(expected, getNumberOfBacklogOrders(Utils.clone(orders)));
        assertEquals(expected, getNumberOfBacklogOrders2(Utils.clone(orders)));
        assertEquals(expected, getNumberOfBacklogOrders3(Utils.clone(orders)));
    }

    @Test public void test() {
        test(new int[][] {{10, 5, 0}, {15, 2, 1}, {25, 1, 1}, {30, 4, 0}}, 6);
        test(new int[][] {{7, 1000000000, 1}, {15, 3, 0}, {5, 999999995, 0}, {5, 1, 1}}, 999999984);
        test(new int[][] {{1, 29, 1}, {22, 7, 1}, {24, 1, 0}, {25, 15, 1}, {18, 8, 1}, {8, 22, 0},
                          {25, 15, 1}, {30, 1, 1}, {27, 30, 0}}, 22);
        test(new int[][] {{26, 5, 1}, {14, 7, 1}, {22, 4, 1}, {30, 18, 1}, {30, 29, 1}, {25, 18, 0},
                          {3, 24, 0}, {3, 5, 0}, {6, 30, 0}, {9, 25, 1}}, 129);
        test(new int[][] {{286791088, 798094111, 0}, {270132718, 190673425, 0},
                          {162145493, 63038428, 0}, {917586234, 459670838, 0},
                          {56705655, 684105415, 0}, {979633297, 873012019, 1},
                          {538081887, 397556342, 0}, {335745593, 43076309, 0},
                          {570656259, 35633247, 1}, {713239998, 137657056, 1},
                          {819696855, 759830612, 1}, {26435212, 790945632, 1},
                          {97041136, 268444554, 1}, {92111248, 324097821, 0},
                          {923090442, 412570485, 0}, {786629891, 280254261, 1},
                          {789438405, 139591698, 1}, {853165425, 408218589, 0},
                          {918972463, 727773439, 1}, {100994505, 456158124, 0},
                          {539811845, 598264780, 0}, {354449870, 991103455, 1},
                          {546878322, 841407093, 1}, {961775285, 627215894, 1},
                          {112407810, 139289530, 1}, {562368390, 599841776, 0},
                          {962951411, 892942017, 1}, {797981924, 168611716, 0},
                          {397662296, 417941689, 1}, {104561073, 439700398, 1},
                          {135685267, 617300259, 0}, {46787891, 322655271, 1},
                          {448381949, 320319716, 1}, {378618697, 292776531, 1},
                          {803949721, 803710552, 0}, {564926072, 130850462, 0},
                          {340495099, 981848852, 1}, {842572379, 351719653, 0},
                          {485131330, 300231397, 1}, {487631412, 980202523, 1},
                          {996021428, 796326963, 1}, {875775273, 955181326, 0},
                          {990300618, 621661558, 0}, {144505487, 52319295, 1},
                          {618050103, 916008477, 1}, {841758347, 263458579, 1},
                          {388241161, 855676122, 1}, {432261198, 330055646, 1},
                          {642378304, 365557201, 0}, {31216170, 159294968, 1},
                          {737920689, 237438772, 0}, {973675810, 121076648, 0},
                          {822835057, 441424728, 0}, {871586369, 106539247, 1},
                          {767357987, 114823374, 0}, {280995627, 71697207, 0},
                          {918210730, 78180691, 0}, {690672572, 900078704, 0},
                          {549193431, 925387284, 1}, {23460593, 633893306, 1},
                          {966381816, 910135503, 1}, {762655634, 866995185, 1},
                          {236153249, 957006218, 0}, {642181364, 530015440, 0},
                          {456817101, 485539261, 1}, {584626991, 979921047, 0},
                          {857018573, 517713410, 0}, {874060607, 902804313, 1},
                          {437346193, 216212554, 0}, {801851939, 571218111, 1},
                          {552208020, 940675048, 1}, {82047753, 433515821, 1},
                          {43077540, 377770580, 1}, {6873285, 203753809, 1},
                          {747319506, 742314739, 0}, {580650000, 128896958, 1},
                          {550284045, 952614463, 1}, {833834368, 583072672, 1},
                          {564387926, 441776763, 1}, {647218670, 676420026, 1},
                          {485892086, 324173812, 0}, {543654114, 476938198, 0},
                          {590552061, 829475366, 0}, {271381554, 683613211, 0},
                          {455457663, 790419242, 0}, {395526922, 451903643, 1},
                          {302317355, 707587320, 0}, {965023202, 97148783, 0},
                          {908754494, 5173679, 1}, {288702557, 498607743, 1},
                          {854599176, 839172022, 1}, {682724429, 804623264, 1}}, 571132385);
        test(new int[][] {{361358476, 417104487, 1}, {862783832, 125101075, 1},
                          {380637994, 155365340, 1}, {159193302, 19502991, 0},
                          {874082362, 279423112, 1}, {921145968, 136492672, 1},
                          {443360004, 276940131, 1}, {794053327, 451780969, 1},
                          {465469967, 360673537, 1}, {231353007, 768224767, 1},
                          {431541765, 768992353, 1}, {98115561, 69193206, 0},
                          {221340730, 160635367, 0}, {784009204, 28063369, 0},
                          {271896932, 167195373, 1}, {831831846, 106249291, 1},
                          {697567471, 776858211, 0}, {895889943, 3216942, 0},
                          {459224380, 657013980, 0}, {590124178, 234373334, 1},
                          {372269019, 50503221, 0}, {788685815, 593600836, 1},
                          {896477150, 721174573, 0}, {284900527, 735034712, 1},
                          {192244583, 515087841, 0}, {844415960, 230016784, 0},
                          {930286848, 418455523, 0}, {718012902, 480277114, 1},
                          {732248290, 545265852, 1}, {971820494, 422069342, 0},
                          {650807021, 916843078, 1}, {113633750, 160130902, 1},
                          {658886615, 972154472, 0}, {179447414, 559143144, 1},
                          {447325175, 797980285, 1}, {933007522, 528792662, 0},
                          {846872697, 815681197, 1}, {924543260, 521811016, 0},
                          {9193275, 813655937, 1}, {994083976, 950433240, 1},
                          {60600678, 580396084, 1}, {321976731, 815888327, 0},
                          {245414606, 545475655, 1}, {290272852, 758342564, 0},
                          {441998361, 159275467, 0}, {275217377, 209713152, 1},
                          {602207362, 676485310, 0}, {572068117, 895979413, 0},
                          {444316431, 290084468, 1}, {515553868, 746704591, 1},
                          {286750538, 527557772, 0}, {535537489, 394798113, 1},
                          {259483159, 363348839, 0}, {650399949, 814880683, 0},
                          {793717384, 944796481, 1}, {574197634, 160448754, 0},
                          {541108369, 350046965, 0}, {692632493, 817555809, 0},
                          {338622061, 668455470, 1}, {515833047, 354379485, 0},
                          {137355481, 952682491, 0}, {749846261, 275381976, 0},
                          {94520165, 682108071, 0}, {604594598, 184773425, 0},
                          {242931949, 604796198, 0}, {540110156, 609428853, 1},
                          {453403106, 351801804, 0}, {830849806, 21530244, 1},
                          {882839191, 660753052, 0}, {90801442, 797844568, 0},
                          {300833006, 730885919, 1}, {288916292, 645468070, 1},
                          {24830042, 130627521, 1}, {372613361, 149723829, 0},
                          {269317728, 964874768, 0}, {731435700, 616336618, 0},
                          {372590210, 83080911, 0}, {778386822, 110820573, 1},
                          {340397622, 267310403, 1}, {568682633, 53436389, 1},
                          {33468510, 84093035, 0}, {994029194, 428774351, 1},
                          {773148979, 685453290, 0}, {100782778, 729762391, 1},
                          {293811407, 8550044, 1}, {120486825, 378346370, 0},
                          {650859744, 995610677, 1}, {435040447, 97788900, 1},
                          {606062809, 449598339, 1}, {323298518, 964563097, 0},
                          {679388031, 324990992, 0}, {57916619, 644343256, 0},
                          {188062365, 258322413, 0}, {963395373, 466661374, 1},
                          {586216675, 21459925, 1}, {578567465, 290913574, 1},
                          {508131224, 135370114, 1}, {761146345, 111404496, 1},
                          {74168486, 702679478, 1}}, 558895984);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
