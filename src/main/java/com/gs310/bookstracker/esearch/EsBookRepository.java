package com.gs310.bookstracker.esearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsBookRepository extends ElasticsearchRepository<EsBook, String> {

    List<EsBook> findEsBookByNameContaining(String title);

}
