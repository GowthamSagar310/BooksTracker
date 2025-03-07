package com.gs310.bookstracker.esearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsAuthorRepository extends ElasticsearchRepository<EsAuthor, String> {

}
