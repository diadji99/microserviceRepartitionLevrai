package com.gestionEnseignement.repartition.service;

import com.gestionEnseignement.repartition.model.Enseignant;
import com.gestionEnseignement.repartition.model.Enseignement;
import com.gestionEnseignement.repartition.model.Repartition;
import com.gestionEnseignement.repartition.repository.EnseignementRepository;
import com.gestionEnseignement.repartition.repository.RepartitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RepartitionService {
    @Autowired
    private RepartitionRepository repartitionRepository;
    @Autowired
    private EnseignementRepository enseignementRepository;

    public Repartition findByEnseignantAndEnseignementAndType(Enseignant enseignant, Enseignement enseignement, String type){
        return repartitionRepository.findByEnseignantAndEnseignementAndType(enseignant,enseignement,type);
    }

    public List<Repartition> findByEnseignementAndType(Enseignement enseignement, String type){
        return repartitionRepository.findByEnseignementAndType(enseignement,type);
    }

    public Repartition findByEnseignement(Enseignement enseignement){
        return repartitionRepository.findByEnseignement(enseignement);
    }

    public  boolean estChoisi(Enseignement enseignement){
        if(repartitionRepository.findByEnseignement(enseignement) != null){
            return true;
        }else{
            return false;
        }
    }

    public Repartition valider(Long id){
        Repartition repartition = repartitionRepository.findById(id).get();
        if(repartition != null){
            if(repartition.isValide()){
                repartition.setValide(false);
            }else{
                repartition.setValide(true);
            }
            repartitionRepository.save(repartition);
        }
        return repartition;
    }

    public Repartition create(Repartition repartition){
        return repartitionRepository.save(repartition);
    }

    public Repartition update(Repartition repartition){
        Repartition repartition1 = repartitionRepository.findById(repartition.getId()).get();
        if(repartition != null){
            return repartitionRepository.save(repartition);
        }
        return null;
    }

    public void delete (Repartition repartition){
        repartitionRepository.delete(repartition);
    }

    public List<Repartition> findByEnseignant(Enseignant enseignant){
        return repartitionRepository.findByEnseignant(enseignant);
    }

    public Repartition findById(Long id){
        return repartitionRepository.findById(id).get();
    }

    public List<Repartition> findAll(){
        return  repartitionRepository.findAll();
    }

    // --- Nouvelles méthodes ---

    // 1. Lister les enseignements disponibles (ceux pour lesquels aucun choix n'a été effectué)
    public List<Enseignement> getEnseignementsDisponibles() {
        List<Enseignement> allEnseignements = enseignementRepository.findAll();
        return allEnseignements.stream()
                .filter(enseignement -> !estChoisi(enseignement))
                .collect(Collectors.toList());
    }

    // 2. Lister les repartitions (et donc les enseignements) non validées
    public List<Repartition> getEnseignementsNonValide() {
        return repartitionRepository.findByValideFalse();
    }

    // 3. Lister les enseignants qui ont fait le même choix sur un enseignement (selon le type de choix)
    public List<Enseignant> getEnseignantsMemeChoix(Enseignement enseignement, String type) {
        return repartitionRepository.findByEnseignementAndType(enseignement, type)
                .stream()
                .map(Repartition::getEnseignant)
                .collect(Collectors.toList());
    }

    public List<Repartition> getRepartitionsByEnseignement(Enseignement enseignement) {
        return repartitionRepository.findRepartitionByEnseignement(enseignement);
    }

    public List<Enseignement> getEnseignementsNonChoisis(Enseignant enseignant) {
        // Récupérer tous les enseignements
        List<Enseignement> allEnseignements = enseignementRepository.findAll();

        // Récupérer les repartitions associées à l'enseignant
        List<Repartition> repartitionsParEnseignant = repartitionRepository.findByEnseignant(enseignant);

        // Extraire les enseignements déjà choisis
        Set<Enseignement> enseignementsChoisis = repartitionsParEnseignant.stream()
                .map(Repartition::getEnseignement)
                .collect(Collectors.toSet());

        // Filtrer et retourner ceux qui ne sont pas encore choisis
        return allEnseignements.stream()
                .filter(e -> !enseignementsChoisis.contains(e))
                .collect(Collectors.toList());
    }
}