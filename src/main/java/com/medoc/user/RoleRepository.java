package com.medoc.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.medoc.entity.Role;


@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

	Optional<Role> findByName(String name);
	
	@Query("SELECT r FROM Role r")
	List<Role> findRoles(Sort sort);


}
