package com.medoc.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.medoc.entity.Setting;
import com.medoc.entity.SettingCategory;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {

	@Autowired SettingRepository repo;
	
	@Test
	public void testCreateGeneralSettings() {
		Setting siteName = new Setting("SITE_NAME", "MedocsFacile", SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO", "MedocsFacile.png", SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2021 MedocsFacile Ltd.", SettingCategory.GENERAL);
		
		repo.saveAll(List.of(siteName, siteLogo, copyright));
		
		Iterable<Setting> iterable = repo.findAll();
		
		assertThat(iterable).size().isGreaterThan(0);
	}
	
}
