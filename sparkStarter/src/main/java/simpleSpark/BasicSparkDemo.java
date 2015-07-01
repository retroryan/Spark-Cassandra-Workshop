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

        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        JavaRDD<Integer> javaRDD = javaSparkContext.parallelize(intList);

        JavaRDD<Integer> newRDD = javaRDD.map(elm -> elm * 3);
        newRDD.foreach(elm -> System.out.println("elm = " + elm));

        JavaRDD<Integer> scndRDD = newRDD.map(elm -> elm * 3);
        scndRDD.foreach(elm -> System.out.println("elm = " + elm));


        JavaRDD<Integer> flatMappedRDD = scndRDD
                .flatMap(indx -> Arrays.asList(indx, indx * 2, indx * 3));

        flatMappedRDD.foreach(elm -> System.out.println("elm = " + elm));

        String lineageInfo = flatMappedRDD.toDebugString();
        System.out.println("lineageInfo = " + lineageInfo);

        JavaRDD<Integer> distinctRDD = flatMappedRDD
                .distinct();

        distinctRDD.foreach(indx -> System.out.println("indx = " + indx));



        javaSparkContext.stop();
    }
}
