package com.medoc.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.medoc.entity.Setting;
import com.medoc.entity.SettingCategory;


public interface SettingRepository extends CrudRepository<Setting, String> {
	public List<Setting> findByCategory(SettingCategory category);
	
}
