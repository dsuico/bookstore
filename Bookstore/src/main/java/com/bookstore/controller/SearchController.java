package com.bookstore.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.Book;
import com.bookstore.domain.User;
import com.bookstore.service.BookService;
import com.bookstore.service.UserService;

@Controller
public class SearchController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;

	@RequestMapping("/searchByCategory")
	public String searchByCategory(
		@RequestParam("category") String category,
		Model model,
		Principal principal
		) {
		if(principal != null) {
			User user = userService.findByUsername(principal.getName());
			model.addAttribute("user", user);
		}
		
		String classActiveCategory = "active"+category;
		
		classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
		classActiveCategory = classActiveCategory.replaceAll("&", "");
		
		model.addAttribute("classActiveCategory", true);
		
		List<Book> bookList = bookService.findByCategory(category);
		
		if(bookList.isEmpty()) {
			model.addAttribute("isEmptyBookList", true);
		}
		
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
	}
	
	@RequestMapping(value="/searchBook", method=RequestMethod.GET)
	public String searchBook(
		@RequestParam("keyword") String keyword,
		Principal principal,
		Model model
		) {
		
		if(principal != null) {
			User user = userService.findByUsername(principal.getName());
			model.addAttribute("user", user);
		}
		
		List<Book> bookList = bookService.blurrySearch(keyword);
		
		if(bookList.isEmpty()) {
			model.addAttribute("isEmptyBookList", true);
		}
		
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
	}
}
