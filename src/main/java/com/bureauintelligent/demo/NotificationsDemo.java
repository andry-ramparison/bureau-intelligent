package com.bureauintelligent.demo;

import com.bureauintelligent.dao.memory.TacheRepositoryMemoire;
import com.bureauintelligent.model.NiveauAlerte;
import com.bureauintelligent.model.ParametresSession;
import com.bureauintelligent.model.Priorite;
import com.bureauintelligent.service.EcouteurSessionNotifiant;
import com.bureauintelligent.service.GestionnaireNotifications;
import com.bureauintelligent.service.GestionnaireRappels;
import com.bureauintelligent.service.GestionnaireSession;
import com.bureauintelligent.service.GestionnaireTaches;
import com.bureauintelligent.service.canal.CanalConsole;

import java.time.LocalDate;

/**
 * Démonstration en ligne de commande de la branche {@code feature/notifications}.
 *
 * <p>Montre les trois sources de notification prévues par le plan :
 * rappels de tâches, fin de session / début de pause, fin de pause,
 * ainsi qu'une alerte visuelle générique (celle que réutilisera plus
 * tard {@code feature/smart-monitoring} pour la posture/l'absence).</p>
 *
 * <p>Exécution : {@code java -cp target/classes com.bureauintelligent.demo.NotificationsDemo}
 * (après {@code mvn compile}).</p>
 */
public final class NotificationsDemo {

    private NotificationsDemo() {
    }

    public static void main(String[] args) {
        GestionnaireNotifications notifications = new GestionnaireNotifications();
        notifications.ajouterCanal(new CanalConsole());

        System.out.println("=== Rappels de tâches ===");
        GestionnaireTaches taches = new GestionnaireTaches(new TacheRepositoryMemoire());
        taches.creerTache("Rendre le rapport", "En retard !", LocalDate.now().minusDays(2), Priorite.HAUTE);
        taches.creerTache("Réviser l'examen", "Échéance aujourd'hui", LocalDate.now(), Priorite.HAUTE);
        taches.creerTache("Tâche future", "Pas encore urgente", LocalDate.now().plusDays(5), Priorite.BASSE);

        GestionnaireRappels rappels = new GestionnaireRappels(taches, notifications);
        rappels.verifierRappels(LocalDate.now());

        System.out.println("\n--- Deuxième vérification (même jour) : aucun doublon ---");
        rappels.verifierRappels(LocalDate.now());

        System.out.println("\n=== Session de travail : fin de session -> début pause -> fin de pause ===");
        GestionnaireSession session = new GestionnaireSession(new EcouteurSessionNotifiant(notifications));
        session.demarrerSession(new ParametresSession(1, 1, 5, 4)); // 1 min travail / 1 min pause, pour la démo
        session.ecoulerSecondes(60); // fin du travail -> "Fin de session" + "Début pause"
        session.ecoulerSecondes(60); // fin de la pause -> "Fin de la pause"

        System.out.println("\n=== Alerte visuelle générique (réutilisée plus tard pour posture/absence) ===");
        notifications.notifierAlerteVisuelle(
                "Exemple d'alerte visuelle", "Ceci illustre le canal générique.", NiveauAlerte.ATTENTION);

        System.out.println("\n=== Historique complet (" + notifications.historique().size() + " notifications) ===");
        notifications.historique().forEach(System.out::println);
    }
}
