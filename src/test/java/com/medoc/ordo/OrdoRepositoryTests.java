package com.medoc.ordo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.medoc.entity.Ordo;
import com.medoc.entity.User;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrdoRepositoryTests {

	@Autowired
	private OrdoRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateOrdo() {
		User user = entityManager.find(User.class, 10);
		
		Ordo ordo = new Ordo();
		
		ordo.setShortDescription("Short description for Acer Aspire");
		
		ordo.setEnabled(true);
		
		ordo.setCreatedTime(new Date());
		ordo.setUpdatedTime(new Date());
		ordo.setUser(user);
		
		Ordo savedOrdo = repo.save(ordo);
		
		assertThat(savedOrdo).isNotNull();
		assertThat(savedOrdo.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllOrdos() {
		Iterable<Ordo> iterableOrdos = repo.findAll();
		
		iterableOrdos.forEach(System.out::println);
	}
	
	@Test
	public void testGetOrdo() {
		Integer id = 1;
		Ordo product = repo.findById(id).get();
		System.out.println(product);
		
		assertThat(product).isNotNull();
	}
	
//	@Test
//	public void testUpdateProduct() {
//		Integer id = 1;
//		Product product = repo.findById(id).get();
//		product.setPrice(499);
//		
//		repo.save(product);
//		
//		Product updatedProduct = entityManager.find(Product.class, id);
//		
//		assertThat(updatedProduct.getPrice()).isEqualTo(499);
//	}
//	
//	@Test
//	public void testDeleteProduct() {
//		Integer id = 3;
//		repo.deleteById(id);
//		
//		Optional<Product> result = repo.findById(id);
//		
//		assertThat(!result.isPresent());
//	}
}
