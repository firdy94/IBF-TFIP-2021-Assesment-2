package com.ibftfip2021.ssfassesment.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class Book {
	private String key;
	private String title;
	private String isbn;
	private String coverUrl;
	private String description;
	private String excerpt;

	public Book() {
	}

	public Book(String key, String title, String isbn, String coverUrl, String description, String excerpt) {
		this.key = key;
		this.title = title;
		this.isbn = isbn;
		this.coverUrl = coverUrl;
		this.description = description;
		this.excerpt = excerpt;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return this.isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getCoverUrl() {
		return this.coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExcerpt() {
		return this.excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public static List<Book> createBook(JsonObject data) {
		List<Book> books = new ArrayList<>();
		JsonArray booksArray = data.getJsonArray("docs");

		for (int i = 0; i < booksArray.size(); i++) {
			Book book = new Book();
			JsonObject bookObject = booksArray.getJsonObject(i);
			String key = bookObject.getString("key").replace("works", "book");
			String title = bookObject.getString("title");
			book.setKey(key);
			book.setTitle(title);
			books.add(book);
			// String isbn =
		}
		return books;
	}

}
