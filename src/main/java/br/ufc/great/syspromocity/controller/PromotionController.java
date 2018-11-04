package br.ufc.great.syspromocity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Promotion;
import br.ufc.great.syspromocity.service.PromotionsService;

@Controller
public class PromotionController {

    private PromotionsService promotionService;

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

        model.addAttribute("promotion", new Promotion());
        return "promotions/form";

    }

    @RequestMapping("/promotions/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("promotion", promotionService.get(id));
        return "promotions/form";

    }

    @RequestMapping(value = "/promotions/save", method = RequestMethod.POST)
    public String save(Promotion promotion, final RedirectAttributes ra) {

        Promotion save = promotionService.save(promotion);
        ra.addFlashAttribute("successFlash", "Promoção foi salva com sucesso.");
        return "redirect:/promotions";

    }

    @RequestMapping("/promotions/delete/{id}")
    public String delete(@PathVariable Long id) {

        promotionService.delete(id);
        return "redirect:/promotions";

    }

}