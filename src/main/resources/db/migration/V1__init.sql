create table limits
(
    id bigserial primary key,
    user_id bigint,
    limit_amount numeric
);

INSERT INTO limits(user_id, limit_amount)
SELECT generate_series, 10000
FROM generate_series(1, 100);