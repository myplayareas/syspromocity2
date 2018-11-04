package br.ufc.great.syspromocity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Coupon;
import br.ufc.great.syspromocity.service.CouponsService;

@Controller
public class CouponController {

    private CouponsService couponService;

    @Autowired
    public void setCouponService(CouponsService couponService) {
        this.couponService = couponService;
    }

    @RequestMapping(value = "/coupons")
    public String index() {
        return "redirect:/coupons/1";
    }

    @RequestMapping(value = "/coupons/{pageNumber}", method = RequestMethod.GET)
    public String list(@PathVariable Integer pageNumber, Model model) {
        Page<Coupon> page = couponService.getList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "coupons/list";

    }

    @RequestMapping("/coupons/add")
    public String add(Model model) {

        model.addAttribute("coupon", new Coupon());
        return "coupons/form";

    }

    @RequestMapping("/coupons/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("coupon", couponService.get(id));
        return "coupons/form";

    }

    @RequestMapping(value = "/coupons/save", method = RequestMethod.POST)
    public String save(Coupon coupon, final RedirectAttributes ra) {

        Coupon save = couponService.save(coupon);
        ra.addFlashAttribute("successFlash", "Cupom foi salvo com sucesso.");
        return "redirect:/coupons";

    }

    @RequestMapping("/coupons/delete/{id}")
    public String delete(@PathVariable Long id) {

        couponService.delete(id);
        return "redirect:/coupons";

    }

}