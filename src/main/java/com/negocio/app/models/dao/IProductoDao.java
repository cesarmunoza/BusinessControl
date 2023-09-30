package com.negocio.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.negocio.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long>{


}
