# BooksTracker
Track your reading habit and organize all your books in one place. 

#### Pipeline 
1. load the data (author and book data in that order)
   1. create a cassandra instance in GCP using datastax 
   2. get the secure-connect bundle (this is independent of spring) from datastax to connect to the instance
2. search the data using openlibrary search api (move to elasticsearch instance later) 

format of data:
books (file) 
authors (file)

Note: 
1. if you are facing issues with connecting to astra, try changing your network. if you are in an office network, most probably it won't work.

#### TODO
- [x] connect to local cassandra
- [x] add postgres to store user identity information
- [x] add support for sign-in / sign-up
- [ ] add sign in with google support
- [ ] search inside content 
- [ ] search using authors 
- [x] search using title 
- [ ] add local elastic search support  

#### Contact
- gmail: gowthamsagartummeda@gmail.com
- website: gowthamsagar.com (under construction)

#### References
1. https://openlibrary.org/developers/dumps
2. https://youtu.be/uwuF9xa3Vyw?t=2970