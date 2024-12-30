package com.gs310.bookstracker.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    private final String COVER_IMAGE_BASE_URL = "http://covers.openlibrary.org/b/id/";

    /*
    * observe that BookRepository is not a concrete implementation. it is just an interface extending CassandraRepository
    * we are not creating the implementation, Spring Data Cassandra is doing that for us
    * CassandraRepository contains the pre-defined methods like findById(id), save(entity)
    *
    * when the spring-boot scans for components, repositories during startup
    * it finds BookRepository, because of @Repository annotation, it registers it as a bean in the application context and
    * creates a "dynamic proxy" (using RepositoryFactory), which is the implementation of the interface BookRepository.
    * this proxy gets injected during @Autowired. understand that the proxy itself does not contain logic for CRUD ops,
    * these calls are delegated to the CassandraRepository.
    * */

    @Autowired
    BookRepository bookRepository;

    @GetMapping(value = "/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model) {
        Optional<Book> optionalBook =  bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String coverImageUrl = "/images/no-image.png"; // local image
            if (book.getCoverIds() != null && !book.getCoverIds().isEmpty()) {
                coverImageUrl = COVER_IMAGE_BASE_URL + book.getCoverIds().get(0) + "-M.jpg";
            }
            model.addAttribute("coverImage", coverImageUrl);
            model.addAttribute("book", book);
            return "book";
        }
        return "book-not-found";
    }

}
