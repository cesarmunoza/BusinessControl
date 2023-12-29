package com.negocio.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name="clientes")
@Data
public class Cliente implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_cliente")
	private Long idCliente;
	
	@NotEmpty
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@NotEmpty
	@Email
	private String mail;
	
	@NotEmpty
	private String telefono;
	
	@NotEmpty	
	private String direccion;
	
	@NotNull
	@Column(name="creado_en")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date creadoEn;
	
	@OneToMany(mappedBy = "cliente", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Factura> facturas;
	
	public Cliente() {		
		facturas = new ArrayList<Factura>();
	}
	
	@Column(name="foto_clientes")
	private String fotoClientes;
	
	public void addFactura(Factura factura) {
		facturas.add(factura);
	}

}
