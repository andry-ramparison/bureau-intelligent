package com.bureauintelligent.service;

import com.bureauintelligent.model.ParametresSession;
import com.bureauintelligent.model.TypeNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EcouteurSessionNotifiantTest {

    private static final ParametresSession PARAMS_COURTS = new ParametresSession(1, 1, 5, 4);

    private GestionnaireNotifications notifications;
    private GestionnaireSession session;

    @BeforeEach
    void setUp() {
        notifications = new GestionnaireNotifications();
        session = new GestionnaireSession(new EcouteurSessionNotifiant(notifications));
    }

    @Test
    void finDeTravail_declencheFinSessionPuisDebutPause() {
        session.demarrerSession(PARAMS_COURTS);

        session.ecoulerSecondes(60); // fin du travail (1 minute)

        assertEquals(2, notifications.historique().size());
        assertEquals(TypeNotification.FIN_SESSION_TRAVAIL, notifications.historique().get(0).getType());
        assertEquals(TypeNotification.DEBUT_PAUSE, notifications.historique().get(1).getType());
    }

    @Test
    void finDePause_declencheNotificationFinPause() {
        session.demarrerSession(PARAMS_COURTS);
        session.ecoulerSecondes(60); // fin travail -> pause

        session.ecoulerSecondes(60); // fin pause (1 minute)

        assertEquals(3, notifications.historique().size());
        assertEquals(TypeNotification.FIN_PAUSE, notifications.historique().get(2).getType());
    }

    @Test
    void aucuneNotification_tantQueLaPhaseNestPasTerminee() {
        session.demarrerSession(PARAMS_COURTS);

        session.ecoulerSecondes(30); // moitié de la minute de travail

        assertEquals(0, notifications.historique().size());
    }
}
