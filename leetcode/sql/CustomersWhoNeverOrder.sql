-- LC183: https://leetcode.com/problems/customers-who-never-order/

-- Suppose that a website contains two tables, the Customers table and the 
-- Orders table. Find all customers who never order anything.

-- Table: Customers.

-- +----+-------+
-- | Id | Name  |
-- +----+-------+
-- | 1  | Joe   |
-- | 2  | Henry |
-- | 3  | Sam   |
-- | 4  | Max   |
-- +----+-------+

-- Table: Orders.

-- +----+------------+
-- | Id | CustomerId |
-- +----+------------+
-- | 1  | 3          |
-- | 2  | 1          |
-- +----+------------+
-- Using the above tables as example, return the following:

-- +-----------+
-- | Customers |
-- +-----------+
-- | Henry     |
-- | Max       |
-- +-----------+

-- Solution 1 (284 ms)
SELECT Name Customers
FROM Customers
WHERE Id NOT IN (SELECT CustomerId FROM Orders);

-- Solution 2 (341 ms)
SELECT Name Customers FROM Customers c
LEFT JOIN Orders ON c.Id = CustomerId
WHERE CustomerId IS NULL;

-- Solution 3 (256 ms)
SELECT Name Customers FROM Customers c
WHERE NOT EXISTS (SELECT 1 FROM Orders WHERE c.Id = CustomerId);