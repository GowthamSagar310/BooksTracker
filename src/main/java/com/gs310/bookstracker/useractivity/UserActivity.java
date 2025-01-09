package com.gs310.bookstracker.useractivity;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

// how is userId interacting with bookId
// did he start / complete / rating

@Table(value = "user_activity_for_book_id")
public class UserActivity {

    /*
     *  @Id
     *  @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
     *  private String bookId;
     *
     *  @Id
     *  @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
     *  private String userId;
     *
     *  there cannot be two partition ids (that can be used independently) for a single table
     *  we need to create a composite primary key
     *
     * */


    @PrimaryKey
    private UserActivityPrimaryKey primaryKey;

    @Column("start_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate startDate;

    @Column("completed_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate completedDate;

    @Column("reading_status")
    @CassandraType(type =  CassandraType.Name.TEXT)
    private String readingStatus;

    @Column("rating")
    @CassandraType(type = CassandraType.Name.INT)
    private int rating;

    public UserActivityPrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public UserActivity setPrimaryKey(UserActivityPrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public UserActivity setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public UserActivity setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
        return this;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public UserActivity setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public UserActivity setRating(int rating) {
        this.rating = rating;
        return this;
    }
}
