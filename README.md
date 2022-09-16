# Real-Time-ETL-with-Kafka-Spark-Cassandra
In this project real time data streams pipeline was developed to process data produced by Kafka, processed by Spark in batches, and stored in Cassandra databases.

## Architecture

The real-time messages are pushed by a python script. The data is then produced by Kafka in the distributed system under a given topic. The produced data is consumed in real time by a consumer by listening to that specific topic. Consumer uses spark to losten to the streaming data. Once the data is received in batches, the spark push the data to Cassandra database. 

Below is the architecture of the end-to-end streaming pipeline.

<img src="img/architecture.png" width='80%' style="align-content: center;">




