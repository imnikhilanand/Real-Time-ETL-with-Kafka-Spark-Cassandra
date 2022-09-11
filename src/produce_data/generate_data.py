"""

Creating keyspaces in Cassandra:

CREATE KEYSPACE my_keyspace WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};


"""

from faker import Faker

faker = Faker()

def get_registered_user():
	return faker.name()+"$"+faker.address()+"$"+faker.year()
	"""return {
		"name": faker.name(),
		"address":faker.address(),
		"created_at": faker.year()
	}"""

if __name__ == "__main__":
	print(get_registered_user())
