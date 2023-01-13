## Repair

- updating a node's data to be current, use case:
  - replication factor increased
  - token range changed
  - node has been down
- useful when having replication factor > 1
- must repair at least once before `gc_grace_period` (10 days default)
- the node being repaired sends its merkle root to other nodes to resolve any inconsistency

```cql
alter keyspace "vehicle_tracker" with replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 2
};

Warnings :
When increasing replication factor you need to run a full (-full) repair to distribute the data.
```

repair node using nodetool

```sh
# repair all key space
bin/nodetool -h 172.18.0.2 repair

# repair one key space
bin/nodetool -h 172.18.0.2 repair vehicle_tracker
```

### Read Repair

- repair as part of a read request
- turned on by default, with value of 10%
  - one out of every 10 request triggers repair
