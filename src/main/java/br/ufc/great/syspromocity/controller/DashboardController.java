package br.ufc.great.syspromocity.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.ufc.great.syspromocity.model.MyStores;
import br.ufc.great.syspromocity.model.Store;
import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.CouponsService;
import br.ufc.great.syspromocity.service.MyStoresService;
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
	private MyStoresService myStoresService;
	private String acesso;

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
	
	@Autowired
	public void setMyStoresService(MyStoresService myStoresService) {
		this.myStoresService = myStoresService;
	}
	
	/**
	 * Verifica quais são as permissões do usuário logado e direciona para o dashboard correto
	 * @param model
	 * @param principal
	 * @return
	 */
    @RequestMapping("/")
    public String index(Model model, Principal principal) {    
    	    
    	String servico="/dashboard";
    	
    	if (hasRole("ADMIN") && hasRole("USER") && hasRole("STOREOWNER")) {
    		servico = servico + "/admin";
    		return "redirect:"+servico;
    	}
    	if (hasRole("USER") && !hasRole("ADMIN") && !hasRole("STOREOWNER")) {
    		servico = servico + "/user";
    		return "redirect:"+servico;
    	}
    	if (hasRole("STOREOWNER") && hasRole("USER")) {
    		servico = servico + "/storeowner";
    		return "redirect:"+servico;
    	}
		return "redirec:/logout";    	           	    	
    }

    /**
     * Carrega o dashboard do usuário administrador do sistema
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/dashboard/admin")
    public String indexAdmin(Model model, Principal principal) {
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
    	
    	checkAccessControl();
    	    	
    	model.addAttribute("totalUsers", totalUsers);
    	model.addAttribute("totalStores", totalStores);
    	model.addAttribute("totalPromotions", totalPromotions);
    	model.addAttribute("totalCoupons", totalCoupons);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("acesso", acesso);
    	     	
        return "dashboard/index";
    }
    
    /**
     * Carrega o dashboard do usuário comum
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/dashboard/user")
    public String indexUser(Model model, Principal principal) {    	
    	this.userName = principal.getName();    	
    	int totalUsers = (int) this.userService.count();    	
    	Users loginUser = userService.getUserByUserName(userName);
    	    	
    	checkAccessControl();
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("totalUsers", totalUsers);
    	model.addAttribute("acesso", acesso);
    	
        return "dashboard/indexUser";
    }

    /**
     * Carrega o dashboard do usuário lojista
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/dashboard/storeowner")
    public String indexStoreOwner(Model model, Principal principal) {
    	this.userName = principal.getName();
    	Users loginUser = userService.getUserByUserName(userName);    	    	
    	int totalMyStores=0;    	
    	MyStores myStores = new MyStores();
    	List<Store> myStoresList = new LinkedList<Store>();    	
    	
    	myStores = this.myStoresService.getMyStoresByUser(loginUser);
    	
    	if (myStores != null) {
    		myStoresList = myStores.getStoreList();
    	}
    	totalMyStores = myStoresList.size();    	
    	    	    	
    	checkAccessControl();
        	
    	model.addAttribute("totalStores", totalMyStores);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("acesso", acesso);
    	    	
        return "dashboard/indexStoreOwner";
    }
    
    /**
     * Checa se uma determinada regra existe na lista de permissoes do usuário logado
     * @param role
     * @return
     */
    private boolean hasRole(String role) {
    	  Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();     
    	  boolean hasRole = false;
    	  
    	  for (GrantedAuthority authority : authorities) {
    	     hasRole = authority.getAuthority().equals(role);
    	     if (hasRole) {
    		  break;
    	     }
    	  }
    	  
    	  return hasRole;
    }  
    
    /**
     * Verifica se o usuário logou como administrador
     */
	private void checkAccessControl() {
		if (hasRole("ADMIN")) {
    		acesso = "ADMIN";
    	}else {
    		acesso = "";
    	}
	}

}
