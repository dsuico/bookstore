package com.bookstore.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.Payment;
import com.bookstore.domain.ShippingAddress;
import com.bookstore.domain.User;
import com.bookstore.domain.UserBilling;
import com.bookstore.domain.UserPayment;
import com.bookstore.domain.UserShipping;
import com.bookstore.service.BillingAddressService;
import com.bookstore.service.CartItemService;
import com.bookstore.service.PaymentService;
import com.bookstore.service.ShippingAddressService;
import com.bookstore.service.UserPaymentService;
import com.bookstore.service.UserService;
import com.bookstore.service.UserShippingService;
import com.bookstore.utility.USConstants;

@Controller
public class CheckoutController {
	
	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShippingAddressService shippingAddressService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private BillingAddressService billingAddressService;
	
	@Autowired
	private UserShippingService userShippingService;
	
	@Autowired
	private UserPaymentService userPaymentService;
	
	@RequestMapping("/checkout/set-payment-method/{id}")
	public String setPaymentMethod(
			@PathVariable("id") Long userPaymentId,
			Model model,
			Principal principal
			) {
		
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();
		
		if(userPayment.getUser().getId() != user.getId()) {
			return "badRequestPage";
		}
		
		paymentService.setByUserPayment(userPayment, payment);
		billingAddressService.setByUserBilling(userBilling, billingAddress);
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		List<String> stateList = USConstants.listOfUSStatesCode;
		
		Collections.sort(stateList);
		
		model.addAttribute("isEmptyShippingList", userShippingList.size() == 0 ? false : true);
		model.addAttribute("isEmptyPaymentList", userPaymentList.size() == 0 ? false : true);
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("payment", payment);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());
		model.addAttribute("stateList", stateList);
		
		model.addAttribute("classActivePayment", true);
		
		return "checkout";
	}
	@RequestMapping("/checkout/set-shipping-address/{id}")
	public String setShippingAddress(
			@PathVariable("id") Long userShippingId,
			Principal principal,
			Model model
			) {
		
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(userShipping.getUser().getId() != user.getId()) {
			return "badRequestPage";
		}
		
		shippingAddressService.setByUserShipping(userShipping, shippingAddress);
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		List<String> stateList = USConstants.listOfUSStatesCode;
		
		Collections.sort(stateList);
		
		model.addAttribute("isEmptyShippingList", userShippingList.size() == 0 ? false : true);
		model.addAttribute("isEmptyPaymentList", userPaymentList.size() == 0 ? false : true);
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("payment", payment);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());
		model.addAttribute("stateList", stateList);
		
		model.addAttribute("classActiveShipping", true);

		return "checkout";
	}
	
	@RequestMapping("/checkout/{id}")
	public String checkout(
			@PathVariable("id") Long cartId,
			@RequestParam(value="missingRequiredField", required=false) boolean missingRequiredField,
			Model model,
			Principal principal
			) {
		
		User user = userService.findByUsername(principal.getName());
		
		if(cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		
		if(cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shopping-cart";
		}
		
		for(CartItem cartItem : cartItemList) {
			if(cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shopping-cart";
			}
		}
		
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		
		model.addAttribute("isEmptyShippingList", userShippingList.size() == 0 ? false : true);
		model.addAttribute("isEmptyPaymentList", userPaymentList.size() == 0 ? false : true);
		
		for(UserShipping userShipping : userShippingList) {
			if(userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}
		
		for(UserPayment userPayment: userPaymentList) {
			if(userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}
		
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveShipping", true);
		
		if(missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}
		
		return "checkout";
	}
	
	
}
