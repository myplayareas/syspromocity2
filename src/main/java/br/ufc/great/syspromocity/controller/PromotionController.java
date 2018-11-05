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
import br.ufc.great.syspromocity.service.PromotionsService;

@Controller
public class PromotionController {

    private PromotionsService promotionService;
    private List<Coupon> listCoupons=null;
    
    @Autowired
    public void setpromotionService(PromotionsService promotionService) {
        this.promotionService = promotionService;
    }

    @RequestMapping(value = "/promotions")
    public String index() {
        return "redirect:/promotions/1";
    }

    @RequestMapping(value = "/promotions/{pageNumber}", method = RequestMethod.GET)
    public String list(@PathVariable Integer pageNumber, Model model) {
        Page<Promotion> page = promotionService.getList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "promotions/list";

    }

    @RequestMapping("/promotions/add")
    public String add(Model model) {

    	List<Coupon> emptyList = new LinkedList<Coupon>();
    	this.listCoupons = emptyList; 
    	
        model.addAttribute("promotion", new Promotion());
        return "promotions/form";

    }

    @RequestMapping("/promotions/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
    	List<Coupon> listCoupons = promotionService.get(Long.valueOf(id)).getCoupons();
    	
    	this.listCoupons = listCoupons;    	
        model.addAttribute("promotion", promotionService.get(id));  
        model.addAttribute("list", listCoupons);

        return "promotions/formEdit";

    }

    @RequestMapping(value = "/promotions/save", method = RequestMethod.POST)
    public String save(Promotion promotion, final RedirectAttributes ra) {
    	promotion.setCoupons(listCoupons);
    	
        Promotion save = promotionService.save(promotion);
        ra.addFlashAttribute("successFlash", "Promoção foi salva com sucesso.");
        return "redirect:/promotions";
    }

    @RequestMapping("/promotions/delete/{id}")
    public String delete(@PathVariable Long id) {

        promotionService.delete(id);
        return "redirect:/promotions";
    }
    
    @RequestMapping(value="/promotions/{id}/coupons", method=RequestMethod.GET)
    public String listCoupons(@PathVariable("id") Integer id, Model model) {
    	List<Coupon> listCoupons = promotionService.get(Long.valueOf(id)).getCoupons();

    	model.addAttribute("idPromotion", id);
        model.addAttribute("list", listCoupons);
        
    	return "promotions/listCoupons";
    }
    
    //add Coupon
    @RequestMapping("/promotions/{id}/coupons/add")
    public String addCoupon(@PathVariable("id") Integer id, Model model) {
    	
    	model.addAttribute("idPromotion", id);
        model.addAttribute("coupon", new Coupon());
        return "promotions/formCoupons";

    }
   
    //save Coupon
    @RequestMapping(value = "/promotions/{id}/coupons/save", method = RequestMethod.POST)
    public String save(@PathVariable("id") Integer id,Coupon coupon, final RedirectAttributes ra) {
    	
    	Promotion promotion = promotionService.get(Long.valueOf(id));
    	promotion.getCoupons().add(coupon);
    	
        Promotion save = promotionService.save(promotion);
        ra.addFlashAttribute("successFlash", "Promoção foi salva com novo cupom.");
        return "redirect:/promotions";
    }

}