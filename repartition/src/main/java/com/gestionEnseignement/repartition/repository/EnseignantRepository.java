package com.gestionEnseignement.repartition.repository;

import com.gestionEnseignement.repartition.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EnseignantRepository extends JpaRepository<Enseignant,Long> {}