#Intro to Spark and Cassandra

## Running a Spark Project against the Spark Master included in DataStax Enterprise

In the next series of exercises we are going to be working the Spark Master that is started with DataStax Enterprise (DSE).  Be sure you have started DSE with Spark Enabled as specified in the main README in the root of the project directory.  That is you have at least one instance of DSE is running as an analytics node.

* Throughout all the exercises you can view information on the Spark Master and currently running jobs at [the Spark Master URL](http://localhost:7080/) or port 7080 of the IP of Your Server.

## Exercise 1 - Run a basic Spark Job against the DSE Spark Master

  * The goal of this exercise is to run the BasicSparkDemo.java with Spark in DSE
  * The previous projects build was greatly simplified to make it easy to run standalone Spark Projects.
  * This project has a more complex build that assembles a uber jar file and prepares it for submitting to the DSE Spark Master.  This uses the maven-assembly-plugin to build the jar file.
  * Another major difference is the scope of most of the dependencies are marked as provided because they are provided by the DSE Spark Master when the project is run.  By marking them as provided they are not included in the uber jar built in the assembly task.
  * The BasicSparkDemo does not specify the Spark Master because it will automatically be set when you run dse spark-submit to the Spark Master running in DSE.  You can find out what the Spark Master is by running:

    ` dsetool sparkmaster`

     `dsetool ring`

     The node that is marked as JT (job tracker) is the Spark Master

  * Build the Simple Spark Project in maven using:

    `mvn install`

  * Run the Spark Project by submitting the job to the DSE Spark Master (modify the command to point to where you installed DSE):

  `dse spark-submit --class simpleSpark.BasicSparkDemo ./target/IntroSparkCassandra-0.1.jar`

  * [See these docs for details about dse spark-submit](http://docs.datastax.com/en/datastax_enterprise/4.7/datastax_enterprise/spark/sparkStart.html)

  * Verify the program ran by going to the Spark Master Web Page (it is on port 7080 of the Spark Master Server) and finding the stdout of the Spark Job.
  * Standard out will actually be in the log file of the Spark Worker and not in the terminal window where the job ran.
  * The same log file can be found on the server under the /var/lib/spark/work/app ...
  * The program might terminate with an ERROR - that doesn't mean the program didn't run.

## Exercise 2 - Connecting to Cassandra from Spark

* The goal of this exercise is to show how to connect to Cassandra from Spark running the BasicReadWriteDemo.java with Spark in DSE
* If Spark was being run stand-alone and not part of DSE then the host of Cassandra would have to be  specified in spark.cassandra.connection.host.  This property is set automatically when run with dse spark-submit This will establish a connection to that Cassandra server.
* The most basic way to connect to Cassandra from Spark is to create a session and execute CQL on that session.
* A session can be retrieved from the CassandraConnector by calling openSession.
* The try-with-resources statement is used to automatically close the connection to Cassandra at the end of the session.

* In BasicSparkDemo review the method basicCassandraSession that opens a Cassandra session and creates a test table.  It then inserts some test data.  The demo can be run using:  

```
mvn package

dse spark-submit --class simpleSpark.BasicReadWriteDemo ./target/IntroSparkCassandra-0.1.jar
```

* Verify the tables are created and the data inserted by going to cqlsh and verifying the data.
* If you want to clean up the tables and re-run any of the following CQL in cqlsh run:

  `drop keyspace test;`

## Exercise 3 - Writing tables from a Spark JavaRDD into Cassandra

The goal of this exercise is to write the list of People in the class BasicReadWriteDemo inside the method writePeopleToCassandra into Cassandra.

* The static import `import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;` brings into scope most of the methods that are needed for working with the Spark Cassandra Connector in Java
* The Java Spark Context has already been setup and passed into the method.
* The most import method is `javaFunctions` which converts a JavaRDD to a RDDJavaFunctions.  That class is a Java API Wrapper around the standard RDD functions and adds functionality to interact with Cassandra.
* From the `RDDJavaFunctions` instance that is returned call the `writeBuilder` that defines the mapping of the RDD into Cassandra.  For example the following call:
  * `writerBuilder("test", "people", mapToRow(Person.class))`

  Maps the write into the keyspace test and the table people.  It then uses a Java Bean Mapper to map the Person Class to the proper columns in the people table.
* Finally call saveToCassandra to persist all of the entries in the RDD to Cassandra.



## Exercise 4 - Reading tables from Cassandra into a Spark JavaRDD

In the next exercise we are going to read data out of Cassandra into a Spark RDD in the readPeopleFromCassandra method.  

*  Using the `sparkContextJavaFunctions` load a Cassandra table into an Java RDD

  `cassandraTable("test", "people" ...  `

* Then use the `mapRowTo` parameter to map each row to a Person automatically:

  `cassandraTable("test", "people", mapRowTo(Person.class))`

*  Map each Cassandra row that is returned as a string and print out the rows.

* Add a filter to the rows.  This is what is called a "push down predicate" because the filter is executed on the server side before it is returned to Cassandra.  This is much more efficient then loading all of the data into Spark and filtering it in Spark.

* As an example add a call to the method  `.where("name=?", "Anna")` after the cassandraTable method.  Print out each person returned.

* Use a projection to select only certain columns from the table `select("id")`

## Next Steps

[Word Count Exercises using Sherlock Holmes](WORD_COUNT.md)

For Word Count Exercises using the KJV of the Bible See the Search Analytics Demo

[See the Streaming Exercises to Learn about Spark Streaming](STREAMING_EXERCISES.md)

## Further Documentation

[Spark Cassandra Connector Java API Documentation](https://github.com/datastax/spark-cassandra-connector/blob/master/doc/7_java_api.md)

[Accessing cassandra from spark in java](http://www.datastax.com/dev/blog/accessing-cassandra-from-spark-in-java)
