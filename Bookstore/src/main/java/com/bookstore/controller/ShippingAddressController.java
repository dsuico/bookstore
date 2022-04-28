package com.bookstore.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.utility.USConstants;

@Controller
@RequestMapping("/shipping-addresses")
public class ShippingAddressController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserShippingService userShippingService;
	
	@GetMapping(value="")
	public String index(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/*model.addAttribute("orderList", user.getOrderList());*/
		
		model.addAttribute("addNewCreditCard", false);
		model.addAttribute("addNewShippingAddress", false);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String addNew(
			@ModelAttribute("userShipping") UserShipping userShipping,
			Model model,
			Principal principal
		) {
		User user = userService.findByUsername(principal.getName());
		
		userService.updateUserShipping(userShipping, user);
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("addNewShippingAddress", false);
		model.addAttribute("listOfCreditCards", false);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("addNewCreditCard", false);
		return "redirect:/shipping-addresses";
	}
	
	@GetMapping(value="/new")
	public String newCard(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/*model.addAttribute("orderList", user.getOrderList());*/
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		
		UserPayment userPayment = new UserPayment();
		UserBilling userBilling = new UserBilling();
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		model.addAttribute("userShipping", userShipping);
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", false);
		model.addAttribute("listOfShippingAddresses", false);
		
		return "myProfile";
	}
	
	@GetMapping(value="/{id}/update")
	public String show(@PathVariable Long id, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		UserShipping userShipping = userShippingService.findById(id);
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		}
		
		model.addAttribute("user", user);
		
		model.addAttribute("userShipping", userShipping);
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("addNewCreditCard", false);
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", false);
		
		return "myProfile";
	}
	
	@RequestMapping(value = "/set-default")
	public String setDefault(@ModelAttribute("defaultShippingAddressId") Long defaultShippingAddressId, Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		userService.setDefaultShipping(defaultShippingAddressId, user);
		
		return "redirect:/shipping-addresses";
	}
	
	@RequestMapping("/{id}/remove")
	public String removeCard(@PathVariable Long id, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(id);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		}
		
		model.addAttribute("user", user);
		
		userShippingService.removeById(id);

		return "redirect:/shipping-addresses";
	}
}
