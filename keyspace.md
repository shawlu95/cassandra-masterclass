## Keyspace

- Analogous to RDBMS databases
- example CQL of setting up a dummy keyspace

```sql
-- list all keyspaces
describe keyspaces;

-- check one keyspace's tables
describe keyspace system;

-- create a keyspace
-- data center 1 must have at least 3 nodes
-- data center 2 musth ave at least 2 nodes
create keyspace vehicle_tracker
with replication = {
  'class': 'NetworkTopologyStrategy'
  'dc1': 3,
  'dc2': 2
};

-- local uses replication factor 1
create keyspace vehicle_tracker
with replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

describe keyspace vehicle_tracker;

-- delete a keyspace
drop keyspace vehicle_tracker;
```
