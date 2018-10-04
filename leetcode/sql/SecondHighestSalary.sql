--  LC176: https://leetcode.com/problems/second-highest-salary/

--  Problem: Write a SQL query to get the second highest salary from the Employee table.
--  +----+--------+
--  | Id | Salary |
--  +----+--------+
--  | 1  | 100    |
--  | 2  | 200    |
--  | 3  | 300    |
--  +----+--------+

-- Solution 1
SELECT
    (SELECT DISTINCT
            Salary
        FROM
            Employee
        ORDER BY Salary DESC
        LIMIT 1 OFFSET 1) AS SecondHighestSalary;


-- Solution 2
SELECT
    IFNULL(
      (SELECT DISTINCT Salary
       FROM Employee
       ORDER BY Salary DESC
        LIMIT 1 OFFSET 1),
    NULL) AS SecondHighestSalary;
