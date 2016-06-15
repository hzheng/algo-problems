import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.UndirectedGraphNode;

// https://leetcode.com/problems/clone-graph/
//
// Clone an undirected graph. Each node in the graph contains a label and a
// list of its neighbors.
public class CloneGraph {
    // DFS
    // beats 66.04%
    public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        return clone(node, new HashMap<>());
    }

    private UndirectedGraphNode clone(UndirectedGraphNode node,
                                      Map<Integer, UndirectedGraphNode> map) {
        if (node == null) return null;

        int label = node.label;
        if (map.containsKey(label)) return map.get(label); // visited

        UndirectedGraphNode cloned = new UndirectedGraphNode(label);
        map.put(label, cloned);
        for (UndirectedGraphNode neighbor : node.neighbors) {
            UndirectedGraphNode clonedNeighbor = clone(neighbor, map);
            cloned.neighbors.add(clonedNeighbor);
        }
        return cloned;
    }

    // BFS
    // beats 19.51%
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
                UndirectedGraphNode clonedNeighbor = null;
                if (map.containsKey(neighbor)) {
                    clonedNeighbor = map.get(neighbor);
                } else {
                    queue.offer(neighbor);
                    clonedNeighbor = new UndirectedGraphNode(neighbor.label);
                    map.put(neighbor, clonedNeighbor);
                }
                cloned.neighbors.add(clonedNeighbor);
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
