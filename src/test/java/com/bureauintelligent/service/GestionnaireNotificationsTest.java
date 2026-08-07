package com.bureauintelligent.service;

import com.bureauintelligent.model.NiveauAlerte;
import com.bureauintelligent.model.Notification;
import com.bureauintelligent.model.TypeNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionnaireNotificationsTest {

    private static class CanalEnregistreur implements CanalNotification {
        final List<Notification> recues = new ArrayList<>();

        @Override
        public void diffuser(Notification notification) {
            recues.add(notification);
        }
    }

    private GestionnaireNotifications gestionnaire;
    private CanalEnregistreur canal;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireNotifications();
        canal = new CanalEnregistreur();
        gestionnaire.ajouterCanal(canal);
    }

    @Test
    void notifierAlerteVisuelle_diffuseSurTousLesCanaux() {
        CanalEnregistreur deuxiemeCanal = new CanalEnregistreur();
        gestionnaire.ajouterCanal(deuxiemeCanal);

        gestionnaire.notifierAlerteVisuelle("Titre", "Message", NiveauAlerte.URGENT);

        assertEquals(1, canal.recues.size());
        assertEquals(1, deuxiemeCanal.recues.size());
        assertEquals(TypeNotification.ALERTE_VISUELLE, canal.recues.get(0).getType());
        assertEquals(NiveauAlerte.URGENT, canal.recues.get(0).getNiveau());
    }

    @Test
    void notifications_sontConserveesDansLHistorique() {
        gestionnaire.notifierAlerteVisuelle("A", "a", NiveauAlerte.INFO);
        gestionnaire.notifierAlerteVisuelle("B", "b", NiveauAlerte.INFO);

        assertEquals(2, gestionnaire.historique().size());
    }

    @Test
    void historique_estUneCopieDefensiveNonModifiable() {
        gestionnaire.notifierAlerteVisuelle("A", "a", NiveauAlerte.INFO);

        assertThrows(UnsupportedOperationException.class, () -> gestionnaire.historique().add(null));
    }
}
