package com.bureauintelligent.service;

import com.bureauintelligent.dao.TacheRepository;
import com.bureauintelligent.exception.EntiteIntrouvableException;
import com.bureauintelligent.model.Priorite;
import com.bureauintelligent.model.StatutTache;
import com.bureauintelligent.model.Tache;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Logique métier de la gestion des tâches : création, modification,
 * suppression, et règles de tri/priorisation.
 */
public class GestionnaireTaches {

    private final TacheRepository repository;

    public GestionnaireTaches(TacheRepository repository) {
        this.repository = repository;
    }

    public Tache creerTache(String titre, String description, LocalDate dateEcheance, Priorite priorite) {
        return repository.creer(new Tache(titre, description, dateEcheance, priorite));
    }

    public Tache obtenirTache(Long id) {
        return repository.trouverParId(id)
                .orElseThrow(() -> new EntiteIntrouvableException("Aucune tâche avec l'id " + id));
    }

    public Tache modifierTache(Long id, String titre, String description, LocalDate dateEcheance, Priorite priorite) {
        Tache tache = obtenirTache(id);
        tache.setTitre(titre);
        tache.setDescription(description);
        tache.setDateEcheance(dateEcheance);
        tache.setPriorite(priorite);
        return repository.mettreAJour(tache);
    }

    public Tache changerStatut(Long id, StatutTache statut) {
        Tache tache = obtenirTache(id);
        tache.setStatut(statut);
        return repository.mettreAJour(tache);
    }

    public void supprimerTache(Long id) {
        repository.supprimer(id);
    }

    public List<Tache> listerTout() {
        return repository.trouverTout();
    }

    /**
     * Liste triée par priorité décroissante (HAUTE d'abord) puis par
     * échéance croissante (les plus urgentes en premier). Les tâches
     * sans échéance passent après celles qui en ont une, à priorité égale.
     */
    public List<Tache> listerTacheesTriees() {
        Comparator<Tache> parPrioriteDecroissante =
                Comparator.comparing(Tache::getPriorite, Comparator.reverseOrder());
        Comparator<Tache> parEcheanceCroissante =
                Comparator.comparing(Tache::getDateEcheance, Comparator.nullsLast(Comparator.naturalOrder()));

        return repository.trouverTout().stream()
                .sorted(parPrioriteDecroissante.thenComparing(parEcheanceCroissante))
                .toList();
    }

    public List<Tache> listerParStatut(StatutTache statut) {
        return repository.trouverParStatut(statut);
    }

    /** Tâches non terminées dont l'échéance est déjà passée. */
    public List<Tache> tachesEnRetard(LocalDate aujourdHui) {
        return repository.trouverTout().stream()
                .filter(t -> t.estEnRetard(aujourdHui))
                .toList();
    }
}
