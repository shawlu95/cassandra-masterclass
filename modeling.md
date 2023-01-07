## Cassandra Data Modeling

Join does not exist in Cassandra. Design table such that a query can be answered by querying a single table.

### Secondary Index

- query a table by a key without partition key would fail
- query a table by a partition key and a non-indexed key would fail
- can create index on non-primary key column
  - for each secondary index, Cassandra creates a hidden table on each node in the cluster
  - index does **not** increase query speed in Cassandra
  - to speed up query, need to create a new table specifically for the query (drawback is having to manually maintain a separate table)
  - secondary index only allows column to be used in where clause

```sql
-- create secondary index
create index code_used_index on activity (code_used);
```

### Composite Partition Key

- Use two columns as partition key, wrap then in bracket

```sql
use vehicle_tracker;

create table location (
vehicle_id text,
date text,
time timestamp,
latitude double,
longitude double,
primary key ((vehicle_id, date), time)
) with clustering order by (time desc);

COPY location (vehicle_id, date, time, latitude, longitude)
FROM '/home/Chapter 8/coordinates.csv' WITH header = true AND delimiter = '|';
```
