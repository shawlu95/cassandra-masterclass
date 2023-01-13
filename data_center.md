## Cluster

Spanning multiple data center

- cannot use `SimpleSnitch` to `GossipingPropertyFileSnitch`
  - very popular, just need to provide the rack and data center of the new node
  - don't need to tell every node about the new node
- replication strategy use `NetworkTopology`

### Steps in Setting up cluster

configure `cassandra.yaml`

- check cluster name must be same for every node
- change `listen_address` to IP address of the node (for gossip)
- change `rpc_address` to IP address of the node (for client app)
- set a list of comma-delimited peer node IP addresses in `seeds`
- change snitch type `endpoint_snitch` to `GossipingPropertyFileSnitch`
- for `GossipingPropertyFileSnitch`, modifies `cassandra/conf/ccassandra-rackdc.properties` file which specifies the topology of the node:

```
dc = DC2
rack = RAC1
```

- rolling restart nodes
- change replication strategy of keyspace

```sql
alter keyspace vehicle_tracker with replication {
  'class': 'NetworkTopologyStrategy',
  'DC1': 2,
  'DC2': 1
};
```

- repair node, one keyspace at a time
