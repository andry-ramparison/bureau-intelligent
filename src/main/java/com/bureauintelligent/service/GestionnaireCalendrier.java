package com.bureauintelligent.service;

import com.bureauintelligent.dao.EvenementRepository;
import com.bureauintelligent.exception.EntiteIntrouvableException;
import com.bureauintelligent.model.EvenementCalendrier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Logique métier de gestion du calendrier : planification d'événements
 * et consultation par jour/semaine.
 */
public class GestionnaireCalendrier {

    private final EvenementRepository repository;

    public GestionnaireCalendrier(EvenementRepository repository) {
        this.repository = repository;
    }

    public EvenementCalendrier planifier(String titre, String description, LocalDateTime debut, LocalDateTime fin) {
        return repository.creer(new EvenementCalendrier(titre, description, debut, fin));
    }

    public EvenementCalendrier obtenir(Long id) {
        return repository.trouverParId(id)
                .orElseThrow(() -> new EntiteIntrouvableException("Aucun événement avec l'id " + id));
    }

    public EvenementCalendrier modifier(Long id, String titre, String description, LocalDateTime debut, LocalDateTime fin) {
        EvenementCalendrier evenement = obtenir(id);
        evenement.setTitre(titre);
        evenement.setDescription(description);
        evenement.setDebut(debut);
        evenement.setFin(fin);
        return repository.mettreAJour(evenement);
    }

    public void supprimer(Long id) {
        repository.supprimer(id);
    }

    public List<EvenementCalendrier> tousLesEvenements() {
        return repository.trouverTout().stream()
                .sorted(Comparator.comparing(EvenementCalendrier::getDebut))
                .toList();
    }

    public List<EvenementCalendrier> evenementsDuJour(LocalDate jour) {
        LocalDateTime debutJour = jour.atStartOfDay();
        LocalDateTime finJour = jour.atTime(23, 59, 59);
        return repository.trouverEntre(debutJour, finJour).stream()
                .sorted(Comparator.comparing(EvenementCalendrier::getDebut))
                .toList();
    }

    public List<EvenementCalendrier> evenementsDeLaSemaine(LocalDate premierJour) {
        LocalDateTime debutSemaine = premierJour.atStartOfDay();
        LocalDateTime finSemaine = premierJour.plusDays(6).atTime(23, 59, 59);
        return repository.trouverEntre(debutSemaine, finSemaine).stream()
                .sorted(Comparator.comparing(EvenementCalendrier::getDebut))
                .toList();
    }
}
