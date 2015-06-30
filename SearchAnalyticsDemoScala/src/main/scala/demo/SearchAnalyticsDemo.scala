package demo

import com.datastax.spark.connector.cql.CassandraConnector
import com.typesafe.sparkworkshop.util.TextUtil._
import org.apache.spark._
import com.datastax.spark.connector._

//  ~/dse-4.7.0/bin/dse spark-submit --class demo.SearchAnalyticsDemo ./target/scala-2.10/SearchAnalyticsDemo-assembly-0.2.0.jar

object SearchAnalyticsDemo {
  def main(args: Array[String]) {

    val conf = new SparkConf()
      .setAppName("SearchAnalyticsDemo")

    val sc = new SparkContext(conf)

    try {
      setup(conf)
      loadAndSaveKJV(sc)
    }
    finally {
      sc.stop()
    }
  }

  def setup(conf:SparkConf) {


    CassandraConnector(conf).withSessionDo { session =>
      session.execute( """CREATE KEYSPACE IF NOT EXISTS search_demo with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1 }""")
      session.execute( """use search_demo""")

      session.execute(
        """CREATE TABLE IF NOT EXISTS kjv (
          |book text ,
          |chapter int,
          |verse int,
          |body text,
          |PRIMARY KEY (book, chapter)
          |)""".stripMargin)
    }
  }


  def loadAndSaveKJV(sc: SparkContext): Unit = {
    val input = sc.textFile("/data/kjvdat.txt")
      .map(line => toKJV(line))
      .collect{ case nxtKJV:Some[KJV] => nxtKJV.get }

    input.saveToCassandra("search_demo","kjv")

    input.take(20).foreach { nxt => {
      println(nxt)
    }
    }
  }
}
