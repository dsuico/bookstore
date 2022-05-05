package com.bookstore.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookstore.domain.CartItem;
import com.bookstore.domain.Order;
import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserService;
import com.bookstore.utility.USConstants;

@Controller
public class OrderController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CartItemService cartItemService;

	@RequestMapping(value="/orders/{id}")
	public String show(
			@PathVariable("id") Long orderId,
			Principal principal,
			Model model
			) {
		
		User user = userService.findByUsername(principal.getName());
		Order order = orderService.findById(orderId);
		
		if(order.getUser().getId() != user.getId()) {
			return "badRequestPage";
		}
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		List<CartItem> cartItemList = cartItemService.findByOrder(order);
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		
		model.addAttribute("stateList", stateList);
		model.addAttribute("userBilling", new UserBilling());
		model.addAttribute("userPayment", new UserPayment());
		model.addAttribute("userShipping", new UserShipping());
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("order", order);
		model.addAttribute("addNewShippingAddress", false);
		model.addAttribute("addNewCreditCard", false);
		model.addAttribute("classActiveShipping", false);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("displayOrderDetail", true);
		model.addAttribute("classActiveOrders", true);
		return "myProfile";
	}
}
