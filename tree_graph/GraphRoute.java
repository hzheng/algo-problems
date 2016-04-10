import java.util.LinkedList;

import tree_graph.Graph.Node;
import static tree_graph.Graph.State.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 4.2:
 * Given a directed graph, find out whether there is a route between two nodes.
 *
 * TODO: print out the route
 */
public class GraphRoute {
    public static boolean hasRouteDFS(Node start, Node end) {
        if (start == null || end == null) {
            return false;
        }

        start.state = VISITED;
        if (start == end) return true;

        for (Node node : start.adjancency) {
            if (node.state == UNVISITED) {
                if (hasRouteDFS(node, end)) return true;
            }
        }
        return false;
    }

    public static boolean hasRoute(Node start, Node end) {
        if (start == null || end == null) {
            return false;
        }

        LinkedList<Node> queue = new LinkedList<Node>();
        start.state = VISITING;
        queue.addLast(start);
        while (!queue.isEmpty()) {
            Node current = queue.removeFirst();
            if (current == null) continue;

            for (Node node : current.adjancency) {
                if (node == end) return true;
                // check this AFTER the last in case of self-loop
                if (node.state == UNVISITED) {
                    node.state = VISITING;
                    queue.add(node);
                }
            }
            current.state = VISITED;
        }
        return false;
    }

    void test(int[] n, boolean expected) {
    }

    @Test
    public void test1() {
        test(new int[] {1}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GraphRoute");
    }
}
