package com.gs310.bookstracker.useractivity;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends CassandraRepository<UserActivity, UserActivityPrimaryKey> {

}
