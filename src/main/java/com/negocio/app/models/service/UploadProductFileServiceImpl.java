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
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadProductFileServiceImpl implements IUploadFileService {

	private static final String UPLOADS_PRODUCTOS = "uploads/productos";

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

}
