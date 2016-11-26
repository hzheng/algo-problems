import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC332: Given a list of airline tickets represented by pairs of departure and
// arrival airports [from, to], reconstruct the itinerary in order. All of the
// tickets belong to a man who departs from JFK. Thus, the itinerary must begin
// with JFK.
// Note:
// If there are multiple valid itineraries, you should return the itinerary
// that has the smallest lexical order when read as a single string.
// All airports are represented by three capital letters (IATA code).
// You may assume all tickets form at least one valid itinerary.
public class ReconstructItinerary {
    // Recursion + DFS + Backtracking
    // beats 27.47%(18 ms for 79 tests)
    public List<String> findItinerary(String[][] tickets) {
        Map<String, List<String> > map = new HashMap<>();
        for (String[] ticket : tickets) {
            String departure = ticket[0];
            if (map.containsKey(departure)) {
                map.get(departure).add(ticket[1]);
            } else {
                map.put(departure, new ArrayList<>(Arrays.asList(ticket[1])));
            }
        }
        for (List<String> arrivals : map.values()) {
            Collections.sort(arrivals);
        }
        String departure = "JFK";
        LinkedList<String> iternary = new LinkedList<>();
        iternary.add(departure);
        findItinerary(departure, tickets.length + 1, map, iternary);
        return iternary;
    }

    private boolean findItinerary(String departure, int len,
                                  Map<String, List<String> > map,
                                  LinkedList<String> iternary) {
        List<String> arrivals = map.getOrDefault(departure, Collections.emptyList());
        for (int i = 0; i < arrivals.size(); i++) {
            String arrival = arrivals.get(i);
            if (arrival == null) continue;

            iternary.add(arrival);
            arrivals.set(i, null);
            if (findItinerary(arrival, len, map, iternary)) return true;

            // backtracking
            iternary.removeLast(); // iternary.remove(iternary.size() - 1);
            arrivals.set(i, arrival);
        }
        return iternary.size() == len;
    }

    // Recursion + Heap + Graph
    // https://en.wikipedia.org/wiki/Eulerian_path#Hierholzer.27s_algorithm
    // beats 57.11%(12 ms for 79 tests)
    public List<String> findItinerary2(String[][] tickets) {
        Map<String, Queue<String> > map = new HashMap<>();
        for (String[] ticket : tickets) {
            String departure = ticket[0];
            if (!map.containsKey(departure)) {
                map.put(departure, new PriorityQueue<>());
            }
            map.get(departure).offer(ticket[1]);
        }
        List<String> iternary = new LinkedList<>();
        findItinerary2("JFK", map, iternary);
        return iternary;
    }

    private void findItinerary2(String departure,
                                Map<String, Queue<String> > map,
                                List<String> iternary) {
        Queue<String> arrivals = map.get(departure);
        if (arrivals != null) {
            while (!arrivals.isEmpty()) {
                findItinerary2(arrivals.poll(), map, iternary);
            }
        }
        iternary.add(0, departure);
    }

    // Solution of Choice
    // Stack + Heap + Graph
    // beats 39.09%(15 ms for 79 tests)
    public List<String> findItinerary3(String[][] tickets) {
        Map<String, Queue<String> > map = new HashMap<>();
        for (String[] ticket : tickets) {
            // map.computeIfAbsent(
            //     ticket[0], k -> new PriorityQueue<>()).add(ticket[1]);
            String departure = ticket[0];
            if (!map.containsKey(departure)) {
                map.put(departure, new PriorityQueue<>());
            }
            map.get(departure).offer(ticket[1]);
        }
        List<String> itinerary = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        stack.push("JFK");
        while (!stack.empty()) {
            while (true) {
                Queue<String> arrivals = map.get(stack.peek());
                if (arrivals == null || arrivals.isEmpty()) break;

                stack.push(arrivals.poll());
            }
            itinerary.add(0, stack.pop());
        }
        return itinerary;
    }

    void test(String[][] tickets, String ... expected) {
        assertArrayEquals(expected, findItinerary(tickets).toArray());
        assertArrayEquals(expected, findItinerary2(tickets).toArray());
        assertArrayEquals(expected, findItinerary3(tickets).toArray());
    }

    @Test
    public void test1() {
        test(new String[][] {{"JFK", "ATL"}, {"ATL", "JFK"}}, "JFK", "ATL", "JFK");
        test(new String[][] {{"MUC", "LHR"}, {"JFK", "MUC"}, {"SFO", "SJC"},
                             {"LHR", "SFO"}}, "JFK", "MUC", "LHR", "SFO", "SJC");
        test(new String[][] {{"JFK", "SFO"}, {"JFK", "ATL"}, {"SFO", "ATL"},
                             {"ATL", "JFK"}, {"ATL", "SFO"}},
             "JFK","ATL","JFK","SFO","ATL","SFO");
        test(new String[][] {{"JFK", "KUL"}, {"JFK", "NRT"}, {"NRT", "JFK"}},
             "JFK", "NRT", "JFK", "KUL");
        test(new String[][] {{"EZE", "AXA"}, {"TIA", "ANU"}, {"ANU", "JFK"},
                             {"JFK", "ANU"}, {"ANU", "EZE"}, {"TIA", "ANU"},
                             {"AXA", "TIA"}, {"TIA", "JFK"}, {"ANU", "TIA"},
                             {"JFK", "TIA"}},
             "JFK", "ANU", "EZE", "AXA", "TIA", "ANU", "JFK", "TIA",
             "ANU", "TIA", "JFK");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReconstructItinerary");
    }
}
