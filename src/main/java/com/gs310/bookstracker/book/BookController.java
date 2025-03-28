package com.gs310.bookstracker.book;

import com.gs310.bookstracker.SecurityUtil;
import com.gs310.bookstracker.esearch.EsBook;
import com.gs310.bookstracker.esearch.EsBookRepository;
import com.gs310.bookstracker.useractivity.UserActivity;
import com.gs310.bookstracker.useractivity.UserActivityPrimaryKey;
import com.gs310.bookstracker.useractivity.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // cassandra
    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserActivityRepository userActivityRepository;

    @Autowired
    EsBookRepository esBookRepository;

    @GetMapping(value = "/books/{bookId}")
    public String getBookFromSearch(@PathVariable String bookId, Model model) {

        // get user details from authentication object
        String userId = SecurityUtil.getUserId(SecurityContextHolder.getContext().getAuthentication());

        Optional<EsBook> optionalEsBook = esBookRepository.findById(bookId);
         if (optionalEsBook.isPresent()) {

            EsBook esBook = optionalEsBook.get();
            String coverImageUrl = "/images/no-image.png"; // local image
            if (esBook.getCoverId() != null && !esBook.getCoverId().isEmpty()) {
             coverImageUrl = COVER_IMAGE_BASE_URL + esBook.getCoverId() + "-L.jpg";
            }
            model.addAttribute("coverImage", coverImageUrl);
            model.addAttribute("book", esBook);

            // TODO: should move this to elastic search
            if (userId != null) {
                model.addAttribute("loginId", userId);
                UserActivityPrimaryKey userActivityPrimaryKey = new UserActivityPrimaryKey();
                userActivityPrimaryKey.setUserId(userId);
                userActivityPrimaryKey.setBookId(bookId);
                Optional<UserActivity> optionalUserActivity = userActivityRepository.findById(userActivityPrimaryKey);
                if (optionalUserActivity.isPresent()) {
                    System.out.println(optionalUserActivity.get());
                    model.addAttribute("userActivity", optionalUserActivity.get());
                } else {
                    model.addAttribute("userActivity", new UserActivity());
                }
            }
            return "book";
         }
        return "book-not-found";
    }

    /* Code for Searching books from Cassandra

    @GetMapping(value = "/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User principal) {

        // search in cassandra. TODO: move to elastic search
        Optional<Book> optionalBook =  bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            String coverImageUrl = "/images/no-image.png"; // local image
            if (book.getCoverIds() != null && !book.getCoverIds().isEmpty()) {
                coverImageUrl = COVER_IMAGE_BASE_URL + book.getCoverIds().get(0) + "-M.jpg";
            }
            model.addAttribute("coverImage", coverImageUrl);
            model.addAttribute("book", book);

            // if the user is logged-in, send the loginID
            if (principal != null && principal.getAttribute("login") != null) {
                // why do we need the whole principal.getAttribute("login") ?
                // boolean value not enough ?
                model.addAttribute("loginId", principal.getAttribute("login"));

                // if the user have read this book already / have some form of interaction with the book
                // we need to display that, if he is logged in.

                // find the activity information user UserActivityPrimaryKey

                UserActivityPrimaryKey userActivityPrimaryKey = new UserActivityPrimaryKey();
                userActivityPrimaryKey.setUserId(principal.getAttribute("login"));
                userActivityPrimaryKey.setBookId(bookId);

                Optional<UserActivity> optionalUserActivity = userActivityRepository.findById(userActivityPrimaryKey);

                if (optionalUserActivity.isPresent()) {
                    System.out.println(optionalUserActivity.get());
                    model.addAttribute("userActivity", optionalUserActivity.get());
                } else {
                    model.addAttribute("userActivity", new UserActivity());
                }

            }
            return "book";
        }
        return "book-not-found";
    }

    */
}
