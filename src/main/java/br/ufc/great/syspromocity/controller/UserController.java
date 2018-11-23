package br.ufc.great.syspromocity.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufc.great.syspromocity.model.Coupon;
import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.UsersService;
import br.ufc.great.syspromocity.util.Constantes;
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
    	List<Users> list = userService.getAll();
    	
    	model.addAttribute("username", user.getUsername());
    	model.addAttribute("emailuser", user.getEmail());
    	model.addAttribute("userid", user.getId());
      	model.addAttribute("list", list);
		
		return "users/list";
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
    	
		Users editUser = userService.get(id);
		
        model.addAttribute("user", editUser);
        model.addAttribute("username", editUser.getUsername());
    	model.addAttribute("emailuser", editUser.getEmail());
    	model.addAttribute("userid", editUser.getId());
    	model.addAttribute("amountofcoupons", editUser.getAmountOfCoupons());
    	model.addAttribute("amountoffriends", editUser.getAmountOfFriends());
    	
        return "users/formpwd";

    }

    @RequestMapping("/users/edit/profile/{id}")
    public String editProfile(@PathVariable Long id, Model model) {
		checkUser();    	
    	
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
    	model.addAttribute("emailuser", user.getEmail());
    	model.addAttribute("userid", user.getId());
    	model.addAttribute("amountofcoupons", user.getAmountOfCoupons());
    	model.addAttribute("amountoffriends", user.getAmountOfFriends());
    	
        return "users/formpwdProfile";

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
            ra.addFlashAttribute("successFlash", "A senha do usuário NÃO confere.");
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

    /**
     * Lista todos os usuários disponíveis
     * @param model
     * @return
     */
    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    public String listAllUsers(Model model) {
    	checkUser();
    	
    	List<Users> users =  this.userService.getAll();
    	
        model.addAttribute("list", users);
        model.addAttribute("username", user.getUsername());
    	model.addAttribute("emailuser", user.getEmail());
    	model.addAttribute("userid", user.getId());
        
        return "users/listAllUsers";
    }
    
    /**
     * Dado um usuário ele adicionar um amigo
     * @param idUser usuário logado
     * @param idFriend id do amigo
     * @param model
     * @param ra
     * @return
     */
    @RequestMapping(value = "/users/{idUser}/add/friend/{idFriend}")
    public String addFriend(@PathVariable long idUser, @PathVariable long idFriend, Model model, final RedirectAttributes ra) {
    	String mensagem="";
    	checkUser();    
    	
    	Users user = this.userService.get(idUser);
    	Users friend = this.userService.get(idFriend);
    	
    	if (user.addIdFriend(friend)) {
    		this.userService.save(user);
    		mensagem = "O amigo foi salvo com sucesso.";
    	}else {
    		mensagem = "O amigo já existe!!!!.";
    	}

    	ra.addFlashAttribute("successFlash", mensagem);
    	return "redirect:/users/list";	
    }

    /**
     * Dado um usuário logado lista os amigos dele
     * @param idUser
     * @param model
     * @return
     */
    @RequestMapping(value = "/users/{idUser}/list/friends", method = RequestMethod.GET)
    public String listFriends(@PathVariable long idUser, Model model) {    
		checkUser();    	

		Users user = this.userService.get(idUser);
		List<Users> idFriends = user.getIdFriendsList();
		
		List<Users> listaAmigos = new LinkedList<Users>();
		
		for (Users id : idFriends) {
			listaAmigos.add(this.userService.get(id.getId()));
		}

        model.addAttribute("list", listaAmigos);
        model.addAttribute("username", user.getUsername());
    	model.addAttribute("emailuser", user.getEmail());
    	model.addAttribute("userid", user.getId());
        
        return "users/listFriends";
    }

    /**
     * Dado um usuário logado, ele remove o amigo selcionado
     * @param idUser
     * @param idFriend
     * @param model
     * @param ra
     * @return
     */
    @RequestMapping(value = "/users/{idUser}/delete/friend/{idFriend}")
    public String deleteFriend(@PathVariable long idUser, @PathVariable long idFriend, Model model, final RedirectAttributes ra) {
    	String mensagem = "";
    	checkUser();    
    	
    	Users user = this.userService.get(idUser);
    	Users friend = this.userService.get(idFriend);
    	
    	if (user.deleteFriend(friend)) {        	 
        	this.userService.save(user);
        	mensagem = "Amigo removido com sucesso!";
    	}else {
    		mensagem = "O amigo não foi removido."; 
    	}
    	
    	ra.addFlashAttribute("successFlash", mensagem);
    	String local = "/users/"+idUser+"/list/friends";
    	return "redirect:"+local;
    }
    
    @RequestMapping(value = "/users/{idUser}/amount/friends")
    @ResponseBody
    public int getAmountOfFriends(@PathVariable(value = "idUser") Long idUser) throws IOException {
    	Users user = this.userService.get(idUser);
    	
        return user.getAmountOfFriends();
    }

    @RequestMapping(value = "/users/{idUser}/amount/coupons")
    @ResponseBody
    public int getAmountOfCoupons(@PathVariable(value = "idUser") Long idUser) throws IOException {
    	Users user = this.userService.get(idUser);
    	
        return user.getAmountOfCoupons();
    }
    //Implementar o profile do usuário https://nixmash.com/post/profile-image-uploads-the-spring-parts

}