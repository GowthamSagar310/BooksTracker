package com.gs310.bookstracker;

import com.gs310.bookstracker.author.Author;
import com.gs310.bookstracker.author.AuthorRepository;
import com.gs310.bookstracker.book.Book;
import com.gs310.bookstracker.book.BookRepository;
import com.gs310.bookstracker.esearch.EsAuthor;
import com.gs310.bookstracker.esearch.EsAuthorRepository;
import com.gs310.bookstracker.esearch.EsBook;
import com.gs310.bookstracker.esearch.EsBookRepository;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// what does this annotation do ?
// can I use spring batch ? to upload data into cassandra ?
@SpringBootApplication
//@EnableConfigurationProperties(value = DataStaxDBProps.class)
public class BooksTrackerDataLoaderApplication {

	@Autowired
	AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	// search
	@Autowired
	EsAuthorRepository esAuthorRepository;

	@Autowired
	EsBookRepository esBookRepository;

	@Autowired
	private Logger logger;

	private final String authorDataLocation = "src/main/resources/testdata/authors-data-mini.txt";
	private final String worksDataLocation = "src/main/resources/testdata/books-data-mini.txt";

	/*
	* if we have to get the location / path from property file, we can use the @Value annotation
	*
	* -> application.properties
	* testdata.location:
	*   author: <path>
	*   works: <path
	*
	* -> Application code
	*
	* @Value("${testdata.location.author}")
	* private String authorsDataLocation;
	*
	* @Value("${testdata.location.works}")
	* private String worksDataLocation;
	*
	* */

	public static void main(String[] args) {
		SpringApplication.run(BooksTrackerDataLoaderApplication.class, args);
	}

	//  This is purely to connect to database hosted by DataStax https://www.datastax.com/ using their secure bundle
	//	@Bean
	//	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxDBProps astraProperties) {
	//		Path bundle = astraProperties.getSecureConnectBundle().toPath();
	//		return builder -> builder.withCloudSecureConnectBundle(bundle);
	//	}

	private void loadAuthorsData() {

		Path path = Paths.get(System.getProperty("user.dir") + File.separator + authorDataLocation);

		// using streams
		//	try (Stream<String> lines = Files.lines(path)) {
		//		lines.limit(10).forEach(line -> {
		//			// parse the line
		//			// create the author object
		//			// persist
		//		});
		//	} catch (IOException e) {
		//		e.printStackTrace();
		//	}

		try (BufferedReader reader = Files.newBufferedReader(path)) {
			reader.lines().forEach(line -> {
				String jsonString = line.substring(line.indexOf("{"));
				try {
					JSONObject jsonObject = new JSONObject(jsonString);

					// save book into cassandra
					Author author = new Author();
					author.setAuthorId(jsonObject.optString("key").replace("/authors/", ""));
					author.setName(jsonObject.optString("name"));
					author.setPersonalName(jsonObject.optString("personal_name")); // not all records have personal_name
					authorRepository.save(author);

					// save book into elastic search
					EsAuthor esAuthor = new EsAuthor();
					esAuthor.setAuthorId(author.getAuthorId());
					esAuthor.setName(author.getName());
					esAuthor.setPersonalName(author.getPersonalName());
					esAuthorRepository.save(esAuthor);

				} catch (JSONException e) {
					logger.error(e.getMessage());
				}
			});
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void loadBooksData() {
		Path path = Paths.get(System.getProperty("user.dir") + File.separator + worksDataLocation);
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(line -> {

				String jsonString = line.substring(line.indexOf("{"));
				try {
					JSONObject jsonObject = new JSONObject(jsonString);

					Book book = new Book();
					book.setBookId(jsonObject.optString("key").replace("/works/", ""));

					// book name or title
					book.setName(jsonObject.optString("title"));

					// description
					JSONObject descriptionObj = jsonObject.optJSONObject("description");
					if (descriptionObj != null) {
						book.setDescription(descriptionObj.optString("value"));
					}

					// created at
					JSONObject publishedDateObj = jsonObject.optJSONObject("created");
					if (publishedDateObj != null) {
						String date = publishedDateObj.getString("value");
						book.setPublishedDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")));
					}

					// cover ids (book covers)
					// multiple cover images for a single book.
					// can be used for recommendations. (recommendation engine)
					JSONArray coversJSONArr = jsonObject.optJSONArray("covers");
					if (coversJSONArr != null) {
						List<String> coverIds = coversJSONArr
								.toList()
								.stream()
								.map(Object::toString)
								.collect(Collectors.toList());
						book.setCoverIds(coverIds);
					}

					// author ids
					JSONArray authorsJSONArr = jsonObject.optJSONArray("authors");
					if (authorsJSONArr != null) {
						List<String> authorIds = authorsJSONArr
								.toList()
								.stream()
								.map(author -> getAuthorId((Map<?, ?>) author))
								.collect(Collectors.toList());
						book.setAuthorIds(authorIds);

						// author names
						List<String> authorNames = authorIds
								.stream()
								.map(id -> authorRepository.findById(id))
								.map(authorRecord -> {
									// if the record / row is null (somehow the id is present, but in db, the record is not)
									// set the author name as Unknown, remember that this will propagate to UI.
									if (authorRecord.isEmpty()) { return "Unknown Author"; }
									return authorRecord.get().getName();
								})
								.collect(Collectors.toList());
						book.setAuthorNames(authorNames);
					}

					// save book into cassandra
					bookRepository.save(book);

					// save book into elastic search
					EsBook esBook = new EsBook();
					esBook.setBookId(book.getBookId());
					esBook.setName(book.getName());
					esBook.setDescription(book.getDescription());
					esBook.setPublishedDate(book.getPublishedDate());
					esBook.setAuthorNames(book.getAuthorNames());
					esBook.setAuthorIds(book.getAuthorIds());
					if (book.getCoverIds() != null && !book.getCoverIds().isEmpty()) {
						esBook.setCoverId(book.getCoverIds().get(0)); // only the first cover image
					}
					esBookRepository.save(esBook);

				} catch (JSONException e) {
					logger.error(e.getMessage());
				}
			});
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	@PostConstruct
	public void start() {

		//  Dummy record
		//	Author author = new Author();
		//	author.setAuthorId("1234");
		//	author.setName("gowtham");
		//	author.setPersonalName("gs310");
		//	authorRepository.save(author);

	 	loadAuthorsData();
		loadBooksData();
	}

	// helper function to get the author ids from book record
	private String getAuthorId(Map<?, ?> author) {
		return ((Map<?, ?>) author.get("author")).get("key").toString().replace("/authors/", "");
	}

}
