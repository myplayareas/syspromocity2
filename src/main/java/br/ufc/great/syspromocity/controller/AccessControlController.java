package br.ufc.great.syspromocity.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Authorities;
import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.AuthoritiesService;
import br.ufc.great.syspromocity.util.GeradorSenha;
import br.ufc.great.syspromocity.service.UsersService;

/**
 * Faz o controle do domínio de Controle de Acesso
 * @author armandosoaressousa
 *
 */
@Controller
public class AccessControlController {
	
	private AuthoritiesService authoritiesService;
	private UsersService userService;
	private Users loginUser;

	@Autowired
	public void setAuthoritiesService(AuthoritiesService authoritiesService) {
		this.authoritiesService = authoritiesService;
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
	 * Lista os usuários e suas permissões
	 * @param model
	 * @return
	 */
    @RequestMapping(value="/accesscontrol", method = RequestMethod.GET)
    public String index(Model model) {
    	List<Authorities> authoritiesList = this.authoritiesService.getListAll();
    	
    	checkUser();
    	model.addAttribute("list", authoritiesList);
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());

        return "accesscontrol/list";
    }
    
    /**
     * Adiciona uma nova permissão a um usário existente
     * @param model
     * @return
     */
    @RequestMapping("/accesscontrol/add")
    public String add(Model model) {

        model.addAttribute("access", new Authorities());
        
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());

        return "accesscontrol/form";

    }

    /**
     * Edita as permissões de um usuário selecionado
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/accesscontrol/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("access", authoritiesService.get(id));
        
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());

        return "accesscontrol/formEdit";

    }
    
    /**
     * Salva as permissões do usuário selecionado
     * @param authorities
     * @param authority
     * @param ra
     * @return
     */
    @RequestMapping(value = "/accesscontrol/save", method = RequestMethod.POST)
    public String save(Authorities authorities , @RequestParam("authority") String authority, final RedirectAttributes ra) {   	    	
    	authorities.setAuthority(authority);
    	Authorities save = authoritiesService.save(authorities);
    	ra.addFlashAttribute("successFlash", "Usuário foi salvo com sucesso.");

    	return "redirect:/accesscontrol";	
    	
    }
	
}
