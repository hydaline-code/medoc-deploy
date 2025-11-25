package com.medoc.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medoc.entity.Setting;
import com.medoc.entity.SettingCategory;

@Service
public class SettingService {

	@Autowired
	private SettingRepository repo;

	public List<Setting> listAllSettings() {
		return (List<Setting>) repo.findAll();
	}

	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));

		return new EmailSettingBag(settings);
	}

	public void saveAll(Iterable<Setting> settings) {
		repo.saveAll(settings);
	}

	public List<Setting> getMailServerSettings() {
		return repo.findByCategory(SettingCategory.MAIL_SERVER);
	}

	public List<Setting> getMailTemplateSettings() {
		return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}

}
