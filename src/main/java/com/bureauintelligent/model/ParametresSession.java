package com.bureauintelligent.model;

/**
 * Paramètres de durée d'une session de travail (technique Pomodoro).
 *
 * <p>Valeurs par défaut alignées sur le README : 50 minutes de travail
 * suivies de 10 minutes de pause. Une pause longue est proposée toutes
 * les {@code cyclesAvantPauseLongue} périodes de travail complétées.</p>
 */
public class ParametresSession {

    private final int dureeTravailMinutes;
    private final int dureePauseMinutes;
    private final int dureePauseLongueMinutes;
    private final int cyclesAvantPauseLongue;

    public ParametresSession(int dureeTravailMinutes, int dureePauseMinutes,
                              int dureePauseLongueMinutes, int cyclesAvantPauseLongue) {
        this.dureeTravailMinutes = exigerPositif(dureeTravailMinutes, "dureeTravailMinutes");
        this.dureePauseMinutes = exigerPositif(dureePauseMinutes, "dureePauseMinutes");
        this.dureePauseLongueMinutes = exigerPositif(dureePauseLongueMinutes, "dureePauseLongueMinutes");
        this.cyclesAvantPauseLongue = exigerPositif(cyclesAvantPauseLongue, "cyclesAvantPauseLongue");
    }

    private static int exigerPositif(int valeur, String nomChamp) {
        if (valeur <= 0) {
            throw new IllegalArgumentException(nomChamp + " doit être strictement positif.");
        }
        return valeur;
    }

    /** Paramètres par défaut : 50 min de travail / 10 min de pause / 30 min de pause longue toutes les 4 sessions. */
    public static ParametresSession parDefaut() {
        return new ParametresSession(50, 10, 30, 4);
    }

    public int getDureeTravailMinutes() {
        return dureeTravailMinutes;
    }

    public int getDureePauseMinutes() {
        return dureePauseMinutes;
    }

    public int getDureePauseLongueMinutes() {
        return dureePauseLongueMinutes;
    }

    public int getCyclesAvantPauseLongue() {
        return cyclesAvantPauseLongue;
    }

    public long dureeTravailSecondes() {
        return dureeTravailMinutes * 60L;
    }

    public long dureePauseSecondes() {
        return dureePauseMinutes * 60L;
    }

    public long dureePauseLongueSecondes() {
        return dureePauseLongueMinutes * 60L;
    }

    @Override
    public String toString() {
        return "ParametresSession{travail=%dmin, pause=%dmin, pauseLongue=%dmin, cyclesAvantPauseLongue=%d}"
                .formatted(dureeTravailMinutes, dureePauseMinutes, dureePauseLongueMinutes, cyclesAvantPauseLongue);
    }
}
