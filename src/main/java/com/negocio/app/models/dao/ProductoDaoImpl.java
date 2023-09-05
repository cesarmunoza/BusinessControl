package com.negocio.app.models.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.negocio.app.models.entity.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProductoDaoImpl implements IProductoDao{
	
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Producto> findAll() {
		return em.createQuery("from Producto").getResultList();
	}

	@Override
	@Transactional
	public void save(Producto producto) {
		em.persist(producto);
		
	}

}
