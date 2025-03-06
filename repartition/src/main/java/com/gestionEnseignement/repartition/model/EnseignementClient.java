package com.gestionEnseignement.repartition.model;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="repartition", url = "http://localhost:8088")
public interface EnseignementClient {
    @GetMapping("/enseignements/{id}")
    Enseignement getEnseignement(@PathVariable Long id);

    @GetMapping("/enseignements")
    List<Enseignement> getAllEnseignements();
    @GetMapping("enseignaments/{id}/nom")
    String getNom(@PathVariable Long id);
}
