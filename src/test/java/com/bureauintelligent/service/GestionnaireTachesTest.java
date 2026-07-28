package com.bureauintelligent.service;

import com.bureauintelligent.dao.memory.TacheRepositoryMemoire;
import com.bureauintelligent.exception.EntiteIntrouvableException;
import com.bureauintelligent.model.Priorite;
import com.bureauintelligent.model.StatutTache;
import com.bureauintelligent.model.Tache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionnaireTachesTest {

    private GestionnaireTaches gestionnaire;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireTaches(new TacheRepositoryMemoire());
    }

    @Test
    void creerTache_attribueUnIdEtStatutParDefaut() {
        Tache tache = gestionnaire.creerTache("Titre", "Desc", LocalDate.now().plusDays(1), Priorite.HAUTE);

        assertNotNull(tache.getId());
        assertEquals(StatutTache.A_FAIRE, tache.getStatut());
    }

    @Test
    void creerTache_titreVide_leveException() {
        assertThrows(IllegalArgumentException.class,
                () -> gestionnaire.creerTache("  ", "Desc", LocalDate.now(), Priorite.BASSE));
    }

    @Test
    void modifierTache_metAJourLesChamps() {
        Tache tache = gestionnaire.creerTache("Ancien titre", "Desc", LocalDate.now(), Priorite.BASSE);

        Tache modifiee = gestionnaire.modifierTache(
                tache.getId(), "Nouveau titre", "Nouvelle desc", LocalDate.now().plusDays(2), Priorite.HAUTE);

        assertEquals("Nouveau titre", modifiee.getTitre());
        assertEquals(Priorite.HAUTE, modifiee.getPriorite());
    }

    @Test
    void modifierTache_idInconnu_leveException() {
        assertThrows(EntiteIntrouvableException.class,
                () -> gestionnaire.modifierTache(999L, "x", "x", LocalDate.now(), Priorite.BASSE));
    }

    @Test
    void supprimerTache_laRetireDeLaListe() {
        Tache tache = gestionnaire.creerTache("A supprimer", null, LocalDate.now(), Priorite.MOYENNE);

        gestionnaire.supprimerTache(tache.getId());

        assertTrue(gestionnaire.listerTout().isEmpty());
    }

    @Test
    void listerTacheesTriees_triParPrioriteQuisEcheance() {
        Tache basseUrgente = gestionnaire.creerTache("Basse, urgente", null, LocalDate.now(), Priorite.BASSE);
        Tache hauteLointaine = gestionnaire.creerTache("Haute, lointaine", null, LocalDate.now().plusDays(10), Priorite.HAUTE);
        Tache hauteUrgente = gestionnaire.creerTache("Haute, urgente", null, LocalDate.now(), Priorite.HAUTE);

        List<Tache> triees = gestionnaire.listerTacheesTriees();

        assertEquals(hauteUrgente, triees.get(0));
        assertEquals(hauteLointaine, triees.get(1));
        assertEquals(basseUrgente, triees.get(2));
    }

    @Test
    void tachesEnRetard_detecteLesEcheancesPasseesNonTerminees() {
        Tache enRetard = gestionnaire.creerTache("En retard", null, LocalDate.now().minusDays(1), Priorite.MOYENNE);
        gestionnaire.creerTache("Future", null, LocalDate.now().plusDays(1), Priorite.MOYENNE);
        gestionnaire.changerStatut(enRetard.getId(), StatutTache.EN_COURS);

        List<Tache> enRetardListe = gestionnaire.tachesEnRetard(LocalDate.now());

        assertEquals(1, enRetardListe.size());
        assertEquals(enRetard.getId(), enRetardListe.get(0).getId());
    }

    @Test
    void tachesEnRetard_ignoreLesTachesTerminees() {
        Tache tache = gestionnaire.creerTache("Terminée mais en retard", null, LocalDate.now().minusDays(5), Priorite.MOYENNE);
        gestionnaire.changerStatut(tache.getId(), StatutTache.TERMINEE);

        assertTrue(gestionnaire.tachesEnRetard(LocalDate.now()).isEmpty());
    }
}
