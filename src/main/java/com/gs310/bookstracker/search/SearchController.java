package com.gs310.bookstracker.search;

import com.gs310.bookstracker.esearch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {


    private final String COVER_IMAGE_BASE_URL = "http://covers.openlibrary.org/b/id/";

    @Autowired
    private EsBookRepository esBookRepository;

    @Autowired
    private EsAuthorRepository esAuthorRepository;

    @GetMapping(value = "/search")
    public String getSearchResultsByTitle(
            @RequestParam String query,
            @RequestParam(defaultValue = "title") String searchType,
            Model model
    ) {
        try {
            List<EsBook> books = null;
            switch (searchType) {
                case "title":
                    books = esBookRepository.findEsBookByName(query);
                    break;
                case "author":
                    // search by author
                    // find the authors -> get their ids -> find the books with those author ids

                    // by default, elastic search won't allow queries starting with wildcard.
                    // to make sure, partial searches on author_name work, we need to use "match" query

                    /*  Elasticsearch Query
                    *   GET authors/_search
                        {
                          "query": {
                            "bool": {
                              "must": [
                                { "match": { "author_name": "Jacky" } },
                                { "match": { "author_name": "Loufrani" } }
                              ]
                            }
                          }
                        }
                    * */

                    List<EsAuthor> authors = esAuthorRepository.findEsAuthorByName(query);
                    if (authors != null && !authors.isEmpty()) {
                        books = esBookRepository.findEsBookByAuthorIdsIn(authors.stream().map(EsAuthor::getAuthorId).toList());
                    }
                    break;
                case "default":
                    model.addAttribute("errorMessage", "Invalid search type.");
                    return "error/error";
            }

            if (books != null && !books.isEmpty()) {
                books.forEach(
                        book -> {
                            String coverId = book.getCoverId();
                            if (coverId != null) {
                                coverId = COVER_IMAGE_BASE_URL + coverId + "-M.jpg";
                            } else {
                                coverId = "/images/no-image.png";
                            }
                            book.setCoverId(coverId);
                        }
                );
                model.addAttribute("searchResults", books);
            } else {
                model.addAttribute("noResults", true);
            }
            return "search";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred during the search. Please try again later.");
            return "error/error";
        }
    }

    /* Code for Searching books from OpenLibrary API

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

    */

}
