package br.ufc.great.syspromocity.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Coupon;
import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.UsersService;
import br.ufc.great.syspromocity.util.GeradorSenha;

/**
 * Faz o controle do domínio de usuários
 * @author armandosoaressousa
 *
 */
@Controller
public class UserController {

	private UsersService userService;
	private Users user;
	
	@Autowired
	public void setUserService(UsersService userServices){
		this.userService = userServices;
	}

	private void checkUser() {
		User userDetails = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
    	this.user = userService.getUserByUserName(userDetails.getUsername());
	}
	
	@RequestMapping(value = "/users")
	public String index(Model model){
		checkUser();    	
    	
    	model.addAttribute("username", user.getUsername());
    	model.addAttribute("emailuser", user.getEmail());
    	model.addAttribute("userid", user.getId());
		
		return "redirect:/users/1";
	}
	
    @RequestMapping(value = "/users/{pageNumber}", method = RequestMethod.GET)
    public String list(@PathVariable Integer pageNumber, Model model) {
    	Page<Users> page = this.userService.getList(pageNumber);

		checkUser();    	

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("username", user.getUsername());
    	model.addAttribute("emailuser", user.getEmail());
    	model.addAttribute("userid", user.getId());
        
        return "users/list";
    }

    @RequestMapping("/users/add")
    public String add(Model model) {
		checkUser();    	
    	
        model.addAttribute("user", new Users());
        model.addAttribute("username", user.getUsername());
    	model.addAttribute("emailuser", user.getEmail());
    	model.addAttribute("userid", user.getId());
    	
        return "users/form";

    }

    @RequestMapping("/users/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
		checkUser();    	
    	
        model.addAttribute("user", userService.get(id));
        model.addAttribute("username", user.getUsername());
    	model.addAttribute("emailuser", user.getEmail());
    	model.addAttribute("userid", user.getId());
    	
        return "users/formpwd";

    }

    @RequestMapping(value = "/users/save", method = RequestMethod.POST)
    public String save(Users user, @RequestParam("password") String password, 
    		@RequestParam("confirmpassword") String confirmPassword, final RedirectAttributes ra) {
    	String senhaCriptografada;
    	
    	if (password.equals(confirmPassword)){
        	senhaCriptografada = new GeradorSenha().criptografa(password);
        	user.setPassword(senhaCriptografada);
            Users save = userService.save(user);
            ra.addFlashAttribute("successFlash", "Usuário foi salvo com sucesso.");
            return "redirect:/users";	
    	}else{
            ra.addFlashAttribute("successFlash", "Usuário foi salvo com sucesso.");
            return "redirect:/users";	    		
    	}    	

    }

    /**
     * Salva as alterações do usuário
     * @param user novos dados do usuário
     * @param originalPassword senha original registrada no banco
     * @param newPassword nova senha passada pelo usuário
     * @param confirmnewpassword compara se é igual a newPassword
     * @param ra redireciona os atributos
     * @return página que lista todos os usuários
     */
    @RequestMapping(value = "/users/saveedited", method = RequestMethod.POST)
    public String saveEdited(Users user, @RequestParam("password") String originalPassword, 
    		@RequestParam("newpassword") String newPassword, @RequestParam("confirmnewpassword") String confirmNewPassword, 
    		final RedirectAttributes ra) {
    	
    	String recuperaPasswordBanco;
    	Users userOriginal = userService.get(user.getId());
    	
    	recuperaPasswordBanco = userOriginal.getPassword();
    	
    	if (newPassword.equals(confirmNewPassword)){
        	if (new GeradorSenha().comparaSenhas(originalPassword, recuperaPasswordBanco)){
        		String novaSenhaCriptografada = new GeradorSenha().criptografa(newPassword);
        		user.setPassword(novaSenhaCriptografada);
                Users save = userService.save(user);
                ra.addFlashAttribute("successFlash", "Usuário " + user.getUsername() + " foi alterado com sucesso.");
                return "redirect:/users";    		
        	}else{
        		ra.addFlashAttribute("successFlash", "A senha informada é diferente da senha original.");
                return "redirect:/users";
        	}
    	}
    	else{
            ra.addFlashAttribute("successFlash", "A nova senha não foi confirmada.");
            return "redirect:/users";
    	}
    }
    
    @RequestMapping("/users/delete/{id}")
    public String delete(@PathVariable Long id) {

        userService.delete(id);
        return "redirect:/users";

    }
    
    @RequestMapping(value = "/users/{idUser}/coupons", method = RequestMethod.GET)
    public String listCoupons(@PathVariable long idUser, Model model) {

    	List<Coupon> coupons =  this.userService.get(idUser).getCouponList();

		checkUser();    	

        model.addAttribute("list", coupons);
        model.addAttribute("idUser", idUser);
        model.addAttribute("username", user.getUsername());
    	model.addAttribute("emailuser", user.getEmail());
    	model.addAttribute("userid", user.getId());
        
        return "users/listCoupons";
    }
    
    //Implementar o profile do usuário https://nixmash.com/post/profile-image-uploads-the-spring-parts

}