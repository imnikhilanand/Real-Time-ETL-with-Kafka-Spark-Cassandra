"""
_______________________________________________________________________________________________________________________

Command to start Zookeeper:

bin/zookeeper-server-start.sh config/zookeeper.properties
_______________________________________________________________________________________________________________________

Command to start Kafka:

bin/kafka-server-start.sh config/server.properties
_______________________________________________________________________________________________________________________

Command for creating Kafka Clusters :

bin/kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 4
_______________________________________________________________________________________________________________________

Commad to start Cassandra:

sudo systemctl start cassandra

Check cassandra is running or not

sudo systemctl status cassandra



"""

# importing the libraries
from kafka import KafkaProducer
import json
from generate_data import get_registered_user
import time

# creating instance

def json_serializer(data):
	return json.dumps(data).encode('utf-8')

producer = KafkaProducer(bootstrap_servers=['localhost:9092'],value_serializer=json_serializer)

if __name__ == "__main__":
	for i in range(0,50):
		registered_user = get_registered_user()
		print(registered_user)
		producer.send("registered_user_2",registered_user)
		time.sleep(3)









