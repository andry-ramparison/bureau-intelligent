package com.bureauintelligent.service;

import com.bureauintelligent.dao.memory.EvenementRepositoryMemoire;
import com.bureauintelligent.exception.EntiteIntrouvableException;
import com.bureauintelligent.model.EvenementCalendrier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionnaireCalendrierTest {

    private GestionnaireCalendrier gestionnaire;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireCalendrier(new EvenementRepositoryMemoire());
    }

    @Test
    void planifier_finAvantDebut_leveException() {
        LocalDateTime debut = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class,
                () -> gestionnaire.planifier("x", null, debut, debut.minusHours(1)));
    }

    @Test
    void modifier_idInconnu_leveException() {
        LocalDateTime maintenant = LocalDateTime.now();
        assertThrows(EntiteIntrouvableException.class,
                () -> gestionnaire.modifier(999L, "x", null, maintenant, maintenant.plusHours(1)));
    }

    @Test
    void evenementsDuJour_neRetourneQueCeuxDuJourDemande() {
        LocalDate aujourdHui = LocalDate.now();
        LocalDateTime debutAujourdHui = aujourdHui.atTime(9, 0);
        gestionnaire.planifier("Aujourd'hui", null, debutAujourdHui, debutAujourdHui.plusHours(1));

        LocalDateTime debutDemain = aujourdHui.plusDays(1).atTime(9, 0);
        gestionnaire.planifier("Demain", null, debutDemain, debutDemain.plusHours(1));

        List<EvenementCalendrier> evenements = gestionnaire.evenementsDuJour(aujourdHui);

        assertEquals(1, evenements.size());
        assertEquals("Aujourd'hui", evenements.get(0).getTitre());
    }

    @Test
    void evenementsDeLaSemaine_couvreLes7Jours() {
        LocalDate lundi = LocalDate.now();
        gestionnaire.planifier("Début semaine", null, lundi.atTime(8, 0), lundi.atTime(9, 0));
        LocalDateTime finSemaine = lundi.plusDays(6).atTime(20, 0);
        gestionnaire.planifier("Fin semaine", null, finSemaine, finSemaine.plusHours(1));

        LocalDateTime semaineSuivante = lundi.plusDays(8).atTime(8, 0);
        gestionnaire.planifier("Semaine suivante", null, semaineSuivante, semaineSuivante.plusHours(1));

        assertEquals(2, gestionnaire.evenementsDeLaSemaine(lundi).size());
    }

    @Test
    void supprimer_retireLEvenement() {
        EvenementCalendrier evenement = gestionnaire.planifier(
                "À supprimer", null, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        gestionnaire.supprimer(evenement.getId());

        assertTrue(gestionnaire.tousLesEvenements().isEmpty());
    }
}
