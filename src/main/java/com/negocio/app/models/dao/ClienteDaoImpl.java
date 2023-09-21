package com.negocio.app.models.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.negocio.app.models.entity.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Repository
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {
		return em.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		log.info("En esta implementación se guarda el cliente", cliente);
		if (cliente.getId() != null && cliente.getId() >0) {
			em.merge(cliente);
		}else {
			em.persist(cliente);
		}		
	}

	@Override
	public Cliente findOne(Long id) {		
		return em.find(Cliente.class, id);
	}


}
