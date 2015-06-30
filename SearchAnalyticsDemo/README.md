#Advance Spark and Cassandra Exercises

## Loading the Bible into Cassandra 

* The text file kjvdat.txt contains the King James Version of the Bible.  First load the file into the Cassandra File System (CFS):
 
 `dse hadoop fs -copyFromLocal data/kjvdat.txt /data/kjvdat.txt`
 `dse hadoop fs -ls /data`

* Run the Application on DSE to load an initial version of the Bible into Cassandra:

`dse spark-submit --class simpleSpark.KJVWordCount ./target/SearchAnalyticsDemo-0.1.jar`

