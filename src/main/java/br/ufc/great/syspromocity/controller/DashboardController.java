package br.ufc.great.syspromocity.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.CouponsService;
import br.ufc.great.syspromocity.service.PromotionsService;
import br.ufc.great.syspromocity.service.StoresService;
import br.ufc.great.syspromocity.service.UsersService;

/**
 * Faz o controle do Dashboard
 * @author armandosoaressousa
 *
 */
@Controller
public class DashboardController {
	
	private UsersService userService;
	private CouponsService couponService;
	private PromotionsService promotionService;
	private StoresService storeService;	
	private String userName; 

	@Autowired
	public void setUserService(UsersService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setCouponService(CouponsService couponService) {
		this.couponService = couponService;
	}

	@Autowired
	public void setPromotionService(PromotionsService promotionService) {
		this.promotionService = promotionService;
	}

	@Autowired
	public void setStoreService(StoresService storeService) {
		this.storeService = storeService;
	}
	
    @RequestMapping("/")
    public String index(Model model, Principal principal) {
    	int totalUsers=0;
    	int totalStores=0;
    	int totalPromotions=0;
    	int totalCoupons=0;
    	
    	totalUsers = (int) this.userService.count();
    	totalStores = (int) this.storeService.count();
    	totalPromotions = (int) this.promotionService.count();
    	totalCoupons = (int) this.couponService.count();
    	this.userName = principal.getName();
    	
    	Users loginUser = userService.getUserByUserName(userName);
    	    	
    	model.addAttribute("totalUsers", totalUsers);
    	model.addAttribute("totalStores", totalStores);
    	model.addAttribute("totalPromotions", totalPromotions);
    	model.addAttribute("totalCoupons", totalCoupons);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
        return "dashboard/index";
    }

}
