package com.bureauintelligent;

/**
 * Point d'entrée de l'application Bureau Intelligent.
 * Délègue le lancement à l'application JavaFX (voir {@link App}).
 */
public final class Main {

    private Main() {
        // Classe utilitaire : instanciation interdite.
    }

    public static void main(String[] args) {
        App.main(args);
    }
}
