package com.gs310.bookstracker.search;

// this is a java object mimicking the JSON response
// from /search?q={query}
// JSON - object convert is taken care by the spring automatically

import java.util.List;

public class SearchResult {

    private int numFound;
    private List<SearchResultBook> docs;

    public List<SearchResultBook> getDocs() {
        return docs;
    }

    public SearchResult setDocs(List<SearchResultBook> docs) {
        this.docs = docs;
        return this;
    }

    public int getNumFound() {
        return numFound;
    }

    public SearchResult setNumFound(int numFound) {
        this.numFound = numFound;
        return this;
    }



}
