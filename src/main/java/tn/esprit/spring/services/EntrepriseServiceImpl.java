package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;

@Service
public class EntrepriseServiceImpl implements IEntrepriseService {
    private static final Logger LOGGER = LogManager.getLogger(EntrepriseServiceImpl.class);


	@Autowired
    EntrepriseRepository entrepriseRepoistory;
	@Autowired
	DepartementRepository deptRepoistory;
	
	public Entreprise ajouterEntreprise(Entreprise entreprise) {
		LOGGER.info("In ajouterEntreprise() : ");
		entrepriseRepoistory.save(entreprise);
		LOGGER.info("Out ajouterEntreprise() without errors.");
		return entreprise;
		
	}

	public Departement ajouterDepartement(Departement dep) {
		deptRepoistory.save(dep);
		return dep;
	}
	@Transactional
	public void affecterDepartementAEntreprise(int depId, int entrepriseId) {
		
				Entreprise entrepriseManagedEntity = entrepriseRepoistory.findById(entrepriseId).orElse(null);
				Departement depManagedEntity = deptRepoistory.findById(depId).orElse(null);
				if(depManagedEntity!=null) {
				depManagedEntity.setEntreprise(entrepriseManagedEntity);
				deptRepoistory.save(depManagedEntity);}
		
	}
	
	public List<String> getAllDepartementsNamesByEntreprise(int entrepriseId) {
		Entreprise entrepriseManagedEntity = entrepriseRepoistory.findById(entrepriseId).orElse(null);
		List<String> depNames = new ArrayList<>();
		if(entrepriseManagedEntity!=null){
		for(Departement dep : entrepriseManagedEntity.getDepartements()){
			depNames.add(dep.getName());
		}}
		
		return depNames;
	}

	@Transactional
	public Boolean  deleteEntrepriseById(int entrepriseId) {
		
	
			Entreprise entrepriseManagedEntity = entrepriseRepoistory.findById(entrepriseId).orElse(null);

			if(entrepriseManagedEntity!=null) {
				

				entrepriseRepoistory.delete(entrepriseManagedEntity);
				
			}
			return true;		
	}

	@Transactional
	public Boolean deleteDepartementById(int depId) {
		Departement depManagedEntity = deptRepoistory.findById(depId).orElse(null);
		if(depManagedEntity!=null) {
			

			deptRepoistory.delete(depManagedEntity);
			
		}

		return true;	
	}


	public Entreprise getEntrepriseById(int entrepriseId) {

		return entrepriseRepoistory.findById(entrepriseId).orElse(null);	
	}

	

}
