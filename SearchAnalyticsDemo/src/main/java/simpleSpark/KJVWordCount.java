package simpleSpark;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;

public class KJVWordCount {

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

        JavaRDD<Verse> versesRDD = linesRDD.map(line -> parseLine(line));
        javaFunctions(versesRDD)
                .writerBuilder("search_demo", "verses", mapToRow(Verse.class))
                .saveToCassandra();
    }

    private static Verse parseLine(String line) {
        String[] splitStrs = line.toLowerCase().split("\\s*\\|\\s*");

        return new Verse(splitStrs[0], Integer.parseInt(splitStrs[1]), Integer.parseInt(splitStrs[2]), splitStrs[3]);
    }


    private static void analyzeWordCount(JavaSparkContext javaSparkContext) {
    }

    private static void setupCassandraTables(CassandraConnector connector) {
        try (Session session = connector.openSession()) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS search_demo WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
            session.execute("CREATE TABLE IF NOT EXISTS search_demo.verses (book TEXT, chapter INT, verse INT, body TEXT, PRIMARY KEY (book, chapter))");
            session.execute("TRUNCATE search_demo.verses");
        }
    }


    public static class Verse implements Serializable {
        private String book;
        private Integer chapter;
        private Integer verse;
        private String body;

        public Verse(String book, Integer chapter, Integer verse, String body) {
            this.book = book;
            this.chapter = chapter;
            this.verse = verse;
            this.body = body;
        }

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public Integer getChapter() {
            return chapter;
        }

        public void setChapter(Integer chapter) {
            this.chapter = chapter;
        }

        public Integer getVerse() {
            return verse;
        }

        public void setVerse(Integer verse) {
            this.verse = verse;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        @Override
        public String toString() {
            return "Verse{" +
                    "book='" + book + '\'' +
                    ", chapter=" + chapter +
                    ", verse=" + verse +
                    ", body='" + body + '\'' +
                    '}';
        }
    }

}
