import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC362: https://leetcode.com/problems/design-hit-counter
//
// Design a hit counter which counts the number of hits received in the past 5 minutes.
// Each function accepts a timestamp parameter and you may assume that calls are
// being made to the system in chronological order. You may assume that the earliest
// timestamp starts at 1.
// It is possible that several hits arrive roughly at the same time.
public class HitCounter {
    interface IHitCounter {
        /** Record a hit. */
        public void hit(int timestamp);

        /** Return the number of hits in the past 5 minutes. */
        public int getHits(int timestamp);
    }

    // SortedMap
    // beats 26.51%(122 ms for 24 tests)
    static class HitCounter1 implements IHitCounter {
        private NavigableMap<Integer, Integer> map = new TreeMap<>();

        // time complexity: O(1)
        public void hit(int timestamp) {
            map.put(timestamp, map.getOrDefault(timestamp, 0) + 1);
        }

        // time complexity: O(log(N))
        public int getHits(int timestamp) {
            int hits = 0;
            for (int hit : map.subMap(timestamp - 299, timestamp + 1).values()) {
                hits += hit;
            }
            return hits;
        }
    }

    // Hash Table
    // Time Limit Exceeded
    static class HitCounter2 implements IHitCounter {
        private Map<Integer, Integer> map = new HashMap<>();

        public HitCounter2() {
            map.put(0, 0);
        }

        public void hit(int timestamp) {
            map.put(timestamp, fill(timestamp) + 1);
        }

        public int getHits(int timestamp) {
            return fill(timestamp) - map.get(Math.max(0, timestamp - 300));
        }

        private int fill(int timestamp) {
            Integer prev = map.get(timestamp);
            if (prev != null) return prev;

            int t = timestamp;
            for (; !map.containsKey(t); t--) {}
            int hit = map.get(t);
            for (int i = timestamp; i > t; i--) {
                map.put(i, hit);
            }
            return hit;
        }
    }

    // List
    // beats 33.42%(118 ms for 24 tests)
    static class HitCounter3 implements IHitCounter {
        private List<int[]> list = new LinkedList<>();

        public HitCounter3() {
            list.add(new int[2]);
        }

        // time complexity: O(1)
        public void hit(int timestamp) {
            int[] first = list.get(0);
            if (first[0] == timestamp) {
                first[1]++;
            } else {
                list.add(0, new int[] {timestamp, 1});
            }
        }

        // time complexity: O(1)
        public int getHits(int timestamp) {
            int hits = 0;
            int low = Math.max(0, timestamp - 300);
            for (int[] data : list) {
                if (data[0] > timestamp) continue;
                if (data[0] <= low) break;

                hits += data[1];
            }
            return hits;
        }
    }

    // Queue
    // beats 24.96%(123 ms for 24 tests)
    static class HitCounter4 implements IHitCounter {
        private Queue<Integer> queue = new LinkedList<>();

        // time complexity: O(1)
        public void hit(int timestamp) {
            queue.offer(timestamp);
        }

        // drawbacks: 1. discard old data 2. 'get' is not immutable operation
        // time complexity: O(1)
        public int getHits(int timestamp) {
            while (!queue.isEmpty() && timestamp - queue.peek() >= 300) {
                queue.poll();
            }
            return queue.size();
        }
    }

    // List
    // beats 74.18%(102 ms for 24 tests)
    static class HitCounter5 implements IHitCounter {
        private List<Integer> list = new ArrayList<>();
        private int cur;

        // time complexity: O(1)
        public void hit(int timestamp) {
            list.add(timestamp);
            adjust(timestamp);
        }

        // time complexity: O(1)
        public int getHits(int timestamp) {
            adjust(timestamp);
            return list.size() - cur;
        }

        private void adjust(int timestamp) {
            for (int end = timestamp - 300; cur < list.size() && list.get(cur) <= end; cur++) {}
        }
    }

    // Array
    // beats 45.51%(112 ms for 24 tests)
    static class HitCounter6 implements IHitCounter {
        private static final int INTERVAL = 300;
        private int[] times = new int[INTERVAL];
        private int[] hits = new int[INTERVAL];

        // drawback: discard old data
        public void hit(int timestamp) {
            int index = timestamp % 300;
            if (times[index] == timestamp) {
                hits[index]++;
            } else {
                times[index] = timestamp;
                hits[index] = 1;
            }
        }

        public int getHits(int timestamp) {
            int res = 0;
            for (int i = 0; i < INTERVAL; i++) {
                if (timestamp - times[i] < INTERVAL) {
                    res += hits[i];
                }
            }
            return res;
        }
    }

    // Circular Array
    // beats 82.82%(99 ms for 24 tests)
    static class HitCounter7 implements IHitCounter {
        private static final int INTERVAL = 300;
        private int[] counts = new int[INTERVAL];
        private int cur = 0;
        private int lastTime = 0;
        private int hits = 0;

        // drawback: discard old data
        public void hit(int timestamp) {
            adjust(timestamp);
            counts[cur]++;
            hits++;
        }

        public int getHits(int timestamp) {
            adjust(timestamp);
            return hits;
        }

        private void adjust(int timestamp) {
            for (int i = Math.min(timestamp - lastTime, INTERVAL); i > 0; i--) {
                cur = (cur + 1) % INTERVAL;
                hits -= counts[cur];
                counts[cur] = 0;
            }
            lastTime = timestamp;
        }
    }

    private void test(IHitCounter counter) {
        counter.hit(1);
        counter.hit(2);
        counter.hit(3);
        assertEquals(3, counter.getHits(4));
        counter.hit(300);
        assertEquals(4, counter.getHits(300));
        assertEquals(3, counter.getHits(301));
        for (int i = 0; i < 10; i++) {
            counter.hit(302);
        }
        assertEquals(12, counter.getHits(302));
        assertEquals(11, counter.getHits(599));
        assertEquals(10, counter.getHits(600));
    }

    private void test2(IHitCounter counter) {
        counter.hit(1);
        counter.hit(2);
        counter.hit(3);
        assertEquals(3, counter.getHits(4));
        counter.hit(300);
        assertEquals(4, counter.getHits(300));
        assertEquals(3, counter.getHits(301));
        counter.hit(1466000001);
        counter.hit(1466000002);
        counter.hit(1466000003);
        assertEquals(3, counter.getHits(1466000004));
        counter.hit(1466000300);
        assertEquals(4, counter.getHits(1466000300));
        assertEquals(3, counter.getHits(1466000301));
    }

    @Test
    public void test() {
        test(new HitCounter1());
        test2(new HitCounter1());
        test(new HitCounter2());
        // test2(new HitCounter2());
        test(new HitCounter3());
        test2(new HitCounter3());
        test(new HitCounter4());
        test2(new HitCounter4());
        test(new HitCounter5());
        test2(new HitCounter5());
        test(new HitCounter6());
        test2(new HitCounter6());
        test(new HitCounter7());
        test2(new HitCounter7());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("HitCounter");
    }
}
