package com.gestionEnseignement.repartition.repository;

import com.gestionEnseignement.repartition.model.Enseignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EnseignementRepository extends JpaRepository<Enseignement,Long> {
}
