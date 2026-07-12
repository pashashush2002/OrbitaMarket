SELECT user_id, COUNT(*) as total_spent_count, SUM(price) as total_spent
FROM orders
WHERE status = 'PAID' GROUP BY user_id ORDER BY total_spent;