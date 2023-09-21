package com.negocio.app.models.dao;

import java.util.List;

import com.negocio.app.models.entity.Producto;

public interface IProductoDao {
	
	public List<Producto> findAll();
	
	public void save(Producto producto);
	
	public Producto findOne(Long id);
	
	public void delete(Long id);

}
