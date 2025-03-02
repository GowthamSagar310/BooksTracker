package com.gs310.bookstracker.useractivity;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

// userId + bookId

@PrimaryKeyClass // so that cassandra knows this class acts as a primary key
public class UserActivityPrimaryKey {

    @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String bookId;

    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String userId;

    public String getBookId() {
        return bookId;
    }

    public UserActivityPrimaryKey setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserActivityPrimaryKey setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
