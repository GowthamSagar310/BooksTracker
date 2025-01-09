package com.gs310.bookstracker.userbooks;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksByUserRepository extends CassandraRepository<BooksByUser, String> {

    // what is Slice ? what is Pageable ?
    // CassandraPageRequest

    Slice<BooksByUser> findAllById(String userId, Pageable pageable);

}
