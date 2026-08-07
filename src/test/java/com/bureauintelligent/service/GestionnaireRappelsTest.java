package com.bureauintelligent.service;

import com.bureauintelligent.dao.memory.TacheRepositoryMemoire;
import com.bureauintelligent.model.Priorite;
import com.bureauintelligent.model.StatutTache;
import com.bureauintelligent.model.Tache;
import com.bureauintelligent.model.TypeNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionnaireRappelsTest {

    private GestionnaireTaches taches;
    private GestionnaireNotifications notifications;
    private GestionnaireRappels rappels;

    @BeforeEach
    void setUp() {
        taches = new GestionnaireTaches(new TacheRepositoryMemoire());
        notifications = new GestionnaireNotifications();
        rappels = new GestionnaireRappels(taches, notifications);
    }

    @Test
    void tacheEnRetard_declencheUnRappel() {
        taches.creerTache("En retard", null, LocalDate.now().minusDays(1), Priorite.HAUTE);

        List<Tache> rappelees = rappels.verifierRappels(LocalDate.now());

        assertEquals(1, rappelees.size());
        assertEquals(1, notifications.historique().size());
        assertEquals(TypeNotification.RAPPEL_TACHE, notifications.historique().get(0).getType());
    }

    @Test
    void tacheEcheantAujourdHui_declencheUnRappel() {
        taches.creerTache("Aujourd'hui", null, LocalDate.now(), Priorite.MOYENNE);

        assertEquals(1, rappels.verifierRappels(LocalDate.now()).size());
    }

    @Test
    void tacheFuture_neDeclenchePasDeRappel() {
        taches.creerTache("Future", null, LocalDate.now().plusDays(3), Priorite.BASSE);

        assertTrue(rappels.verifierRappels(LocalDate.now()).isEmpty());
    }

    @Test
    void tacheTerminee_neDeclenchePasDeRappelMemeEnRetard() {
        Tache tache = taches.creerTache("Terminée", null, LocalDate.now().minusDays(5), Priorite.MOYENNE);
        taches.changerStatut(tache.getId(), StatutTache.TERMINEE);

        assertTrue(rappels.verifierRappels(LocalDate.now()).isEmpty());
    }

    @Test
    void memeTache_nEstRappeleeQuUneSeuleFois() {
        taches.creerTache("En retard", null, LocalDate.now().minusDays(1), Priorite.HAUTE);

        rappels.verifierRappels(LocalDate.now());
        List<Tache> deuxiemeAppel = rappels.verifierRappels(LocalDate.now());

        assertTrue(deuxiemeAppel.isEmpty());
        assertEquals(1, notifications.historique().size());
    }
}
