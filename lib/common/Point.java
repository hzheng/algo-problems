package common;

import java.util.Objects;

public class Point implements Comparable<Point> {
    public int x;
    public int y;

    public Point() {
    }

    public Point(int a, int b) {
        x = a;
        y = b;
    }

    public int compareTo(Point other) {
        return x != other.x ? x - other.x : y - other.y;
    }

    public boolean equals(Object other) {
        return compareTo((Point)other) == 0;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
