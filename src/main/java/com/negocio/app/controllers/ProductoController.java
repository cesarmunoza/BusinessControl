package com.negocio.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.negocio.app.models.entity.Producto;
import com.negocio.app.models.service.IProductoService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@SessionAttributes("producto")
public class ProductoController {
	
	@Autowired
	private IProductoService productoService;	
	
	@GetMapping(value = "listarProductos")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 4);
		
		Page<Producto> productos = productoService.findAll(pageRequest);
		
		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productoService.findAll());
		return "/listarProductos";
	}
	
	@RequestMapping(value="/formProducts")
	public String crearProducto(Map<String, Object> model) {
		log.info("Aquí se maneja la petición para crear el formulario de productos y se muestra el formulario formProducts");
		Producto producto = new Producto();
		model.put("producto", producto);
		model.put("titulo", "Formulario de producto");
		return "formProducts";		
	}
	
	@RequestMapping(value="/formProducts/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		log.info("Se va a editar el producto con id: {}", id);
		Producto producto= null;
		
		if (id>0) {
			producto = productoService.findOne(id);
			if (producto == null) {
				flash.addFlashAttribute("error", "El id del producto no existe en la base de datos!");
				return "redirect:/listarProductos";
			}
		}else {
			flash.addFlashAttribute("error", "El id del producto no puede ser cero!");
			return "redirect:/listarProductos";
		}
		model.put("producto", producto);
		model.put("titulo", "Editar producto");
		return "formProducts";
	}
	
	@RequestMapping(value="/formProducts", method=RequestMethod.POST)
	public String guardar(@Valid Producto producto, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			log.info("Existe algún error que impide que se guarde el producto para el producto: {}", producto);
			model.addAttribute("titulo", "Formulario de producto");
			return "formProducts";
		}
		String mensajeFlash = (producto.getId() != null)? "Producto editado con éxito!" : "Producto creado con éxito!";
		
		productoService.save(producto);
		status.setComplete();
		log.info("En esta parte se procesan los datos del formulario y se envían en el submit ");
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listarProductos";		
	}
	
	@RequestMapping(value="/eliminarProducto/{id}")
	public String eliminarProducto(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		if (id>0) {
			productoService.delete(id);
			log.info("Se borró el producto con id: {}", id);
			flash.addFlashAttribute("success", "Producto eliminado con Éxito!");
		}
		return "redirect:/listarProductos";
	}
}
