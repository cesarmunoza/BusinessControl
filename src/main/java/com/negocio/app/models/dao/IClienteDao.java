package com.negocio.app.models.dao;

import java.util.List;

import com.negocio.app.models.entity.Cliente;

public interface IClienteDao {
	
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
	
	public Cliente findOne(Long id);
	
	public void delete(Long id);

}
