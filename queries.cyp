// Label scan for a person's friends
MATCH (p:Person)-[:FRIEND]->(f)
WHERE p.id = 123
RETURN f

6640 ms

// With an index applied
CREATE INDEX ON :Person(id)

MATCH (p:Person)-[:FRIEND]->(f)
WHERE p.id = 123
RETURN f

40 ms
