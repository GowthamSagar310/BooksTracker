package com.gs310.bookstracker.userbooks;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

// userId -> bookId, authors, coverIds
// what is difference between @PrimaryKey and @PrimaryKeyColumn ?

@Table(value = "books_by_user")
public class BooksByUser {


    // why PARTITIONED ? what does ordinal 0 mean ?
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    // why CLUSTERED ? what does ordinal 1 mean ?
    @PrimaryKeyColumn(name = "book_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = CassandraType.Name.TEXT)
    private String bookId;

    @CassandraType(type = CassandraType.Name.TIMEUUID)
    private UUID timeUUID;

    // currently reading books are shown first. so ascending
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    @CassandraType(type = CassandraType.Name.TEXT)
    private String readingStatus;

    // observe that book data is duplicated.
    @Column("book_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String bookName;

    @Column("author_names")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorNames;

    @Column("cover_ids")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> covertIds;

    @Column("rating")
    @CassandraType(type = CassandraType.Name.INT)
    private int rating;

    // transient fields are not stored, then why make it a field here ?
    @Transient
    private String coverUrl;

    public BooksByUser() {
        this.timeUUID = Uuids.timeBased();
    }

    public String getId() {
        return id;
    }

    public BooksByUser setId(String id) {
        this.id = id;
        return this;
    }

    public String getBookId() {
        return bookId;
    }

    public BooksByUser setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public UUID getTimeUUID() {
        return timeUUID;
    }

    public BooksByUser setTimeUUID(UUID timeUUID) {
        this.timeUUID = timeUUID;
        return this;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public BooksByUser setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
        return this;
    }

    public String getBookName() {
        return bookName;
    }

    public BooksByUser setBookName(String bookName) {
        this.bookName = bookName;
        return this;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public BooksByUser setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
        return this;
    }

    public List<String> getCovertIds() {
        return covertIds;
    }

    public BooksByUser setCovertIds(List<String> covertIds) {
        this.covertIds = covertIds;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public BooksByUser setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public BooksByUser setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
        return this;
    }

    // why equals method ?

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BooksByUser that = (BooksByUser) o;
        return Objects.equals(id, that.id) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(bookId);
        return result;
    }
}
