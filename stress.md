## Stress Test

- available in `/opt/cassandra/tools/bin`

```bash
cd /opt/cassandra/
./tools/bin/cassandra-stress help write

# insert 100,000 rows
tools/bin/cassandra-stress write -h cassandra_1 -n 100000
tools/bin/cassandra-stress read -h cassandra_1 -n 100000
```

```sql
-- check the data inserted by stress test
describe keyspaces;
describe keyspace "Keyspace1";
```
