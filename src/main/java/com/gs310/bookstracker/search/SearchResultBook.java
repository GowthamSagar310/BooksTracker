package com.gs310.bookstracker.search;

import java.util.List;

public class SearchResultBook {

    // observe the casing here, these are exactly how the JSON response contains.
    // but there are cases where we use camelCase in code, but in props file, the key name would be in snake case

    private String key;
    private String title;
    private List<String> author_i;
    private String cover_i;
    private int first_publish_year;

    public String getKey() {
        return key;
    }

    public SearchResultBook setKey(String key) {
        this.key = key;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SearchResultBook setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getAuthor_i() {
        return author_i;
    }

    public SearchResultBook setAuthor_i(List<String> author_i) {
        this.author_i = author_i;
        return this;
    }

    public String getCover_i() {
        return cover_i;
    }

    public SearchResultBook setCover_i(String cover_i) {
        this.cover_i = cover_i;
        return this;
    }

    public int getFirst_publish_year() {
        return first_publish_year;
    }

    public SearchResultBook setFirst_publish_year(int first_publish_year) {
        this.first_publish_year = first_publish_year;
        return this;
    }
}
