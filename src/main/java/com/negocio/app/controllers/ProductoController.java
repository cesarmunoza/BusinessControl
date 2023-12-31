package com.negocio.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.negocio.app.controllers.util.paginator.PageRender;
import com.negocio.app.models.entity.Producto;
import com.negocio.app.models.service.IProductoService;
import com.negocio.app.models.service.IUploadFileService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes("producto")
public class ProductoController {

	@Autowired
	private IProductoService productoService;

	@Autowired
	@Qualifier("uploadProductFileServiceImpl")
	private IUploadFileService uploadFileService;

	@GetMapping(value = "/uploads/productos/{filename:.+}")
	public ResponseEntity<Resource> verFotoProducto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@GetMapping(value = "/verProducto/{idProducto}")
	public String verProducto(@PathVariable(value = "idProducto") Long idProducto, Map<String, Object> model,
			RedirectAttributes flash) {

		Producto producto = productoService.findOne(idProducto);
		if (producto == null) {
			flash.addFlashAttribute("error", "El producto no existe en la base de datos");
			return "redirect:/listar";
		}

		model.put("producto", producto);
		model.put("titulo", "Detalle producto: " + producto.getNombre());

		return "verProducto";
	}

	@GetMapping(value = "listarProductos")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 5);

		Page<Producto> productos = productoService.findAll(pageRequest);

		PageRender<Producto> pageRender = new PageRender<>("/listarProductos", productos);

		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productos);
		model.addAttribute("page", pageRender);
		return "/listarProductos";
	}

	@RequestMapping(value = "/formProducts")
	public String crearProducto(Map<String, Object> model) {
		log.info(
				"Aquí se maneja la petición para crear el formulario de productos y se muestra el formulario formProducts");
		Producto producto = new Producto();
		model.put("producto", producto);
		model.put("titulo", "Formulario de producto");
		return "formProducts";
	}

	@RequestMapping(value = "/formProducts/{idProducto}")
	public String editar(@PathVariable(value = "idProducto") Long idProducto, Map<String, Object> model,
			RedirectAttributes flash) {
		log.info("Se va a editar el producto con id: {}", idProducto);
		Producto producto = null;

		if (idProducto > 0) {
			producto = productoService.findOne(idProducto);
			if (producto == null) {
				flash.addFlashAttribute("error", "El id del producto no existe en la base de datos!");
				return "redirect:/listarProductos";
			}
		} else {
			flash.addFlashAttribute("error", "El id del producto no puede ser cero!");
			return "redirect:/listarProductos";
		}
		model.put("producto", producto);
		model.put("titulo", "Editar producto");
		return "formProducts";
	}

	@RequestMapping(value = "/formProducts", method = RequestMethod.POST)
	public String guardar(@Valid Producto producto, BindingResult result, Model model,
			@RequestParam("file") MultipartFile fotoProductos, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			log.info("Existe algún error que impide que se guarde el producto para el producto: {}", producto);
			model.addAttribute("titulo", "Formulario de producto");
			return "formProducts";
		}

		if (!fotoProductos.isEmpty()) {

			if (producto.getIdProducto() != null && producto.getIdProducto() > 0 && producto.getFotoProductos() != null
					&& producto.getFotoProductos().length() > 0) {

				uploadFileService.delete(producto.getFotoProductos());
			}

			String uniqueProductFilename = null;
			try {
				uniqueProductFilename = uploadFileService.copy(fotoProductos);
			} catch (IOException e) {
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Has subido la foto correctamente '" + uniqueProductFilename + "'");

			producto.setFotoProductos(uniqueProductFilename);

		}

		String mensajeFlash = (producto.getIdProducto() != null) ? "Producto editado con éxito!"
				: "Producto creado con éxito!";

		productoService.save(producto);
		status.setComplete();
		log.info("En esta parte se procesan los datos del formulario y se envían en el submit ");
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listarProductos";
	}

	@RequestMapping(value = "/eliminarProducto/{idProducto}")
	public String eliminarProducto(@PathVariable(value = "idProducto") Long idProducto, RedirectAttributes flash) {
		if (idProducto > 0) {
			Producto producto = productoService.findOne(idProducto);
			productoService.delete(idProducto);
			log.info("Se borró el producto con id: {}", idProducto);
			flash.addFlashAttribute("success", "Producto eliminado con Éxito!");

			if (uploadFileService.delete(producto.getFotoProductos())) {
				flash.addFlashAttribute("info", "La foto " + producto.getFotoProductos() + " se eliminó!");
			} else {
				flash.addFlashAttribute("error", "No se pudo eliminar la foto " + producto.getFotoProductos());
			}

		}
		return "redirect:/listarProductos";
	}
}
