-- LC184: https://leetcode.com/problems/department-highest-salary/

-- The Employee table holds all employees. Every employee has an Id, a salary, and there is also a column for the department Id.

-- +----+-------+--------+--------------+
-- | Id | Name  | Salary | DepartmentId |
-- +----+-------+--------+--------------+
-- | 1  | Joe   | 70000  | 1            |
-- | 2  | Henry | 80000  | 2            |
-- | 3  | Sam   | 60000  | 2            |
-- | 4  | Max   | 90000  | 1            |
-- +----+-------+--------+--------------+
-- The Department table holds all departments of the company.

-- +----+----------+
-- | Id | Name     |
-- +----+----------+
-- | 1  | IT       |
-- | 2  | Sales    |
-- +----+----------+
-- Write a SQL query to find employees who have the highest salary in each of 
-- the departments. For the above tables, Max has the highest salary in the IT 
-- department and Henry has the highest salary in the Sales department.

-- +------------+----------+--------+
-- | Department | Employee | Salary |
-- +------------+----------+--------+
-- | IT         | Max      | 90000  |
-- | Sales      | Henry    | 80000  |
-- +------------+----------+--------+

-- Solution 1 (392 ms)
SELECT Department.Name Department, Employee.Name Employee, Salary
FROM Department, Employee
WHERE  
Employee.DepartmentId=Department.id
AND
(Salary, Employee.DepartmentId)
IN
    (SELECT MAX(Salary), DepartmentId
    FROM Employee
    GROUP BY DepartmentId);

-- Solution 1' (305 ms)
SELECT
    Department.Name Department, Employee.Name Employee, Salary
FROM Employee
JOIN Department ON Employee.DepartmentId = Department.Id
WHERE
    (Salary, Employee.DepartmentId) IN
    (SELECT MAX(Salary), DepartmentId
    FROM Employee
    GROUP BY DepartmentId);

-- Solution 2 (655 ms)
SELECT dep.Name Department, emp.Name Employee, emp.Salary 
FROM Department dep, Employee emp 
WHERE DepartmentId = dep.Id 
AND Salary = (SELECT MAX(Salary) FROM Employee e WHERE e.DepartmentId = dep.Id);

-- Solution 3 (354 ms)
SELECT d.Name Department, e1.Name Employee, e1.Salary 
FROM 
	Employee e1, Department d   
WHERE e1.DepartmentId = d.Id 
  AND NOT EXISTS 
  (SELECT 1 
   FROM Employee e2 
   WHERE e2.Salary > e1.Salary AND e1.DepartmentId = e2.DepartmentId);

-- Solution 4 (405 ms)
SELECT D.Name Department, E.Name Employee, E.Salary 
FROM
	Employee E,
	(SELECT DepartmentId, MAX(Salary) max FROM Employee GROUP BY DepartmentId) T,
	Department D
WHERE E.DepartmentId = T.DepartmentId 
  AND E.Salary = T.max
  AND E.DepartmentId = D.id;

-- Solution 5 (468 ms)
SELECT Department.Name Department, e1.Name Employee, Salary
FROM Employee e1, Department
WHERE e1.DepartmentId = Department.Id 
    AND Salary >= 
        ALL(SELECT Salary FROM Employee e2 WHERE e2.DepartmentId = e1.DepartmentId);