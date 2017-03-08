import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC379: https://leetcode.com/problems/design-phone-directory
//
// Design a Phone Directory which supports the following operations:
// get: Provide a number which is not assigned to anyone.
// check: Check if a number is available or not.
// release: Recycle or release a number.
public class PhoneDirectory {
    interface Directory {
        /** Provide a number which is not assigned to anyone.
           @return - Return an available number. Return -1 if none is available. */
        public int get();

        /** Check if a number is available or not. */
        public boolean check(int number);

        /** Recycle or release a number. */
        public void release(int number);
    }

    // SortedMap
    // beats 20.34%(644 ms for 18 tests)
    static class PhoneDirectory1 implements Directory {
        private int max;
        private NavigableMap<Integer, Integer> map = new TreeMap<>();

        public PhoneDirectory1(int maxNumbers) {
            max = maxNumbers;
            map.put(0, max - 1);
        }

        public int get() {
            if (map.isEmpty()) return -1;

            int start = map.firstKey();
            int end = map.remove(start);
            if (start < end) {
                map.put(start + 1, end);
            }
            return start;
        }

        public boolean check(int number) {
            Map.Entry<Integer, Integer> floor = map.floorEntry(number);
            return floor != null && floor.getValue() >= number;
        }

        public void release(int number) {
            Map.Entry<Integer, Integer> floor = map.floorEntry(number);
            if (floor != null && floor.getValue() >= number) return;

            int end = map.containsKey(number + 1) ? map.remove(number + 1) : number;
            int start = (floor != null && floor.getValue() + 1 == number) ? floor.getKey() : number;
            map.put(start, end);
        }
    }

    // Array
    // Time Limit Exceeded
    static class PhoneDirectory2 implements Directory {
        private boolean[] used = new boolean[0];
        private int next = -1;

        public PhoneDirectory2(int maxNumbers) {
            if (maxNumbers > 0) {
                used = new boolean[maxNumbers];
                next = 0;
            }
        }

        public int get() {
            if (next < 0) return -1;

            used[next] = true;
            int res = next++;
            for (next %= used.length; next != res; next = (next + 1) % used.length) {
                if (!used[next]) return res;
            }
            next = -1;
            return res;
        }

        public boolean check(int number) {
            return number >= 0 && number < used.length && !used[number];
        }

        public void release(int number) {
            if (number >= 0 && number < used.length) {
                used[number] = false;
                if (next < 0) {
                    next = number;
                }
            }
        }
    }

    // LinkedList
    // beats 85.71%(464 ms for 18 tests)
    static class PhoneDirectory3 implements Directory {
        class Node {
            int start;
            int end;
            Node next;
            Node(int start, int end) {
                this.start = start;
                this.end = end;
            }
        }

        private Node head = new Node(-1, -1);
        private int max;

        public PhoneDirectory3(int maxNumbers) {
            max = maxNumbers;
            if (maxNumbers > 0) {
                head.next = new Node(0, maxNumbers - 1);
            }
        }

        public int get() {
            Node node = head.next;
            if (node == null) return -1;

            int res = node.start;
            if (node.end == res) {
                head.next = node.next;
            } else {
                node.start++;
            }
            return res;
        }

        public boolean check(int number) {
            for (Node node = head.next; node != null; node = node.next) {
                if (node.start > number) return false;

                if (node.end >= number) return true;
            }
            return false;
        }

        public void release(int number) {
            if (number < 0 || number >= max) return;

            Node prev = head;
            for (Node node = head.next; node != null; prev = node, node = node.next) {
                if (node.start - 1 == number) {
                    node.start = number;
                    return;
                }
                if (node.start > number) {
                    if (node.start == number + 1) {
                        node.start++;
                    } else {
                        prev.next = new Node(number, number);
                        prev.next.next = node;
                    }
                    return;
                }
                if (node.end >= number) return;

                if (node.end + 1 == number) {
                    node.end++;
                    if (node.next != null && node.next.start == number) {
                        node.end = node.next.end;
                        node.next = node.next.next;
                    }
                    return;
                }
            }
            prev.next = new Node(number, number);
        }
    }

    // BitSet
    // beats 96.12%(372 ms for 18 tests)
    static class PhoneDirectory4 implements Directory {
        private BitSet flags;
        private int max;
        private int firstFree;

        public PhoneDirectory4(int maxNumbers) {
            max = maxNumbers;
            flags = new BitSet(maxNumbers);
        }

        public int get() {
            if (firstFree == max) return -1;

            int res = firstFree;
            flags.set(firstFree);
            firstFree = flags.nextClearBit(firstFree);
            return res;
        }

        public boolean check(int number) {
            return !flags.get(number);
        }

        public void release(int number) {
            if (flags.get(number)) {
                flags.clear(number);
                if (number < firstFree) {
                    firstFree = number;
                }
            }
        }
    }

    // Set + Queue
    // beats 68.79%(538 ms for 18 tests)
    static class PhoneDirectory5 implements Directory {
        private Set<Integer> used = new HashSet<>();
        private Queue<Integer> available = new LinkedList<>();
        private int max;

        public PhoneDirectory5(int maxNumbers) {
            max = maxNumbers;
            for (int i = 0; i < maxNumbers; i++) { // better offer when needed
                available.offer(i);
            }
        }

        public int get() {
            Integer res = available.poll();
            if (res == null) return -1;

            used.add(res);
            return res;
        }

        public boolean check(int number) {
            return number >= 0 && number < max && !used.contains(number);
        }

        public void release(int number) {
            if (used.remove(number)) {
                available.offer(number);
            }
        }
    }

    void test(Directory obj, int maxNumbers) {
        for (int i = 0; i < maxNumbers - 1; i++) {
            assertEquals(i, obj.get());
            assertTrue(obj.check(i + 1));
        }
        assertEquals(maxNumbers - 1, obj.get());
        assertFalse(obj.check(maxNumbers - 1));
        assertEquals(-1, obj.get());
        for (int i = 0; i < maxNumbers; i += 2) {
            obj.release(i);
        }
        for (int i = 0; i < maxNumbers; i++) {
            assertEquals(i % 2 == 0, obj.check(i));
        }
        for (int i = 1; i < maxNumbers; i += 2) {
            obj.release(i);
        }
        for (int i = 0; i < maxNumbers; i++) {
            assertTrue(obj.check(i));
        }
    }

    private void test(String className, int maxNumbers) {
        try {
            Class<?> clazz = Class.forName("PhoneDirectory$" + className);
            Constructor<?> ctor = clazz.getConstructor(int.class);
            test((Directory)ctor.newInstance(new Object[] {maxNumbers}), maxNumbers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test2(Directory obj) {
        assertTrue(obj.check(0));
        assertEquals(0, obj.get());
        assertFalse(obj.check(0));
        assertEquals(-1, obj.get());
        obj.release(0);
        assertTrue(obj.check(0));
        assertEquals(0, obj.get());
    }

    private void test3(Directory obj) {
        obj.release(1);
        assertEquals(0, obj.get());
        assertTrue(obj.check(1));
        assertTrue(obj.check(1));
        obj.release(1);
        assertTrue(obj.check(1));
        assertEquals(1, obj.get());
        assertFalse(obj.check(0));
        assertFalse(obj.check(1));
        assertFalse(obj.check(1));
    }

    private void test4(Directory obj) {
        assertEquals(0, obj.get());
        obj.release(0);
        assertTrue(obj.check(0));
    }

    private void test0(String className) {
        try {
            Class<?> clazz = Class.forName("PhoneDirectory$" + className);
            Constructor<?> ctor = clazz.getConstructor(int.class);
            test2((Directory)ctor.newInstance(new Object[] {1}));
            test3((Directory)ctor.newInstance(new Object[] {2}));
            test4((Directory)ctor.newInstance(new Object[] {2}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test(String className) {
        test(className, 5);
        test(className, 50);
        test0(className);
    }

    @Test
    public void test() {
        test("PhoneDirectory1");
        test("PhoneDirectory2");
        test("PhoneDirectory3");
        test("PhoneDirectory4");
        test("PhoneDirectory5");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PhoneDirectory");
    }
}
