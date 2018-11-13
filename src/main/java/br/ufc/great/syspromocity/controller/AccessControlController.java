package br.ufc.great.syspromocity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Authorities;
import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.AuthoritiesService;
import br.ufc.great.syspromocity.util.GeradorSenha;

/**
 * Faz o controle do domínio de Controle de Acesso
 * @author armandosoaressousa
 *
 */
@Controller
public class AccessControlController {
	
	private AuthoritiesService authoritiesService;

	@Autowired
	public void setAuthoritiesService(AuthoritiesService authoritiesService) {
		this.authoritiesService = authoritiesService;
	}
	
    @RequestMapping(value="/accesscontrol", method = RequestMethod.GET)
    public String index(Model model) {
    	List<Authorities> authoritiesList = this.authoritiesService.getListAll();
    	
    	model.addAttribute("list", authoritiesList); 
        return "accesscontrol/list";
    }
    
    @RequestMapping("/accesscontrol/add")
    public String add(Model model) {

        model.addAttribute("access", new Authorities());
        return "accesscontrol/form";

    }

    @RequestMapping("/accesscontrol/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("access", authoritiesService.get(id));
        return "accesscontrol/formEdit";

    }
    
    @RequestMapping(value = "/accesscontrol/save", method = RequestMethod.POST)
    public String save(Authorities authorities , @RequestParam("authority") String authority, final RedirectAttributes ra) {   	    	
    	authorities.setAuthority(authority);
    	Authorities save = authoritiesService.save(authorities);
    	ra.addFlashAttribute("successFlash", "Usuário foi salvo com sucesso.");

    	return "redirect:/accesscontrol";	
    	
    }
	
}
