import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC690: https://leetcode.com/problems/employee-importance/
//
// Given a data structure of employee information, which includes the employee's unique id, his
// importance value and his direct subordinates' id. Now given the employee information of a
// company, and an employee id, return the total importance value of he and all his subordinates.
public class EmployeeImportance {
    private class Employee {
        // It's the unique id of each node;
        // unique id of this employee
        public int id;
        // the importance value of this employee
        public int importance;
        // the id of direct subordinates
        public List<Integer> subordinates;
    }

    //
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(98.47%),  46.5 MB(94.40%) for 108 tests
    public int getImportance(List<Employee> employees, int id) {
        Map<Integer, Employee> map = new HashMap<>();
        for (Employee e : employees) {
            map.put(e.id, e);
        }
        return getImportance(map, id);
    }

    private int getImportance(Map<Integer, Employee> map, int id) {
        Employee e = map.get(id);
        int res = e.importance;
        for (int sId : e.subordinates) {
            res += getImportance(map, sId);
        }
        return res;
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(N)
    // 6 ms(51.87%),  46.6 MB(94.40%) for 108 tests
    public int getImportance2(List<Employee> employees, int id) {
        Map<Integer, Employee> map = new HashMap<>();
        for (Employee e : employees) {
            map.put(e.id, e);
        }
        int res = 0;
        Queue<Integer> queue = new LinkedList<>();
        for (queue.offer(id); !queue.isEmpty(); ) {
            Employee e = map.get(queue.poll());
            res += e.importance;
            for (int sId : e.subordinates) {
                queue.offer(sId);
            }
        }
        return res;
    }

    void test(List<Employee> employees, int id, int expected) {
        assertEquals(expected, getImportance(employees, id));
        assertEquals(expected, getImportance2(employees, id));
    }

    @Test
    public void test() {
        test(convert(new Object[][]{
                {1, 5, new int[]{2, 3}},
                {2, 3, new int[0]},
                {3, 3, new int[0]}}), 1, 11);
    }

    private List<Employee> convert(Object[][] employees) {
        return Arrays.stream(employees).map(e -> {
            Employee employee = new Employee();
            employee.id = (int)e[0];
            employee.importance = (int)e[1];
            employee.subordinates = Arrays.stream((int[])e[2]).boxed().collect(Collectors.toList());
            return employee;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
