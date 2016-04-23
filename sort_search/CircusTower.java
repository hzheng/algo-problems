import java.lang.reflect.Array;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 11.7:
 * A circus is designing a tower routine consisting of people standing atop one
 * another's shoulders. Each person must be both shorter and lighter than the
 * person below him. Given the heights and weights of each person in the circus,
 * compute the largest possible number of people in such a tower.
 */
public class CircusTower {
    static class Person implements Comparable<Person> {
        private int h;
        private int w;

        public Person(int h, int w) {
            this.h = h;
            this.w = w;
        }

        @Override
        public int compareTo(Person second) {
            if (h != second.h) {
                return h - second.h;
            } else {
                return w - second.w;
            }
        }

        /** Returns true if <code>this</code> should be lined up before
         * <code>other</code>. Note that it's possible that this.isBefore(other)
         * and other.isBefore(this) are both false. This is different from the
         * compareTo method, where if a < b then b > a. */
        public boolean isBefore(Person other) {
            // return (h < other.h) && (w < other.w);
            return (h <= other.h) && (w <= other.w);
        }

        @Override
        public String toString() {
            return "(" + h + ", " + w + ")";
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Person)) return false;
            Person that = (Person)other;
            return h == that.h && w == that.w;
        }

        @Override
        public int hashCode() {
            return Objects.hash(h, w);
        }
    } // class Person

    // time complexity: O(N ^ 2), space complexity: O(N)
    @SuppressWarnings("unchecked")
    public static List<Person> buildTower(List<Person> people) {
        Collections.sort(people);

        final int size = people.size();
        // cached stores each person's best result with him at the very bottom
        List<Person>[] cached = (List<Person>[]) new List[size];
        int maxCount = 0;
        List<Person> maxResult = null;
        for (int index = 0; index < size; index++) {
            int beforeCount = 0;
            List<Person> beforeResult = null;
            Person curPerson = people.get(index);
            // given a person, check all that's possible on his top
            for (int i = 0; i < index; ++i) {
                if (people.get(i).w <= curPerson.w) {
                    List<Person> rs = cached[i];
                    if (rs.size() > beforeCount) {
                        beforeCount = rs.size();
                        beforeResult = rs;
                    }
                }
            }
            List<Person> result = cached[index] = new ArrayList<Person>();
            if (beforeResult != null) {
                result.addAll(beforeResult);
            }
            result.add(curPerson);
            if (++beforeCount > maxCount) {
                maxCount = beforeCount;
                maxResult = result;
            }
        }
        return maxResult;
    }

    // from the book
    // Returns longer sequence
    private static List<Person> seqWithMaxLength(List<Person> seq1, List<Person> seq2) {
        if (seq1 == null) {
            return seq2;
        } else if (seq2 == null) {
            return seq1;
        }
        return seq1.size() > seq2.size() ? seq1 : seq2;
    }

    private static void longestIncreasingSubsequence(List<Person> array, List<Person>[] solutions, int current_index) {
        if (current_index >= array.size() || current_index < 0) {
            return;
        }
        Person current_element = array.get(current_index);

        // Find longest sequence that we can append current_element to
        List<Person> best_sequence = null;
        for (int i = 0; i < current_index; i++) {
            if (array.get(i).isBefore(current_element)) { // If current_element is bigger than list tail
                best_sequence = seqWithMaxLength(best_sequence, solutions[i]); // Set best_sequence to our new max
            }
        }

        // Append current_element
        List<Person> new_solution = new ArrayList<Person>();
        if (best_sequence != null) {
            new_solution.addAll(best_sequence);
        }
        new_solution.add(current_element);

        // Add to list and recurse
        solutions[current_index] = new_solution;
        longestIncreasingSubsequence(array, solutions, current_index + 1);
    }

    @SuppressWarnings("unchecked")
    private static List<Person> longestIncreasingSubsequence(List<Person> array) {
        List<Person>[] solutions = new List[array.size()];
        longestIncreasingSubsequence(array, solutions, 0);

        List<Person> best_sequence = null;
        for (int i = 0; i < array.size(); i++) {
            best_sequence = seqWithMaxLength(best_sequence, solutions[i]);
        }

        return best_sequence;
    }

    public static List<Person> buildTower2(List<Person> people) {
        Collections.sort(people);
        return longestIncreasingSubsequence(people);
    }

    private List<Person> test(Function<List<Person>, List<Person> > buildTower,
                              String name, List<Person> people) {
        System.out.println("testing " + name);
        long t1 = System.nanoTime();
        List<Person> result = buildTower.apply(people);
        if (people.size() < 10) {
            System.out.println("===input===");
            for (Person person : people) {
                System.out.println(person);
            }
            System.out.println("===output===");
            for (Person person : result) {
                System.out.println(person);
            }
        }
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return result;
    }

    private void test(List<Person> people) {
        List<Person> rs1 = test(CircusTower::buildTower, "buildTower", people);
        List<Person> rs2 = test(CircusTower::buildTower2, "buildTower2", people);
        if (rs1.size() != rs2.size()) {
            assertEquals(rs1, rs2);
        }
    }

    private List<Person> createPeople(int[][] data) {
        Person[] people = new Person[data.length];
        for (int i = 0; i < data.length; ++i) {
            int[] sizes = data[i];
            people[i] = new Person(sizes[0], sizes[1]);
        }
        return Arrays.asList(people);
    }

    private void test(int[][] data) {
        test(createPeople(data));
    }

    @Test
    public void test1() {
        test(new int[][] {
            {65, 60}, {70, 150}, {56, 90}, {75, 190}, {60, 95}, {68, 110},
            {35, 65}, {40, 60}, {45, 63}
        });
    }

    @Test
    public void test2() {
        test(new int[][] {
            {65, 60}, {70, 150}, {56, 90}, {75, 190}, {60, 95}, {68, 110},
            {35, 65}, {40, 60}, {45, 63}, {90, 100}, {100, 100}
        });
    }

    private int ran(int max) {
        return ThreadLocalRandom.current().nextInt(1, max + 1);
    }

    @Test
    public void test3() {
        // int N = 10000; // buildTower <0.5s, while buildTower2 StackOverflow
        int N = 1000; // no big difference
        for (int times = 10; times > 0; times--) {
            System.out.println("===testing " + N + " people===");
            List<Person> people = new ArrayList<Person>();
            for (int i = 0; i < N; ++i) {
                int j = 10 + i;
                people.add(new Person(ran(150), ran(50)));
            }
            test(people);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CircusTower");
    }
}
