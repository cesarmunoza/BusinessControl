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
public class UploadClientFileServiceImpl implements IUploadFileService {

	private static final String UPLOADS_CLIENTES = "D:\\Estudio\\Java\\Spring\\WorkspaceChileno\\uploads\\clientes";

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFotoCliente = getPath(filename);
		log.info("pathFotoCliente: " + pathFotoCliente);

		Resource recurso = null;

		recurso = new UrlResource(pathFotoCliente.toUri());
		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFotoCliente.toString());
		}

		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueClientFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueClientFilename);

		log.info("rootPath: " + rootPath);

		Files.copy(file.getInputStream(), rootPath);

		return uniqueClientFilename;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();

		if (archivo.exists() && archivo.isFile() && archivo.canRead()) {
			if (archivo.delete()) {
				log.info("Foto eliminada con éxito!");
				return true;
			} else {
				log.info("No se pudo eliminar la foto!");
				return false;
			}
		} else {
			log.info("La foto no existe o no se puede leer");
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_CLIENTES).resolve(filename).toAbsolutePath();
	}

//	@Override
//	public void init() throws IOException {
//		try {
//			Path uploadsPath = Paths.get(UPLOADS_CLIENTES).getParent();
//			
//			//Se crea la carpeta raíz: uploads, solo aquí porque es la raíz entonces no se crea en la implementación de Producto
//			if(!Files.exists(uploadsPath)) {
//				Files.createDirectories(uploadsPath);
//				log.info("Se creó la carpeta uploads para subir las fotos de los clientes: {}", uploadsPath);
//			}else {
//				log.info("La carpeta uploads ya existe: {}", uploadsPath);
//			}
//			
//			//Se crea la carpeta de clientes
//			Path clientesPath = Paths.get(UPLOADS_CLIENTES);
//			
//				Files.createDirectories(clientesPath);
//				log.info("Se creó la carpeta de clientes: {}", clientesPath);
//			
//		} catch (Exception e) {
//			log.error("Error durante la inicialización", e);
//		}		
//		
//	}
	
//	@Override
//	public void deleteAll() {
//		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_CLIENTES).getParent().toFile());		
//	}
//
//	@Override
//	@PreDestroy
//	public void cleanUp() {
//		//Lógica para limpiar al detener la aplicación
//		System.out.println("Limpiando las fotos de clientes...");
//		deleteAll();
//	}
	
	

}
