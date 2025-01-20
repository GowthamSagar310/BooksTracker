package com.gs310.bookstracker.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Table(value = "book_by_id")
public class Book {

    @Id @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String bookId;

    @Column("book_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column("book_description")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String description;

    @Column("published_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate publishedDate;

    // cover ids -> array of ids
    // each id corresponds to the cover image of the book
    @Column("cover_ids")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> coverIds;

    // each record in the data dump contains,
    // a json array of authors information, which essentially contain the id of the author
    // we are converting the id into the name (by querying author_by_id table) and adding it into the book_by_id table
    // observe the duplication here.
    @Column("author_names")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorNames;


    // why do we again require author id ?
    // when a user clicks the author, author's details page needs to be opened
    // which are present in the author_by_id table
    // it would be easy if we have the id present
    // or else we need to do a like query, search for the author in the author_by_id table and get details
    // or if we store only the id instead of name, every time we load a book, we need to query the db for name
    // to avoid these queries, we are duplicating the data
    // observe the pattern / deviation from RDBMS, we are structuring our data by the queries and essentially duplicating
    @Column("author_ids")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorIds;

    public String getBookId() {
        return bookId;
    }

    public Book setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Book setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public Book setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
        return this;
    }

    public List<String> getCoverIds() {
        return coverIds;
    }

    public Book setCoverIds(List<String> coverIds) {
        this.coverIds = coverIds;
        return this;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public Book setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
        return this;
    }

    public List<String> getAuthorId() {
        return authorIds;
    }

    public Book setAuthorIds(List<String> authorId) {
        this.authorIds = authorId;
        return this;
    }
}
