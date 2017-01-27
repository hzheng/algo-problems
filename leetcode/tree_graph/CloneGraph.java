import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.UndirectedGraphNode;

// LC133: https://leetcode.com/problems/clone-graph/
//
// Clone an undirected graph. Each node in the graph contains a label and a
// list of its neighbors.
public class CloneGraph {
    // DFS + Recursion
    // beats 44.40%(8 ms)
    public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        return node == null ? null : clone(node, new HashMap<>());
    }

    private UndirectedGraphNode clone(UndirectedGraphNode node,
                                      Map<UndirectedGraphNode, UndirectedGraphNode> map) {
        if (!map.containsKey(node)) {
            UndirectedGraphNode cloned = new UndirectedGraphNode(node.label);
            map.put(node, cloned);
            for (UndirectedGraphNode neighbor : node.neighbors) {
                cloned.neighbors.add(clone(neighbor, map));
            }
        }
        return map.get(node);
    }

    // Solution of Choice
    // BFS + Queue + Hash Table
    // beats 31.89%(9 ms)
    public UndirectedGraphNode cloneGraph2(UndirectedGraphNode node) {
        if (node == null) return null;

        Queue<UndirectedGraphNode> queue = new LinkedList<>();
        Map<UndirectedGraphNode, UndirectedGraphNode> map = new HashMap<>();
        UndirectedGraphNode root = new UndirectedGraphNode(node.label);
        map.put(node, root);
        queue.offer(node);
        while (!queue.isEmpty()) {
            UndirectedGraphNode n = queue.poll();
            UndirectedGraphNode cloned = map.get(n);
            for (UndirectedGraphNode neighbor : n.neighbors) {
                if (!map.containsKey(neighbor)) {
                    queue.offer(neighbor);
                    map.put(neighbor, new UndirectedGraphNode(neighbor.label));
                }
                cloned.neighbors.add(map.get(neighbor));
            }
        }
        return root;
    }

    void test(String s) {
        UndirectedGraphNode node = UndirectedGraphNode.of(s);
        assertEquals("{" + s + "}", cloneGraph(node).toString());
        assertEquals("{" + s + "}", cloneGraph2(node).toString());
    }

    @Test
    public void test1() {
        test("0,1,2#1,2#2,2");
        test("0,1,2#1,2#2,3");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CloneGraph");
    }
}
