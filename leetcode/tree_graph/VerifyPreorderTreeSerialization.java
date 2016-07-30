import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/
//
// One way to serialize a binary tree is to use pre-order traversal. When we
// encounter a non-null node, we record the node's value. If it is a null node,
// we record using a sentinel value such as #.
// Given a string of comma separated values, verify whether it is a correct
//  preorder traversal serialization of a binary tree. Find an algorithm without
//  reconstructing the tree.
public class VerifyPreorderTreeSerialization {
    // beats 24.43%(21 ms)
    public boolean isValidSerialization(String preorder) {
        Stack<Integer> stack = new Stack<>();
        String[] nodes = preorder.split(",");
        for (int i = 0; i < nodes.length; i++) {
            String node = nodes[i];
            if (!node.equals("#")) {
                stack.push(2);
                continue;
            }

            while (!stack.isEmpty()) {
                int top = stack.pop();
                if (--top > 0) {
                    stack.push(top);
                    break;
                }
            }
            if (stack.isEmpty()) return i + 1 == nodes.length;
        }
        return stack.isEmpty();
    }

    // graph theory
    // https://discuss.leetcode.com/topic/35976/7-lines-easy-java-solution
    // beats 52.68%(12 ms)
    public boolean isValidSerialization2(String preorder) {
        int diff = 1; // diff = outdegree - indegree
        for (String node : preorder.split(",")) {
            if (--diff < 0) return false;

            if (!node.equals("#")) {
                diff += 2;
            }
        }
        return diff == 0;
    }

    void test(String s, boolean expected) {
        assertEquals(expected, isValidSerialization(s));
        assertEquals(expected, isValidSerialization2(s));
    }

    @Test
    public void test1() {
        test("#", true);
        test("#,#", false);
        test("1,#", false);
        test("9,#,#,1", false);
        test("9,3,4,#,#,1,#,#,#,2,#,6,#,#", false);
        test("9,3,4,#,#,1,#,#,2,#,6,#,#", true);
        test("#,7,6,9,#,#,#", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("VerifyPreorderTreeSerialization");
    }
}
