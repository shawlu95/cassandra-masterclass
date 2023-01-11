## Update Statement

- syntax is same as sql syntax
- unlike RDBMS **there is no disk seek, find, update and save**
- update is just a new write, appended into memtable, and then flushed into a new SSTable once memtable is full

```sql
-- update one field
update home_security.home set phone = '310-883-7197'
where home_id = 'H01474777';

-- update more fields
update home_security.home set phone = '310-883-7197', contact_name = 'shaw'
where home_id = 'H01474777';
```

```bash
# flush table
/opt/cassandra/bin/nodetool flush home_security home

# data file can be found here
cd /var/lib/cassandra/data/home_security/home-eccfa4c0916c11ed9e1f47ec639afef9/
```
