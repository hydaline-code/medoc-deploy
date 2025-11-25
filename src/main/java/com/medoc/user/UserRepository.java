package com.medoc.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.medoc.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);

	public Long countById(Integer id);

	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' '," + " u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);

	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);

	@Query("SELECT c FROM User c WHERE c.verificationCode = ?1")
	public User findByVerificationCode(String code);

	@Query("UPDATE User c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	@Modifying
	public void enable(Integer id);

	public User findByResetPasswordToken(String token);

	@Query("SELECT p FROM User p JOIN p.roles r WHERE r.id = ?1")
	Page<User> findAllInRole(Integer categoryId, Pageable pageable);

	
	@Query("SELECT p FROM User p JOIN p.roles r "
	        + "WHERE r.id = ?1 "
	        + "AND (p.lastName LIKE CONCAT('%', ?2, '%') "
	        + "OR p.firstName LIKE CONCAT('%', ?2, '%'))")
	Page<User> searchInRole(Integer roleId, String keyword, Pageable pageable);

	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
	List<User> findUsersByRole(@Param("roleName") String roleName);
	
}