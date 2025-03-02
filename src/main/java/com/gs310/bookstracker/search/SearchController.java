package com.gs310.bookstracker.search;

import com.gs310.bookstracker.book.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class SearchController {

    private final String COVER_IMAGE_BASE_URL = "http://covers.openlibrary.org/b/id/";

    private final WebClient webClient;

    public SearchController(WebClient.Builder webClientBuilder, BookRepository bookRepository) {

        // exchange strategy is used here to increase the maxInMemory
        // if the response crosses a certain limit, spring throws an exception

        this.webClient = webClientBuilder
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16*1024*1024)).build()
                )
                .baseUrl("http://openlibrary.org/search.json").build();
    }

    @GetMapping(value = "/search")
    public String getSearchResults(@RequestParam String title, Model model) {

        // once we get the response, we can convert the search results into objects automatically
        try {
            Mono<SearchResult> searchResultMono = this.webClient.get()
                    .uri("?title={title}", title)
                    .retrieve()
                    .bodyToMono(SearchResult.class);
            SearchResult searchResult = searchResultMono.block();

            assert searchResult != null;
            List<SearchResultBook> books = searchResult.getDocs()
                    .stream()
                    .limit(10)
                    .peek(
                            bookResult -> {
                                bookResult.setKey(bookResult.getKey().replace("/works/", ""));
                                String coverId = bookResult.getCover_i();
                                if (StringUtils.hasText(coverId)) {
                                    coverId = COVER_IMAGE_BASE_URL + coverId + "-M.jpg";
                                } else {
                                    coverId = "/images/no-image.png";
                                }
                                bookResult.setCover_i(coverId);
                            }
                    )
                    .toList();
            model.addAttribute("searchResults", books);
            return "search";
        } catch (WebClientException e) {
            // timeouts, connection refused, etc.
            model.addAttribute("errorMessage", "Failed to connect to the OpenLibrary API. Please try again later.");
            return "error/error";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred during the search. Please try again later.");
            return "error/error"; 
        }

    }


}
