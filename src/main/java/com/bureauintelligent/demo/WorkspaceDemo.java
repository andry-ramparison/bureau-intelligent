package com.bureauintelligent.demo;

import com.bureauintelligent.dao.memory.EvenementRepositoryMemoire;
import com.bureauintelligent.dao.memory.TacheRepositoryMemoire;
import com.bureauintelligent.model.Priorite;
import com.bureauintelligent.model.StatutTache;
import com.bureauintelligent.model.Tache;
import com.bureauintelligent.service.GestionnaireCalendrier;
import com.bureauintelligent.service.GestionnaireTaches;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Démonstration en ligne de commande de la branche {@code feature/workspace-management}.
 *
 * <p>Ne dépend ni de JavaFX ni de SQLite : sert à vérifier rapidement,
 * sans interface graphique, que la gestion des tâches et du calendrier
 * fonctionne (création, tri par priorité/échéance, mise à jour, suppression).</p>
 *
 * <p>Exécution : {@code java -cp target/classes com.bureauintelligent.demo.WorkspaceDemo}
 * (après {@code mvn compile}).</p>
 */
public final class WorkspaceDemo {

    private WorkspaceDemo() {
    }

    public static void main(String[] args) {
        GestionnaireTaches taches = new GestionnaireTaches(new TacheRepositoryMemoire());
        GestionnaireCalendrier calendrier = new GestionnaireCalendrier(new EvenementRepositoryMemoire());

        System.out.println("=== Création de tâches ===");
        Tache rapport = taches.creerTache(
                "Rendre le rapport de projet", "Section architecture + tests",
                LocalDate.now().minusDays(1), Priorite.HAUTE);
        taches.creerTache(
                "Réviser pour l'examen", "Chapitres 3 à 5",
                LocalDate.now().plusDays(3), Priorite.HAUTE);
        taches.creerTache(
                "Ranger le bureau", null,
                LocalDate.now().plusDays(7), Priorite.BASSE);
        taches.listerTout().forEach(System.out::println);

        System.out.println("\n=== Tâches triées (priorité puis échéance) ===");
        taches.listerTacheesTriees().forEach(System.out::println);

        System.out.println("\n=== Tâches en retard ===");
        taches.tachesEnRetard(LocalDate.now()).forEach(System.out::println);

        System.out.println("\n=== Mise à jour d'une tâche ===");
        taches.changerStatut(rapport.getId(), StatutTache.TERMINEE);
        System.out.println(taches.obtenirTache(rapport.getId()));

        System.out.println("\n=== Suppression d'une tâche ===");
        taches.supprimerTache(rapport.getId());
        System.out.println("Nombre de tâches restantes : " + taches.listerTout().size());

        System.out.println("\n=== Planification d'événements ===");
        LocalDateTime debut = LocalDate.now().atTime(14, 0);
        calendrier.planifier("Session de travail — Rapport", "Pomodoro 50/10", debut, debut.plusHours(2));
        calendrier.planifier("Réunion d'équipe", null, debut.plusDays(1), debut.plusDays(1).plusMinutes(30));
        calendrier.tousLesEvenements().forEach(System.out::println);

        System.out.println("\n=== Événements du jour ===");
        calendrier.evenementsDuJour(LocalDate.now()).forEach(System.out::println);
    }
}
