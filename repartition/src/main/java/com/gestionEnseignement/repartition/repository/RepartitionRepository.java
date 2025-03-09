package com.gestionEnseignement.repartition.repository;

import com.gestionEnseignement.repartition.model.Enseignant;
import com.gestionEnseignement.repartition.model.Enseignement;
import com.gestionEnseignement.repartition.model.Repartition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource
public interface RepartitionRepository extends JpaRepository<Repartition,Long> {
    List<Repartition> findByEnseignant(Enseignant enseignant);
    Repartition findByEnseignantAndEnseignementAndType(Enseignant enseignant, Enseignement enseignement, String type);
    Repartition findByEnseignement(Enseignement enseignement);
    List<Repartition> findByEnseignementAndType(Enseignement enseignement, String type);
    // Nouvelle méthode pour les repartitions non validées
    List<Repartition> findByValideFalse();
    List<Repartition> findRepartitionByEnseignement(Enseignement enseignement);
}