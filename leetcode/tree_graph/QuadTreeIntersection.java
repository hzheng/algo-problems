import org.junit.Test;
import static org.junit.Assert.*;

// LC558: https://leetcode.com/problems/quad-tree-intersection/
//
// The quad tree is used to represent a N * N boolean grid. For each node, it
// will be subdivided into four children nodes until the values in the region it
// represents are all the same. Each node has another two boolean attributes:
// isLeaf and val. isLeaf is true if and only if the node is a leaf node. The
// val attribute for a leaf node contains the value of the region it represents.
// Implement a function that will take two quadtrees and return a quadtree that
// represents the logical OR of the two trees.
public class QuadTreeIntersection {
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
    // beats 82.44%(4 ms for 58 tests)
    public Node intersect(Node tree1, Node tree2) {
        if (tree1.isLeaf) return tree1.val ? tree1 : tree2;
        if (tree2.isLeaf) return tree2.val ? tree2 : tree1;

        Node tl = intersect(tree1.topLeft, tree2.topLeft);
        Node tr = intersect(tree1.topRight, tree2.topRight);
        Node bl = intersect(tree1.bottomLeft, tree2.bottomLeft);
        Node br = intersect(tree1.bottomRight, tree2.bottomRight);
        if (tl.val == tr.val && tl.val == bl.val && tl.val == br.val
            && tl.isLeaf && tr.isLeaf && bl.isLeaf && br.isLeaf) {
            return new Node(tl.val, true, null, null, null, null);
        }
        return new Node(false, false, tl, tr, bl, br);
    }

    @Test
    public void test() {}

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
