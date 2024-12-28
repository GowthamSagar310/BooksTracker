# BooksTracker
track your book reading habit 

pipeline 
1. load the data (author and book data in that order)
   1. create a cassandra instance in GCP using datastax 
   2. get the secure-connect bundle (this is independent of spring) from datastax to connect to the instance
   3. 
2. search the data using openlibrary search api (move to elasticsearch instance later) 
3.  

format of data:
books (file) 
authors (file)

Note: 
1. if you are facing issues with connecting to astra, try changing your network. if you are in an office network, most probably it won't work.

what is datastax astra ? 
how to connect to cassandra instance through datastax ? 

what is spring data ?
why cassandra ? why not a RDMS like postgres ? 

how to connect to local cassandra instance ? 
how to connect to cassandra instance in GCP without intermediary like datastax ? 

how to deal with circular dependencies in spring? 
1. lazy loading ? 
