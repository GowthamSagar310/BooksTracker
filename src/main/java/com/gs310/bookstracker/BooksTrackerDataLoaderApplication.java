package com.gs310.bookstracker;

import com.gs310.bookstracker.author.Author;
import com.gs310.bookstracker.author.AuthorRepository;
import com.gs310.bookstracker.book.Book;
import com.gs310.bookstracker.book.BookRepository;
import com.gs310.bookstracker.dataconnection.DataStaxDBProps;
import jakarta.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

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
@EnableConfigurationProperties(value = DataStaxDBProps.class)
public class BooksTrackerDataLoaderApplication {

	@Autowired
	AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

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

					Author author = new Author();
					author.setAuthorId(jsonObject.optString("key").replace("/authors/", ""));
					author.setName(jsonObject.optString("name"));
					author.setPersonalName(jsonObject.optString("personal_name")); // not all records have personal_name
					authorRepository.save(author);

				} catch (JSONException e) { // todo: move this to JSONException
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace(); // move on to the next line for now.
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

					JSONObject publishedDateObj = jsonObject.optJSONObject("created");
					if (publishedDateObj != null) {
						String date = publishedDateObj.getString("value");
						book.setPublishedDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")));
					}

					// cover ids (book covers)
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
					bookRepository.save(book);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

//	@PostConstruct
//	public void start() {
//
//		//  Dummy record
//		//	Author author = new Author();
//		//	author.setAuthorId("1234");
//		//	author.setName("gowtham");
//		//	author.setPersonalName("gs310");
//		//	authorRepository.save(author);
//
//		// loadAuthorsData();
//		loadBooksData();
//
//	}

	// helper function to get the author ids from book record
	private String getAuthorId(Map<?, ?> author) {
		return ((Map<?, ?>) author.get("author")).get("key").toString().replace("/authors/", "");
	}

}
