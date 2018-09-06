import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC853: https://leetcode.com/problems/car-fleet/
//
// N cars are going to the same destination along a one lane road.  Each car i
// has a constant speed speed[i] (in miles per hour), and initial position[i]
// miles towards the target along the road. A car can never pass another car
// ahead of it, but it can catch up to it, and drive bumper to bumper at the
// same speed. The distance between these two cars is ignored - they are assumed
// to have the same position. A car fleet is some non-empty set of cars driving
// at the same position and same speed. If a car catches up to a car fleet right
//  at the destination point, it will still be considered as one car fleet.
// How many car fleets will arrive at the destination?
public class CarFleet {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 66.06%(28 ms for 44 tests)
    public int carFleet(int target, int[] position, int[] speed) {
        int n = speed.length;
        int[][] cars = new int[n][2];
        for (int i = 0; i < n; i++) {
            cars[i] = new int[] { position[i], speed[i] };
        }
        Arrays.sort(cars, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return b[0] - a[0];
            }
        });
        int fleets = 0;
        double prev = 0;
        for (int[] car : cars) {
            double time = (target - car[0]) / (double) car[1];
            if (time > prev) {
                fleets++;
                prev = time;
            }
        }
        return fleets;
    }

    // SortedMap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 78.48%(26 ms for 44 tests)
    public int carFleet2(int target, int[] position, int[] speed) {
        SortedMap<Integer, Double> map = new TreeMap<>();
        for (int i = 0; i < position.length; i++) {
            map.put(-position[i], (double)(target - position[i]) / speed[i]);
        }
        int fleets = 0;
        double prev = 0;
        for (double time : map.values()) {
            if (time > prev) {
                fleets++;
                prev = time;
            }
        }
        return fleets;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 44.55%(44 ms for 44 tests)
    public int carFleet3(int target, int[] position, int[] speed) {
        int n = position.length;
        Car[] cars = new Car[n];
        for (int i = 0; i < n; i++) {
            cars[i] = new Car(position[i],
                              (double) (target - position[i]) / speed[i]);
        }
        Arrays.sort(cars);
        int fleets = 0;
        int i = n;
        while (--i > 0) {
            if (cars[i].time < cars[i - 1].time) {
                fleets++;
            } else {
                cars[i - 1] = cars[i];
            }
        }
        return fleets + (i == 0 ? 1 : 0);
    }

    class Car implements Comparable<Car> {
        int position;
        double time;

        Car(int p, double t) {
            position = p;
            time = t;
        }

        public int compareTo(Car other) {
            return position - other.position;
        }
    }

    void test(int target, int[] position, int[] speed, int expected) {
        assertEquals(expected, carFleet(target, position, speed));
        assertEquals(expected, carFleet2(target, position, speed));
        assertEquals(expected, carFleet3(target, position, speed));
    }

    @Test
    public void test() {
        test(12, new int[] { 10, 8, 0, 5, 3 }, new int[] { 2, 4, 1, 1, 3 }, 3);
        test(10, new int[] { }, new int[] { }, 0);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
