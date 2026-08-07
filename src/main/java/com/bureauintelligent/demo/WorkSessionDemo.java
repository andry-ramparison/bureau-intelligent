package com.bureauintelligent.demo;

import com.bureauintelligent.model.ParametresSession;
import com.bureauintelligent.model.SessionTravail;
import com.bureauintelligent.service.EcouteurSession;
import com.bureauintelligent.service.GestionnaireSession;

/**
 * Démonstration en ligne de commande de la branche {@code feature/work-session}.
 *
 * <p>Simule un cycle Pomodoro complet en avançant le chronomètre par grands
 * pas (pas d'attente réelle), pour vérifier la pause automatique et le
 * comptage des cycles sans passer 50 minutes devant le terminal.</p>
 *
 * <p>Exécution : {@code java -cp target/classes com.bureauintelligent.demo.WorkSessionDemo}
 * (après {@code mvn compile}).</p>
 */
public final class WorkSessionDemo {

    private WorkSessionDemo() {
    }

    public static void main(String[] args) {
        EcouteurSession ecouteurConsole = new EcouteurSession() {
            @Override
            public void surDemarrage(SessionTravail session) {
                System.out.println(">> Démarrage : " + session);
            }

            @Override
            public void surFinDeTravail(SessionTravail session) {
                System.out.println(">> Fin de la période de travail, pause automatique : " + session);
            }

            @Override
            public void surFinDePause(SessionTravail session) {
                System.out.println(">> Fin de la pause, le travail reprend : " + session);
            }

            @Override
            public void surArret(SessionTravail session) {
                System.out.println(">> Session arrêtée : " + session);
            }
        };

        // Paramètres raccourcis pour que la démo tienne en quelques lignes :
        // 3 minutes de travail / 1 minute de pause / 2 minutes de pause longue toutes les 2 cycles.
        ParametresSession parametres = new ParametresSession(3, 1, 2, 2);
        GestionnaireSession gestionnaire = new GestionnaireSession(ecouteurConsole);

        gestionnaire.demarrerSession(parametres);
        System.out.println("Temps restant : " + gestionnaire.getSession().getTempsRestantFormate());

        System.out.println("\n--- On avance de 90 secondes ---");
        gestionnaire.ecoulerSecondes(90);
        System.out.println("Temps restant : " + gestionnaire.getSession().getTempsRestantFormate());

        System.out.println("\n--- On avance de 90 secondes (fin du 1er cycle de travail) ---");
        gestionnaire.ecoulerSecondes(90);

        System.out.println("\n--- On avance de 60 secondes (fin de la pause courte) ---");
        gestionnaire.ecoulerSecondes(60);

        System.out.println("\n--- On avance de 180 secondes (fin du 2e cycle -> pause LONGUE) ---");
        gestionnaire.ecoulerSecondes(180);

        System.out.println("\n--- On suspend puis on arrête la session ---");
        gestionnaire.suspendre();
        System.out.println("En cours ? " + gestionnaire.getSession().estEnCours());
        gestionnaire.arreter();

        System.out.println("\nCycles de travail complétés : " + gestionnaire.getSession().getCyclesTravailCompletes());
        System.out.println("Secondes travaillées au total : " + gestionnaire.getSession().getSecondesTravailleesTotal());
        System.out.println("Secondes de pause au total : " + gestionnaire.getSession().getSecondesPauseTotal());
    }
}
