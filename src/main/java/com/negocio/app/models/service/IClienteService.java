package com.negocio.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.negocio.app.models.entity.Cliente;
import com.negocio.app.models.entity.Factura;
import com.negocio.app.models.entity.Producto;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public void save(Cliente cliente);
	
	public Cliente findOne(Long idCliente);
	
	public void delete(Long idCliente);

	public List<Producto> findByNombre(String term);
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(Long id);
	
	public Factura findFacturaById(Long id);
}
