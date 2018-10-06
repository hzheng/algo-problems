-- LC182: https://leetcode.com/problems/duplicate-emails/description/

-- Write a SQL query to find all duplicate emails in a table named Person.

-- +----+---------+
-- | Id | Email   |
-- +----+---------+
-- | 1  | a@b.com |
-- | 2  | c@d.com |
-- | 3  | a@b.com |
-- +----+---------+

-- Solution 1 (369 ms)
SELECT DISTINCT Email FROM Person
WHERE (SELECT (COUNT(*)) FROM Person p WHERE p.Email = person.Email) > 1;

-- Solution 2 (206 ms)
SELECT Email FROM
(
  SELECT Email, COUNT(Email) as num
  FROM Person
  GROUP BY Email
) statistic
WHERE num > 1;

-- Solution 3 (238 ms)
SELECT Email FROM Person
GROUP BY Email
HAVING COUNT(Email) > 1;

-- Solution 4 (208 ms)
SELECT DISTINCT p1.Email FROM Person p1
JOIN Person p2
ON p1.Email = p2.Email
WHERE p1.Id <> p2.Id;