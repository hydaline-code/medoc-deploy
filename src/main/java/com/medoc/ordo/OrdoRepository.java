package com.medoc.ordo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.medoc.entity.Ordo;

public interface OrdoRepository extends CrudRepository<Ordo, Integer>, PagingAndSortingRepository<Ordo, Integer> {

	List<Ordo> findByUserId(Integer userId);

	@Query("UPDATE Ordo p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	public Long countById(Integer id);
	
	@Query("SELECT c FROM Ordo c WHERE c.enabled = true ORDER BY c.updatedTime ASC")
	public List<Ordo> findAllEnabled();
	
	@Query("SELECT c FROM Ordo c ORDER BY c.updatedTime DESC")
	public List<Ordo> findAll();

}
