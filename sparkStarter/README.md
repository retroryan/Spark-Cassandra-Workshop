#Intro to Spark with Java 8 Lambdas

This first lesson is an introduction to Spark using Java 8 Lambdas.  
Take a look at the Spark Function Interface to understand how they can be used with Java 8 Lambdas.  The function is defined in the package `org.apache.spark.api.java.function`

```
  public interface Function<T1, R> extends Serializable {
    public R call(T1 v1) throws Exception;
  }
```

## Running the Basic Read Write Demo

* Run the following command in a teminal window to verify you can run the BasicSparkDemo:
  `./compileAndRunSparkStarter.sh`

* This builds and executs the project using maven.

### Connecting to a Spark Master

 The SparkContext is what drives the enter Spark Job.  It creates a connection to a Spark Cluster and executes the job.  When a SparkContext is constructed, there are several constructors that can be used.  A very basic context can be created with:

 `SparkContext sc = new SparkContext("local", "BasicSparkDemo")`

  This takes a string for the "master" and an arbitrary job name. The master must be one of the following:

* local: Start the Spark job standalone and use a single thread to run the job.
* local[k]: Use k threads instead. Should be less than the number of cores.
* spark://host:port: Connect to a running, standalone Spark cluster.  This is the URL we will use to connect to Spark with DataStax Enterprise.
* mesos://host:port: Connect to a running, Mesos-managed Spark cluster.
* yarn-client or yarn-cluster: Connect to a YARN cluster


The current BasicSparkDemo program creates a Spark Configuration and passes it to the SparkContext to create a local Spark Master.  It then uses the SparkContext to create a JavaSparkContext which provides a Java-friendly version of the Spark Context.  The primary difference is it works with Java collections instead of Scala Collections.

### Exercise 1 - Creating a basic Spark Job

* Create an Array List:

  `List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);`

* Distribute the Java Collection to form an RDD:

  `javaSparkContext.parallelize(intList);`

* Try some basic list manipulations using map, filter or other transformations.

* Look at the lineage of the RDD's created by using toDebugString on the final RDD that is created.

* experiement with the different of map vs. flatMap by returning a list from each of them.  For example try returning from each of them:

   `Arrays.asList(indx, indx * 2, indx * 3)`

 * Calculate the sum of the list using reduce
