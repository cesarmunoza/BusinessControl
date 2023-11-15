package com.negocio.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.negocio.app.controllers.util.paginator.PageRender;
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
	
	@GetMapping(value="/verCliente/{idCliente}")
	public String verCliente(@PathVariable(value="idCliente") Long idCliente, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(idCliente);
		if (cliente ==null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: "+cliente.getNombre());
		
		return "verCliente";
	}
	
	@RequestMapping(value = "listarClientes")
	public String listar(@RequestParam(name="page", defaultValue = "0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 5);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listarClientes", clientes);
		
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
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
	
	@RequestMapping(value="/formClients/{idCliente}")
	public String editar(@PathVariable(value="idCliente") Long idCliente, Map<String, Object> model, RedirectAttributes flash) {
		log.info("Se va a editar el cliente con id: {}", idCliente);
		Cliente cliente = null;
		
		if (idCliente > 0) {
			cliente = clienteService.findOne(idCliente);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El id del cliente no existe en la base de datos!");
				return "redirect:/listarClientes";
			}
		}else {
			flash.addFlashAttribute("error", "El id del cliente no puede ser cero!");
			return "redirect:/listarClientes";
		}
		model.put("cliente", cliente);		
		model.put("titulo", "Editar Cliente");
	return "formClients";
	}
	
	@RequestMapping(value="/formClients", method=RequestMethod.POST)	
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile fotoClientes, RedirectAttributes flash , SessionStatus status) {
		if (result.hasErrors()) {
			log.info("Existe algún error que impide que se guarde el producto para el cliente: {}", cliente);
			model.addAttribute("titulo", "Formulario de Cliente");
			return "formClients";
		}
		
		if (!fotoClientes.isEmpty()) {			
			String rootPath = "D:\\Estudio\\Java\\Spring\\WorkspaceChileno\\uploads";
			try {
				byte[] bytes = fotoClientes.getBytes();
				Path rutaCompleta = Paths.get(rootPath + "//" + fotoClientes.getOriginalFilename());
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info", "Has subido la foto correctamente '" + fotoClientes.getOriginalFilename() + "'");
				
				cliente.setFotoClientes(fotoClientes.getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String mensajeFlash = (cliente.getIdCliente() != null)? "Cliente editado con éxito" : "Cliente creado con éxito!";
		
		clienteService.save(cliente);
		status.setComplete();
		log.info("En esta parte se procesan los datos y se envían en el submit del formulario del cliente: {}", cliente);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listarClientes";
	}
	
	@RequestMapping(value="/eliminarCliente/{idCliente}")
	public String eliminarCliente(@PathVariable(value="idCliente") Long idCliente, RedirectAttributes flash) {
		if (idCliente > 0) {
			clienteService.delete(idCliente);
			log.info("Se borró el cliente con id: {}", idCliente);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito!");
		}
		return "redirect:/listarClientes";
	}

}
