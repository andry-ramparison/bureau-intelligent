package com.bureauintelligent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test minimal de la branche {@code chore/setup}.
 * Vérifie simplement que l'environnement de test (JUnit 5) est opérationnel.
 */
class SetupTest {

    @Test
    void environnementDeTestFonctionnel() {
        assertTrue(true, "L'environnement de test JUnit 5 est correctement configuré.");
    }
}
