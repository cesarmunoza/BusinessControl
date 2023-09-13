package com.negocio.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.negocio.app.models.dao.IClienteDao;
import com.negocio.app.models.entity.Cliente;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ClienteController {
	
	@Autowired
	private IClienteDao clienteDao;
	
	@GetMapping(value = "listarClientes")
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clienteDao.findAll());
		return "/listarClientes";
	}
	
	@RequestMapping(value="/formClients")
	public String crearCliente(Map<String, Object> model) {
		log.info("Aquí se maneja la petición para crear el formulario de clientes y se muestra el formulario formClients");
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);		
		model.put("titulo", "Formulario de Cliente");
		return "formClients";
	}
	
	@RequestMapping(value="/formClients", method=RequestMethod.POST)	
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "formClients";
		}
		clienteDao.save(cliente);
		log.info("En esta parte se procesan los datos del formulario y se envían en el submit");
		return "redirect:listarClientes";
	}

}
