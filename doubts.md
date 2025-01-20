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


1. what is datastax astra ?
2. how to connect to cassandra instance through datastax ? 
3. what is spring data ? 
4. why cassandra ? why not a RDMS like postgres ?
5. how to connect to cassandra instance in GCP without intermediary like datastax ? 
6. how to deal with circular dependencies in spring?
   1. lazy loading ?
7. what is happening behind the scenes with secure-bundle ? how is spring able to connect to cassandra instance ?
add filters in search ? 