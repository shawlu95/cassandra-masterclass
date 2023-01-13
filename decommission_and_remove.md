## Decommission Node

- node may be removed if down-scaling, hardware fail
- planned removal: `nodetool decommission`
  - decommissioned node will pass its data and token range to other nodes
  - decommissioned node can be brought back to life
- best to delete data from decommissioned node, and rejoin as fresh node
  - clear data
  - clear commit log
  - clear `saved_caches`

```bash
# in virtual machine
nodetool -h 172.18.0.3 -p 7199 decommission

# in docker
docker exec cassandra_1 nodetool -h 172.18.0.3 -p 7199 decommission

# delete data from decommissioned node
cd /var/lib/cassandra
rm -r commitlog data saved_caches

# rejoin cluster, just like starting a new node (-f for foreground)
/opt/cassandra/bin/cassandra -f

# run clean up on other node to remove data that have been assumed by the new node
/opt/cassandra/bin/nodetool -h ... -p 7199 cleanup
```

# Remove Dead Node

- remove dead node: `nodetool removenode`
  - reassign token range from dead node to other nodes
  - and populate other nodes with the dead node's data

```bash
# get $host_id from nodetool status
/opt/cassandra/bin/nodetool removenode $host_id

# watch status when removing node
watch -n 2 /opt/cassandra/bin/nodetool removenode status
```
