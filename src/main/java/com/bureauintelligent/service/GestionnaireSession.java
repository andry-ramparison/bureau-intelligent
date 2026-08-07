package com.bureauintelligent.service;

import com.bureauintelligent.model.EvenementSession;
import com.bureauintelligent.model.ParametresSession;
import com.bureauintelligent.model.SessionTravail;

/**
 * Orchestre une {@link SessionTravail} : démarrage/arrêt, avancement du
 * chronomètre, et notification d'un {@link EcouteurSession} lors des
 * transitions de phase (pause automatique comprise).
 */
public class GestionnaireSession {

    private final EcouteurSession ecouteur;
    private SessionTravail session;

    public GestionnaireSession(EcouteurSession ecouteur) {
        this.ecouteur = ecouteur == null ? new EcouteurSession() {} : ecouteur;
    }

    /** Démarre une nouvelle session avec les paramètres donnés. */
    public SessionTravail demarrerSession(ParametresSession parametres) {
        this.session = new SessionTravail(parametres);
        this.session.demarrer();
        ecouteur.surDemarrage(session);
        return session;
    }

    /** Démarre une nouvelle session avec les paramètres par défaut (50 min / 10 min). */
    public SessionTravail demarrerSession() {
        return demarrerSession(ParametresSession.parDefaut());
    }

    public void suspendre() {
        exigerSessionActive();
        session.suspendre();
    }

    public void reprendre() {
        exigerSessionActive();
        session.demarrer();
    }

    /** Arrête complètement la session courante (remise à zéro). */
    public void arreter() {
        exigerSessionActive();
        session.arreter();
        ecouteur.surArret(session);
    }

    /**
     * Fait avancer le chronomètre de {@code secondes} secondes et notifie
     * l'écouteur si une transition de phase a eu lieu (pause automatique).
     */
    public void ecoulerSecondes(long secondes) {
        exigerSessionActive();
        EvenementSession evenement = session.avancer(secondes);
        switch (evenement) {
            case FIN_TRAVAIL -> ecouteur.surFinDeTravail(session);
            case FIN_PAUSE -> ecouteur.surFinDePause(session);
            case AUCUN -> { /* rien à notifier */ }
        }
    }

    public SessionTravail getSession() {
        exigerSessionActive();
        return session;
    }

    public boolean aSessionActive() {
        return session != null;
    }

    private void exigerSessionActive() {
        if (session == null) {
            throw new IllegalStateException("Aucune session n'a été démarrée.");
        }
    }
}
