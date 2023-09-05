package com.negocio.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name="productos")
@Data
public class Producto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String descripcion;
	
	@Column(name="valor_compra")
	private String valorCompra;
	
	@Column(name="valor_venta")
	private String valorVenta;
	
	private Integer cantidad;
	
	@Column(name="creado_en")
	@Temporal(TemporalType.DATE)
	private Date creadoEn;
	
	@PrePersist
	public void prePersist() {
		creadoEn = new Date();
	}

}
