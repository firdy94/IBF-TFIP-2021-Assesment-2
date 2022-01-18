package com.ibftfip2021.ssfassesment.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.ibftfip2021.ssfassesment.model.Book;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class BookService {

	private static final Logger logger = Logger.getLogger(BookService.class.getName());

	public String search(String search) {
		String url = UriComponentsBuilder
				.fromUriString("http://openlibrary.org/search.json")
				.queryParam("title", search)
				.queryParam("limit", 20)
				.toUriString();
		RequestEntity<Void> req = RequestEntity.get(url).build();
		RestTemplate template = new RestTemplate();
		String respBody = "No books found";
		try {
			ResponseEntity<String> resp = template.exchange(req, String.class);
			respBody = resp.getBody();
		} catch (RestClientException e) {
			logger.info(e.getMessage());
		} finally {
			return respBody;
		}
	}

	public List<Book> stringToBook(String jsonString) throws IOException {
		List<Book> searchedBooks = new ArrayList<>();
		try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
			JsonReader reader = Json.createReader(is);
			JsonObject data = reader.readObject();
			searchedBooks = Book.createBook(data);
			// Weather currentWeather = Weather.createWeather(data);
			return searchedBooks;
		}
	}

	public String normaliseString(String s) {
		s = s.trim().toLowerCase().replace(" ", "+");
		return s;
	}

	public Book getBook(String key) throws IOException {
		Book bookDetails = new Book();
		String baseUrl = "http://openlibrary.org/works/";
		String url = String.format("%s%s.json", baseUrl, key);
		logger.info(url);
		RequestEntity<Void> req = RequestEntity.get(url).build();
		RestTemplate template = new RestTemplate();
		try {
			ResponseEntity<String> resp = template.exchange(req, String.class);
			String respBody = resp.getBody();
			InputStream is = new ByteArrayInputStream(respBody.getBytes());
			JsonReader reader = Json.createReader(is);
			JsonObject data = reader.readObject();
			bookDetails.setTitle(data.getString("title"));
			String coverId = String.valueOf(data.getJsonArray("covers").getJsonNumber(0));
			String coverUrl = String.format("https://covers.openlibrary.org/b/id/%s.jpg", coverId);
			bookDetails.setCoverUrl(coverUrl);
			Optional<JsonArray> excerpts = Optional.ofNullable(data.getJsonArray("excerpts"));
			Optional<JsonValue> descriptions = Optional.ofNullable(data.get("description"));

			String excerpt = null;
			String description = null;

			if (!excerpts.isEmpty()) {
				JsonArray nnExcerpts = excerpts.get();
				excerpt = nnExcerpts.get(0).asJsonObject().getString("excerpt");
			}
			if (!descriptions.isEmpty()) {
				if (descriptions.get().getValueType().equals(JsonObject.EMPTY_JSON_OBJECT.getValueType())) {
					description = data.getJsonObject("description").getString("value");

				} else {
					description = data.getString("description");

				}
			}
			bookDetails.setExcerpt(excerpt);
			bookDetails.setDescription(description);

		} catch (RestClientException e) {
			logger.info(e.getMessage());
		}

		return bookDetails;
	}

}
