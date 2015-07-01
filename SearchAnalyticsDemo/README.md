#Spark and Cassandra Exercises

The goal of this exercise is to get familiar with the more advanced functionality of Spark by reading a text file and storing a count of the words in Cassandra.  An important part of this is learning to use Pair RDD's.

DataStax Enterprise needs to be run in Search and Analytics Mode for these exercises.  Start DSE with:

`dse cassandra -k -s`

## Loading the Bible into Cassandra 

* The text file kjvdat.txt contains the King James Version of the Bible.  First load the file into the Cassandra File System (CFS):
 
 `dse hadoop fs -copyFromLocal data/kjvdat.txt /data/kjvdat.txt`
 `dse hadoop fs -ls /data`

* Run the Application on DSE to load an initial version of the Bible into Cassandra:

`dse spark-submit --class simpleSpark.LoadKVJ ./target/SearchAnalyticsDemo-0.1.jar`

* Use the dsetool to create a solr core of the Bible Verses:

`dsetool create_core search_demo.verses reindex=true generateResources=true`

* Verify the Solr core was created by browsing to the [Solr Management Interface](http://localhost:8983/solr) and trying some basic queries.

* In cqlsh you can also run some queries, i.e.:

`select * from search_demo.verses where solr_query='{"q":"body:*Baptize*","facet":{"field":"book"}}';`

For example queries see the [DSE Search Tutorial](http://docs.datastax.com/en/datastax_enterprise/4.7/datastax_enterprise/srch/srchTutCQL.html)

## KJV Word Count Demo - Counting the words in the Bible

* The template class to load and analyze the Bible Verses from Cassandra is in KJVWordCount.  It can be run with:

`dse spark-submit --class simpleSpark.KJVWordCount ./target/SearchAnalyticsDemo-0.1.jar`

* Search and Analytics can be combined to find and analyze specific data.  In this exercise we will use search for verses that contain specific words and then count the number of words in that verse.

* Data can be filtered using a solr quer by adding a where clause to the function that loads the data.  For example:

```
        sparkContextJavaFunctions
              .cassandraTable("search_demo", "verses", mapRowTo(BibleVerse.class))
              .where("solr_query='body:*Baptize* AND book:mat'");
```                


Additional Functionality is provided on RDD's that are key / value pairs.  These RDDs are called pair RDDs.  They provide additional APIs that are performed on each key in the RDD.  Some common examples are reduceByKey which performs a reduce function that operates on each key.  Or there is a join operation which joins two RDD's by key.  

Pair RDD's are defined using a Tuple of 2.  Tuples are defined as "a finite ordered list of elements".  They can be thought of as a generic container of data. In the case of Pair RDD's the first element is the key and the second element is the value.  

In Java a Pair RDD can be created using the method `mapToPair`.  It operates on each element in an RDD and returns a new element that is a Tuple2.   The method 'mapToPair' then expects a function that takes a single element and returns a pair of key and value.

* Split each string into a list of words.  Hint - use flatMap and then split each line with `nextLine.split("\\s+")`

* Convert each word in the RDD to a Pair RDD of (Word, 1) using `mapToPair`.  The second value is what will be used to count each word in the list.

* Use `reduceByKey` to count all words.

* Map the results of the list to the `WordCountFileName` to make it easy to save out to Cassandra.

* Save the count of words to Cassandra

## Bonus Exercises - Analyzing the results

* Filter out stop words to improve the accuracy of the count.  A stopWords.txt file is included in the data directory.

* Map the results to a Pair RDD with the count as the key

* Find the top word counts out of the RDD.
