package br.ufc.great.syspromocity.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.service.UsersService;
import br.ufc.great.syspromocity.util.Constantes;

@Controller
public class FileUploadController {
	
	private UsersService userService;
	private Users loginUser;
	
	@Autowired
	public void setUserService(UsersService userServices){
		this.userService = userServices;
	}

	private void checkUser() {
		User userDetails = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
    	this.loginUser = userService.getUserByUserName(userDetails.getUsername());
	}

  @RequestMapping("/uploads")
  public String UploadPage(Model model) {
	  return "uploads/uploadview";
  }
  
  public File renameFile(File file, String newName) throws IOException {
	  // File (or directory) with new name
	  File file2 = new File(newName);

	  if (file2.exists())
	     throw new java.io.IOException("file exists");

	  // Rename file (or directory)
	  boolean success = file.renameTo(file2);

	  if (success) {
		  return file;
	  }else {
		  return null;
	  }
	
  }
  
  @RequestMapping("/upload/selected/image/users/{idUser}")
  public String upload(@PathVariable(value = "idUser") Long idUser, Model model,@RequestParam("photouser") MultipartFile[] files) {
	  StringBuilder fileNames = new StringBuilder();
	  String uploadFilePath = new Constantes().uploadUserDirectory; 	  
	  String idAux = String.valueOf(idUser);
	  
	  for (MultipartFile file : files) {
		  Path fileNameAndPath = Paths.get(uploadFilePath, idAux+".png");
		  fileNames.append(idAux+".png"+" ");
		  try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	  
	  checkUser();
	  Users user = this.userService.get(idUser);
	  
   	  model.addAttribute("user", user);
	  model.addAttribute("loginusername", loginUser.getUsername());
  	  model.addAttribute("loginemailuser", loginUser.getEmail());
  	  model.addAttribute("loginuserid", loginUser.getId());
  	  model.addAttribute("amountofcoupons", user.getAmountOfCoupons());
  	  model.addAttribute("amountoffriends", user.getAmountOfFriends());
	  model.addAttribute("successFlash", "Successfully uploaded files "+fileNames.toString());
	  
	  return "uploads/formpwd";
  }
  
  @RequestMapping(value = "/upload/image/users/{imageName}")
  @ResponseBody
  public byte[] getUserImage(@PathVariable(value = "imageName") String imageName) throws IOException {
	  String uploadFilePath = new Constantes().uploadUserDirectory;
	  
	  File serverFile = new File(uploadFilePath + FileSystems.getDefault().getSeparator() + imageName + ".png");      
	  File userPadrao = new File(uploadFilePath + FileSystems.getDefault().getSeparator() + "anonymous2.png");
	  
	  if (serverFile.length() > 0) {
		  return Files.readAllBytes(serverFile.toPath());	  
	  }else {		  
		return Files.readAllBytes(userPadrao.toPath());
	  }

  }

  
  @RequestMapping(value = "/upload/image/{imageName}")
  @ResponseBody
  public byte[] getImage(@PathVariable(value = "imageName") String imageName) throws IOException {
	  String uploadFilePath = new Constantes().uploadDirectory;
	  
	  File serverFile = new File(uploadFilePath + FileSystems.getDefault().getSeparator() + imageName + ".png");
      return Files.readAllBytes(serverFile.toPath());
  }

  @RequestMapping(value = "/coupons/image/{imageName}")
  @ResponseBody
  public byte[] getQRCodeImage(@PathVariable(value = "imageName") String imageName) throws IOException {
	  String couponsPath = new Constantes().filePathQRCode;
	  
	  File serverFile = new File(couponsPath + FileSystems.getDefault().getSeparator() + imageName + ".png");
	  File cupomPadrao = new File(couponsPath + FileSystems.getDefault().getSeparator() + "cupom.png");
	  
	  if (serverFile.length() > 0) {
		  return Files.readAllBytes(serverFile.toPath());	  
	  }else {		  
		return Files.readAllBytes(cupomPadrao.toPath());
	  }
      
  }

  
  @RequestMapping(value="/viewFile")
  public String viewFile() {
	  return "viewfileuploaded";
  }
  
}
