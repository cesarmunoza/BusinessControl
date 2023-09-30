package com.negocio.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.negocio.app.models.dao.IProductoDao;
import com.negocio.app.models.entity.Producto;

@Service
public class ProductoServiceImpl implements IProductoService {
	
	@Autowired
	private IProductoDao productoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAll() {		
		return productoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Producto producto) {
		productoDao.save(producto);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findOne(Long id) {		
		return productoDao.findOne(id);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		productoDao.delete(id);

	}

}
