package com.gs310.bookstracker.esearch;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsBookRepository extends ElasticsearchRepository<EsBook, String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"book_name\": {\"query\": \"?0\", \"operator\": \"and\"}}}]}}")
    List<EsBook> findEsBookByName(String title);

    List<EsBook> findEsBookByAuthorIdsIn(List<String> authorIds);
}
