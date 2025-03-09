package com.gs310.bookstracker.esearch;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsAuthorRepository extends ElasticsearchRepository<EsAuthor, String> {

    // get authors by name
    @Query("{\"bool\": {\"must\": [{\"match\": {\"author_name\": {\"query\": \"?0\", \"operator\": \"and\"}}}]}}")
    List<EsAuthor> findEsAuthorByName(String name);
}
