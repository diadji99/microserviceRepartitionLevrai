package com.gestionEnseignement.repartition.controller;

import com.gestionEnseignement.repartition.model.Enseignant;
import com.gestionEnseignement.repartition.model.Enseignement;
import com.gestionEnseignement.repartition.model.Repartition;
import com.gestionEnseignement.repartition.repository.EnseignantRepository;
import com.gestionEnseignement.repartition.repository.EnseignementRepository;
import com.gestionEnseignement.repartition.service.RepartitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/choix")
public class RepartitionController {
    @Autowired
    private RepartitionService repartitionService;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private EnseignementRepository enseignementRepository;

    // lister tous les choix
    @GetMapping("")
    public ResponseEntity<List<Repartition>> findAll() {
        return ResponseEntity.ok(repartitionService.findAll());
    }

    // Pour rechercher un choix par id
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Repartition repartition = repartitionService.findById(id);
        if (repartition == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Le choix avec cet id("+id+") n'existe pas");
        } else {
            return ResponseEntity.ok(repartition);
        }
    }

    // Pour le bouton valider le choix
    @PutMapping("/{id}/valider")
    public ResponseEntity<?> valider(@PathVariable Long id) {
        Repartition repartition = repartitionService.valider(id);
        if (repartition == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("vous ne pouvez pas valider le choix car il est null");
        }
        return ResponseEntity.ok(repartition);
    }

    // pour un bouton pour modifier un choix
    @PutMapping("/{id}/modifier")
    public ResponseEntity<?> modifierChoix(
            @PathVariable Long id,
            @RequestBody Repartition repartitionUpdate) {
        Repartition repartitionExisting = repartitionService.findByEnseignantAndEnseignementAndType(
                repartitionUpdate.getEnseignant(),
                repartitionUpdate.getEnseignement(),
                repartitionUpdate.getType()
        );

        // Si la repartition existe déjà, renvoyer un message avec un code HTTP 409 (Conflict)
        if(repartitionExisting != null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("L'enseignant a déjà fait le choix");  // Message d'erreur
        }

        Repartition repartition = repartitionService.findById(id);
        if (repartition != null) {
            if (repartition.getType() != null && !repartition.getType().equals(repartitionUpdate.getType())) {
                repartition.setType(repartitionUpdate.getType());
            }
            repartition.setDateChoix(LocalDate.now());
            if ((repartition.getEnseignant() != repartitionUpdate.getEnseignant()) && (repartitionUpdate.getEnseignant() != null)) {
                repartition.setEnseignant(repartitionUpdate.getEnseignant());
            }
            if ((repartition.getEnseignement() != repartitionUpdate.getEnseignement()) && (repartitionUpdate.getEnseignement() != null)) {
                repartition.setEnseignement(repartitionUpdate.getEnseignement());
            }
            repartition.setValide(repartitionUpdate.isValide());
            Repartition repartition1 = repartitionService.update(repartition);
            return ResponseEntity.ok(repartition1);
        }
        return ResponseEntity.notFound().build();
    }

    // Pour le bouton supprimer un choix
    @DeleteMapping("/{id}/supprimer")
    public ResponseEntity<?> supprimer(@PathVariable Long id){
        Repartition repartition = repartitionService.findById(id);
        if(repartition != null){
            repartitionService.delete(repartition);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Le choix a été supprimé");
        }
        return ResponseEntity.notFound().build();
    }

    // Pour le bouton faire choix sur un enseignement
    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouter(@RequestBody Repartition repartition){

        // Vérifier si une repartition avec les mêmes enseignant, enseignement et type existe déjà
        Repartition repartitionExisting = repartitionService.findByEnseignantAndEnseignementAndType(
                repartition.getEnseignant(),
                repartition.getEnseignement(),
                repartition.getType()
        );

        // Si la repartition existe déjà, renvoyer un message avec un code HTTP 409 (Conflict)
        if(repartitionExisting != null){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("L'enseignant a déjà fait le choix");  // Message d'erreur
        }

        // Si la repartition n'existe pas, on définit la date du choix et on crée la repartition
        repartition.setDateChoix(LocalDate.now());
        Repartition repartition1 = repartitionService.create(repartition);

        // Retourner la repartition créée avec un code HTTP 200 (OK)
        return ResponseEntity.ok(repartition1);
    }

    // --- Nouveaux endpoints ---

    // 1. Lister les enseignements disponibles pour qu'à partir de la l'enseignant va faire les choix
    // Exemple d'endpoint pour obtenir les enseignements non choisis par un enseignant
    @GetMapping("/enseignements/non-choisis")
    public ResponseEntity<?> getEnseignementsNonChoisis(@RequestParam Long enseignantId) {
        // Récupérer l'enseignant à partir de son identifiant
        Enseignant enseignant = enseignantRepository.findById(enseignantId).orElse(null);
        if (enseignant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Enseignant non trouvé pour id: " + enseignantId);
        }
        // Récupérer les enseignements non choisis
        List<Enseignement> nonChoisis = repartitionService.getEnseignementsNonChoisis(enseignant);
        return ResponseEntity.ok(nonChoisis);
    }

    // Pour Cette methode, ça nous permet de de lister les choix qui ne sont pas valides donc ctd les
    // les enseignements que doit valider le chefDepartement
    // 2. Lister les enseignements non validés (les repartitions non validées)
    @GetMapping("/non-valide")
    public ResponseEntity<List<Repartition>> getEnseignementsNonValide() {
        List<Repartition> nonValides = repartitionService.getEnseignementsNonValide();
        return ResponseEntity.ok(nonValides);
    }

    // 3. Lister les enseignants qui ont fait le même choix sur un enseignement donné
    // et c'est à partir de là que tu va sélectionner l'enseignant pour valider
    // Exemple d'appel : /choix/enseignants/meme-choix?enseignementId=1&type=FIRST_CHOICE
    @GetMapping("/enseignants/meme-choix")
    public ResponseEntity<?> getEnseignantsMemeChoix(@RequestParam Long enseignementId, @RequestParam String type) {
        Enseignement enseignement = enseignementRepository.findById(enseignementId).orElse(null);
        if (enseignement == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Enseignement non trouvé pour id: " + enseignementId);
        }
        List<Enseignant> enseignants = repartitionService.getEnseignantsMemeChoix(enseignement, type);
        return ResponseEntity.ok(enseignants);
    }


    // Nouvel endpoint pour récupérer tous les enseignants
    @GetMapping("/enseignants")
    public ResponseEntity<List<Enseignant>> getAllEnseignants() {
        List<Enseignant> enseignants = enseignantRepository.findAll();
        return ResponseEntity.ok(enseignants);
    }

    // Nouvel endpoint pour récupérer tous les enseignements
    @GetMapping("/enseignements")
    public ResponseEntity<List<Enseignement>> getAllEnseignements() {
        List<Enseignement> enseignements = enseignementRepository.findAll();
        return ResponseEntity.ok(enseignements);
    }

}