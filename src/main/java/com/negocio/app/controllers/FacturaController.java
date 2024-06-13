package com.negocio.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.negocio.app.models.entity.Cliente;
import com.negocio.app.models.entity.Factura;
import com.negocio.app.models.entity.ItemFactura;
import com.negocio.app.models.entity.Producto;
import com.negocio.app.models.service.IClienteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value="clienteId") Long clienteId,
			Map<String, Object> model,
			RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(clienteId);
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listarClientes";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.put("factura", factura);
		model.put("titulo", "Crear Factura");
		
		return "factura/formFactura";		
	}
	
	@GetMapping(value="/cargar-productos/{term}", produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
		return clienteService.findByNombre(term);
	}
	
	@PostMapping("/formFactura")
	public String guardar(Factura factura,
			@RequestParam(name = "item_id[]", required =false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
			RedirectAttributes flash,
			SessionStatus status) {
		
//		Long[] itemId = new Long[itemIdStrings.length];
		
		for (int i = 0; i < itemId.length; i++) {
			try {
				System.out.println("---"+itemId[i]);
//				itemId[i] = Long.parseLong(itemId[i]);
			} catch (NumberFormatException e) {				
				flash.addFlashAttribute("error", "El ID del producto '" + itemId[i] + "' no es válido");				
				log.info("El ID del producto '" + itemId[i] + "' no es válido");
				return "redirect:/factura/form/" + factura.getCliente().getIdCliente();
			}
		}
		
		log.info("Iniciando el procesamiento del formulario de facturas...");
		
		if (itemId == null || itemId.length == 0) {
			flash.addFlashAttribute("error", "La factura debe tener al menos un producto.");
			return "redirect:/factura/form/" + factura.getCliente().getIdCliente();
		}
		
		Cliente cliente = clienteService.findOne(factura.getCliente().getIdCliente());
	    if (cliente == null) {
	        flash.addFlashAttribute("error", "El cliente no existe en la base de datos.");
	        return "redirect:/listarClientes";
	    }
	    
	    factura.setCliente(cliente);

	    log.info("Iniciando el procesamiento del formulario de facturas...");
		
		for (int i = 0; i < itemId.length; i++) {
//			itemId[i] = Long.parseLong(itemIdStrings[i]);
			Producto producto = clienteService.findProductoById(itemId[i]);
			
			if (producto == null) {
	            flash.addFlashAttribute("error", "El producto con ID " + itemId[i] + " no existe.");
	            return "redirect:/factura/form/" + factura.getCliente().getIdCliente();
	        }
			
			log.debug("Procesando producto con ID: {}, Nombre: {}, Cantidad: {}", producto.getIdProducto(), producto.getNombre(), cantidad[i]);
			
			log.debug("ID: "+itemId[i].toString()+ ", cantidad: "+cantidad.toString());
			
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			
			factura.addItemFactura(linea);
			
			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
		}
		
		log.info("Guardando la factura en la base de datos...");
		
		log.debug("Contenido de la factura antes de guardarla: {}", factura);
		
		clienteService.saveFactura(factura);
		status.setComplete();
		
		flash.addFlashAttribute("Success", "Factura creada con éxito!");
		
		log.info("Redirigiendo al cliente...");
		
		return "redirect:/verCliente/" + factura.getCliente().getIdCliente();
	}

}
