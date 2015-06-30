package simpleSpark;

import com.datastax.spark.connector.cql.CassandraConnector;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;

public interface SparkConfSetup {

    static public SparkConf getSparkConf() {
        return new SparkConf()
                .setAppName("SimpleSpark");
    }

    static public JavaSparkContext getJavaSparkContext() {
        SparkContext sparkContext = new SparkContext(getSparkConf());
        return new JavaSparkContext(sparkContext);
    }

    static public CassandraConnector getCassandraConnector() {
        return CassandraConnector.apply((getSparkConf()));
    }
}
