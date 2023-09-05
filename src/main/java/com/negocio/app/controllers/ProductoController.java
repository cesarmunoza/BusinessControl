package com.negocio.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.negocio.app.models.dao.IProductoDao;
import com.negocio.app.models.entity.Producto;

@Controller
public class ProductoController {
	
	@Autowired
	private IProductoDao productoDao;
	
	@RequestMapping(value="/listarProductos", method=RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productoDao.findAll());
		return "/listarProductos";
	}
	
	@RequestMapping(value="/formProducts")
	public String crearProducto(Map<String, Object> model) {
		
		Producto producto = new Producto();
		model.put("producto", producto);
		model.put("titulo", "Formulario de producto");
		return "formProducts";		
	}
	
	@RequestMapping(value="/formProducts", method=RequestMethod.POST)
	public String guardar(Producto producto) {
		productoDao.save(producto);
		return "redirect:listarProductos";
		
	}

}
