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
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.utility.USConstants;

@Controller
@RequestMapping("/credit-cards")
public class CreditCardController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserPaymentService userPaymentService;
	
	@GetMapping(value="")
	public String index(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/*model.addAttribute("orderList", user.getOrderList());*/
		
		model.addAttribute("addNewCreditCard", false);
		model.addAttribute("addNewShippingAddress", false);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String addNew(
			@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling,
			Model model,
			Principal principal
		) {
		User user = userService.findByUsername(principal.getName());
		
		if(userPayment.getId() != null) {
			UserPayment userPaymentObj = userPaymentService.findById(userPayment.getId());
			userBilling.setId(userPaymentObj.getUserBilling().getId());
		}
		
		userService.updateUserBilling(userBilling, userPayment, user);
		
		return "redirect:/credit-cards";
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

		model.addAttribute("listOfCreditCards", false);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("listOfShippingAddresses", false);
		
		return "myProfile";
	}
	
	@GetMapping(value="/{id}/update")
	public String show(@PathVariable Long id, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(id);
		
		if(user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		}
		
		model.addAttribute("user", user);
		
		model.addAttribute("userPayment", userPayment);
		model.addAttribute("userBilling", userPayment.getUserBilling());
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfCreditCards", false);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
	}
	
	@RequestMapping("/{id}/remove")
	public String removeCard(@PathVariable Long id, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(id);
		
		if(user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		}
		
		model.addAttribute("user", user);
		
		userPaymentService.removeById(id);

		return "redirect:/credit-cards";
	}
	
	@RequestMapping(value = "/set-default", method=RequestMethod.GET)
	public String setDefault(@ModelAttribute("defaultUserPaymentId") Long defaultUserPaymentId, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		userService.setDefaultPayment(defaultUserPaymentId, user);
		
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		model.addAttribute("addNewCreditCard", false);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		return "redirect:/credit-cards";
	}
	
}
