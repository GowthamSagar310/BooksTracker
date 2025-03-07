package com.gs310.bookstracker.esearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// elastic search index for authors
// enables search on authors through authorId, name.

// index name where the documents will be stored.
@Document(indexName = "authors")
@Getter @Setter
public class EsAuthor {

    @Id
    private String authorId;

    // FieldType.Text -> analyzed (split into tokens) and indexed (stored in inverted index)
    // FieldType.Keyword -> not analyzed (stored as it is) and indexed (stored in inverted index). can be used for IDs, categories.
    // FieldType.Date -> date values

    @Field(type = FieldType.Text, name = "author_name")
    private String name;

    @Field(type = FieldType.Text, name = "personal_name")
    private String personalName;

}
