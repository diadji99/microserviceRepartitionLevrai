package com.gestionEnseignement.repartition.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="repartition", url = "http://localhost:8088")
public interface EnseignantClient {
    @GetMapping("/enseignants/{id}")
    Enseignant getEnseignant(@PathVariable Long id);

    @GetMapping("/enseignants")
    List<Enseignant> getEnseignant();

    @GetMapping("/{id}/prenom")
    String getPrenom(@PathVariable Long id);

    @GetMapping("/{id}/nom")
    String getNom(@PathVariable Long id);
}
