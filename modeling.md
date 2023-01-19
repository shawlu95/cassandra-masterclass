## Cassandra Data Modeling

Join does not exist in Cassandra. Design table such that a query can be answered by querying a single table.

- **no-join**: either join from client side (bad) or denormalize the data (preferred)
- **no referential integrity** e.g. cascading delete
- query-centric design (RDBMS is data-centric)
  - minimize the number of partitions scanned in order to satisfy a query
- optimize for storage: each table is stored as separate file on disk; keep related columns in the same table
- sorting is a design choice: query cannot define how to sort, order is defiend with `create table` clustering columns
- **wide-column store** sparse multi-dimensional hashmap. each value has a key
- empty key doesn't consume space as `null` does in RDBMS
- primary key consists of partition key and clustering key
  - partition key unique determines partition
  - clustering key determines rows' order in a partition
  - primary key cannot contain null when inserting value
  - support **upsert**: insert and update is treated the same way
- column (key:val pair) -> row -> partition -> table -> keyspace -> cluster (aka ring)
- key space is similar to database in RDBMS
  - container for tables
  - define keyspace wide attributes such as replication factor
- table is a container for an ordered collection of rows
- row is a ordered collection of columns
- enum is stored as string

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

### Special data types

- counter: race-free increments across data center. Not idempotent
- `inet`: IPv4 or IPv6
- `timeuuid`: a nice substitute for `uuid`
- `UDT` user-defined type, considered as collection, cannot store as map value unless `frozen`
