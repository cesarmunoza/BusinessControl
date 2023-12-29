package com.negocio.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name="facturas")
public class Factura implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descripcion;
	private String observacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name="created_at")
	private Date createdAt;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Cliente cliente;
	
	@PrePersist
	public void prePersist() {
		createdAt = new Date();
	}
	
	private static final long serialVersionUID = 1L;

}
