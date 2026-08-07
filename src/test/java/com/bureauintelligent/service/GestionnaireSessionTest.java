package com.bureauintelligent.service;

import com.bureauintelligent.model.ParametresSession;
import com.bureauintelligent.model.PhaseSession;
import com.bureauintelligent.model.SessionTravail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionnaireSessionTest {

    /** Écouteur de test qui enregistre les événements reçus, dans l'ordre. */
    private static class EcouteurEnregistreur implements EcouteurSession {
        final List<String> evenements = new ArrayList<>();

        @Override
        public void surDemarrage(SessionTravail session) {
            evenements.add("DEMARRAGE");
        }

        @Override
        public void surFinDeTravail(SessionTravail session) {
            evenements.add("FIN_TRAVAIL");
        }

        @Override
        public void surFinDePause(SessionTravail session) {
            evenements.add("FIN_PAUSE");
        }

        @Override
        public void surArret(SessionTravail session) {
            evenements.add("ARRET");
        }
    }

    private EcouteurEnregistreur ecouteur;
    private GestionnaireSession gestionnaire;

    // Paramètres courts pour des tests rapides : 2 min travail / 1 min pause / 5 min pause longue tous les 2 cycles.
    private static final ParametresSession PARAMS_COURTS = new ParametresSession(2, 1, 5, 2);

    @BeforeEach
    void setUp() {
        ecouteur = new EcouteurEnregistreur();
        gestionnaire = new GestionnaireSession(ecouteur);
    }

    @Test
    void demarrerSession_parDefaut_utiliseLesDureesDuReadme() {
        SessionTravail session = gestionnaire.demarrerSession();

        assertEquals(50 * 60L, session.getSecondesRestantes());
        assertEquals(PhaseSession.TRAVAIL, session.getPhase());
        assertTrue(session.estEnCours());
        assertEquals(List.of("DEMARRAGE"), ecouteur.evenements);
    }

    @Test
    void ecoulerSecondes_sansAtteindreLaFin_neDeclenchePasDeTransition() {
        gestionnaire.demarrerSession(PARAMS_COURTS);

        gestionnaire.ecoulerSecondes(30);

        assertEquals(PhaseSession.TRAVAIL, gestionnaire.getSession().getPhase());
        assertEquals(90, gestionnaire.getSession().getSecondesRestantes());
        assertEquals(List.of("DEMARRAGE"), ecouteur.evenements);
    }

    @Test
    void finDeTravail_basculeAutomatiquementEnPause() {
        gestionnaire.demarrerSession(PARAMS_COURTS);

        gestionnaire.ecoulerSecondes(120); // 2 minutes = fin du travail

        assertEquals(PhaseSession.PAUSE, gestionnaire.getSession().getPhase());
        assertEquals(60, gestionnaire.getSession().getSecondesRestantes());
        assertEquals(List.of("DEMARRAGE", "FIN_TRAVAIL"), ecouteur.evenements);
    }

    @Test
    void finDePause_repartEnTravail() {
        gestionnaire.demarrerSession(PARAMS_COURTS);
        gestionnaire.ecoulerSecondes(120); // fin travail -> pause

        gestionnaire.ecoulerSecondes(60); // fin pause -> travail

        assertEquals(PhaseSession.TRAVAIL, gestionnaire.getSession().getPhase());
        assertEquals(120, gestionnaire.getSession().getSecondesRestantes());
        assertEquals(List.of("DEMARRAGE", "FIN_TRAVAIL", "FIN_PAUSE"), ecouteur.evenements);
    }

    @Test
    void apresNCyclesConfigures_laPauseEstLongue() {
        gestionnaire.demarrerSession(PARAMS_COURTS);
        gestionnaire.ecoulerSecondes(120); // cycle 1 -> pause courte
        gestionnaire.ecoulerSecondes(60);  // retour travail
        gestionnaire.ecoulerSecondes(120); // cycle 2 -> pause LONGUE (2 cycles configurés)

        assertEquals(PhaseSession.PAUSE_LONGUE, gestionnaire.getSession().getPhase());
        assertEquals(5 * 60L, gestionnaire.getSession().getSecondesRestantes());
        assertEquals(2, gestionnaire.getSession().getCyclesTravailCompletes());
    }

    @Test
    void suspendre_arreteLeDecompteSansLeReinitialiser() {
        gestionnaire.demarrerSession(PARAMS_COURTS);
        gestionnaire.ecoulerSecondes(30);

        gestionnaire.suspendre();
        gestionnaire.ecoulerSecondes(30); // ne doit rien faire : session suspendue

        assertFalse(gestionnaire.getSession().estEnCours());
        assertEquals(90, gestionnaire.getSession().getSecondesRestantes());
    }

    @Test
    void reprendre_apresSuspension_continueLeDecompte() {
        gestionnaire.demarrerSession(PARAMS_COURTS);
        gestionnaire.ecoulerSecondes(30);
        gestionnaire.suspendre();

        gestionnaire.reprendre();
        gestionnaire.ecoulerSecondes(30);

        assertEquals(60, gestionnaire.getSession().getSecondesRestantes());
    }

    @Test
    void arreter_reinitialiseLaSessionSurUnePhaseDeTravailNeuve() {
        gestionnaire.demarrerSession(PARAMS_COURTS);
        gestionnaire.ecoulerSecondes(120); // en pause

        gestionnaire.arreter();

        assertFalse(gestionnaire.getSession().estEnCours());
        assertEquals(PhaseSession.TRAVAIL, gestionnaire.getSession().getPhase());
        assertEquals(PARAMS_COURTS.dureeTravailSecondes(), gestionnaire.getSession().getSecondesRestantes());
        assertTrue(ecouteur.evenements.contains("ARRET"));
    }

    @Test
    void ecoulerSecondes_sansSessionActive_leveException() {
        assertThrows(IllegalStateException.class, () -> gestionnaire.ecoulerSecondes(10));
    }

    @Test
    void tempsRestantFormate_afficheMinutesEtSecondes() {
        gestionnaire.demarrerSession(PARAMS_COURTS);
        gestionnaire.ecoulerSecondes(65); // 120 - 65 = 55s restantes

        assertEquals("00:55", gestionnaire.getSession().getTempsRestantFormate());
    }
}
