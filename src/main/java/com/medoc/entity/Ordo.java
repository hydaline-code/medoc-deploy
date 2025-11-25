package com.medoc.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ordonnances")
public class Ordo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 512, nullable = false, name = "short_description")
	private String shortDescription;

	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "updated_time")
	private Date updatedTime;

	private boolean enabled;

	// Relation Many-to-One avec Users
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "main_image", nullable = false)
	private String mainImage;
	
	@OneToMany(mappedBy = "ordo", cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<OrdoImage> images = new HashSet<>();


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Ordo [shortDescription=" + shortDescription + ", createdTime=" + createdTime + ", user=" + user + "]";
	}
	
	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public Set<OrdoImage> getImages() {
		return images;
	}

	public void setImages(Set<OrdoImage> images) {
		this.images = images;
	}
	
	public void addExtraImage(String imageName) {
		this.images.add(new OrdoImage(imageName, this));
	}
	
	@Transient
	public String getMainImagePath() {
		if (id == null || mainImage == null) return "/images/image-thumbnail.png";
		
		return "/ordo-images/" + this.id + "/" + this.mainImage;
	}
	
	public boolean containsImageName(String imageName) {
		Iterator<OrdoImage> iterator = images.iterator();
		
		while (iterator.hasNext()) {
			OrdoImage image = iterator.next();
			if (image.getName().equals(imageName)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Transient
	public String getShortName() {
		if (shortDescription.length() > 70) {
			return shortDescription.substring(0, 10).concat("...");
		}
		return shortDescription;
	}

}
