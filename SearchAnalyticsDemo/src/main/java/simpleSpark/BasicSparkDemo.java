package simpleSpark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class BasicSparkDemo {


    public static void main(String[] args) {

        JavaSparkContext javaSparkContext = SparkConfSetup.getJavaSparkContext();

        //Create a basic Array List here and convert it to a JavaRDD
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        JavaRDD<Integer> listRDD = javaSparkContext.parallelize(intList);
        Integer sum = listRDD.reduce((currentCount, nextElement) -> currentCount + nextElement);

        System.out.println("sum = " + sum);
        System.out.println("listRDD = " + listRDD.toDebugString());

        javaSparkContext.stop();
    }
}
