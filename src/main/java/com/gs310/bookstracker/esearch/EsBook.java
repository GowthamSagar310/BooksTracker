package com.gs310.bookstracker.esearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;

// elastic search entity for books
// enables search on books through bookId, name, description, publishedDate, authorNames

@Document(indexName = "books")
@Getter @Setter
public class EsBook {

    @Id
    private String bookId;

    @Field(type = FieldType.Text, name = "book_name")
    private String name;

    @Field(type = FieldType.Text, name = "book_description")
    private String description;

    @Field(type = FieldType.Date, name = "published_date")
    private LocalDate publishedDate;

    @Field(type = FieldType.Text, name = "author_names")
    private List<String> authorNames;

    // might be useful in the future, to directly get author details from elastic search
    @Field(type = FieldType.Keyword, name = "author_ids")
    private List<String> authorIds;

    @Field(type = FieldType.Keyword, name = "cover_id")
    private String coverId;

}
