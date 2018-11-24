package br.ufc.great.syspromocity.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Coupon;
import br.ufc.great.syspromocity.model.Promotion;
import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.PromotionsService;
import br.ufc.great.syspromocity.service.UsersService;
import br.ufc.great.syspromocity.util.Constantes;
import br.ufc.great.syspromocity.util.GeradorQRCode;
import br.ufc.great.syspromocity.util.GeradorSenha;
import br.ufc.great.syspromocity.util.ManipuladorDatas;

@Controller
public class PromotionController {

    private PromotionsService promotionService;
    private List<Coupon> listCoupons=null;
    private UsersService userService;
    private Users loginUser;
    
    @Autowired
    public void setpromotionService(PromotionsService promotionService) {
        this.promotionService = promotionService;
    }
    
    @Autowired
    public void setUserService(UsersService userService) {
    	this.userService = userService;
    }

	private void checkUser() {
		User userDetails = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();      	
    	this.loginUser = userService.getUserByUserName(userDetails.getUsername());
	}
    
	/**
	 * Lista todas as promoções cadastradas
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/promotions")
    public String index(Model model) {
    	checkUser();    	  
    	List<Promotion> list = this.promotionService.getAll();
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
    	model.addAttribute("list", list);

        return "promotions/list";
    }

    /**
     * Lista as promoções paginadas
     * @param pageNumber
     * @param model
     * @return
     */
    @RequestMapping(value = "/promotions/{pageNumber}", method = RequestMethod.GET)
    public String list(@PathVariable Integer pageNumber, Model model) {
    	checkUser();
    	Page<Promotion> page = promotionService.getList(pageNumber);
        
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
        
        return "promotions/list";

    }

    /**
     * Cadastra uma nova promoção
     * @param model
     * @return
     */
    @RequestMapping("/promotions/add")
    public String add(Model model) {
    	checkUser();
    	List<Coupon> emptyList = new LinkedList<Coupon>();
    	this.listCoupons = emptyList; 
    	    	    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
        model.addAttribute("promotion", new Promotion());
        
        return "promotions/form";

    }

    /**
     * Edita uma promoção selecionada
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/promotions/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
    	checkUser();
    	List<Coupon> listCoupons = promotionService.get(Long.valueOf(id)).getCoupons();    	    	    	
    	
    	this.listCoupons = listCoupons;    	
    	
        model.addAttribute("promotion", promotionService.get(id));  
        model.addAttribute("idPromotion", id);
        model.addAttribute("list", listCoupons);
    	
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());

        return "promotions/formEdit";

    }

    /**
     * Salva uma promoção nova
     * @param promotion
     * @param ra
     * @return
     */
    @RequestMapping(value = "/promotions/save", method = RequestMethod.POST)
    public String save(Promotion promotion, final RedirectAttributes ra) {
    	promotion.setCoupons(listCoupons);
    	
        Promotion save = promotionService.save(promotion);
        ra.addFlashAttribute("successFlash", "Promoção foi salva com sucesso.");
        return "redirect:/promotions";
    }

    /**
     * Remove uma promoção selecinada
     * @param id
     * @return
     */
    @RequestMapping("/promotions/delete/{id}")
    public String delete(@PathVariable Long id) {

        promotionService.delete(id);
        return "redirect:/promotions";
    }
    
    /**
     * Lista os cupons de uma promoção selecionada
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="/promotions/{id}/coupons", method=RequestMethod.GET)
    public String listCoupons(@PathVariable("id") Long id, Model model) {
    	checkUser(); 
    	List<Coupon> listCoupons = promotionService.get(Long.valueOf(id)).getCoupons();
    	   	
    	model.addAttribute("idPromotion", id);
        model.addAttribute("list", listCoupons);
        
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
        
    	return "promotions/listCoupons";
    }
    
    /**
     * Adiciona um novo cupom na promoção e gera o QRCode do cupom 
     * baseado no id da promoção e data de criação do cupom
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/promotions/{id}/coupons/add")
    public String addCoupon(@PathVariable("id") Integer id, Model model) {
    	checkUser();
    	
    	String dataCorrente = new ManipuladorDatas().getCurrentDataTime("yyyy/MM/dd HH:mm:ss");
		String valor = String.valueOf(id) + dataCorrente;
		String couponCode = new GeradorSenha().criptografa(valor);
		    	        
    	model.addAttribute("idPromotion", id);
        model.addAttribute("coupon", new Coupon());
        
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
    	model.addAttribute("couponCode", couponCode);

        return "promotions/formCoupons";

    }
   
    /**
     * Salva o novo cupom na promoção selecionada e gera a imagem do QRCode
     * @param id
     * @param coupon
     * @param ra
     * @return
     */
    @RequestMapping(value = "/promotions/{id}/coupons/save", method = RequestMethod.POST)
    public String save(@PathVariable("id") Integer id,Coupon coupon, final RedirectAttributes ra) {
    	
    	Promotion promotion = promotionService.get(Long.valueOf(id));
    	promotion.getCoupons().add(coupon);
    	    	
        Promotion save = promotionService.save(promotion);        
    	String promotionCode = coupon.getQrCode();
    	
    	int indexCoupon = save.getCoupons().size()-1;
    	Coupon couponAux = save.getCoupons().get(indexCoupon);
    	
    	String idCoupon = String.valueOf(couponAux.getId());
    	new GeradorQRCode().gerar(promotionCode, new Constantes().filePathQRCode, idCoupon+".png");

        ra.addFlashAttribute("successFlash", "Promoção foi salva com novo cupom.");
        return "redirect:/promotions";
    }
    
    /**
     * Editar um cupom de uma promoção promotions/edit/idPromotion/coupons/edit/idCoupon
     * Dada uma promoção, 
     * recuperar a lista de cupons
     * Dada a lista de cupons, localizar um cupom
     *  
     * @param idPromotion
     * @param idCoupon
     * @param model
     * @return
     */
    @RequestMapping("/promotions/edit/{idPromotion}/coupons/edit/{idCoupon}")
    public String editCoupon(@PathVariable Long idPromotion, @PathVariable Long idCoupon, Model model) {
    	List<Coupon> listCoupons = promotionService.get(Long.valueOf(idPromotion)).getCoupons();
    	Coupon coupon = new Coupon();
    	 	
    	this.listCoupons = listCoupons;
    	//Procura o cupom, na lista de cupons da promoção, que vai ser editado
    	for (Coupon element : listCoupons) {
    		if (element.getId()==idCoupon) {
    			coupon.setId(idCoupon);
    			coupon.setDescription(element.getDescription());
    			coupon.setDiscount(element.getDiscount());
    			coupon.setQrCode(element.getQrCode());
    			break;
    		}
    	}
    	
    	checkUser();   
    	
        model.addAttribute("promotion", promotionService.get(idPromotion));  
        model.addAttribute("idPromotion", idPromotion);
        model.addAttribute("list", listCoupons);
        model.addAttribute("coupon", coupon);
        model.addAttribute("idCoupon", idCoupon);
    	
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());

        return "promotions/formEditCoupon";

    }

    /**
     * Salvar um cupom de uma promoção promotions/idPromotion/coupons/save/idCoupon
     * 
     * Data uma promoção, e dado cupom, salvar o cupom na lista da promoção 
     * e atualizar a lista da promoção
     * 
     * @param idPromotion
     * @param idCoupon
     * @param coupon
     * @param ra
     * @return
     */
    @RequestMapping(value = "/promotions/{idPromotion}/coupons/save/{idCoupon}", method = RequestMethod.POST)
    public String saveCoupon(@PathVariable("idPromotion") Integer idPromotion, @PathVariable("idCoupon") Integer idCoupon, Coupon coupon, final RedirectAttributes ra) {
    	Promotion promotion = promotionService.get(Long.valueOf(idPromotion));
    	
    	//Procura o cupom, com os dados antigos, na lista de cupons da promoção
    	for (Coupon element : promotion.getCoupons()) {
    		if (element.getId()==Long.valueOf(idCoupon)) {
    			//remove o cupom antigo da lista de promoções
    			promotion.getCoupons().remove(element);
    			break;
    		}    		
    	}
    	//adiciona o cupom, com novos dados, na lista de cupons da promoção
    	promotion.getCoupons().add(coupon);
    	
    	//salva a promoção com os novos dados de cupom
        Promotion save = promotionService.save(promotion);
        
        ra.addFlashAttribute("successFlash", "Os novos dados do cupom foi salvo na Promoção.");
        return "redirect:/promotions";
    }

    /**
     * Remover um cupom de uma promoção promotions/edit/idPromotion/coupons/edit/idCoupon
     * 
     * @param idPromotion
     * @param idCoupon
     * @param ra
     * @return
     */
    @RequestMapping(value = "/promotions/edit/{idPromotion}/coupons/delete/{idCoupon}")
    public String deleteCoupon(@PathVariable("idPromotion") Integer idPromotion, @PathVariable("idCoupon") Integer idCoupon, final RedirectAttributes ra) {
    	Promotion promotion = promotionService.get(Long.valueOf(idPromotion));
    	
    	//Procura o cupom na lista de cupons da promoção
    	for (Coupon element : promotion.getCoupons()) {
    		if (element.getId()==Long.valueOf(idCoupon)) {
    			//remove o cupom da lista de promoções
    			promotion.getCoupons().remove(element);
    			break;
    		}    		
    	}
    	
    	//salva a promoção depois do cupom removido
        Promotion save = promotionService.save(promotion);
        
        ra.addFlashAttribute("successFlash", "Os novos dados do cupom foi salvo na Promoção.");
        return "redirect:/promotions";
    }
    
}