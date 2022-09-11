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

// assigning kafka parameters
val kafkaParams = Map(
  "bootstrap.servers" -> "localhost:9092",
  "key.deserializer" -> classOf[StringDeserializer],
  "value.deserializer" -> classOf[StringDeserializer],
  "group.id" -> "spark-streaming-notes",
  "auto.offset.reset" -> "earliest"
)

// offsets
val offsets = Map(new TopicPartition("registered_user_2", 0) -> 2L)

// creating direct sream from kafka to spark
val dstream = KafkaUtils.createDirectStream[String, String](
  ssc,
  preferredHosts,
  ConsumerStrategies.Subscribe[String, String](topics, kafkaParams, offsets))

// printing the streaming data
dstream.map(record=>(record.value().toString)).print

// pushing data from spark to Cassandra database
dstream.map(line => {val arr = line.value().toString.filter(_ >= ' ').split("""\$"""); (arr(0),arr(1),arr(2))}).saveToCassandra("my_keyspace", "user_details_5", SomeColumns("name","address","date"))

// starting the spark context
ssc.start

