package simpleSpark;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.RDDJavaFunctions;
import com.datastax.spark.connector.japi.SparkContextJavaFunctions;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import com.google.common.base.Objects;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapRowTo;

public class KJVWordCount {

    public static void main(String[] args) {

        JavaSparkContext javaSparkContext = SparkConfSetup.getJavaSparkContext();

        CassandraConnector connector = SparkConfSetup.getCassandraConnector();

        loadAnalyzeBible(javaSparkContext);

        javaSparkContext.stop();

    }


    /**
    *  Exercise  - Read a verse from the Bible out of Cassandra and Analyze it With Spark
    **/
    private static void loadAnalyzeBible(JavaSparkContext javaSparkContext) {

        SparkContextJavaFunctions sparkContextJavaFunctions = javaFunctions(javaSparkContext);

        CassandraJavaRDD<CassandraRow> facetedRDD = sparkContextJavaFunctions
                .cassandraTable("search_demo", "verses")
                .where("solr_query='body:*baptize*, facet:{field:book}'");

        facetedRDD.collect().forEach(System.out::println);

    }

}
