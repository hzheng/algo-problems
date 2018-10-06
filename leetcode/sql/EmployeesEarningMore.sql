-- LC181: https://leetcode.com/problems/employees-earning-more-than-their-managers/

-- The Employee table holds all employees including their managers. Every employee has an Id, and there is also a column for the manager Id.

-- +----+-------+--------+-----------+
-- | Id | Name  | Salary | ManagerId |
-- +----+-------+--------+-----------+
-- | 1  | Joe   | 70000  | 3         |
-- | 2  | Henry | 80000  | 4         |
-- | 3  | Sam   | 60000  | NULL      |
-- | 4  | Max   | 90000  | NULL      |
-- +----+-------+--------+-----------+
-- Given the Employee table, write a SQL query that finds out employees who earn more than their managers. 

-- Solution 1 (609 ms)
SELECT Name Employee
FROM Employee e
WHERE e.Salary > (SELECT Salary FROM Employee WHERE Id = e.ManagerId);

-- Solution 2(299 ms)
SELECT
    a.Name Employee
FROM
    Employee a,
    Employee b
WHERE
    a.ManagerId = b.Id
    AND a.Salary > b.Salary;

-- Solution 3(381 ms)
SELECT
    a.Name Employee
FROM Employee a JOIN Employee b
    ON a.ManagerId = b.Id 
    AND a.Salary > b.Salary;

-- Solution 3'(362 ms)
SELECT
    a.Name Employee
FROM Employee a JOIN Employee b
    ON a.ManagerId = b.Id 
WHERE a.Salary > b.Salary;