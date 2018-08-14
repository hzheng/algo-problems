import org.junit.Test;
import static org.junit.Assert.*;

// LC478: https://leetcode.com/problems/generate-random-point-in-a-circle/
//
// Given the radius and x-y positions of the center of a circle, write a 
// function randPoint which generates a uniform random point in the circle.
public class RandomPointInCircle {
    static interface IRandomPointInCircle {
        public double[] randPoint();
    }

    // beats 33.87%(260 ms for 8 tests)
    static class RandomPointInCircle1 implements IRandomPointInCircle {
        private double radius;
        private double centerX;
        private double centerY;

        public RandomPointInCircle1(double radius, double x_center,
                                    double y_center) {
            this.radius = radius;
            this.centerX = x_center;
            this.centerY = y_center;
        }

        public double[] randPoint() {
            double len = Math.sqrt(Math.random()) * radius;
            double degree = Math.random() * 2 * Math.PI;
            double x = centerX + len * Math.cos(degree);
            double y = centerY + len * Math.sin(degree);
            return new double[] { x, y };
        }
    }

    // void test(, int expected) {
    // assertEquals(expected, f());
    // }

    // TODO: add tests
    @Test
    public void test() {
    // test();
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
