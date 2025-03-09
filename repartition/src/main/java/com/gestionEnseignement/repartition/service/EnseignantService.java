package com.gestionEnseignement.repartition.service;

import com.gestionEnseignement.repartition.model.Enseignant;
import com.gestionEnseignement.repartition.model.EnseignantClient;
import com.gestionEnseignement.repartition.repository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnseignantService {
    @Autowired
    private EnseignantClient enseignantClient;
    @Autowired
    private EnseignantRepository enseignantRepository;

    public Enseignant findById(Long id){
        return enseignantClient.getEnseignant(id);
    }

    public List<Enseignant> getAll(){
        return enseignantClient.getEnseignant();
    }

    public String findPrenomById(Long id){
        return enseignantClient.getPrenom(id);
    }

    public String findByNom(Long id){
        return enseignantClient.getNom(id);
    }

    public void save(Enseignant enseignant){
        enseignantRepository.save(enseignant);
    }
}