package com.ibftfip2021.ssfassesment.repository;

import java.time.Duration;
import java.util.Optional;

import com.ibftfip2021.ssfassesment.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

	@Autowired
	@Qualifier("createRedisTemplate")
	RedisTemplate<String, Object> template;

	public void save(String key, Book book) {
		template.opsForValue().set(key, book, Duration.ofMinutes(10));
	}

	public Optional<Book> getBook(String key) {
		Optional<Book> book = Optional.ofNullable((Book) template.opsForValue().get(key));
		return book;

	}

}
