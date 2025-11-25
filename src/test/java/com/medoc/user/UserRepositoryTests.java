package com.medoc.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.medoc.entity.Role;
import com.medoc.entity.User;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userNamHM = new User();
		userNamHM.setAddressLine1("Yaoundé");
		userNamHM.setAddressLine2("Yaoundé");
		userNamHM.setCity("Yaoundé");
		userNamHM.setCreatedTime(new Date());
		userNamHM.setEmail("djougancharlen@gmail.com");
		userNamHM.setEnabled(true);
		userNamHM.setFirstName("chacha");
		userNamHM.setLastName("chacha admin");
		userNamHM.setOrdonnances(null);
		userNamHM.setPassword("$2a$10$pqqWvGC.GruNWO66TqghheOWBIS.ph4Xs9WsswXNXi8Nw4lkap0Ei");
		userNamHM.setPhoneNumber("1234567890");
		userNamHM.setPhotos("WhatsApp Image 2024-07-29 à 11.24.36_4e55a2bc.jpg");
		userNamHM.setPostalCode("23455");
		userNamHM.setResetPasswordToken(null);
		
		userNamHM.setState("sdad");
		userNamHM.setVerificationCode(null);
		userNamHM.getResetPasswordToken();
		
		userNamHM.addRole(roleAdmin);
		
		User savedUser = repo.save(userNamHM);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userRavi = new User("ravi@gmail.com", "ravi2020", "Ravi", "Kumar");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(2);
		
		userRavi.addRole(roleEditor);
		userRavi.addRole(roleAssistant);
		
		User savedUser = repo.save(userRavi);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userNam = repo.findById(1).get();
		System.out.println(userNam);
		assertThat(userNam).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userNam = repo.findById(1).get();
		userNam.setEnabled(true);
		userNam.setEmail("namjavaprogrammer@gmail.com");
		
		repo.save(userNam);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userRavi = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userRavi.getRoles().remove(roleEditor);
		userRavi.addRole(roleSalesperson);
		
		repo.save(userRavi);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
		
	}
}
