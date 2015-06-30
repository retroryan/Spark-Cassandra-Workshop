## CQL Demo Scripts

This is an example of using CQL to create a basic Cassandra Data Model and then import data into the tables.

* Import the cql demo scripts into Cassandra
  * cqlsh -f cql_demo_scripts.cql
* In a cqlsh explore the data
  *  ~/dse-4.6.6/bin/cqlsh localhost
  *  use sample_ks;
  *  describe tables;
  *  describe table messages1;
  *  select * from messages1;
