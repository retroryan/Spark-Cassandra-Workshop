package simpleSpark;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;
import com.datastax.spark.connector.japi.SparkContextJavaFunctions;

public class LoadKVJ {

    public static String DATA_FILE_DIR = "/data/";
    public static String KJV_FILE = "kjvdat.txt";
    public static String STOP_WORDS = "stopWords.txt";
    public static List<String> DATA_FILES = Arrays.asList("LesMiserables.txt", "TheAdventuresOfSherlockHolmes.txt");

    public static void main(String[] args) {

        JavaSparkContext javaSparkContext = SparkConfSetup.getJavaSparkContext();
        CassandraConnector connector = SparkConfSetup.getCassandraConnector();

        setupCassandraTables(connector);

        loadAndSaveKJV(javaSparkContext);

        javaSparkContext.stop();

    }

    private static void loadAndSaveKJV(JavaSparkContext javaSparkContext) {

        JavaRDD<String> linesRDD = javaSparkContext.textFile(DATA_FILE_DIR + KJV_FILE);
        System.out.println("processing fileName = " + KJV_FILE + " with " + linesRDD.count() + " words.");

        JavaRDD<BibleVerse> versesRDD = linesRDD.map(LoadKVJ::parseLine);
        javaFunctions(versesRDD)
                .writerBuilder("search_demo", "verses", mapToRow(BibleVerse.class))
                .saveToCassandra();

    }

    private static BibleVerse parseLine(String line) {
        String[] splitStrs = line.toLowerCase().split("\\s*\\|\\s*");

        int chapter = Integer.parseInt(splitStrs[1]);
        String book = splitStrs[0];
        int verse = Integer.parseInt(splitStrs[2]);
        String body = splitStrs[3];

        return new BibleVerse(book, chapter, verse, body);
    }


    private static void analyzeWordCount(JavaSparkContext javaSparkContext) {
    }

    private static void setupCassandraTables(CassandraConnector connector) {
        try (Session session = connector.openSession()) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS search_demo WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
            session.execute("CREATE TABLE IF NOT EXISTS search_demo.verses (book TEXT, chapter INT, verse INT, body TEXT, PRIMARY KEY (book, chapter, verse))");
        }
    }
}
