create table limits
(
    user_id bigserial primary key,
    limit_amount numeric
);

INSERT INTO limits(limit_amount)
SELECT 10000
FROM generate_series(1, 100);