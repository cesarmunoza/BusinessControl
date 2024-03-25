package com.negocio.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.negocio.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long>{

}
