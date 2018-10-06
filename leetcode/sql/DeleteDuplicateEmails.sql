-- LC196: https://leetcode.com/problems/delete-duplicate-emails/

-- Write a SQL query to delete all duplicate email entries in a table named 
-- Person, keeping only unique emails based on its smallest Id.

-- +----+------------------+
-- | Id | Email            |
-- +----+------------------+
-- | 1  | john@example.com |
-- | 2  | bob@example.com  |
-- | 3  | john@example.com |
-- +----+------------------+
-- Id is the primary key column for this table.
-- For example, after running your query, the above Person table should have the following rows:

-- +----+------------------+
-- | Id | Email            |
-- +----+------------------+
-- | 1  | john@example.com |
-- | 2  | bob@example.com  |
-- +----+------------------+

-- WRONG Solution:
DELETE FROM Person
WHERE id NOT IN
(
    SELECT Min(Id) FROM Person
    GROUP BY Email
);

-- Reason: You can't specify target table 'Person' for update in FROM clause

-- Solution 1 (573 ms)
DELETE FROM Person
WHERE id NOT IN
( 
    SELECT t.id FROM (
        SELECT MIN(id) id FROM Person
        GROUP BY email
    ) t
);

-- Solution 2 (882 ms)
DELETE p1 FROM Person p1, Person p2
WHERE
    p1.Email = p2.Email AND p1.Id > p2.Id;
