package com.bureauintelligent.service;

import com.bureauintelligent.model.StatutTache;
import com.bureauintelligent.model.Tache;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Vérifie les tâches gérées par {@link GestionnaireTaches} (branche
 * {@code feature/workspace-management}) et déclenche un rappel pour
 * celles qui arrivent à échéance aujourd'hui ou qui sont déjà en retard.
 *
 * <p>Un même identifiant de tâche n'est rappelé qu'une seule fois par
 * instance de ce gestionnaire, pour éviter le spam si
 * {@link #verifierRappels(LocalDate)} est appelée régulièrement
 * (par exemple depuis un minuteur périodique dans l'UI).</p>
 */
public class GestionnaireRappels {

    private final GestionnaireTaches taches;
    private final GestionnaireNotifications notifications;
    private final Set<Long> tachesDejaRappelees = new HashSet<>();

    public GestionnaireRappels(GestionnaireTaches taches, GestionnaireNotifications notifications) {
        this.taches = taches;
        this.notifications = notifications;
    }

    /** Émet un rappel pour chaque tâche non terminée échue aujourd'hui ou en retard, une seule fois. */
    public List<Tache> verifierRappels(LocalDate aujourdHui) {
        List<Tache> aRappeler = taches.listerTout().stream()
                .filter(t -> t.getDateEcheance() != null)
                .filter(t -> !t.getDateEcheance().isAfter(aujourdHui))
                .filter(t -> t.getStatut() != StatutTache.TERMINEE)
                .filter(t -> !tachesDejaRappelees.contains(t.getId()))
                .toList();

        for (Tache tache : aRappeler) {
            notifications.notifierRappelTache(tache);
            tachesDejaRappelees.add(tache.getId());
        }
        return aRappeler;
    }
}
