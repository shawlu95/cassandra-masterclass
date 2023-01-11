## Delete

### Command

- `delete`: a row, or many rows
- `truncate`: delete all rows from a table
- `drop`: delete a table or keyspace

```bash
create keyspace playground with replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
use playground;

# run a cql file
source '/home/Chapter 10/create.cql';

# the cql file content
USE playground;

CREATE TABLE messages_by_user (
sender text,
sent timestamp,
recip text,
body text,
message_id uuid,
PRIMARY KEY (sender, sent)
) WITH CLUSTERING ORDER BY (sent DESC);

COPY messages_by_user (sender, sent, recip, body, message_id) FROM '/home/Chapter 10/messages.csv' WITH header = true AND delimiter = '|';

# delete a field
delete body from messages_by_user where sender = 'juju' and sent = '2013-07-21 15:32:16';

# delete entire row
delete from messages_by_user where sender = 'juju' and sent = '2013-07-21 15:32:16';

# empty the table
truncate messages_by_user;

# delete entire table (no longer exists)
drop table messages_by_user;

# drop keyspace
drop keyspace playground;
```

### Behind-the-Scene

- deleted records are marked with **tombstone**
- after `gc_grace_period` (default 10 days), the records are actually deleted during **compaction** process
- compaction combines SSTable and reclaim disk space

```sql
-- check gc_grace_period
use home_security;
describe table home;
delete from home where home_id = 'H01474777';
```

```bash
# flush deletion
/opt/cassandra/bin/nodetool flush home_security home
# view tombstone

# manual trigger compaction, won't delete the tombstoned records
# but will reduce the number of SSTables
/opt/cassandra/bin/nodetool compact home_security home
```

![alt-text](./assets/compaction.png)
