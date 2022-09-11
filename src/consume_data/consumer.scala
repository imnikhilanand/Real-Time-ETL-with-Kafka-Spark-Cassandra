/* Run this in code in terminal to download libraries to connect kafka with spark and cassandra with spark (must be done every time one starts the program)

bin/spark-shell --packages "com.datastax.spark:spark-cassandra-connector_2.12:3.0.0","org.apache.spark:spark-streaming-kafka-0-10_2.12:3.2.1"

*/

// Importing the libraries
import org.apache.spark.streaming._
import org.apache.spark.SparkContext
import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.TopicPartition

// creating sparkcontext
val sc = SparkContext.getOrCreate

// reading streaming data at an interval of 10 seconds
val ssc = new StreamingContext(sc, Seconds(10))

val preferredHosts = LocationStrategies.PreferConsistent

// listening to topic registered_user_2 (topic name)
val topics = List("registered_user_2")



