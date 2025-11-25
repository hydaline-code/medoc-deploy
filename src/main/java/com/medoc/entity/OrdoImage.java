package com.medoc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ordo_images")
public class OrdoImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "ordo_id")
	private Ordo ordo;

	public OrdoImage() {
	}
	
	public OrdoImage(String name, Ordo ordo) {
		this.name = name;
		this.ordo = ordo;
	}

	public OrdoImage(Integer id,String name, Ordo ordo) {
		this.id = id;
		this.name = name;
		this.ordo = ordo;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Ordo getOrdo() {
		return ordo;
	}

	public void setOrdo(Ordo ordo) {
		this.ordo = ordo;
	}
	
	@Transient
	public String getImagePath() {
		return "/ordo-images/" + ordo.getId() + "/extras/" + this.name;
	}

	@Override
	public String toString() {
		return "OrdoImage [id=" + id + ", name=" + name + ", ordo=" + ordo + "]";
	}

}
