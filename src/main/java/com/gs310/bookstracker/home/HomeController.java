package com.gs310.bookstracker.home;

import com.gs310.bookstracker.SecurityUtil;
import com.gs310.bookstracker.userbooks.BooksByUser;
import com.gs310.bookstracker.userbooks.BooksByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    @Autowired
    private BooksByUserRepository booksByUserRepository;

    @GetMapping("/home")
    public String home(Model model) {

        // get user details from authentication object
        String userId = SecurityUtil.getUserId(SecurityContextHolder.getContext().getAuthentication());

        if (userId != null) {
            Slice<BooksByUser> booksSlice = booksByUserRepository.findAllById(userId, CassandraPageRequest.of(0, 100));
            List<BooksByUser> booksByUser = booksSlice.getContent();
            booksByUser =booksByUser.stream().distinct().peek(
                    book -> {
                        String coverImageUrl = "/images/no-image.png";
                        if (book.getCovertIds() != null && !book.getCovertIds().isEmpty()) {
                            coverImageUrl = COVER_IMAGE_ROOT + book.getCovertIds().get(0) + "-M.jpg";
                        }
                        // transient
                        book.setCoverUrl(coverImageUrl);
                    }
            ).collect(Collectors.toList());
            model.addAttribute("books", booksByUser);
        }
        return "home";
    }
}
