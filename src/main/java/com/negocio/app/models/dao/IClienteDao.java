package com.negocio.app.models.dao;

import java.util.List;

import com.negocio.app.models.entity.Cliente;

public interface IClienteDao {
	
	public List<Cliente> findAll();

}
