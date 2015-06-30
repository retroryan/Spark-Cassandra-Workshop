Cassandra Setup
=====================================

Start DSE in Search and Analytics Mode
bin/dse cassandra -k -s

bin/dsetool create_core search_demo.kjv reindex=true generateResources=true
http://localhost:8983/solr/#/

~/dse-4.7.0/bin/dse hadoop fs -copyFromLocal data/kjvdat.txt /data/kjvdat.txt
~/dse-4.7.0/bin/dse hadoop fs -ls /data
~/dse-4.7.0/bin/dse spark-submit --class demo.SearchAnalyticsDemo ./target/scala-2.10/SearchAnalyticsDemo-assembly-0.2.0.jar



Modify cassandra.yaml
Change broadcast_address to be the outside address in EC2  - broadcast_rpc_address: 52.8.63.225
Modify the seeds to point to other servers


http://docs.datastax.com/en/datastax_enterprise/4.7/datastax_enterprise/srch/srchJSON.html?scroll=srchJSON__solrFacetquery

Kafka Server Setup - Local Testing
===================================

Start Kafka, create the topics and test:

bin/zookeeper-server-start.sh config/zookeeper.properties

bin/kafka-server-start.sh config/server.properties

bin/kafka-create-topic.sh --zookeeper localhost:2181 --replica 1 --partition 1 --topic acc_data

bin/kafka-list-topic.sh --zookeeper localhost:2181

bin/kafka-console-producer.sh --broker-list localhost:9092 --topic acc_data

bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic acc_data --from-beginning

Kafka Server Setup - Remote Server Setup
===================================

mkdir runlogs

nohup bin/zookeeper-server-start.sh config/zookeeper.properties > runlogs/zookeeper.log 2> runlogs/zookeeper.err < /dev/null &

nohup bin/kafka-server-start.sh config/server.properties > runlogs/kafka.log 2> runlogs/kafka.err < /dev/null &

bin/kafka-create-topic.sh --zookeeper localhost:2181 --replica 1 --partition 1 --topic acc_data

bin/kafka-list-topic.sh --zookeeper localhost:2181

bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic acc_data --from-beginning


Sensor Analyzer Setup - Local Server
====================================

[Install sbt] (http://www.scala-sbt.org/release/tutorial/Setup.html)

sbt run -Dspark.cassandra.connection.host=52.8.63.225 -Dzookeeper.host=localhost:2181

Sensor Analyzer Setup - Remote Server
====================================

on local machine bundle the server for deployment:

sbt assembly

copy target/scala-2.10/SensorAnalyzer-assembly-0.2.0.jar to remote server.

on remote machine:

mkdir runlogs

nohup java  -Dspark.cassandra.connection.host=52.8.63.225 -Dzookeeper.host=localhost:2181 -jar SensorAnalyzer-assembly-0.2.0.jar  > runlogs/analyzer.log 2> runlogs/analyzer.err < /dev/null & 

./bin/spark-submit --name "My app" --master local[4] --conf spark.shuffle.spill=false
  --conf "spark.executor.extraJavaOptions=-XX:+PrintGCDetails -XX:+PrintGCTimeStamps" myApp.jar
  
  
How to Verify Results in Cassandra
========================================                             	

* verify result in cqlsh:

dse/bin/cqlsh

cqlsh> use sensors;

cqlsh:test> select * from sensors.acceleration limit 10;


Setup and Run Cassandra
=============================
[Fix DSE so Kafka works](https://support.datastax.com/hc/en-us/articles/204226489--java-lang-NoSuchMethodException-seen-when-attempting-Spark-streaming-from-Kafka)

Run DSE as an analytics node:
dse/bin/dse cassandra -k

To build and run the Kafka example
========================================

* Build the jar file -> 'sbt assembly'
* Make sure you've got a running spark server and Cassandra node listening on localhost
* Make sure you've got a running Kafka server on localhost with the topic events pre-provisioned.
* Start the Kafka producer sbt "runMain com.datastax.streamingDemos.KafkaProducer"
* Submit the assembly to the spark server ~/dse/bin/dse spark-submit --class com.datastax.streamingDemos.KafkaConsumer ./target/scala-2.10/simpleSpark-assembly-0.2.0.jar
* Data will be posted to the C* column families demo.event_log and demo.event_counters

~/dse/bin/dse spark-submit --class com.datastax.sparkDemo.ClusterCopy ./target/scala-2.10/simpleSpark-assembly-0.2.0.jar