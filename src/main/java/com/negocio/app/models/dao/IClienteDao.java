package com.negocio.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.negocio.app.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>, PagingAndSortingRepository<Cliente, Long>{
	
}
