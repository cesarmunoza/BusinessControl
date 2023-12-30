package com.negocio.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadProductFileServiceImpl implements IUploadFileService {

	private static final String UPLOADS_PRODUCTOS = "D:\\Estudio\\Java\\Spring\\WorkspaceChileno\\uploads\\productos";

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFotoProducto = getPath(filename);
		log.info("pathFotoProducto: " + pathFotoProducto);

		Resource recurso = null;

		recurso = new UrlResource(pathFotoProducto.toUri());
		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFotoProducto.toString());
		}

		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueProductFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueProductFilename);

		log.info("rootPath: " + rootPath);

		Files.copy(file.getInputStream(), rootPath);

		return uniqueProductFilename;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();
		
		if (archivo.exists() && archivo.isFile() && archivo.canRead()) {
			if (archivo.delete()) {
				log.info("Foto eliminada con éxito!");
				return true;
			}else {
				log.info("No se pudo eliminar la foto!");
			}				
		}else {
			log.info("La foto no existe o no se puede leer");
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_PRODUCTOS).resolve(filename).toAbsolutePath();
	}
	
//	@Override
//	public void init() throws IOException {
//		try {
//			Path uploadsPath = Paths.get(UPLOADS_PRODUCTOS).getParent();
//			log.info("Se creó la carpeta uploads para subir las fotos de los productos: {}", uploadsPath);
//			
//			//Esto solo se hace en la parte de cliente porque solo se debe crear la carpeta una vez
////			if(!Files.exists(uploadsPath)) {
////				Files.createDirectories(uploadsPath);
////				log.info("Se creó la carpeta uploads para subir las fotos de los productos: {}", uploadsPath);
////			}else {
////				log.info("La carpeta uploads ya existe: {}", uploadsPath);
////			}
//					
//			//Se crea la carpeta de productos
//			Path productosPath = Paths.get(UPLOADS_PRODUCTOS);
//			
//				Files.createDirectories(productosPath);
//				log.info("Se creó la carpeta de productos: {}", productosPath);
//			
//		} catch (Exception e) {
//			log.error("Error durante la inicialización", e);
//		}
//	}
//	
//	@Override
//	public void deleteAll() {
//		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_PRODUCTOS).toFile());
//		
//	}
//	
//	@Override
//	@PreDestroy
//	public void cleanUp() {
//		//Lógica para limpiar al detener la aplicación
//				System.out.println("Limpiando las fotos de productos...");
//				deleteAll();		
//	}

}
