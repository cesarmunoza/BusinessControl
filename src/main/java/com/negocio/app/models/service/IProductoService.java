package com.negocio.app.models.service;

import java.util.List;

import com.negocio.app.models.entity.Producto;

public interface IProductoService {
	
	public List<Producto> findAll();
	
	public void save(Producto producto);
	
	public Producto findOne(Long id);
	
	public void delete(Long id);

}
