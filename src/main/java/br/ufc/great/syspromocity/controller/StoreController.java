package br.ufc.great.syspromocity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Store;
import br.ufc.great.syspromocity.service.StoresService;

@Controller
public class StoreController {

    private StoresService storeService;

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

    @RequestMapping("/stores/delete/{id}")
    public String delete(@PathVariable Long id) {

        storeService.delete(id);
        return "redirect:/stores";

    }

}