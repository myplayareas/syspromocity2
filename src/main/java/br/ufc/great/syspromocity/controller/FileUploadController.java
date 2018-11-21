package br.ufc.great.syspromocity.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.ufc.great.syspromocity.util.Constantes;

@Controller
public class FileUploadController {
  
  @RequestMapping("/uploads")
  public String UploadPage(Model model) {
	  return "uploads/uploadview";
  }
  
  @RequestMapping("/upload")
  public String upload(Model model,@RequestParam("files") MultipartFile[] files) {
	  StringBuilder fileNames = new StringBuilder();
	  String uploadFilePath = new Constantes().uploadDirectory; 
	  
	  for (MultipartFile file : files) {
		  Path fileNameAndPath = Paths.get(uploadFilePath, file.getOriginalFilename());
		  fileNames.append(file.getOriginalFilename()+" ");
		  try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	  model.addAttribute("msg", "Successfully uploaded files "+fileNames.toString());
	  return "uploads/uploadstatusview";
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
      return Files.readAllBytes(serverFile.toPath());
  }

  
  @RequestMapping(value="/viewFile")
  public String viewFile() {
	  return "viewfileuploaded";
  }
  
}
