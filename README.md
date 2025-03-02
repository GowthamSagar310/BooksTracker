# BooksTracker

Track your reading habits and organize all your books in one place.

## Pipeline

1. Load the data (author and book data in that order)
    1. Create a Cassandra instance in GCP using Datastax.
    2. Get the secure-connect bundle (independent of Spring) from Datastax to connect to the instance.
2. Search the data using OpenLibrary search API (move to Elasticsearch instance later).

## Note

1. If you are facing issues with connecting to Astra, try changing your network. If you are in an office network, it most probably won't work.

## TODO

- [x] Connect to local Cassandra
- [x] Add Postgres to store user identity information
- [x] Add support for sign-in / sign-up
- [ ] Add sign-in with Google support
- [ ] Search inside content
- [ ] Search using authors
- [x] Search using title
- [ ] Add local Elasticsearch support

## Contact

- Gmail: [gowthamsagartummeda@gmail.com](mailto:gowthamsagartummeda@gmail.com)
- Website: [gowthamsagar.com](http://gowthamsagar.com) (under construction)

## References

1. [OpenLibrary Dumps](https://openlibrary.org/developers/dumps)
2. [YouTube Playlist](https://youtu.be/uwuF9xa3Vyw?t=2970)
