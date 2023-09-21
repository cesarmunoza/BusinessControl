package com.negocio.app.models.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.negocio.app.models.entity.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
@Slf4j
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
	@Transactional(readOnly = true)
	public Producto findOne(Long id) {
		return em.find(Producto.class, id);
	}

	@Override
	@Transactional
	public void save(Producto producto) {
		log.info("En esta implementación se guarda el producto");
		if (producto.getId() != null && producto.getId() > 0) {
			em.merge(producto);
		}else {
			em.persist(producto);
		}				
	}

	@Override
	@Transactional
	public void delete(Long id) {
		em.remove(findOne(id));
	}

}
