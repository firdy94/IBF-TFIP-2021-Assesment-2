package com.ibftfip2021.ssfassesment.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibftfip2021.ssfassesment.model.Book;
import com.ibftfip2021.ssfassesment.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/", produces = MediaType.TEXT_HTML_VALUE)
public class SearchController {
	private static final Logger logger = Logger.getLogger(SearchController.class.getName());
	@Autowired
	BookService bookSvc;

	@GetMapping
	public String LandingPage() {
		return "index";
	}

	@GetMapping("{book}")
	public String resultPage(@RequestParam String search, Model model) {
		String normalisedString = bookSvc.normaliseString(search);
		logger.info(normalisedString);
		List<Book> bookList = new ArrayList<>();
		try {
			bookList = bookSvc.stringToBook(bookSvc.search(normalisedString));
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
		if (bookList.isEmpty()) {
			return "error";
		}
		logger.info(String.valueOf(bookList.size()));
		model.addAttribute("searchTerm", search);
		model.addAttribute("books", bookList);
		return "result";

	}

}
