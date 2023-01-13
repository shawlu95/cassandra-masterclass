## Monitoring

Monitoring tool communicates with Cassandra via JMX (Java Management Extension)

- nondetool provided in cassandra/bin
- JConsole: comes with Java JDK
- OpsCenter: install **OpsCenter** on a server, **OpsCenter Agent** on every node of the sluter

```bash
# see all command
nodetool

# cluster status
nodetool status

# about a particular node
nodetool info

# token range
nodetool ring

# column family (old phrase for table)
nodetool cfstats

# read and write latency
nodetool cfhistograms

nodetool compactionstats
```
