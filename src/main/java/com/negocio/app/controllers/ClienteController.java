package com.negocio.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.negocio.app.models.entity.Cliente;
import com.negocio.app.models.service.IClienteService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping(value = "listarClientes")
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clienteService.findAll());
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
	
	@RequestMapping(value="/formClients/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		log.info("Se va a editar el cliente con id: {}", id);
		Cliente cliente = null;
		
		if (id > 0) {
			cliente = clienteService.findOne(id);
		}else {
			return "redirect:/listarClientes";
		}
		model.put("cliente", cliente);		
		model.put("titulo", "Editar Cliente");
	return "formClients";
	}
	
	@RequestMapping(value="/formClients", method=RequestMethod.POST)	
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status) {
		if (result.hasErrors()) {
			log.info("Existe algún error que impide que se guarde el producto para el cliente: {}", cliente);
			model.addAttribute("titulo", "Formulario de Cliente");
			return "formClients";
		}
		clienteService.save(cliente);
		status.setComplete();
		log.info("En esta parte se procesan los datos y se envían en el submit del formulario del cliente: {}", cliente);
		return "redirect:listarClientes";
	}
	
	@RequestMapping(value="/eliminarCliente/{id}")
	public String eliminarCliente(@PathVariable(value="id") Long id) {
		if (id > 0) {
			clienteService.delete(id);
			log.info("Se borró el cliente con id: {}", id);
		}
		return "redirect:/listarClientes";
	}

}
