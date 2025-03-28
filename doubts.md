#### add "users" schema
```postgresql
create table users (
    user_id serial primary key,
    email varchar(250) unique not null,
    password varchar(250) not null,
    role varchar(50) not null
);

insert into users (email, password, role) values ('abc@mail.com', '{noop}DummyUser875', 'read');
insert into users (email, password, role) values ('admin@mail.com', '{bcrypt}$2a$12$AUi8iLlohkNutrQBSCY2wOTLdaqajC9BzAqIU7541cE5BmXrM0sXq', 'admin');
```


### Primary-key vs Partition-key vs Clustering-key

primary-key = partition-key + clustering-key (optional)

| **Primary Key**                              | **Partition Key**                          | **Clustering Key**                          |
|----------------------------------------------|--------------------------------------------|--------------------------------------------|
| Uniquely identifies a row in a table.        | Determines how data is distributed across nodes. | Determines the order of rows within a partition. |
| Consists of the **partition key** and optional **clustering keys**. | Subset of the primary key.                 | Subset of the primary key, excluding the partition key. |

#### Single-column Primary Key
```postgresql
    CREATE TABLE books (
        book_id TEXT PRIMARY KEY,
        title TEXT,
        author TEXT
    );
```
- **Primary Key:** `book_id`
- **Partition Key:** `book_id`
- **Clustering Key:** None

**Usage:**
- Rows are uniquely identified by `book_id`.
- Each `book_id` determines which node stores the data. (partitioner (consistent hashing to minimize redistributions) determines based on `book_id`)

#### Composite Primary Key with Clustering Key
```postgresql
    CREATE TABLE books (
        book_id TEXT,
        chapter_id INT,
        content TEXT,
        PRIMARY KEY (book_id, chapter_id)
    );
```
- **Primary Key:** `(book_id, chapter_id)`
- **Partition Key:** `book_id`
- **Clustering Key:** `chapter_id`

**Usage:**
- Data with the same `book_id` is stored on the same node.
- Within the partition for a `book_id`, rows are ordered by `chapter_id`.

**Example Query:**
```postgresql
    SELECT * FROM books WHERE book_id = 'B001' AND chapter_id = 2;
```

- choose partition keys such that the data is distributed evenly across nodes. (partition is automated)
- clustering keys can be used for when querying data by date ranges or such.
--- 

Doubts
---
1. What is Datastax Astra ?
- DataStax Astra - DBaas (Database as a Service) built on Apache Cassandra.
- We can create a Cassandra database instance in the cloud using DataStax Astra.
- Highly available, scalable and secure.
- Supports multiple cloud providers like AWS, GCP, Azure.
- We connect to the database instance using the secure bundle (which contains the necessary configurations and certificates to make a secure connection to the database) provided by DataStax Astra.
---
2. what is spring data ?
- Spring Data is a part of the Spring Framework that simplifies data access layer by providing easy-to-use abstractions for various data stores like RDBMS, NoSQL, etc.
- Spring Data is a broader project focused on simplifying data access across various data stores, both relational and NoSQL.
- Spring Data JPA, Spring Data Cassandra, Spring Data Redis, Spring Data MongoDB, etc. are some of the modules provided by Spring Data.
- Spring Data JPA is specifically designed for RDBMS, while for other NoSQL databases, Spring Data Cassandra, Spring Data MongoDB etc. are used. 
- Spring Data JPA is implementation JPA (Java Persistence API) for RDBMS.
- [Spring Data JPA vs Hibernate](https://stackoverflow.com/questions/23862994/whats-the-difference-between-hibernate-and-spring-data-jpa)
---
3. JDBC vs JPA vs Hibernate vs Spring Data JPA
- JDBC (Java Database Connectivity):
    - Low-level API for interacting with databases.
    - Requires writing SQL queries and handling connections, statements, result sets, etc.
    - Tedious and error-prone.
    - Manual mapping of results to Java objects
- JPA (Java Persistence API):
    - Standard API for object-relational mapping in Java.
    - Often avoids direct SQL, uses JPQL/Criteria API. 
    - Provides a set of annotations to map Java objects to database tables.
    - Reduces boilerplate code and simplifies data access layer.
- Hibernate (ORM Framework):
    - Popular ORM framework that implements JPA.
    - Provides additional features like caching, lazy loading, etc.
    - Simplifies database interactions and improves performance.
- Spring Data JPA (Data Access Layer):
   - Spring Data JPA builds upon JPA implementations like Hibernate, using them as the underlying ORM.
   - It reduces boilerplate code through conventions and repository interfaces.
   - simplifying database access by automatically generating common queries and handling transactions (findUserByEmail, saveUser, etc.)
---
4. Why cassandra ? why not a RDMS like postgres ?
---
5. How to connect to Cassandra instance in GCP without intermediary like datastax ?
- can use BigTable or Datastore in GCP or host an VM with a cassandra instance running, but not recommended, as it is not completely managed. (need to handle backups, scaling, etc.)
- [GCP guide to get started with cassandra in a compute instance](https://github.com/GoogleCloudPlatform/compute-cassandra-python)
- [BigTable Documentation](https://cloud.google.com/bigtable/docs/reference/)
---

6. Circular Dependency in Spring
- If Bean A depends on Bean B and Bean B depends on Bean A, it creates a circular dependency. 
   1. lazy loading 

---
7. SLF4J vs Logback vs Log4j
- SLF4J (Simple Logging Facade for Java):
    - An abstraction layer for various logging frameworks.
    - Provides a simple API for logging, allowing the underlying logging framework to be changed without changing the application code.
- Logback:
    - A logging framework developed by the creator of Log4j.
    - It is the successor to Log4j and provides more features and better performance.
    - Logback is the default logging framework in Spring Boot (which is used by SLF4J).
---

## Entities involved 
   1. Book (cassandra)
   2. Author (cassandra)
   3. UserActivity (cassandra)
   4. User (postgres)

## Details Regarding User Registration
- User enters email, password, username to create an account. User can log in using email / username along with password.
- User passwords are encrypted using bcrypt hashing algorithm before storing in the database.
- "UserEntity" file contains the schema for the "users" table in the postgres database. JPA Entity. 
- "UserRepository" is used to interact with the "users" table in the database. Spring Data provides these CRUD operations. userRepository.save(), userRepository.findByEmail(), userRepository.findByUsername(), etc.
- 
- "LoginController" is used to handle the login requests involving sign-in, sign-up, sign-out, forgot password, reset password. 
- 
- "CustomAuthenticationFailureHandler" is used to handle cases where the user authentication fails. If the user enters wrong credentials, the authentication failure handler is used to display custom error message. 
- "CustomOAuth2UserService" is used to check if the email of the user is already present in the database before signing up using "Sign-in with Github" / "Sign-in with Google". If the email is already present in the user table, we do not want to create a new user using this mail, as this will create discrepancies in the database, where the same user can have multiple accounts. (multiple records in the database). To avoid problems like merging accounts, maintaining OAuth provider details, and to keep it simple, multiple accounts for the same user is avoided. 
- email, username are unique in the user table.

--- 
- [ ] Search for books by author, title, content. 
  - [ ] search using openlibrary API
  - [ ] search using local elastic search instance 
- [ ] Sign-in, Sign-up, Sign-out, forgot password, reset password.
  - [ ] Sign-in with google, github. 
- [ ] Rate books, start date, end date, mark reading status. 
- [ ] Show user's past books read, currently reading in home page after login.
