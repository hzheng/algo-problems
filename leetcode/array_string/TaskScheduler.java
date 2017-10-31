import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC621: https://leetcode.com/problems/task-scheduler/
//
// Given a char array representing tasks CPU need to do. It contains capital
// letters A to Z where different letters represent different tasks. Tasks could
// be done without original order. Each task could be done in one interval. For
// each interval, CPU could finish one task or just be idle.
// However, there is a non-negative cooling interval n that means between two
// same tasks, there must be at least n intervals that CPU are doing different
// tasks or just be idle.
// You need to return the least number of intervals the CPU will take to finish
// all the given tasks.
// Note:
// The number of tasks is in the range [1, 10000].
// The integer n is in the range [0, 100].
public class TaskScheduler {
    // time complexity: O(N), space complexity: O(1)
    // beats 99.56%(8 ms for 64 tests)
    public int leastInterval(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }
        int max = 0;
        for (int c : count) {
            max = Math.max(max, c);
        }
        int res = (max - 1) * (n + 1);
        for (int c : count) {
            if (c == max) {
                res++;
            }
        }
        return Math.max(res, tasks.length);
    }

    // Sort
    // time complexity: O(N), space complexity: O(1)
    // beats 87.41%(10 ms for 64 tests)
    public int leastInterval2(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }
        Arrays.sort(count);
        int max = count[25] - 1;
        int slots = max * n;
        for (int i = 24; i >= 0 && count[i] > 0; i--) {
            slots -= Math.min(count[i], max);
        }
        return Math.max(slots, 0) + tasks.length;
    }

    // Sort + Simulation
    // time complexity: O(|res|), space complexity: O(1)
    // beats 45.35%(27 ms for 64 tests)
    public int leastInterval3(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }
        Arrays.sort(count);
        int res = 0;
        while (count[25] > 0) {
            for (int i = 0; i <= n && count[25] > 0; i++, res++) {
                if (i < 26 && count[25 - i] > 0) {
                    count[25 - i]--;
                }
            }
            Arrays.sort(count);
        }
        return res;
    }

    // Heap + Simulation
    // time complexity: O(|res|), space complexity: O(1)
    // beats 37.32%(72 ms for 64 tests)
    public int leastInterval4(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }
        PriorityQueue<Integer> pq
            = new PriorityQueue<>(26, Collections.reverseOrder());
        for (int c : count) {
            if (c > 0) {
                pq.offer(c);
            }
        }
        int res = 0;
        while (!pq.isEmpty()) {
            List<Integer> tmp = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                if (!pq.isEmpty()) {
                    if (pq.peek() > 1) {
                        tmp.add(pq.poll() - 1);
                    } else {
                        pq.poll();
                    }
                }
                res++;
                if (pq.isEmpty() && tmp.isEmpty()) break;
            }
            for (int c : tmp) {
                pq.offer(c);
            }
        }
        return res;
    }

    void test(String tasks, int n, int expected) {
        assertEquals(expected, leastInterval(tasks.toCharArray(), n));
        assertEquals(expected, leastInterval2(tasks.toCharArray(), n));
        assertEquals(expected, leastInterval3(tasks.toCharArray(), n));
        assertEquals(expected, leastInterval4(tasks.toCharArray(), n));
    }

    @Test
    public void test() {
        test("AAABBB", 2, 8);
        test("AAABBB", 0, 6);
        test ("GCAHAGGFGJHCAGEAHEFDBDHHEGFBCGFHJFACGDIJAGDFBFHIGJGHFEHJCEHFCEF"
              + "HHIGAGDCBIDBCJIBGCHDIABAJCEBFBJJDDHIIBAEHJJAJEHGBFCHCBJBAHBDIF"
              + "AEJHCEGFGBGCGAHEFHFCGBIEBJDBBGCAJBJJFJCAGJEGJCDDAIAJFHJDDDCEDD"
              + "FBAJDIHBAFEBJAHDEIBHCCCGCBEAGHHAIABADAIECCDABHDECAHBIABEHCBADH"
              + "EJBJABGJJFFHIAHFCHDHCCEIGJHDEIJCCHJCGIEDEHJAHDABFIFJJHDICGJCCD"
              + "BEBEBGBACFEHBDCHFAIAEJFAEBIGHDBFDBIBEDIDFAEHBIGFDEBECCCJJCHIBH"
              + "FHFDJDDHHCDAJDFDGBIFJJCCIFGFCEGEFDAIIHGHHAJDJGFGEEAHBGAJJEIHAG"
              + "ECDIBEAGACEBJCBADJEJIFFCBIHCFBCGDAABFCDBIIHHJAFJFJFHGFDJGIEBCG"
              + "IFFJHHGAAJCGFBAAEEAEIGFDBIFABJFFJBFJFJFIEJHDGGDFGBJFJAJEGHIEGD"
              + "IBDJAAGAIIAAIIHECAGIFFCDJJIAAFCJGCCHEAHFBJGIAAHGBEGDICGJCCIHBD"
              + "JHBJHBFJEJAGHBEHBFFHEBEGHJGJBHCHAABEIHBIDJJCDGIJGJDFJEFDEBDBCB"
              + "BCCIFDEIGGIBHGJAAHIIHAIFCDACGEGEEHDCGDIAGGDAHHIFEIADHBBGICGBII"
              + "DFFCCAIEAEJAHCDACBGHGJGIHBACHIDDCFGBHEBBHCBGGCFBEJBBIDHDIIAAHG"
              + "FBJFDEGFAGGDABBBJAFHHDCJIAHGCJIFJCAECHJHHFGEACFJHDGGDDCBHBCEFB"
              + "DJHJJJAFFDEFCIBHHDEAIABFGFFIEEGAIDFCHECGHFFHJHGAEHBGGDDDFIAFFD"
              + "EHJEDDAJFEEEFIDAFFJEIJDDGACGGIEGEHEDEJBGIJCHCCAABCGBDIDEHJJBFE"
              + "JHHIGBD", 1, 1000);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
