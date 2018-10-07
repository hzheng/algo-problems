-- LC596: https://leetcode.com/problems/classes-more-than-5-students/

-- There is a table courses with columns: student and class

-- Please list out all classes which have more than or equal to 5 students.

-- For example, the table:

-- +---------+------------+
-- | student | class      |
-- +---------+------------+
-- | A       | Math       |
-- | B       | English    |
-- | C       | Math       |
-- | D       | Biology    |
-- | E       | Math       |
-- | F       | Computer   |
-- | G       | Math       |
-- | H       | Math       |
-- | I       | Math       |
-- +---------+------------+
-- Should output:

-- +---------+
-- | class   |
-- +---------+
-- | Math    |
-- +---------+

-- Solution 1 (1590 ms)
SELECT class
FROM (
SELECT class, COUNT(DISTINCT student) c
FROM courses
GROUP BY class
) t
WHERE t.c > 4;

-- Solution 2 (1620 ms)
SELECT class
FROM courses
GROUP BY class
HAVING COUNT(DISTINCT student) >= 5;

-- Solution 3 (1620 ms)