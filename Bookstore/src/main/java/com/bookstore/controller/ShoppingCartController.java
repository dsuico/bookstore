package com.bookstore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bookstore.domain.Book;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;
import com.bookstore.domain.UserPayment;
import com.bookstore.service.BookService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.ShoppingCartService;
import com.bookstore.service.UserService;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	ShoppingCartService shoppingCartService;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping("")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		
		return "shoppingCart";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String addItem(
			@ModelAttribute("book") Book book,
			@ModelAttribute("qty") String qty,
			Model model,
			Principal principal
		) {
		User user = userService.findByUsername(principal.getName());
		book = bookService.findById(book.getId());
		
		if(Integer.parseInt(qty) > book.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			
			return "forward:/book-detail/"+book.getId();
		}
		
		CartItem cartItem = cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));
		model.addAttribute("addBookSuccess", true);
		
		return "forward:/book-detail/"+book.getId();
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(
			@ModelAttribute("id") Long cartItemId,
			@ModelAttribute("qty") int qty,
			Model model,
			Principal principal
		) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/shopping-cart";
		
	}
	
	@RequestMapping("/{id}/remove")
	public String removeCard(@PathVariable Long id, Model model, Principal principal) {

		CartItem cartItem = cartItemService.findById(id);

		cartItemService.removeById(cartItem);

		return "redirect:/shopping-cart";
	}
}
