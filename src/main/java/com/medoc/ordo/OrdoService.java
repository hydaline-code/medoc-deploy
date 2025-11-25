package com.medoc.ordo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medoc.entity.Ordo;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class OrdoService {

	@Autowired private OrdoRepository ordoRepository;
	
	 // 🔹 Récupérer toutes les ordonnances d'un utilisateur
    public List<Ordo> getOrdonnancesByUser(Integer userId) {
        return ordoRepository.findByUserId(userId);
    }
    
    public Ordo save(Ordo ordo) {
		if (ordo.getId() == null) {
			ordo.setCreatedTime(new Date());
		}else {
			ordo.setCreatedTime(ordo.getCreatedTime());
		}
		
		ordo.setEnabled(true);
		
		ordo.setUpdatedTime(new Date());
		
		return ordoRepository.save(ordo);
	}
    
    public void updateOrdoEnabledStatus(Integer id, boolean enabled) {
    	ordoRepository.updateEnabledStatus(id, enabled);
	}
    
    public void delete(Integer id) throws OrdoNotFoundException {
		Long countById = ordoRepository.countById(id);
		
		if (countById == null || countById == 0) {
			throw new OrdoNotFoundException("Could not find any ordonnance with ID " + id);			
		}
		
		ordoRepository.deleteById(id);
	}
    
    public Ordo get(Integer id) throws OrdoNotFoundException {
		try {
			return ordoRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrdoNotFoundException("Could not find any ordonnance with ID " + id);
		}
	}
    
    public List<Ordo> listEnabledOrdo() {
		
		List<Ordo> listEnabledOrdos = ordoRepository.findAllEnabled();
		
		return listEnabledOrdos;
	}
    
}
