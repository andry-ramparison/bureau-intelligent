package com.bureauintelligent.exception;

/**
 * Levée lorsqu'une opération référence une entité (tâche, événement, ...)
 * qui n'existe pas dans le repository.
 */
public class EntiteIntrouvableException extends RuntimeException {

    public EntiteIntrouvableException(String message) {
        super(message);
    }
}
