package com.bureauintelligent.model;

/**
 * État et logique d'une session de travail (chronomètre + cycle Pomodoro).
 *
 * <p>Cette classe ne connaît pas le temps réel : elle est avancée
 * explicitement via {@link #avancer(long)}, appelée soit avec de petits
 * pas (1 seconde, depuis un minuteur JavaFX dans l'UI) soit avec de
 * grands pas (pour les tests ou une démo console instantanée).</p>
 */
public class SessionTravail {

    private final ParametresSession parametres;
    private PhaseSession phase;
    private long secondesRestantes;
    private boolean enCours;
    private int cyclesTravailCompletes;
    private long secondesTravailleesTotal;
    private long secondesPauseTotal;

    public SessionTravail(ParametresSession parametres) {
        this.parametres = parametres;
        this.phase = PhaseSession.TRAVAIL;
        this.secondesRestantes = parametres.dureeTravailSecondes();
        this.enCours = false;
        this.cyclesTravailCompletes = 0;
    }

    /** Démarre (ou reprend) le décompte de la phase courante. */
    public void demarrer() {
        this.enCours = true;
    }

    /** Met en pause le décompte sans perdre la progression (ex. l'utilisateur suspend la session). */
    public void suspendre() {
        this.enCours = false;
    }

    /**
     * Arrête complètement la session et la réinitialise sur une phase de travail neuve.
     * Utilisé par le bouton "Arrêt" (par opposition à une simple pause).
     */
    public void arreter() {
        this.enCours = false;
        this.phase = PhaseSession.TRAVAIL;
        this.secondesRestantes = parametres.dureeTravailSecondes();
    }

    /**
     * Fait avancer le chronomètre de {@code secondes} secondes si la session est en cours.
     * Gère automatiquement la bascule travail → pause → travail ("pause automatique").
     *
     * @return l'événement de transition produit (peut être {@link EvenementSession#AUCUN}).
     */
    public EvenementSession avancer(long secondes) {
        if (!enCours || secondes <= 0) {
            return EvenementSession.AUCUN;
        }

        if (phase == PhaseSession.TRAVAIL) {
            secondesTravailleesTotal += Math.min(secondes, secondesRestantes);
        } else {
            secondesPauseTotal += Math.min(secondes, secondesRestantes);
        }

        secondesRestantes -= secondes;
        if (secondesRestantes > 0) {
            return EvenementSession.AUCUN;
        }

        secondesRestantes = 0;
        return terminerPhaseCourante();
    }

    private EvenementSession terminerPhaseCourante() {
        if (phase == PhaseSession.TRAVAIL) {
            cyclesTravailCompletes++;
            boolean pauseLongue = cyclesTravailCompletes % parametres.getCyclesAvantPauseLongue() == 0;
            phase = pauseLongue ? PhaseSession.PAUSE_LONGUE : PhaseSession.PAUSE;
            secondesRestantes = pauseLongue ? parametres.dureePauseLongueSecondes() : parametres.dureePauseSecondes();
            return EvenementSession.FIN_TRAVAIL;
        } else {
            phase = PhaseSession.TRAVAIL;
            secondesRestantes = parametres.dureeTravailSecondes();
            return EvenementSession.FIN_PAUSE;
        }
    }

    public ParametresSession getParametres() {
        return parametres;
    }

    public PhaseSession getPhase() {
        return phase;
    }

    public long getSecondesRestantes() {
        return secondesRestantes;
    }

    public boolean estEnCours() {
        return enCours;
    }

    public int getCyclesTravailCompletes() {
        return cyclesTravailCompletes;
    }

    public long getSecondesTravailleesTotal() {
        return secondesTravailleesTotal;
    }

    public long getSecondesPauseTotal() {
        return secondesPauseTotal;
    }

    /** Temps restant formaté "MM:SS", pratique pour l'affichage (LCD, UI). */
    public String getTempsRestantFormate() {
        long minutes = secondesRestantes / 60;
        long secondes = secondesRestantes % 60;
        return "%02d:%02d".formatted(minutes, secondes);
    }

    @Override
    public String toString() {
        return "SessionTravail{phase=%s, restant=%s, enCours=%s, cyclesCompletes=%d}"
                .formatted(phase, getTempsRestantFormate(), enCours, cyclesTravailCompletes);
    }
}
