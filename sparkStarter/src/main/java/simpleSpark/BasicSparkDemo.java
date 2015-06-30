package simpleSpark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class BasicSparkDemo {

    public static JavaSparkContext getJavaSparkContext() {
        SparkConf sparkConf = new SparkConf()
                .setAppName("BasicSparkDemo")
                .setMaster("local[1]");

        SparkContext sparkContext = new SparkContext(sparkConf);
        return new JavaSparkContext(sparkContext);
    }

    public static void main(String[] args) {

        JavaSparkContext javaSparkContext = getJavaSparkContext();

        //Create a basic Array List here and convert it to a JavaRDD
        javaSparkContext.stop();
    }
}
