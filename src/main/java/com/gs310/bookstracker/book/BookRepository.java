package com.gs310.bookstracker.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CassandraRepository<Book, String> {

    /*
    * to add custom methods to this repository,
    * we need find extend this class with another interface, to delegate the work
    *
    *
    * public interface BookRepository extends CassandraRepository<Book, String>, CustomRepository {
    *   List<Book> findBookByCriteria(Criteria criteria);
    * }
    *
    * CustomRepository.java (Interface)
    * @Repository
    * public interface CustomRepository {
    *   List<Book> findBookByCriteria(Criteria criteria);
    * }
    *
    * CustomRepositoryImpl.java (Class)
    * // naming convention is important, if CustomRepository is annotated as @Repository
    * // spring automatically searches for CustomRepository + "Impl" class
    * // observe that the implementation is not annotated with Repository, this is not required as naming conventions are followed
    * // if that is not the case, we can annotate this implementation as a repository, so spring knows to create the bean.
    * // so when the proxy tries to find bean that implements CustomRepository, it can find in the context
    * public class CustomRepositoryImpl implements CustomRepository {
    *
    *   @Autowired
    *   private CassandraTemplate cassandraTemplate;
    *
    *   @Override
    *   List<Book> findBookByCriteria(Criteria criteria) {
    *       // interact with Cassandra using CassandraTemplate
    *   }
    * }
    *
    * */

}
