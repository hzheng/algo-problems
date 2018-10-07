-- LC626: https://leetcode.com/problems/exchange-seats/

-- Mary is a teacher in a middle school and she has a table seat storing students' names and their corresponding seat ids.

-- The column id is continuous increment.
-- Mary wants to change seats for the adjacent students.
-- Can you write a SQL query to output the result for Mary?
-- +---------+---------+
-- |    id   | student |
-- +---------+---------+
-- |    1    | Abbot   |
-- |    2    | Doris   |
-- |    3    | Emerson |
-- |    4    | Green   |
-- |    5    | Jeames  |
-- +---------+---------+
-- For the sample input, the output is:
-- +---------+---------+
-- |    id   | student |
-- +---------+---------+
-- |    1    | Doris   |
-- |    2    | Abbot   |
-- |    3    | Green   |
-- |    4    | Emerson |
-- |    5    | Jeames  |
-- +---------+---------+
-- Note:
-- If the number of students is odd, there is no need to change the last one's seat.

-- Solution 1 (281 ms)
SELECT S0.id, IF(S0.id % 2 = 0, S2.student, IFNULL(S1.student, S0.student)) student FROM seat S0
LEFT JOIN seat S1
ON S1.id % 2 = 0 AND S0.id + 1 = S1.id
LEFT JOIN seat S2
ON S2.id % 2 = 1 AND S0.id - 1 = S2.id
ORDER BY id;

-- Solution 2 (311 ms)
SELECT
    (CASE
        WHEN MOD(id, 2) != 0 AND counts != id THEN id + 1
        WHEN MOD(id, 2) != 0 AND counts = id THEN id
        ELSE id - 1
    END) id,
    student
FROM
    seat,
    (SELECT COUNT(*) AS counts
    FROM seat) seat_counts
ORDER BY id;

-- Solution 3 (285 ms)
SELECT
    s1.id, COALESCE(s2.student, s1.student) student
FROM seat s1
    LEFT JOIN seat s2 
    ON ((s1.id + 1) ^ 1) - 1 = s2.id
ORDER BY s1.id;

-- Solution 4 (285 ms)
SELECT
IF(id < (SELECT COUNT(*) FROM seat),
   IF(id MOD 2 = 0, id - 1, id + 1), IF(id MOD 2 = 0, id - 1, id)) id, student
FROM seat
ORDER BY id;

-- Solution 4 (324 ms)
SELECT id,
CASE 
    WHEN id % 2 = 0
    THEN (SELECT student FROM seat WHERE id = (s1.id - 1))  
    WHEN id % 2 != 0 AND id < (SELECT COUNT(student) FROM seat)
    THEN (SELECT student FROM seat WHERE id = (s1.id + 1))
    ELSE student
END student
FROM seat s1;

-- Solution 5 (305 ms)
SELECT s1.id - 1 id, s1.student
FROM Seat s1
WHERE s1.id MOD 2 = 0
UNION
SELECT s2.id + 1 id, s2.student
FROM Seat s2
WHERE s2.id MOD 2 = 1 AND s2.id != (SELECT MAX(id) FROM Seat)
UNION
SELECT s3.id, s3.student
FROM Seat s3
WHERE s3.id MOD 2 = 1 AND s3.id = (SELECT MAX(id) FROM Seat)
ORDER BY id;