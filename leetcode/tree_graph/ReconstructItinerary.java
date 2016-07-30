import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a list of airline tickets represented by pairs of departure and
// arrival airports [from, to], reconstruct the itinerary in order. All of the
// tickets belong to a man who departs from JFK. Thus, the itinerary must begin
// with JFK.
// Note:
// If there are multiple valid itineraries, you should return the itinerary
// that has the smallest lexical order when read as a single string.
// All airports are represented by three capital letters (IATA code).
// You may assume all tickets form at least one valid itinerary.
public class ReconstructItinerary {
    // DFS + backtracking
    // beats 24.01%(23 ms)
    public List<String> findItinerary(String[][] tickets) {
        Map<String, List<String> > map = new TreeMap<>();
        for (String[] ticket : tickets) {
            String key = ticket[0];
            if (map.containsKey(key)) {
                map.get(key).add(ticket[1]);
            } else {
                map.put(key, new ArrayList<>(Arrays.asList(ticket[1])));
            }
        }

        for (List<String> arrivals : map.values()) {
            Collections.sort(arrivals);
        }

        String departure = "JFK";
        List<String> iternary = new LinkedList<>();
        iternary.add(departure);
        findItinerary(departure, tickets.length + 1, map, iternary);
        return iternary;
    }

    private boolean findItinerary(String departure, int len,
                                  Map<String, List<String> > map,
                                  List<String> iternary) {
        List<String> tickets = map.getOrDefault(departure, Collections.emptyList());
        for (int i = 0; i < tickets.size(); i++) {
            String arrival = tickets.get(i);
            if (arrival == null) continue;

            iternary.add(arrival);
            tickets.set(i, null);
            if (findItinerary(arrival, len, map, iternary)) return true;

            // backtracking
            iternary.remove(iternary.size() - 1);
            tickets.set(i, arrival);
        }
        return iternary.size() == len;
    }

    void test(String[][] tickets, String ... expected) {
        assertArrayEquals(expected, findItinerary(tickets).toArray());
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
