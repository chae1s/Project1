package com.toy.project1.handler;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileHandler {
	
	public String fileUpload(String filePath, MultipartFile multipartFile) throws Exception {
		String path = "static";
		
		ClassPathResource classPathResource = new ClassPathResource(path);
		
		File file = new File(classPathResource.getFile(), filePath);
		
		if(!file.exists()) file.mkdirs();
		
		String fileName = "";
		fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
		
		file = new File(file, fileName);
		
		multipartFile.transferTo(file);
		
		
		return fileName;
	}
	
	
	public boolean fileDelete(String filePath, String fileName) throws Exception {
		ClassPathResource classPathResource = new ClassPathResource("static");
		
		boolean result = false;
		
		Path path = Paths.get(classPathResource.getFile() + "/" + filePath);
		File file = new File(path.toString(), fileName);
		
		if(file.exists()) {
			result = file.delete();
		}
		
		return result;
	}

}
