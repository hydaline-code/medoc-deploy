package com.medoc;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.medoc.entity.Ordo;
import com.medoc.entity.Question;
import com.medoc.entity.Role;
import com.medoc.entity.Setting;
import com.medoc.entity.SettingCategory;
import com.medoc.entity.User;
import com.medoc.ordo.OrdoRepository;
import com.medoc.question.QuestionRepository;
import com.medoc.setting.SettingRepository;
import com.medoc.user.RoleRepository;
import com.medoc.user.UserRepository;

/**
 * DataInitializer - Initializes seed data for the Medoc application
 * This component runs when the application starts and populates the database
 * with initial data if it doesn't exist.
 * 
 * Default password for all users: Test@1234
 */
@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrdoRepository ordoRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private SettingRepository settingRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final String DEFAULT_PASSWORD = "Test@1234";

	@Override
	public void run(String... args) throws Exception {
		// Only initialize if data doesn't exist
		if (roleRepository.count() == 0) {
			System.out.println("========== INITIALIZING MEDOC APPLICATION DATA ==========");
			
			initializeRoles();
			initializeUsers();
			initializeOrdonnances();
			initializeQuestions();
			initializeSettings();

			System.out.println("========== DATA INITIALIZATION COMPLETE ==========");
		} else {
			System.out.println("Database already initialized, skipping seed data.");
		}
	}

	/**
	 * Initialize roles: Admin, Pharmacie, Client
	 */
	private void initializeRoles() {
		System.out.println("Initializing roles...");
		
		Role adminRole = new Role("Admin", "Administrator - manage everything");
		Role pharmacieRole = new Role("Pharmacie", "Pharmacist - manage prescriptions and orders");
		Role clientRole = new Role("Client", "Client - can purchase medications");

		roleRepository.save(adminRole);
		roleRepository.save(pharmacieRole);
		roleRepository.save(clientRole);
		
		System.out.println("✓ 3 roles created");
	}

	/**
	 * Initialize users with proper roles
	 */
	private void initializeUsers() {
		System.out.println("Initializing users...");
		
	// RoleRepository.findByName returns Optional<Role> so unwrap safely
	java.util.Optional<Role> adminRoleOpt = roleRepository.findByName("Admin");
	java.util.Optional<Role> pharmacieRoleOpt = roleRepository.findByName("Pharmacie");
	java.util.Optional<Role> clientRoleOpt = roleRepository.findByName("Client");

	Role adminRole = adminRoleOpt.orElseThrow(() -> new IllegalStateException("Admin role not found"));
	Role pharmacieRole = pharmacieRoleOpt.orElseThrow(() -> new IllegalStateException("Pharmacie role not found"));
	Role clientRole = clientRoleOpt.orElseThrow(() -> new IllegalStateException("Client role not found"));

		// ADMIN USER
		User admin = new User();
		admin.setEmail("admin@medoc.com");
		admin.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
		admin.setFirstName("Admin");
		admin.setLastName("System");
		admin.setPhoneNumber("+237670000001");
		admin.setAddressLine1("123 Admin Street");
		admin.setAddressLine2("Suite 100");
		admin.setCity("Yaoundé");
		admin.setState("Centre");
		admin.setPostalCode("00000");
		admin.setCreatedTime(new Date());
		admin.setEnabled(true);
		Set<Role> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);
		admin.setRoles(adminRoles);
		userRepository.save(admin);

		// PHARMACIE USERS
		User pharma1 = new User();
		pharma1.setEmail("pharma1@medoc.com");
		pharma1.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
		pharma1.setFirstName("Marie");
		pharma1.setLastName("Dupont");
		pharma1.setPhoneNumber("+237670000002");
		pharma1.setAddressLine1("456 Pharmacy Avenue");
		pharma1.setAddressLine2("Apt 5");
		pharma1.setCity("Douala");
		pharma1.setState("Littoral");
		pharma1.setPostalCode("20000");
		pharma1.setCreatedTime(new Date());
		pharma1.setEnabled(true);
		Set<Role> pharmaRoles = new HashSet<>();
		pharmaRoles.add(pharmacieRole);
		pharma1.setRoles(pharmaRoles);
		userRepository.save(pharma1);

		User pharma2 = new User();
		pharma2.setEmail("pharma2@medoc.com");
		pharma2.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
		pharma2.setFirstName("Jean");
		pharma2.setLastName("Michel");
		pharma2.setPhoneNumber("+237670000003");
		pharma2.setAddressLine1("789 Health Plaza");
		pharma2.setAddressLine2("Building B");
		pharma2.setCity("Yaoundé");
		pharma2.setState("Centre");
		pharma2.setPostalCode("00100");
		pharma2.setCreatedTime(new Date());
		pharma2.setEnabled(true);
		pharma2.setRoles(pharmaRoles);
		userRepository.save(pharma2);

		// CLIENT USERS
		User client1 = new User();
		client1.setEmail("client1@medoc.com");
		client1.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
		client1.setFirstName("Pierre");
		client1.setLastName("Bernard");
		client1.setPhoneNumber("+237670000004");
		client1.setAddressLine1("321 Patient Road");
		client1.setAddressLine2("House 10");
		client1.setCity("Yaoundé");
		client1.setState("Centre");
		client1.setPostalCode("00200");
		client1.setCreatedTime(new Date());
		client1.setEnabled(true);
		Set<Role> clientRoles = new HashSet<>();
		clientRoles.add(clientRole);
		client1.setRoles(clientRoles);
		userRepository.save(client1);

		User client2 = new User();
		client2.setEmail("client2@medoc.com");
		client2.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
		client2.setFirstName("Sophie");
		client2.setLastName("Martin");
		client2.setPhoneNumber("+237670000005");
		client2.setAddressLine1("654 Client Lane");
		client2.setAddressLine2("Apt 12");
		client2.setCity("Douala");
		client2.setState("Littoral");
		client2.setPostalCode("20100");
		client2.setCreatedTime(new Date());
		client2.setEnabled(true);
		client2.setRoles(clientRoles);
		userRepository.save(client2);

		User client3 = new User();
		client3.setEmail("client3@medoc.com");
		client3.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
		client3.setFirstName("Marc");
		client3.setLastName("Lefebvre");
		client3.setPhoneNumber("+237670000006");
		client3.setAddressLine1("987 Health Street");
		client3.setAddressLine2("No. 5");
		client3.setCity("Bangui");
		client3.setState("Centre");
		client3.setPostalCode("00300");
		client3.setCreatedTime(new Date());
		client3.setEnabled(true);
		client3.setRoles(clientRoles);
		userRepository.save(client3);

		System.out.println("✓ 6 users created (1 Admin, 2 Pharmacies, 3 Clients)");
	}

	/**
	 * Initialize prescription ordonnances
	 */
	private void initializeOrdonnances() {
		System.out.println("Initializing ordonnances...");
		
	User pharma1 = userRepository.getUserByEmail("pharma1@medoc.com");
	User pharma2 = userRepository.getUserByEmail("pharma2@medoc.com");

		// Prescriptions from Pharmacy 1
		Ordo ordo1 = new Ordo();
		ordo1.setShortDescription("Paracétamol 500mg - Boîte de 20 comprimés");
		ordo1.setCreatedTime(new Date());
		ordo1.setUpdatedTime(new Date());
		ordo1.setEnabled(true);
		ordo1.setUser(pharma1);
		ordo1.setMainImage("paracetamol.jpg");
		ordoRepository.save(ordo1);

		Ordo ordo2 = new Ordo();
		ordo2.setShortDescription("Ibuprofène 200mg - Flacon de 100 comprimés");
		ordo2.setCreatedTime(new Date());
		ordo2.setUpdatedTime(new Date());
		ordo2.setEnabled(true);
		ordo2.setUser(pharma1);
		ordo2.setMainImage("ibuprofen.jpg");
		ordoRepository.save(ordo2);

		Ordo ordo3 = new Ordo();
		ordo3.setShortDescription("Aspirine 100mg - Boîte de 30 comprimés");
		ordo3.setCreatedTime(new Date());
		ordo3.setUpdatedTime(new Date());
		ordo3.setEnabled(true);
		ordo3.setUser(pharma1);
		ordo3.setMainImage("aspirin.jpg");
		ordoRepository.save(ordo3);

		Ordo ordo6 = new Ordo();
		ordo6.setShortDescription("Vitamine C 1000mg - Tube de 20 comprimés");
		ordo6.setCreatedTime(new Date());
		ordo6.setUpdatedTime(new Date());
		ordo6.setEnabled(true);
		ordo6.setUser(pharma1);
		ordo6.setMainImage("vitamine_c.jpg");
		ordoRepository.save(ordo6);

		Ordo ordo7 = new Ordo();
		ordo7.setShortDescription("Calcium + Vitamine D - Boîte de 30 comprimés");
		ordo7.setCreatedTime(new Date());
		ordo7.setUpdatedTime(new Date());
		ordo7.setEnabled(true);
		ordo7.setUser(pharma1);
		ordo7.setMainImage("calcium_vit_d.jpg");
		ordoRepository.save(ordo7);

		// Prescriptions from Pharmacy 2
		Ordo ordo4 = new Ordo();
		ordo4.setShortDescription("Amoxicilline 500mg - Boîte de 24 gélules");
		ordo4.setCreatedTime(new Date());
		ordo4.setUpdatedTime(new Date());
		ordo4.setEnabled(true);
		ordo4.setUser(pharma2);
		ordo4.setMainImage("amoxicilline.jpg");
		ordoRepository.save(ordo4);

		Ordo ordo5 = new Ordo();
		ordo5.setShortDescription("Céphalose 250mg - Flacon de 20 comprimés");
		ordo5.setCreatedTime(new Date());
		ordo5.setUpdatedTime(new Date());
		ordo5.setEnabled(true);
		ordo5.setUser(pharma2);
		ordo5.setMainImage("cefalose.jpg");
		ordoRepository.save(ordo5);

		Ordo ordo8 = new Ordo();
		ordo8.setShortDescription("Metformine 500mg - Boîte de 60 comprimés");
		ordo8.setCreatedTime(new Date());
		ordo8.setUpdatedTime(new Date());
		ordo8.setEnabled(true);
		ordo8.setUser(pharma2);
		ordo8.setMainImage("metformine.jpg");
		ordoRepository.save(ordo8);

		Ordo ordo9 = new Ordo();
		ordo9.setShortDescription("Losartan 50mg - Flacon de 30 comprimés");
		ordo9.setCreatedTime(new Date());
		ordo9.setUpdatedTime(new Date());
		ordo9.setEnabled(true);
		ordo9.setUser(pharma2);
		ordo9.setMainImage("losartan.jpg");
		ordoRepository.save(ordo9);

		Ordo ordo10 = new Ordo();
		ordo10.setShortDescription("Atorvastatine 20mg - Boîte de 30 comprimés");
		ordo10.setCreatedTime(new Date());
		ordo10.setUpdatedTime(new Date());
		ordo10.setEnabled(true);
		ordo10.setUser(pharma2);
		ordo10.setMainImage("atorvastatine.jpg");
		ordoRepository.save(ordo10);

		System.out.println("✓ 10 ordonnances created");
	}

	/**
	 * Initialize questions and answers
	 */
	private void initializeQuestions() {
		System.out.println("Initializing questions and answers...");
		
	User pharma1 = userRepository.getUserByEmail("pharma1@medoc.com");
	User pharma2 = userRepository.getUserByEmail("pharma2@medoc.com");

		// Get ordonnances
		Ordo ordo1 = ordoRepository.findById(1).orElse(null);
		Ordo ordo2 = ordoRepository.findById(2).orElse(null);
		Ordo ordo3 = ordoRepository.findById(3).orElse(null);
		Ordo ordo4 = ordoRepository.findById(4).orElse(null);
		Ordo ordo5 = ordoRepository.findById(5).orElse(null);
		Ordo ordo6 = ordoRepository.findById(6).orElse(null);
		Ordo ordo8 = ordoRepository.findById(8).orElse(null);
		Ordo ordo9 = ordoRepository.findById(9).orElse(null);

		// Questions on Paracétamol
		Question q1 = new Question();
		q1.setQuestionContent("Quel est le prix de ce médicament?");
		q1.setAnswer("Le prix est 5000 FCFA par boîte");
		q1.setAskTime(new Date());
		q1.setAnswerTime(new Date());
		q1.setOrdo(ordo1);
		q1.setAnswerer(pharma1);
		questionRepository.save(q1);

		Question q2 = new Question();
		q2.setQuestionContent("Peut-on prendre cela avec le café?");
		q2.setAnswer(null);
		q2.setAskTime(new Date());
		q2.setAnswerTime(null);
		q2.setOrdo(ordo1);
		q2.setAnswerer(null);
		questionRepository.save(q2);

		// Questions on Ibuprofène
		Question q3 = new Question();
		q3.setQuestionContent("Combien de temps le médicament met pour agir?");
		q3.setAnswer("Il agit généralement en 30-60 minutes");
		q3.setAskTime(new Date());
		q3.setAnswerTime(new Date());
		q3.setOrdo(ordo2);
		q3.setAnswerer(pharma1);
		questionRepository.save(q3);

		Question q4 = new Question();
		q4.setQuestionContent("Y a-t-il des contre-indications?");
		q4.setAnswer("Oui, éviter chez les personnes allergiques à l'AINS");
		q4.setAskTime(new Date());
		q4.setAnswerTime(new Date());
		q4.setOrdo(ordo2);
		q4.setAnswerer(pharma1);
		questionRepository.save(q4);

		// Questions on Aspirine
		Question q5 = new Question();
		q5.setQuestionContent("À partir de quel âge peut-on le prendre?");
		q5.setAnswer("À partir de 16 ans, sauf avis médical");
		q5.setAskTime(new Date());
		q5.setAnswerTime(new Date());
		q5.setOrdo(ordo3);
		q5.setAnswerer(pharma1);
		questionRepository.save(q5);

		// Questions on Amoxicilline
		Question q6 = new Question();
		q6.setQuestionContent("Faut-il une ordonnance médicale?");
		q6.setAnswer("Oui, c'est un antibiotique qui nécessite une ordonnance");
		q6.setAskTime(new Date());
		q6.setAnswerTime(new Date());
		q6.setOrdo(ordo4);
		q6.setAnswerer(pharma2);
		questionRepository.save(q6);

		Question q7 = new Question();
		q7.setQuestionContent("Quel est le prix par boîte?");
		q7.setAnswer("Le prix est 8500 FCFA par boîte de 24 gélules");
		q7.setAskTime(new Date());
		q7.setAnswerTime(new Date());
		q7.setOrdo(ordo4);
		q7.setAnswerer(pharma2);
		questionRepository.save(q7);

		// Questions on Céphalose
		Question q8 = new Question();
		q8.setQuestionContent("Quand faut-il prendre ce médicament?");
		q8.setAnswer("Il doit être pris pendant les repas");
		q8.setAskTime(new Date());
		q8.setAnswerTime(new Date());
		q8.setOrdo(ordo5);
		q8.setAnswerer(pharma2);
		questionRepository.save(q8);

		// Questions on Vitamine C
		Question q9 = new Question();
		q9.setQuestionContent("Est-ce que c'est naturel?");
		q9.setAnswer("Oui, dérivé naturellement avec compléments synthétiques");
		q9.setAskTime(new Date());
		q9.setAnswerTime(new Date());
		q9.setOrdo(ordo6);
		q9.setAnswerer(pharma1);
		questionRepository.save(q9);

		// Questions on Metformine
		Question q10 = new Question();
		q10.setQuestionContent("Quel est le coût mensuel?");
		q10.setAnswer("Environ 15000 FCFA pour une cure d'un mois");
		q10.setAskTime(new Date());
		q10.setAnswerTime(new Date());
		q10.setOrdo(ordo8);
		q10.setAnswerer(pharma2);
		questionRepository.save(q10);

		// Questions on Losartan
		Question q11 = new Question();
		q11.setQuestionContent("Quels sont les résultats attendus?");
		q11.setAnswer("Baisse de la tension artérielle dans une semaine");
		q11.setAskTime(new Date());
		q11.setAnswerTime(new Date());
		q11.setOrdo(ordo9);
		q11.setAnswerer(pharma2);
		questionRepository.save(q11);

		System.out.println("✓ 11 questions created");
	}

	/**
	 * Initialize application settings
	 */
	private void initializeSettings() {
		System.out.println("Initializing settings...");
		
		// GENERAL SETTINGS
		settingRepository.save(new Setting("SITE_NAME", "MedocsFacile", SettingCategory.GENERAL));
		settingRepository.save(new Setting("SITE_LOGO", "MedocsFacile.png", SettingCategory.GENERAL));
		settingRepository.save(new Setting("COPYRIGHT", "Copyright (C) 2024 MedocsFacile Ltd.", SettingCategory.GENERAL));

		// MAIL SERVER SETTINGS
		settingRepository.save(new Setting("MAIL_HOST", "smtp.gmail.com", SettingCategory.MAIL_SERVER));
		settingRepository.save(new Setting("MAIL_PORT", "587", SettingCategory.MAIL_SERVER));
		settingRepository.save(new Setting("MAIL_USERNAME", "your-email@gmail.com", SettingCategory.MAIL_SERVER));
		settingRepository.save(new Setting("MAIL_PASSWORD", "your-app-password", SettingCategory.MAIL_SERVER));
		settingRepository.save(new Setting("SMTP_AUTH", "true", SettingCategory.MAIL_SERVER));
		settingRepository.save(new Setting("SMTP_SECURED", "true", SettingCategory.MAIL_SERVER));
		settingRepository.save(new Setting("SENDER_NAME", "MedocsFacile Support", SettingCategory.MAIL_SERVER));

		// MAIL TEMPLATES
		settingRepository.save(new Setting("ORDER_CONFIRMATION_SUBJECT", "Order Confirmation - MedocsFacile",
				SettingCategory.MAIL_TEMPLATES));
		settingRepository.save(new Setting("ORDER_CONFIRMATION_CONTENT",
				"Your order has been confirmed. Thank you!", SettingCategory.MAIL_TEMPLATES));
		settingRepository.save(new Setting("QUESTION_ANSWER_SUBJECT", "Answer to Your Question",
				SettingCategory.MAIL_TEMPLATES));
		settingRepository.save(new Setting("QUESTION_ANSWER_CONTENT",
				"Your question has been answered. Please check the website.", SettingCategory.MAIL_TEMPLATES));

		// PAYMENT SETTINGS
		settingRepository.save(new Setting("PAYMENT_METHOD", "Orange Money / MTN Mobile Money", SettingCategory.PAYMENT));
		settingRepository.save(new Setting("CURRENCY", "FCFA", SettingCategory.PAYMENT));

		System.out.println("✓ 17 settings created");
	}
}
