package com.bureauintelligent.service;

import com.bureauintelligent.model.SessionTravail;

/**
 * Implémentation de {@link EcouteurSession} (branche {@code feature/work-session})
 * qui traduit chaque transition de session en notification.
 *
 * <p>La fin de la phase de travail déclenche deux notifications distinctes,
 * comme prévu dans le plan : "Fin de session" puis "Début pause".</p>
 */
public class EcouteurSessionNotifiant implements EcouteurSession {

    private final GestionnaireNotifications notifications;

    public EcouteurSessionNotifiant(GestionnaireNotifications notifications) {
        this.notifications = notifications;
    }

    @Override
    public void surFinDeTravail(SessionTravail session) {
        notifications.notifierFinSession(session);
        notifications.notifierDebutPause(session);
    }

    @Override
    public void surFinDePause(SessionTravail session) {
        notifications.notifierFinPause(session);
    }
}
