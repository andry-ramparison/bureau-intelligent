package com.bureauintelligent.service;

import com.bureauintelligent.model.NiveauAlerte;
import com.bureauintelligent.model.Notification;
import com.bureauintelligent.model.SessionTravail;
import com.bureauintelligent.model.Tache;
import com.bureauintelligent.model.TypeNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Point d'entrée central des notifications : construit les
 * {@link Notification} pour chaque situation métier (rappel, fin de
 * session, pause, alerte visuelle) et les diffuse sur tous les
 * {@link CanalNotification} enregistrés.
 *
 * <p>Conserve aussi un historique en mémoire, exploitable telle quelle
 * par {@code feature/history-statistics} ou persistée plus tard par
 * {@code feature/database}.</p>
 */
public class GestionnaireNotifications {

    private final List<CanalNotification> canaux = new CopyOnWriteArrayList<>();
    private final List<Notification> historique = new ArrayList<>();

    public void ajouterCanal(CanalNotification canal) {
        canaux.add(canal);
    }

    public List<Notification> historique() {
        return List.copyOf(historique);
    }

    private void notifier(Notification notification) {
        historique.add(notification);
        for (CanalNotification canal : canaux) {
            canal.diffuser(notification);
        }
    }

    public void notifierRappelTache(Tache tache) {
        String message = tache.getDateEcheance() != null
                ? "Échéance : " + tache.getDateEcheance()
                : "Cette tâche n'a pas d'échéance définie.";
        NiveauAlerte niveau = tache.getDateEcheance() != null
                ? NiveauAlerte.URGENT
                : NiveauAlerte.ATTENTION;
        notifier(new Notification(TypeNotification.RAPPEL_TACHE, "Rappel : " + tache.getTitre(), message, niveau));
    }

    public void notifierFinSession(SessionTravail session) {
        notifier(new Notification(
                TypeNotification.FIN_SESSION_TRAVAIL,
                "Session de travail terminée",
                "Cycle n°" + session.getCyclesTravailCompletes() + " complété.",
                NiveauAlerte.INFO));
    }

    public void notifierDebutPause(SessionTravail session) {
        notifier(new Notification(
                TypeNotification.DEBUT_PAUSE,
                "C'est l'heure de la pause",
                "Durée de la pause : " + session.getTempsRestantFormate(),
                NiveauAlerte.ATTENTION));
    }

    public void notifierFinPause(SessionTravail session) {
        notifier(new Notification(
                TypeNotification.FIN_PAUSE,
                "Fin de la pause",
                "Le travail reprend.",
                NiveauAlerte.ATTENTION));
    }

    public void notifierAlerteVisuelle(String titre, String message, NiveauAlerte niveau) {
        notifier(new Notification(TypeNotification.ALERTE_VISUELLE, titre, message, niveau));
    }
}
