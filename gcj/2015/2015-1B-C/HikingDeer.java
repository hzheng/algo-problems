import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/8224486/dashboard#s=p1
// Round 1B 2015: Problem C - Hiking Deer
//
// Herbert the deer is going for a hike: one clockwise loop around a circular
// trail, starting at degree zero. Herbert has perfect control over his speed,
// which can be any nonnegative value at any time -- he can change his speed
// instantaneously whenever he wants. When Herbert reaches his starting point
// again, the hike is over. The trail is also used by human hikers, who also
// walk clockwise around the trail. Each hiker has a starting point and moves at
// her own constant speed. Humans continue to walk around and around the trail
// forever. Herbert does not like to have encounters with hikers. Herbert can
// have multiple separate encounters with the same hiker. If more than one hiker
// is encountered at the same instant, all of them count as separate encounters.
// Any encounter at the exact instant that Herbert finishes his hike still
// counts as an encounter. Herbert knows the starting position and speed of each
// hiker. What is the minimum encounters with hikers that he can possibly have?
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow. Each begins with one line with an integer N, and is followed by N
// lines, each of which represents a group of hikers starting at the same
// position on the trail. The ith of these lines has three space-separated
// integers: a starting position Di (representing Di/360ths of the way around
// the trail from the deer's starting point), the number Hi of hikers in the
// group, and Mi, the amount of time (in minutes) it takes for the fastest hiker
// in that group to make each complete revolution around the circle. The other
// hikers in that group each complete a revolution in Mi+1, Mi+2, ..., Mi+Hi-1
// minutes. Herbert always starts at position 0, and no group of hikers does.
// Multiple groups of hikers may begin in the same place, but no two hikers will
// both begin in the same place and have the same speed.
// Output
// For each test case, output one line containing "Case #x: y", where x is the
// test case number (starting from 1) and y is the minimum number of encounters
// with hikers that the deer can have.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ Di ≤ 359.
// 1 ≤ N ≤ 1000.
// 1 ≤ Hi.
// 1 ≤ Mi ≤ 10^9.
// Small dataset 1
// The total number of hikers in each test case will not exceed 2.
// Small dataset 2
// The total number of hikers in each test case will not exceed 10.
// Large dataset
// The total number of hikers in each test case will not exceed 500000.
public class HikingDeer {
    // Heap + Stack + Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // large dataset: <15 sec
    public static int minEncounter(int[][] hikerGroups) {
        List<Hiker> hikers = new ArrayList<>();
        for (int[] group : hikerGroups) {
            for (int i = 0; i < group[1]; i++) {
                hikers.add(new Hiker(group[0], (long)group[2] + i));
            }
        }
        Collections.sort(hikers);
        PriorityQueue<Double> arrivals = new PriorityQueue<>(
            (p, q) -> Double.compare(q, p));
        int n = hikers.size();
        Hiker hiker = hikers.get(0);
        for (int i = 1; i < n; i++) {
            arrivals.offer(hiker.finishTime + i * hiker.time);
        }
        for (int i = 1; i < n; i++) {
            hiker = hikers.get(i);
            for (double t = hiker.finishTime + hiker.time; ; t += hiker.time) {
                if (t < arrivals.peek()) {
                    arrivals.poll();
                    arrivals.offer(t);
                } else break;
            }
        }
        ArrayDeque<Double> stack = new ArrayDeque<>();
        while (!arrivals.isEmpty()) {
            stack.push(arrivals.poll());
        }
        int minMeet = n - 1;
        for (int i = 1, surpassed = 0; i < n && surpassed < minMeet; i++) {
            for (double time = hikers.get(i).finishTime;; surpassed++) {
                Double t = stack.peek();
                if (t == null) return Math.min(minMeet, surpassed);
                if (t > time || surpassed >= minMeet) break;

                stack.pop();
            }
            minMeet = Math.min(minMeet, surpassed + n - 1 - i);
        }
        return minMeet;
    }

    // Heap + Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // large dataset: <4 sec
    public static int minEncounter2(int[][] hikerGroups) {
        List<Hiker> hikers = new ArrayList<>();
        PriorityQueue<Hiker> pq = new PriorityQueue<>(
            (a, b) -> Double.compare(a.toZero, b.toZero));
        for (int[] group : hikerGroups) {
            for (int i = 0; i < group[1]; i++) {
                Hiker hiker = new Hiker(group[0], (long)group[2] + i);
                hikers.add(hiker);
                hiker.toZero += hiker.time;
                pq.offer(hiker);
            }
        }
        Collections.sort(hikers);
        int n = hikers.size();
        int minMeet = n - 1;
        for (int i = 1, surpassed = 0; i < n && surpassed < minMeet; i++) {
            for (Double time = hikers.get(i).finishTime;; surpassed++) {
                Hiker cur = pq.peek();
                if (cur == null) return Math.min(minMeet, surpassed);
                if (cur.toZero > time || surpassed >= minMeet) break;

                cur.toZero += cur.time;
                pq.poll();
                pq.offer(cur);
            }
            minMeet = Math.min(minMeet, surpassed + n - 1 - i);
        }
        return minMeet;
    }

    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // large dataset: <5 sec
    public static int minEncounter3(int[][] hikerGroups) {
        PriorityQueue<Hiker> pq = new PriorityQueue<>(new Comparator<Hiker>() {
            public int compare(Hiker a, Hiker b) {
                int diff = Double.compare(a.toZero, b.toZero);
                return diff != 0 ? diff : (a.firstTime() ? 1 : -1);
            }
        });
        for (int[] group : hikerGroups) {
            for (int i = 0; i < group[1]; i++) {
                pq.offer(new Hiker(group[0], (long)group[2] + i));
            }
        }
        int n = pq.size();
        int minMeet = n;
        for (int i = n * 2, meet = n; i >= 0; i--) {
            Hiker cur = pq.poll();
            meet += cur.firstTime() ? -1 : 1;
            minMeet = Math.min(minMeet, meet);
            cur.toZero += cur.time;
            pq.offer(cur);
        }
        return minMeet;
    }

    // Heap + Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // large dataset: <5 sec
    public static int minEncounter3_2(int[][] hikerGroups) {
        PriorityQueue<Hiker> pq = new PriorityQueue<>(new Comparator<Hiker>() {
            public int compare(Hiker a, Hiker b) {
                int diff = Long.compare(a.timePos, b.timePos);
                return diff != 0 ? diff : (a.isFirstTime() ? 1 : -1);
            }
        });
        for (int[] group : hikerGroups) {
            for (int i = 0; i < group[1]; i++) {
                pq.offer(new Hiker(group[0], (long)group[2] + i));
            }
        }
        int n = pq.size();
        int minMeet = n;
        for (int i = n * 2, meet = n; i >= 0; i--) {
            Hiker cur = pq.poll();
            meet += cur.isFirstTime() ? -1 : 1;
            minMeet = Math.min(minMeet, meet);
            cur.timePos += cur.time * 360;
            pq.offer(cur);
        }
        return minMeet;
    }

    // Sort + Binary Search
    // large dataset: 2 min
    public static int minEncounter0(int[][] hikerGroups) {
        long shortestTime = Long.MAX_VALUE;
        int total = 0;
        for (int[] group : hikerGroups) {
            shortestTime = Math.min(shortestTime, group[2]);
            total += group[1];
        }
        long maxTime = shortestTime * (total + 1);
        int minMeet = total - 1;
        int ignore = 0;
        List<Hiker> hikers = new ArrayList<>();
        for (int[] group : hikerGroups) {
            for (int i = 0; i < group[1]; i++) {
                Hiker hiker = new Hiker(group[0], (long)group[2] + i);
                if (hiker.endAfter(maxTime)) {
                    ignore += group[1] - i;
                    break;
                } else {
                    hikers.add(hiker);
                }
            }
        }
        Collections.sort(hikers);
        minMeet -= ignore;
        int n = hikers.size();
        int high = n - 1;
        int low = 0;
        outer : while (low + 1 < high) {
            int mid = (high + low) / 2;
            long surpassed = 0;
            Hiker cur = hikers.get(mid);
            for (int j = 0; j < mid; j++) {
                surpassed += hikers.get(j).surpass(cur);
                if (surpassed > n - 1) { // > mid + 1
                    high = mid;
                    continue outer;
                }
            }
            if (mid - surpassed > low) {
                low = (int)(mid - surpassed);
                // minMeet = (int)Math.min(minMeet, n - 1 - low);
            } else break;
        }
        return count(hikers, low, high, minMeet) + ignore;
    }

    private static int count(List<Hiker> hikers, int low, int high,
                             int minMeet) {
        for (int i = high, n = hikers.size(); i >= low; i--) {
            long meet = n - 1 - i;
            Hiker cur = hikers.get(i);
            long surpass = 0;
            for (int j = 0; j < i; j++) {
                surpass += hikers.get(j).surpass(cur);
                if (meet + surpass >= minMeet) break;
            }
            // if (surpass <= n - 1 - i) break;
            minMeet = (int)Math.min(minMeet, meet + surpass);
            if (meet >= minMeet) break;
        }
        return minMeet;
    }

    private static class Hiker implements Comparable<Hiker> {
        int pos;
        long time;
        long timePos;
        double toZero;
        double finishTime;
        Hiker(int pos, long time) {
            this.pos = pos;
            this.time = time;
            finishTime = toZero = time / 360d * (360 - pos);
            timePos = time * (360L - pos);
        }

        boolean isFirstTime() {
            return timePos == time * (360L - pos);
        }

        boolean firstTime() {
            return toZero == finishTime;
        }

        boolean endAfter(long maxTime) {
            return time * (360L - pos) > 360L * maxTime;
        }

        long surpass(Hiker other) {
            if (time >= other.time) return 0;
            return (long)((pos + other.time * (360d - other.pos) / time) /
                          360) - 1;
        }

        public int compareTo(Hiker other) {
            int res = Long.compare(time * (360 - pos),
                                   other.time * (360 - other.pos));
            return res != 0 ? res : Long.compare(time, other.time);
        }

        public String toString() {
            return "(" + pos + "," + time + ")";
        }
    }

    void test(int[][] hikerGroups, int expected) {
        assertEquals(expected, minEncounter(hikerGroups));
        assertEquals(expected, minEncounter2(hikerGroups));
        assertEquals(expected, minEncounter3(hikerGroups));
        assertEquals(expected, minEncounter3_2(hikerGroups));
        assertEquals(expected, minEncounter0(hikerGroups));
    }

    @Test
    public void test() {
        test(new int[][] {{90, 1, 4}, {180, 1, 2}}, 1);
        test(new int[][] {{180, 2, 1}}, 0);
        test(new int[][] {{1, 1, 12}, {359, 1, 12}, {2, 1, 12}, {358, 1, 12}},
             0);
        test(new int[][] {{180, 1, 4522}, {285, 1, 54264}, {288, 1, 56520},
                          {3, 1, 11400}, {74, 1, 6300}, {20, 1, 11970},
                          {255, 1, 38760}, {180, 1, 22610},
                          {270, 1, 45216}, {300, 1, 67824}}, 3);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz =
                new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n",
                       clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }
        try {
            in = new Scanner(new File(args[0]));
            if (args.length > 1) {
                out = new PrintStream(args[1]);
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int N = in.nextInt();
        int[][] hikerGroups = new int[N][3];
        for (int i = 0; i < N; i++) {
            hikerGroups[i] =
                new int[] {in.nextInt(), in.nextInt(), in.nextInt()};
        }
        out.println(minEncounter3_2(hikerGroups));
    }
}
