package br.ufc.great.syspromocity.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Coupon;
import br.ufc.great.syspromocity.model.Promotion;
import br.ufc.great.syspromocity.model.Store;
import br.ufc.great.syspromocity.service.StoresService;

@Controller
public class StoreController {

    private StoresService storeService;
    private List<Promotion> listPromotions=null;
	private List<Coupon> listCoupons;
    
    @Autowired
    public void setCouponService(StoresService storeService) {
        this.storeService = storeService;
    }

    @RequestMapping(value = "/stores")
    public String index() {
        return "redirect:/stores/1";
    }

    @RequestMapping(value = "/stores/{pageNumber}", method = RequestMethod.GET)
    public String list(@PathVariable Integer pageNumber, Model model) {
        Page<Store> page = storeService.getList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "stores/list";

    }
    
    @RequestMapping("/stores/add")
    public String add(Model model) {

        model.addAttribute("store", new Store());
        return "stores/form";

    }

    @RequestMapping("/stores/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("store", storeService.get(id));
        return "stores/form";

    }

    @RequestMapping(value = "/stores/save", method = RequestMethod.POST)
    public String save(Store store, final RedirectAttributes ra) {

        Store save = storeService.save(store);
        ra.addFlashAttribute("successFlash", "Loja foi salva com sucesso.");
        return "redirect:/stores";

    }
    
    //save new Promotion
    @RequestMapping(value = "/stores/{id}/promotions/save", method = RequestMethod.POST)
    public String save(@PathVariable("id") Integer id,Promotion promotion, final RedirectAttributes ra) {
    	
    	Store store = storeService.get(Long.valueOf(id));
    	store.getPromotionList().add(promotion);
    	
        Store save = storeService.save(store);
        
        ra.addFlashAttribute("successFlash", "Loja foi salva com nova promoção.");
        return "redirect:/stores";
    }


    @RequestMapping("/stores/delete/{id}")
    public String delete(@PathVariable Long id) {

        storeService.delete(id);
        return "redirect:/stores";
    }
    
    @RequestMapping(value="/stores/{id}/promotions", method=RequestMethod.GET)
    public String listPromotions(@PathVariable("id") Long id, Model model) {
    	List<Promotion> listPromotionsAux = storeService.get(Long.valueOf(id)).getPromotionList();
    	this.listPromotions = listPromotionsAux;
    	
    	model.addAttribute("idStore", id);
        model.addAttribute("list", listPromotionsAux);
        
    	return "stores/listPromotions";
    }

    //add Promotion in Store
    @RequestMapping("/stores/{id}/promotions/add")
    public String addPromotion(@PathVariable("id") Integer id, Model model) {
    	
    	List<Coupon> emptyList = new LinkedList<Coupon>();
    	this.listCoupons = emptyList; 
    	
    	model.addAttribute("idStore", id);
        model.addAttribute("promotion", new Promotion());
        return "stores/formPromotions";

    }
       
    //Editar uma promoção de uma loja stores/edit/idStore/promotions/edit/idPromotion
    /*
     * Dada uma loja, 
     * recuperar a lista de promoções
     * Dada a lista de promoções, localizar uma promoção
     */
    @RequestMapping("/stores/edit/{idStore}/promotions/edit/{idPromotion}")
    public String editPromotion(@PathVariable Long idStore, @PathVariable Long idPromotion, Model model) {    	
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
    		}
    	}
    	
        model.addAttribute("store", storeService.get(idStore));  
        model.addAttribute("idStore", idStore);
        model.addAttribute("list", listPromotionsAux);
        model.addAttribute("promotion", promotion);
        model.addAttribute("idPromotion", idPromotion);

        return "stores/formEditPromotion";

    }

    //save Promotion in Store
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
        return "redirect:/stores";
    }
    
    //Remover uma promoção de uma dada loja stores/edit/idStore/promotions/delete/idPromotion
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
        return "redirect:/stores";
    }


    
}