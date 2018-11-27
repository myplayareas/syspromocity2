package br.ufc.great.syspromocity.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Coupon;
import br.ufc.great.syspromocity.model.MyStores;
import br.ufc.great.syspromocity.model.Promotion;
import br.ufc.great.syspromocity.model.Store;
import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.MyStoresService;
import br.ufc.great.syspromocity.service.StoresService;
import br.ufc.great.syspromocity.service.UsersService;

@Controller
public class StoreController {

    private StoresService storeService;
    private List<Promotion> listPromotions=null;
	private List<Coupon> listCoupons;
	private UsersService userService;
	private Users loginUser; 
	private MyStoresService myStoresService;
    
    @Autowired
    public void setStoreService(StoresService storeService) {
        this.storeService = storeService;
    }
    
    @Autowired
    public void setUserService(UsersService userService) {
    	this.userService = userService;
    }

	@Autowired
	public void setMyStoresService(MyStoresService myStoresService) {
		this.myStoresService = myStoresService;
	}

	private void checkUser() {
		User userDetails = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
    	this.loginUser = userService.getUserByUserName(userDetails.getUsername());
	}
    
	/**
	 * Lista todas as lojas cadastradas
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/stores")
    public String index(Model model) {
    	checkUser();
    	List<Store> list = storeService.getAll();    	
    	    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("list", list);
    	
        return "stores/list";
    }

    /**
     * Lista todas as lojas de forma paginada
     * @param pageNumber
     * @param model
     * @return
     */
    @RequestMapping(value = "/stores/{pageNumber}", method = RequestMethod.GET)
    public String list(@PathVariable Integer pageNumber, Model model) {
    	checkUser();
    	Page<Store> page = storeService.getList(pageNumber);
        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());
        
        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
        return "stores/list";
    }
    
    /**
     * Cadastra uma nova loja
     * @param model
     * @return
     */
    @RequestMapping("/stores/add")
    public String add(Model model) {
    	checkUser();
    	this.listPromotions = null;
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
        model.addAttribute("store", new Store());
        
        return "stores/form";

    }

    /**
     * Edita uma loja selecionada
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/stores/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
    	checkUser();
    	this.listPromotions = storeService.get(id).getPromotionList();
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
        model.addAttribute("store", storeService.get(id));
        
        return "stores/form";

    }

    /**
     * Salva uma nova loja
     * @param store
     * @param ra
     * @return
     */
    @RequestMapping(value = "/stores/save", method = RequestMethod.POST)
    public String save(Store store, final RedirectAttributes ra) {
    	
    	store.setPromotionList(this.listPromotions);
        Store save = storeService.save(store);
        ra.addFlashAttribute("successFlash", "Loja foi salva com sucesso.");
        
        return "redirect:/stores";

    }
    
    /**
     * Salva uma nova promoção na loja selecionada
     * @param id
     * @param promotion
     * @param ra
     * @return
     */
    @RequestMapping(value = "/stores/{id}/promotions/save", method = RequestMethod.POST)
    public String savePromotion(@PathVariable("id") Integer id,Promotion promotion, final RedirectAttributes ra) {    	
    	Store store = storeService.get(Long.valueOf(id));
    	
    	store.getPromotionList().add(promotion);    	
        Store save = storeService.save(store);        
        ra.addFlashAttribute("successFlash", "Loja foi salva com nova promoção.");
        
        return "redirect:/stores";
    }

    /**
     * Remove uma loja selecionada
     * @param id
     * @return
     */
    @RequestMapping("/stores/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        storeService.delete(id);
        return "redirect:/stores";
    }
    
    /**
     * Lista as promoções de uma loja selecionada
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="/stores/{id}/promotions")
    public String listPromotions(@PathVariable("id") Long id, Model model) {
    	checkUser();
    	List<Promotion> listPromotionsAux = storeService.get(Long.valueOf(id)).getPromotionList();    	    	
    	this.listPromotions = listPromotionsAux; 
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
    	model.addAttribute("idStore", id);
        model.addAttribute("list", listPromotionsAux);
        
    	return "stores/listPromotions";
    }

    /**
     * Cadastra uma nova promoção em uma loja selecionada
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/stores/{id}/promotions/add")
    public String addPromotion(@PathVariable("id") Integer id, Model model) {    	
    	checkUser();
    	List<Coupon> emptyList = new LinkedList<Coupon>();
       	this.listCoupons = emptyList; 
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
    	model.addAttribute("idStore", id);
        model.addAttribute("promotion", new Promotion());
        
        return "stores/formPromotions";
    }
       
    /**
     * Editar uma promoção de uma loja stores/edit/idStore/promotions/edit/idPromotion
     * 
     * Dada uma loja, 
     * recuperar a lista de promoções
     * Dada a lista de promoções, localizar uma promoção
     * 
     * @param idStore
     * @param idPromotion
     * @param model
     * @return
     */
    @RequestMapping("/stores/edit/{idStore}/promotions/edit/{idPromotion}")
    public String editPromotion(@PathVariable Long idStore, @PathVariable Long idPromotion, Model model) {    	
    	checkUser();
    	List<Promotion> listPromotionsAux = storeService.get(idStore).getPromotionList();
    	Promotion promotion = new Promotion();
    	
    	this.listPromotions = listPromotionsAux;
    	
    	//Procura a promoção, na lista de promoções da loja, que vai ser editada
    	for (Promotion element : listPromotionsAux) {
    		if (element.getId()==idPromotion) {
    			promotion.setId(element.getId());
    			promotion.setDescription(element.getDescription());
    			promotion.setFromDate(element.getFromDate());
    			promotion.setToDate(element.getToDate());
    			promotion.setCoupons(this.listCoupons);
    			break;
    		}
    	}
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
        model.addAttribute("store", storeService.get(idStore));  
        model.addAttribute("idStore", idStore);
        model.addAttribute("list", listPromotionsAux);
        model.addAttribute("promotion", promotion);
        model.addAttribute("idPromotion", idPromotion);

        return "stores/formEditPromotion";
    }

    /**
     * Salva uma promoção em uma loja
     * @param idStore
     * @param idPromotion
     * @param promotion
     * @param ra
     * @return
     */
    @RequestMapping(value = "/stores/{idStore}/promotions/save/{idPromotion}", method = RequestMethod.POST)
    public String savePromotion(@PathVariable("idStore") Integer idStore,@PathVariable("idPromotion") Integer idPromotion,Promotion promotion, final RedirectAttributes ra) {
    	Store store = storeService.get(Long.valueOf(idStore));
    	
    	//Procura a promoção, com os dados antigos, na lista de promoção da loja
    	for (Promotion element : store.getPromotionList()) {
    		if (element.getId()==Long.valueOf(idPromotion)) {
    			//remove a promoção antiga da lista de promoções
    			store.getPromotionList().remove(element);
    			break;
    		}    		
    	}
    	
    	store.getPromotionList().add(promotion);
    	
    	//salva a loja com os novos dados de promoão
        Store save = storeService.save(store);
        
        ra.addFlashAttribute("successFlash", "Os novos dados da promoção foi salva na Loja.");
        return "redirect:/stores/"+idStore+"/promotions";
    }
    
    /**
     * Remover uma promoção de uma dada loja stores/edit/idStore/promotions/delete/idPromotion
     * 
     * @param idStore
     * @param idPromotion
     * @param ra
     * @return
     */
    @RequestMapping(value = "/stores/edit/{idStore}/promotions/delete/{idPromotion}")
    public String deletePromotion(@PathVariable("idStore") Integer idStore, @PathVariable("idPromotion") Integer idPromotion, final RedirectAttributes ra) {
    	Store store = storeService.get(Long.valueOf(idStore));
    	
    	//Procura a promoção na lista de promoções da loja
    	for (Promotion element : store.getPromotionList()) {
    		if (element.getId()==Long.valueOf(idPromotion)) {
    			store.getPromotionList().remove(element);
    			break;
    		}    		
    	}
    	
    	//salva a promoção depois do cupom removido
        Store save = storeService.save(store);
        
        ra.addFlashAttribute("successFlash", "A promoção foi removida da lista.");
        return "redirect:/stores/"+idStore+"/promotions";
    }
     
    //mostrar um formulário para criar uma nova loja do usuário
    /**
     * Mostra o Formulário de nova loja do lojista 
     * @param model
     * @return
     */
    @RequestMapping("/stores/users/add")
    public String showFormNewUsersStore(Model model) {
    	checkUser();
    	this.listPromotions = null;
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
        model.addAttribute("store", new Store());
        
        return "stores/formMyStore";
    }
    
    //cria e salva uma nova loja de um usuário
    @RequestMapping(value="/stores/users/save", method = RequestMethod.POST)
    public String saveNewUsersStore(Store store, final RedirectAttributes ra) {
    	checkUser();
    	
    	//criar uma nova loja
    	store.setPromotionList(this.listPromotions);
        Store save = storeService.save(store);
    	
    	//Checa se já existe MyStores          
        MyStores myStore = this.myStoresService.getMyStoresByUser(loginUser);
        
        //se myStore nao existe cria um novo
        if (myStore == null) {
        	myStore = new MyStores();
        }
        myStore.setUser(loginUser);    
        myStore.addStore(store);
        
    	//salva a nova relação minhaLoja
        this.myStoresService.save(myStore);
    	ra.addFlashAttribute("successFlash", "Loja do lojista foi salva com sucesso.");
    	
    	String servico = "/stores/users/"+loginUser.getId();
    	
    	return "redirect:"+servico;
    }
    
    @RequestMapping(value = "/stores/users/{idUser}")
    public String StoresOfUser(Model model, @PathVariable("idUser") Long idUser) {
    	checkUser();    	    	
    	List<Store> myStoresList = new LinkedList<Store>();
    	MyStores myStore = new MyStores();
    	
    	Users user = this.userService.get(idUser);
		myStore = this.myStoresService.getMyStoresByUser(user); 
    	
		if (myStore != null) {
			myStoresList = myStore.getStoreList();
		}
		
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("list", myStoresList);
    	
        return "stores/listStoresOfUser";
    }

    /**
     * Edita uma loja selecionada de um usuário
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/stores/users/{idUser}/edit/{idStore}")
    public String editUserStore(@PathVariable Long idUser, @PathVariable Long idStore, Model model) {
    	checkUser();
    	this.listPromotions = storeService.get(idStore).getPromotionList();
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());    	
        model.addAttribute("store", storeService.get(idStore));
        
        return "stores/formStoreOfUser";

    }

    //cria e salva uma nova loja de um usuário
    @RequestMapping(value="/stores/users/save/edited", method = RequestMethod.POST)
    public String saveEditedUsersStore(Store store, final RedirectAttributes ra) {
    	checkUser();
    	
    	store.setPromotionList(this.listPromotions);
        Store save = storeService.save(store);
    	
        ra.addFlashAttribute("successFlash", "Loja do lojista foi salva com sucesso.");
    	
    	String servico = "/stores/users/"+loginUser.getId();
    	
    	return "redirect:"+servico;
    }

    /**
     * Remove uma loja selecionada do usuário
     * @param id
     * @return
     */
    @RequestMapping("/stores/users/{idUser}/delete/{idStore}")
    public String deleteStoreOfUser(@PathVariable("idUser") Long idUser, @PathVariable("idStore") Long idStore) {
        storeService.delete(idStore);
    	String servico = "/stores/users/"+idUser;
    	
    	return "redirect:"+servico;

    }
    
    /**
     * Lista as promoções de uma loja selecionada do usuário
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="/stores/users/{idUser}/promotions/{idStore}")
    public String listPromotionsStoreOfUser(@PathVariable("idUser") Long idUser, @PathVariable("idStore") Long idStore, Model model) {
    	checkUser();
    	List<Promotion> listPromotionsAux = storeService.get(Long.valueOf(idStore)).getPromotionList();    	    	
    	this.listPromotions = listPromotionsAux; 
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
    	model.addAttribute("idStore", idStore);
        model.addAttribute("list", listPromotionsAux);
        
    	return "stores/listPromotionsStoreOfUser";
    }

    /**
     * Cadastra uma nova promoção em uma loja selecionada do usuário
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/stores/users/{idUser}/promotions/{idStore}/add")
    public String addPromotionStoreOfUser(@PathVariable("idUser") Integer idUser, @PathVariable("idStore") Integer idStore, Model model) {    	
    	checkUser();
    	List<Coupon> emptyList = new LinkedList<Coupon>();
       	this.listCoupons = emptyList; 
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
    	model.addAttribute("idStore", idStore);
        model.addAttribute("promotion", new Promotion());
        
        return "stores/formPromotionsStoreOfUser";
    }

    /**
     * Salva uma nova promoção na loja selecionada do usuário
     * @param id
     * @param promotion
     * @param ra
     * @return
     */
    @RequestMapping(value = "/stores/users/{idUser}/promotions/{idStore}/save", method = RequestMethod.POST)
    public String saveNewPromotionStoreOfUser(@PathVariable("idUser") Integer idUser, @PathVariable("idStore") Integer idStore,Promotion promotion, final RedirectAttributes ra) {    	
    	Store store = storeService.get(Long.valueOf(idStore));
    	
    	store.getPromotionList().add(promotion);    	
        Store save = storeService.save(store);        
        ra.addFlashAttribute("successFlash", "Loja foi salva com nova promoção.");
        
        return "redirect:/stores/users/"+idUser+"/promotions/"+idStore;
    }

    
    /**
     * Salva uma promoção editada em uma loja de um usuario
     * @param idStore
     * @param idPromotion
     * @param promotion
     * @param ra
     * @return
     */
    @RequestMapping(value = "/stores/users/{idUser}/promotions/{idStore}/save/{idPromotion}", method = RequestMethod.POST)
    public String saveEditedPromotion(@PathVariable("idUser") Integer idUser, @PathVariable("idStore") Integer idStore,@PathVariable("idPromotion") Integer idPromotion,Promotion promotion, final RedirectAttributes ra) {
    	Store store = storeService.get(Long.valueOf(idStore));
    	
    	//Procura a promoção, com os dados antigos, na lista de promoção da loja
    	for (Promotion element : store.getPromotionList()) {
    		if (element.getId()==Long.valueOf(idPromotion)) {
    			//remove a promoção antiga da lista de promoções
    			store.getPromotionList().remove(element);
    			break;
    		}    		
    	}
    	
    	store.getPromotionList().add(promotion);
    	
    	//salva a loja com os novos dados de promoão
        Store save = storeService.save(store);
        
        ra.addFlashAttribute("successFlash", "Os novos dados da promoção foi salva na Loja do usuário.");
        return "redirect:/stores/users/"+idUser+"/promotions/"+idStore;
    }
    
}