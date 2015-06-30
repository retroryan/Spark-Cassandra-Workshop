## Exercise 5 - Word Count Demo - Counting the words in a novel

The goal of this exercise is to get familiar with the more advanced functionality of Spark by reading a text file and storing a count of the words in Cassandra.  An important part of this is learning to use Pair RDD's.

Additional Functionality is provided on RDD's that are key / value pairs.  These RDDs are called pair RDDs.  They provide additional APIs that are performed on each key in the RDD.  Some common examples are reduceByKey which performs a reduce function that operates on each key.  Or there is a join operation which joins two RDD's by key.  

Pair RDD's are defined using a Tuple of 2.  Tuples are defined as "a finite ordered list of elements".  They can be thought of as a generic container of data. In the case of Pair RDD's the first element is the key and the second element is the value.  

In Java a Pair RDD can be created using the method `mapToPair`.  It operates on each element in an RDD and returns a new element that is a Tuple2.   The method 'mapToPair' then expects a function that takes a single element and returns a pair of key and value.

* Copy the data directory into the /tmp directory on BOTH the Spark Driver and the Spark Master where DSE is running.  The program will try and read the text files from the /tmp/data directory.

* The SparkWordCount can be run using:

 `dse spark-submit --class simpleSpark.SparkWordCount ./target/IntroSparkCassandra-0.1.jar`

* In the method sparkWordCount for each of the files in the constant  `DATA_FILES` read the file into a Spark RDD
  * `javaSparkContext.textFile(DATA_FILE_DIR + fileName);`

* Split each string into a list of words.  Hint - use flatMap and then split each line with `nextLine.split("\\s+")`

* Convert each word in the RDD to a Pair RDD of (Word, 1) using `mapToPair`.  The second value is what will be used to count each word in the list.

* Use `reduceByKey` to count all words.

* Map the results of the list to the `WordCountFileName` to make it easy to save out to Cassandra.

* Save the count of words to Cassandra

* Bonus filter out stop words to improve the accuracy of the count.

## Exercise 6 - Word Count Demo - Analyzing the results

* Read the results back out of the database for all datasets into WordCountFileName.

* Map the results to a Pair RDD with the count as the key

* Find the top word counts out of the RDD.
