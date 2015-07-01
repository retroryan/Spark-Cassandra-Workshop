package simpleSpark;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.RDDJavaFunctions;
import com.datastax.spark.connector.japi.SparkContextJavaFunctions;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import com.google.common.base.Objects;
import org.apache.hadoop.util.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class BasicReadWriteDemo {

    public static void main(String[] args) {

        JavaSparkContext javaSparkContext = SparkConfSetup.getJavaSparkContext();

        CassandraConnector connector = SparkConfSetup.getCassandraConnector();

        basicCassandraSession(connector);

        writePeopleToCassandra(javaSparkContext);

        readPeopleFromCassandra(javaSparkContext);

        javaSparkContext.stop();

    }

    private static void basicCassandraSession(CassandraConnector connector) {

        try(Session session = connector.openSession())  {
            session.execute("CREATE KEYSPACE IF NOT EXISTS test WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
            session.execute("CREATE TABLE IF NOT EXISTS test.key_value (key INT PRIMARY KEY, value VARCHAR)");
            session.execute("TRUNCATE test.key_value");

            session.execute("INSERT INTO test.key_value(key, value) VALUES (1, 'first row')");
            session.execute("INSERT INTO test.key_value(key, value) VALUES (2, 'second row')");
            session.execute("INSERT INTO test.key_value(key, value) VALUES (3, 'third row')");

            //this will be used in the next test
            session.execute("CREATE TABLE IF NOT EXISTS test.people (id INT, name TEXT, birth_date TIMESTAMP, PRIMARY KEY (id))");
            //secondary indexes are not recommended in production.  This is just a sample to demonstrate the connector
            session.execute("CREATE INDEX IF NOT EXISTS people_name_idx ON test.people(name)");
        }
    }

    /**
    *  Exercise 3 - Save a list of people to Cassandra
    **/
    private static void writePeopleToCassandra(JavaSparkContext javaSparkContext) {
        // here we are going to save some data to Cassandra...
        List<Person> people = Arrays.asList(
                Person.newInstance(1, "John", new Date()),
                Person.newInstance(2, "Anna", new Date()),
                Person.newInstance(3, "Andrew", new Date())
        );


        JavaRDD<Person> peopleRDD = javaSparkContext.parallelize(people);

        JavaPairRDD<Integer, Person> integerPersonJavaPairRDD = peopleRDD.keyBy(np -> np.getId());

        RDDJavaFunctions<Person> cassandraPersonRDD = CassandraJavaUtil.javaFunctions(peopleRDD);

        cassandraPersonRDD.writerBuilder("test", "people", CassandraJavaUtil.mapToRow(Person.class)).saveToCassandra();



    }

    /**
    *  Exercise 4 - Read a list of people from Cassandra
    **/
    private static void readPeopleFromCassandra(JavaSparkContext javaSparkContext) {

        SparkContextJavaFunctions sparkContextJavaFunctions = CassandraJavaUtil.javaFunctions(javaSparkContext);

        CassandraJavaRDD<Person> personCassandraJavaRDD = sparkContextJavaFunctions
                .cassandraTable("test", "people", CassandraJavaUtil.mapRowTo(Person.class));


        JavaRDD<String> readPeopleRDD =  personCassandraJavaRDD
                .map(Person::toString);

        System.out.println("Data as Person beans: \n" + StringUtils.join("\n", readPeopleRDD.collect()));


        //   select id from test.people where id=45;

        JavaRDD<String> rdd4 = sparkContextJavaFunctions
                .cassandraTable("test", "people")
                .select("id")
                .where("id = 45")
                .map(CassandraRow::toString);

        System.out.println("Data with only 'id' column fetched: \n" + StringUtils.join("\n", rdd4.collect()));

    }

    public static class Person implements Serializable {
        private Integer id;
        private String name;
        private Date birthDate;


        public static Person newInstance(Integer id, String name, Date birthDate) {
            Person person = new Person();
            person.setId(id);
            person.setName(name);
            person.setBirthDate(birthDate);
            return person;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("id", id)
                    .add("name", name)
                    .add("birthDate", birthDate)
                    .toString();
        }
    }
}
