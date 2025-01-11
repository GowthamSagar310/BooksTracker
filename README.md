# BooksTracker
Track your reading habit and organize all your books in one place. 

pipeline 
1. load the data (author and book data in that order)
   1. create a cassandra instance in GCP using datastax 
   2. get the secure-connect bundle (this is independent of spring) from datastax to connect to the instance
   3. 
2. search the data using openlibrary search api (move to elasticsearch instance later) 

format of data:
books (file) 
authors (file)

Note: 
1. if you are facing issues with connecting to astra, try changing your network. if you are in an office network, most probably it won't work.

what is datastax astra ? 
how to connect to cassandra instance through datastax ? 

what is spring data ?
why cassandra ? why not a RDMS like postgres ? 

how to connect to cassandra instance in GCP without intermediary like datastax ? 
how to deal with circular dependencies in spring? 
1. lazy loading ?

what is happening behind the scenes with secure-bundle ? how is spring able to connect to cassandra instance ?
add filters in search
- search in title
- search authors 
- search in content

TODO
- [x] connect to local cassandra


Design Principle 

What can be done more ?
Contact 

### References
1. https://openlibrary.org/developers/dumps
2. 