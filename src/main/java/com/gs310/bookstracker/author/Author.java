package com.gs310.bookstracker.author;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

// this a model class (schema) for author table in the db
// the schema-action mentioned in application.yml makes sure that the table is created only if it does not exist.

// if keyspace is not mentioned in application props, can add here @Table(value = "author_by_id", keyspace = "main")
@Table(value = "author_by_id")
public class Author {

    /*
    *   @PrimaryKeyColumn -> says to cassandra to create "author_id" column in "author_by_id table"
    *          name -> column name
    *          ordinal -> order of primary key column (if there are multiple primary keys)
    *          partition type ->
    * */

    @Id @PrimaryKeyColumn(name = "author_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String authorId;

    @Column("author_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column("personal_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String personalName;


    public String getAuthorId() {
        return authorId;
    }

    public Author setAuthorId(String authorId) {
        this.authorId = authorId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Author setName(String name) {
        this.name = name;
        return this;
    }

    public String getPersonalName() {
        return personalName;
    }

    public Author setPersonalName(String personalName) {
        this.personalName = personalName;
        return this;
    }
}
