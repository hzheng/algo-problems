import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC855: https://leetcode.com/problems/exam-room/
//
// In an exam room, there are N seats in a single row, numbered 0, 1, ..., N-1.
// When a student enters the room, they must sit in the seat that maximizes the
// distance to the closest person.  If there are multiple such seats, they sit 
//in the seat with the lowest number. Return a class ExamRoom(int N) that 
// exposes two functions: ExamRoom.seat() returning an int representing what
// seat the student sat in, and ExamRoom.leave(int p) representing that the
// student in seat number p now leaves the room.  It is guaranteed that any 
// calls to ExamRoom.leave(p) have a student sitting in seat p.
public class ExamRoom {
    // Hash Table + SortedSet
    // beats 66.42%(103 ms for 128 tests)
    class ExamRoom1 {
        class Interval implements Comparable<Interval> {
            int start;
            int end;

            public Interval(int start, int end) {
                this.start = start;
                this.end = end;
            }

            public Interval[] split() {
                if (start < 0) {
                    return new Interval[]
                           {new Interval(-1, 0), new Interval(0, end)};
                }
                if (end == N) {
                    return new Interval[]
                           {new Interval(start, N - 1), new Interval(N - 1, N)};
                }
                return new Interval[]
                       {new Interval(start, (start + end) / 2),
                        new Interval((start + end) / 2, end) };
            }

            private int value() {
                if (end == N) return N - 1 - start;

                return (start >= 0) ? (end - start) / 2 : end;
            }

            public int compareTo(Interval other) {
                int diff = other.value() - value();
                return diff != 0 ? diff : start - other.start;
            }

            @Override
            public int hashCode() {
                return Integer.hashCode(start);
            }

            @Override
            public boolean equals(Object other) {
                return start == ((Interval)other).start;
            }

            @Override
            public String toString() {
                return "(" + start + "," + end + ")";
            }
        }

        private NavigableSet<Interval> gaps = new TreeSet<>();
        private Map<Integer, Interval> starts = new HashMap<>();
        private Map<Integer, Interval> ends = new HashMap<>();
        private int N;

        public ExamRoom1(int N) {
            this.N = N;
            addGap(new Interval(-1, N));
        }

        private void addGap(Interval i) {
            gaps.add(i);
            starts.put(i.start, i);
            ends.put(i.end, i);
        }

        // time complexity: O(log(P))
        public int seat() {
            Interval gap = gaps.pollFirst();
            Interval[] halves = gap.split();
            for (Interval i : halves) {
                addGap(i);
            }
            return halves[0].end;
        }

        // time complexity: O(log(P))
        public void leave(int p) {
            Interval start = starts.get(p);
            gaps.remove(start);
            Interval end = ends.get(p);
            gaps.remove(end);
            addGap(new Interval(end.start, start.end));
        }
    }

    // Hash Table + SortedSet
    // beats 42.35%(113 ms for 128 tests)
    class ExamRoom2 {
        private int N;
        private SortedSet<Integer> students = new TreeSet<>();
    
        public ExamRoom2(int N) {
            this.N = N;
        }
    
        // time complexity: O(P)
        public int seat() {
            int student = 0;
            if (!students.isEmpty()) {
                int dist = students.first();
                Integer prev = null;
                for (Integer s : students) {
                    if (prev != null) {
                        int d = (s - prev) / 2;
                        if (d > dist) {
                            dist = d;
                            student = prev + d;
                        }
                    }
                    prev = s;
                }
                if (N - 1 - students.last() > dist) {
                    student = N - 1;
                }
            }
            students.add(student);
            return student;
        }
    
        // time complexity: O(log(P))
        public void leave(int p) {
            students.remove(p);
        }
    }

    void test1(String className) throws Exception {
        Object outerObj =
            new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] { className, "seat", "seat", "seat", "seat",
                            "leave", "seat" },
             new Object[][] { new Object[] { outerObj , 10}, 
                              new Integer[] { }, new Integer[] { },
                              new Integer[] { }, new Integer[] { },
                              new Integer[] { 4 }, new Integer[] { } },
             new Integer[] { null, 0, 9, 4, 2, null, 5 });
        test(new String[] { className, "seat", "seat", "seat", "leave", "leave" },
             new Object[][] { new Object[] { outerObj , 10}, 
                              new Integer[] { }, new Integer[] { },
                              new Integer[] { }, new Integer[] { 0 },
                              new Integer[] { 4 } },
             new Integer[] { null, 0, 9, 4, null, null });
        test(new String[] { className, "seat", "seat", "seat", "seat",
                            "leave", "leave", "seat" },
             new Object[][] { new Object[] { outerObj , 4}, 
                              new Integer[] { }, new Integer[] { },
                              new Integer[] { }, new Integer[] { },
                              new Integer[] { 1 }, new Integer[] { 3 },
                              new Integer[] { } },
             new Integer[] { null, 0, 3, 1, 2, null, null, 1 });
    }

    void test(String[] methods, Object[][] args,
              Integer[] expected) throws Exception {
        final String name =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        final Object[] VOID = new Object[] {};
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(
                    obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("ExamRoom1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
