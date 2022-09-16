# Real-Time-ETL-with-Kafka-Spark-Cassandra
In this project real time data streams pipeline was developed to process data produced by Kafka, processed by Spark in batches, and stored in Cassandra databases.

## Architecture

The real-time messages are pushed by a python script. The data is then produced by Kafka in the distributed system under a given topic. The produced data is consumed in real time by a consumer by listening to that specific topic. Consumer uses spark to losten to the streaming data. Once the data is received in batches, the spark push the data to Cassandra database. 

Below is the architecture of the end-to-end streaming pipeline.

<p align="center">
	<img src="img/architecture.png" width='80%'>
</p>

## Producer

To produce syntheic data, Faker library is used that generates fake data which will later be sent to the producers. The format of the produced data is shown below

```

# importing the faker library
from faker import Faker

# creating the instance of the class
faker = Faker()

# function to generate synthetic data
def get_registered_user():
	return faker.name()+"$"+faker.address()+"$"+faker.year()

```

The data is produced using KafkaProduce library in Python on registered_user_2 topic.

```

# calling the class instance for the Kafka producer
producer = KafkaProducer(bootstrap_servers=['localhost:9092'],value_serializer=json_serializer)

# producing the data in the topic
producer.send("registered_user_2",registered_user)
		
```

## Consumer

To consume the data in real time, spark is used. The spark created a direct stream context on the kafka topic to listen to the incoming messages produced in real-time. Once these messages are arrived and grouped into mini batches, they are sent to the cassandra database. 


```

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

```

## Database connection

To keep data in disctributed database for analytical purposes, cassandra is used. The data coming form the spark batches are directly saved into cassandra tables. The keyspace for the cassandra table is created as my_keyspace and the table name is user_details_5.

```

# select statement to check all the entries from Kafka
select * from user_details_5;

```

 name                    | address                                                       | date
-------------------------+---------------------------------------------------------------+-------
            "James Olson |                    983 Alvarado Vista\nPort Matthew, MA 57760 | 2002"
             "Eric Olson |                              PSC 9053, Box 6656\nAPO AA 71822 | 1984"
       "Gabriella Steele |                      649 Steele Parks\nNew Adamstad, IN 74098 | 1979"
           "Sarah Garner |                21793 Diaz Rue Suite 260\nEmilymouth, NV 68222 | 2019"
          "Joshua Wilson |      6555 Herman Islands Suite 781\nNew Destinystad, DE 03728 | 1978"
          "Travis Parker |                              Unit 9943 Box 7215\nDPO AA 36455 | 1970"
            "Jill Rivera |               727 Linda Manors Apt. 542\nNew Marcia, SC 88348 | 2000"
          "Connie Werner |       69793 Derek Mills Apt. 332\nNorth Jeffreystad, CO 36671 | 1983"
            "Megan Baker |                        949 Nguyen Pass\nJohnborough, IL 70770 | 1987"


 ## Summary
 
 A real-time data pipelines is created that produces and consumes messages. The pipeline can be scaled on multiple machines in hormizontal manner. The incoming data is distributed among different nodes based on the key of the messages and partitioned based on the hash function. This pipeline can be used to create more complex and scalable data pipleine to solve more real world streaming problems.