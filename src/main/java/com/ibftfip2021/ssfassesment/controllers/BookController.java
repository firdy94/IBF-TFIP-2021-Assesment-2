package com.ibftfip2021.ssfassesment.controllers;

import java.io.IOException;

import com.ibftfip2021.ssfassesment.model.Book;
import com.ibftfip2021.ssfassesment.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/book", produces = MediaType.TEXT_HTML_VALUE)
public class BookController {

	@Autowired
	BookService bookSvc;

	@GetMapping("{id}")
	public String bookPage(@PathVariable String id, Model model) {
		Book book = new Book();
		try {
			book = bookSvc.getBook(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("myBook", book);
		return "detailView";

	}

}
