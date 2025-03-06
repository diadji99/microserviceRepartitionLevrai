package com.gestionEnseignement.repartition.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Enseignement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String nom;
    String maquettee;
    @ManyToMany(mappedBy = "enseignements",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Enseignant> enseignants = new ArrayList<>();
}
