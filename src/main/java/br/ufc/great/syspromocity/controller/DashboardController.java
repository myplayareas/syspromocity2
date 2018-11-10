package br.ufc.great.syspromocity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String index(Model model) {
    	int totalUsers=0;
    	int totalStores=0;
    	int totalPromotions=0;
    	int totalCoupons=0;
    	
    	totalUsers = (int) this.userService.count();
    	totalStores = (int) this.storeService.count();
    	totalPromotions = (int) this.promotionService.count();
    	totalCoupons = (int) this.couponService.count();
    	
    	model.addAttribute("totalUsers", totalUsers);
    	model.addAttribute("totalStores", totalStores);
    	model.addAttribute("totalPromotions", totalPromotions);
    	model.addAttribute("totalCoupons", totalCoupons);
    	
        return "dashboard/index";
    }


}
