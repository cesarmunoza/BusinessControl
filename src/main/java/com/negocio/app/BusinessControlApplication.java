package com.negocio.app;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.negocio.app.models.service.IUploadFileService;

@SpringBootApplication
public class BusinessControlApplication{
	
//	@Autowired
//	private List<IUploadFileService> uploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(BusinessControlApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		
//		//Inicialización del directorio al inicio de la aplicación		
//		uploadFileService.forEach(t -> {
//			try {
//				t.init();
//			} catch (IOException e) {				
//				e.printStackTrace();
//			}
//		});
//		
//		//uploadFileService.deleteAll(); el de borrar lo haré en las implementaciones
//		
//	}

}
