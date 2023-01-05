## Table

- a group of columns (key-value pairs)
- common data type: ascii, bigint, blob, boolean, **counter**, decimal, double, float, **inet**, int
- collection type: list, map, set
- others: timestmap, uuid, timeuuid, varint, text (same as varchar), varchar

### Primary Key

- uniquely identify a record
- consists of one or more columns aka compound primary key (in this case, home_id, datetime)
- if just one column, can put `PRIMARY KEY` immediately after type
- but to make it easier to read, it's better to always place the PK declaration after all column definition, even if the primary key has only one column
- primary key columns are grouped into partition key and clustering key
- partition key determine which node stores the data
  - by default, the first column before the comma in primary key definition is the partition key
  - aka row key (old name for partition key, partition used to be named 'row' in Thrift API before CQL)
  - if partition key consists of more than one column, wrap all partition keys in bracket: `PRIMARY KEY ((race_year, race_name), rank) `
- clustering key decides the records exact location in a partition
  - default is ascending order
  - cannot change clustering order after table creation (a design choice)

```sql
create keyspace home_security
with replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

use home_security;

-- home_id is the partition key
-- datetime is clustering key, sort in descending order
create table activity (
  home_id text,
  datetime timestamp,
  event text,
  code_used text,
  primary key (home_id, datetime)
) with clustering order by (datetime desc);

describe table activity;

drop table activity;

create table home (
  home_id text,
  address text,
  city text,
  state text,
  zip text,
  contact_name text,
  phone text,
  alt_phone text,
  phone_password text,
  email text,
  main_code text,
  guest_code text,
  primary key (home_id)
);
```
