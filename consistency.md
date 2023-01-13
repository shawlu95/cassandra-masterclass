## Consistency

if consistency level is N:

- just need N nodes to fulfill read request to be successful
- just need N nodes to fulfill write request to be successful
- if the number of replicas drops below N, read and write will all fail

many available options: `one`, `two`, `quorum` (for strong consistency), `all` (not recommended, too rigid) etc

```sql
-- check current
consistency;

-- change consistency level
consistency two;
consistency;
```

## Hinted Handoff

- enabeld by default
- allow write even if nodes are down
- node accepting the write request is "coordinator node"
- coordinator node can temporarily store the write request while the other nodes are down
- hinted handoff does not count towards "one", "quorum" or "all" consistency level
- meets the `any` consistecy level requirement and return to client, even if it never gets fulfilled (node never came back)

```yaml
# enabled by default
hinted_handoff_enabled: true

# 3 hours
max_hint_window_in_ms: 10800000
```
