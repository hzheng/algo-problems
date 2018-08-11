import org.junit.Test;
import static org.junit.Assert.*;

// LC427: https://leetcode.com/problems/construct-quad-tree/
//
// We want to use quad trees to store an N x N boolean grid. Each cell in the
// grid can only be true or false. The root node represents the whole grid. For
// each node, it will be subdivided into four children nodes until the values in
// the region it represents are all the same. Each node has another two boolean
// attributes : isLeaf and val. The val attribute for a leaf node contains the
// value of the region it represents.
// Your task is to use a quad tree to represent a given grid.
// Note: N is less than 1000 and guaranteened to be a power of 2.
public class ConstructQuadTree {
    class Node {
        public boolean val;
        public boolean isLeaf;
        public Node topLeft;
        public Node topRight;
        public Node bottomLeft;
        public Node bottomRight;

        public Node() {}

        public Node(boolean _val, boolean _isLeaf, Node _topLeft,
                    Node _topRight, Node _bottomLeft, Node _bottomRight) {
            val = _val;
            isLeaf = _isLeaf;
            topLeft = _topLeft;
            topRight = _topRight;
            bottomLeft = _bottomLeft;
            bottomRight = _bottomRight;
        }
    };

    // Recursion
    // beats 84.18%(3 ms for 20 tests)
    public Node construct(int[][] grid) {
        return construct(grid, 0, 0, grid.length);
    }

    private Node construct(int[][] grid, int x, int y, int n) {
        Node node = new Node(grid[x][y] != 0, true, null, null, null, null);
        outer : for (int i = x; i < x + n; i++) {
            for (int j = y; j < y + n; j++) {
                if (grid[i][j] != grid[x][y]) {
                    node.isLeaf = false;
                    break outer;
                }
            }
        }
        if (!node.isLeaf) {
            node.topLeft = construct(grid, x, y, n / 2);
            node.topRight = construct(grid, x, y + n / 2, n / 2);
            node.bottomLeft = construct(grid, x + n / 2, y, n / 2);
            node.bottomRight = construct(grid, x + n / 2, y + n / 2, n / 2);
        }
        return node;
    }

    void test(int[][] grid) {
        System.out.println(construct(grid));
    }

    @Test
    public void test() {
        test(new int[][] { { 1, 1, 1, 1, 0, 0, 0, 0 }, { 1, 1, 1, 1, 0, 0, 0, 0 },
                           { 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1, 1 },
                           { 1, 1, 1, 1, 0, 0, 0, 0 }, { 1, 1, 1, 1, 0, 0, 0, 0 },
                           { 1, 1, 1, 1, 0, 0, 0, 0 }, { 1, 1, 1, 1, 0, 0, 0, 0 } });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
