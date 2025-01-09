package com.gs310.bookstracker.useractivity;

import com.gs310.bookstracker.book.Book;
import com.gs310.bookstracker.book.BookRepository;
import com.gs310.bookstracker.userbooks.BooksByUser;
import com.gs310.bookstracker.userbooks.BooksByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Optional;


@Controller
public class UserActivityController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private BooksByUserRepository booksByUserRepository;

    // under the book view
    // if the user is logged in, show a form with details (startDate, completedDate, rating)

    @PostMapping("/addUserBook")
    public ModelAndView addBookForUser(
            @RequestBody MultiValueMap<String, String> formData,
            @AuthenticationPrincipal OAuth2User principal) {

        // if the user is not loggedIn, adding an entry against user-book-activity does not make sense
        if (principal == null || principal.getAttribute("login") == null) {
            return null;
        }

        System.out.println(formData);

        // get the book's details that user is interacting with
        String bookId = formData.getFirst("bookId");

        // ideally bookId shouldn't be null
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            return new ModelAndView("redirect:/");
        }
        // user activity with book
        UserActivity userActivity = new UserActivity();
        UserActivityPrimaryKey userActivityPrimaryKey = new UserActivityPrimaryKey();
        userActivityPrimaryKey.setUserId(principal.getAttribute("login"));
        userActivityPrimaryKey.setBookId(bookId);

        userActivity.setPrimaryKey(userActivityPrimaryKey);
        userActivity.setStartDate(LocalDate.parse(formData.getFirst("startDate")));
        userActivity.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        userActivity.setReadingStatus(formData.getFirst("readingStatus"));
        userActivity.setRating(Integer.parseInt(formData.getFirst("rating")));

        // save user activity to "user_activity_for_book_id" table
        userActivityRepository.save(userActivity);

        // save that this book is read by this user. why not use a single table ?

        Book book = optionalBook.get();
        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setId(principal.getAttribute("login"));
        booksByUser.setBookId(bookId);
        booksByUser.setBookName(book.getName());
        booksByUser.setCovertIds(book.getCoverIds());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
        booksByUser.setRating(Integer.parseInt(formData.getFirst("rating")));
        booksByUserRepository.save(booksByUser);

        return new ModelAndView("redirect:/books/" + bookId);
    }
}
