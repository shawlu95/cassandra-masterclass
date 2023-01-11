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
- inserting an existing primary key would just update the row (no difference between insert and update)

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

### Loading Data into Table

- `insert into` cql command for single row of data (e.g. web app)
- `copy` import from CSV file, or export Cassandra into CSV file
- `sstableloader` streams SSTable files into live clusters

#### Insert Into

```sql
use home_security;

INSERT INTO activity (home_id, datetime, event, code_used) VALUES ('H01474777', '2014-05-21 07:32:16', 'alarm set', '5599');

select * from activity;
select home_id, code_used from activity;
```

#### Copy

- for docker, need to bind volume to container
- if seeing error "'module' object has no attribute 'parse_options'", use the cassandra's bin/cqlsh instead (see [stackoverflow](https://stackoverflow.com/questions/40289324/cqlsh-client-module-object-has-no-attribute-parse-options))

```bash
cd learning_apache_cassandra_working_files

# start up cassandra, expose 9042 to host machine
docker run --rm -d -v "$(pwd)/:/home/" -p 9042:9042 --name cassandra --hostname cassandra --network cassandra cassandra

# start up cql shell
docker run --rm -it --network cassandra \
  nuvo/docker-cqlsh cqlsh cassandra 9042 --cqlversion='3.4.6'

# use cassandra/bin/cqlsh to copy csv
docker exec -it cassandra bash
/opt/cassandra/bin/cqlsh
```

```sql
COPY activity (home_id, datetime, event, code_used) FROM '/home/Chapter 7/events.csv' WITH header = true AND delimiter = '|';
COPY home (home_id, address, city, state, zip, contact_name, phone, alt_phone, phone_password, email, main_code, guest_code) FROM '/home/Chapter 7/homes.csv' WITH header = true AND delimiter = '|';
```
