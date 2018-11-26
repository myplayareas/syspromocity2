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

import br.ufc.great.syspromocity.model.Authorities;
import br.ufc.great.syspromocity.model.Coupon;
import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.AuthoritiesService;
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
	private Users loginUser;
	private AuthoritiesService authoritiesService;
	
	@Autowired
	public void setUserService(UsersService userServices){
		this.userService = userServices;
	}
	
	@Autowired
	public void setAuthoritiesService(AuthoritiesService authoritiesService) {
		this.authoritiesService = authoritiesService;
	}

	private void checkUser() {
		User userDetails = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
    	this.loginUser = userService.getUserByUserName(userDetails.getUsername());
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/users")
	public String index(Model model){
		checkUser();    	
    	List<Users> list = userService.getAll();
    	
    	model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
      	
    	model.addAttribute("list", list);
		
		return "users/list";
	}
	
	/**
	 * Faz a paginação da lista de usuários
	 * @param pageNumber
	 * @param model
	 * @return
	 */
    @RequestMapping(value = "/users/{pageNumber}", method = RequestMethod.GET)
    public String list(@PathVariable Integer pageNumber, Model model) {
    	checkUser();
    	Page<Users> page = this.userService.getList(pageNumber);
		   
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
        
        return "users/list";
    }

    /**
     * Faz o cadastro de um novo usuário
     * @param model
     * @return
     */
    @RequestMapping("/users/add")
    public String add(Model model) {
		checkUser();    	
    	
        model.addAttribute("user", new Users());
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	
        return "users/form";

    }

    /**
     * Edita um usuário selecionado
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/users/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
		checkUser();    	
    	
		Users editUser = userService.get(id);
		
        model.addAttribute("user", editUser);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("amountofcoupons", editUser.getAmountOfCoupons());
    	model.addAttribute("amountoffriends", editUser.getAmountOfFriends());
    	
        return "users/formpwd";

    }

    /**
     * Edita profile do usuário logado
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/users/edit/profile/{id}")
    public String editProfile(@PathVariable Long id, Model model) {
		checkUser();    	
    	
        model.addAttribute("user", loginUser);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("amountofcoupons", loginUser.getAmountOfCoupons());
    	model.addAttribute("amountoffriends", loginUser.getAmountOfFriends());
    	
        return "users/formpwdProfile";

    }
    
    /**
     * Salva os dados de um usuário novo
     * @param user
     * @param password
     * @param confirmPassword
     * @param ra
     * @return
     */
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
     * Salva as alterações do usuário editado
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
    	
    	checkUser();
    	
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
    
    /**
     * Remove um usuário selecionado
     * @param id
     * @return
     */
    @RequestMapping("/users/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users";
    }
    
    /**
     * Lista os cupons de um usuário
     * @param idUser
     * @param model
     * @return
     */
    @RequestMapping(value = "/users/{idUser}/coupons", method = RequestMethod.GET)
    public String listCoupons(@PathVariable long idUser, Model model) {
    	checkUser();
    	List<Coupon> coupons =  this.userService.get(idUser).getCouponList();
		    	
        model.addAttribute("list", coupons);
        model.addAttribute("idUser", idUser);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
        
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
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
        
        return "users/listAllUsers";
    }
    
    /**
     * Dado um usuário ele adiciona um amigo
     * @param idUser usuário logado
     * @param idFriend id do amigo
     * @param model
     * @param ra
     * @return
     */
    @RequestMapping(value = "/users/{idUser}/add/friend/{idFriend}")
    public String addFriend(@PathVariable long idUser, @PathVariable long idFriend, Model model, final RedirectAttributes ra) {
    	checkUser();
    	String mensagem="";    	        	
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
        
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
        
        return "users/listFriends";
    }

    /**
     * Dado um usuário logado, ele remove o amigo selecionado
     * @param idUser
     * @param idFriend
     * @param model
     * @param ra
     * @return
     */
    @RequestMapping(value = "/users/{idUser}/delete/friend/{idFriend}")
    public String deleteFriend(@PathVariable long idUser, @PathVariable long idFriend, Model model, final RedirectAttributes ra) {
    	checkUser();
    	String mensagem = "";
    	        	
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
    
    /**
     * Pega a quantidade de amigos de um usuário
     * @param idUser
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/users/{idUser}/amount/friends")
    @ResponseBody
    public int getAmountOfFriends(@PathVariable(value = "idUser") Long idUser) throws IOException {
    	Users user = this.userService.get(idUser);    	
        return user.getAmountOfFriends();
    }

    /**
     * Pega a quantidade de cupons de um usuário
     * @param idUser
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/users/{idUser}/amount/coupons")
    @ResponseBody
    public int getAmountOfCoupons(@PathVariable(value = "idUser") Long idUser) throws IOException {
    	Users user = this.userService.get(idUser);    	
        return user.getAmountOfCoupons();
    }
 
	@RequestMapping(value = "/users/{idUser}/select/image")
	public String selectImage(@PathVariable(value = "idUser") Long idUser, Model model){
		checkUser();    	

		Users editUser = userService.get(idUser);
		
        model.addAttribute("user", editUser);
        model.addAttribute("loginusername", loginUser.getUsername());
    	model.addAttribute("loginemailuser", loginUser.getEmail());
    	model.addAttribute("loginuserid", loginUser.getId());
    	model.addAttribute("idUser", editUser.getId());
    	model.addAttribute("username", editUser.getUsername());
    	model.addAttribute("completename", editUser.getCompletename());
    	
        return "users/formImage";

		
	}

	/**
	 * Return registration form template
	 * @param model
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegistrationPage(Model model){
		//TODO é preciso zerar a sessão do usuário
		model.addAttribute("user", new Users());		
		return "/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processRegistrationForm(Model model, Users user, @RequestParam("password") String password, 
    		@RequestParam("confirmpassword") String confirmPassword, @RequestParam("authority") String authority, 
    		final RedirectAttributes ra) {
		
		String username = user.getUsername();
		Users userExists = this.userService.getUserByUserName(username);
		
		if (userExists != null) {			
			ra.addFlashAttribute("msgerro", "Usuário já existe!");
			return "redirect:/register";
		}else {	
			//checa a senha do usuário 			
			if (password.equals(confirmPassword)) {
			  	String senhaCriptografada = new GeradorSenha().criptografa(password);

			  	user.setPassword(senhaCriptografada);
				user.setEnabled(true);
				this.userService.save(user);
				
				Authorities authorities = new Authorities();
				authorities.setUsername(username);	
				
				//checa o tipo do usuárioa
				if (authority.equals("USER")) {				
					authorities.setAuthority("USER");
					Authorities save = authoritiesService.save(authorities);
					model.addAttribute("msg", "Usuário registrado com sucesso!");
					return "/login";				
				}
				
				//checa o tipo do usuário
				if (authority.equals("LOJISTA")) {			
					authorities.setAuthority("LOJISTA");
					Authorities save = authoritiesService.save(authorities);
					model.addAttribute("msg", "Usuário lojista registrado com sucesso!");
					return "/login";				
				}	        	
			}else {
				ra.addFlashAttribute("msgerro", "Senha não confere!");
				return "redirect:/register";
			}			
			
		}
		return "/login";
	}
	
}