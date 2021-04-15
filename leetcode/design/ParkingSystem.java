import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1603: https://leetcode.com/problems/design-parking-system/
//
// Design a parking system for a parking lot. The parking lot has three kinds of parking spaces:
// big, medium, and small, with a fixed number of slots for each size.
// Implement the ParkingSystem class:
// ParkingSystem(int big, int medium, int small) Initializes object of the ParkingSystem class. The
// number of slots for each parking space are given as part of the constructor.
// bool addCar(int carType) Checks whether there is a parking space of carType for the car that
// wants to get into the parking lot. carType can be of three kinds: big, medium, or small, which
// are represented by 1, 2, and 3 respectively. A car can only park in a parking space of its
// carType. If there is no space available, return false, else park the car in that size space and
// return true.
//
// Constraints:
// 0 <= big, medium, small <= 1000
// carType is 1, 2, or 3
// At most 1000 calls will be made to addCar
public class ParkingSystem {
    // 66 ms(99.87%), 39.4 MB(77.52%) for 102 tests
    static class ParkingSystem1 {
        private int[] cars;

        public ParkingSystem1(int big, int medium, int small) {
            cars = new int[]{big, medium, small};
        }

        public boolean addCar(int carType) {
            return cars[carType - 1]-- > 0;
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "addCar", "addCar", "addCar", "addCar"},
             new Object[][] {{1, 1, 0}, {1}, {2}, {3}, {1}},
             new Object[] {null, true, true, false, false});
    }

    void test(String[] methods, Object[][] args, Object[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
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
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("ParkingSystem1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
