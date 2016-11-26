import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC331: https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/
//
// One way to serialize a binary tree is to use pre-order traversal. When we
// encounter a non-null node, we record the node's value. If it is a null node,
// we record using a sentinel value such as #.
// Given a string of comma separated values, verify whether it is a correct
//  preorder traversal serialization of a binary tree. Find an algorithm without
//  reconstructing the tree.
public class VerifyPreorderTreeSerialization {
    // Stack
    // beats 30.55%(19 ms for 150 tests)
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

    // Stack
    // beats 30.55%(19 ms for 150 tests)
    public boolean isValidSerialization1(String preorder) {
        Stack<Boolean> stack = new Stack<>();
        for (String token : preorder.split(",")) {
            boolean isNull = token.equals("#");
            if (isNull) {
                for (; !stack.isEmpty() && stack.peek(); stack.pop()) {
                    stack.pop(); // pop the left null-sibling
                    if (stack.isEmpty()) return false;
                }
            }
            stack.push(isNull);
        }
        return stack.size() == 1 && stack.peek();
    }

    // Solution of Choice
    // Graph Theory
    // https://discuss.leetcode.com/topic/35976/7-lines-easy-java-solution
    // beats 94.19%(9 ms for 150 tests)
    public boolean isValidSerialization2(String preorder) {
        int degreeDiff = 1; // degreeDiff = outdegree - indegree
        for (String node : preorder.split(",")) {
            if (--degreeDiff < 0) return false;

            if (!node.equals("#")) {
                degreeDiff += 2;
            }
        }
        return degreeDiff == 0;
    }

    void test(String s, boolean expected) {
        assertEquals(expected, isValidSerialization(s));
        assertEquals(expected, isValidSerialization1(s));
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
