package com.medoc.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.medoc.entity.Role;
import com.medoc.entity.User;

import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class UserService {

	public static final int USERS_PER_PAGE = 10;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Role> listRolesInForm() {
		return (List<Role>) roleRepo.findRoles(Sort.by("name").ascending());
	}

	public User getByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}

	public List<User> listAll() {
		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
	}

	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer roleId) {
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);

		if (keyword != null && !keyword.isEmpty()) {

			if (roleId != null && roleId > 0) {
				return userRepo.searchInRole(roleId, keyword, pageable);
			}

			return userRepo.findAll(keyword, pageable);
		}

		if (roleId != null && roleId > 0) {
			return userRepo.findAllInRole(roleId, pageable);
		}

		return userRepo.findAll(pageable);
	}

	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);

		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();

			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}

		} else {
			encodePassword(user);
		}

		return userRepo.save(user);
	}

	public User registerCustomer(User user) {
		encodePassword(user);
		user.setEnabled(false);
		user.setCreatedTime(new Date());

		// Vérifie si aucun rôle n'a été défini
		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			Role clientRole = roleRepo.findByName("Client")
					.orElseThrow(() -> new RuntimeException("Rôle 'Client' non trouvé"));
			user.setRoles(Set.of(clientRole));
		}

		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);

		return userRepo.save(user);

	}

	public User updateAccount(User userInForm) {
		User userInDB = userRepo.findById(userInForm.getId()).get();

		if (!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}

		if (userInForm.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}

		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());

		return userRepo.save(userInDB);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);

		if (userByEmail == null)
			return true;

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (userByEmail != null)
				return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}

		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}

	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}

		userRepo.deleteById(id);
	}

	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
	}

	public boolean verify(String verificationCode) {
		User user = userRepo.findByVerificationCode(verificationCode);

		if (user == null || user.isEnabled()) {
			return false;
		} else {
			userRepo.enable(user.getId());
			return true;
		}
	}

	public String updateResetPasswordToken(String email) throws UserNotFoundException {
		User user = userRepo.getUserByEmail(email);
		if (user != null) {
			String token = RandomString.make(30);
			user.setResetPasswordToken(token);
			userRepo.save(user);

			return token;
		} else {
			throw new UserNotFoundException("Could not find any customer with the email " + email);
		}
	}

	public User getByResetPasswordToken(String token) {
		return userRepo.findByResetPasswordToken(token);
	}

	public void updatePassword(String token, String newPassword) throws UserNotFoundException {
		User user = userRepo.findByResetPasswordToken(token);
		if (user == null) {
			throw new UserNotFoundException("No customer found: invalid token");
		}

		user.setPassword(newPassword);
		user.setResetPasswordToken(null);
		encodePassword(user);

		userRepo.save(user);
	}

	public List<User> findUsersByRole(String roleName) {
		return userRepo.findUsersByRole(roleName);
	}
	
	public User getUserByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}
}