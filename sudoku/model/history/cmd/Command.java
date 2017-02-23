package sudoku.model.history.cmd;

import serie03.Text;

/**
 * Une commande est un objet capable de modifier un texte selon certains 
 *  critères.
 * La sémantique de la commande ne pourra être complète que dans les classes
 *  qui implémenteront cette interface.
 * @inv <pre>
 *     getState() != null
 *     getText() != null
 *     canDo() ==> getState() == State.DO
 *     canUndo() ==> getState() == State.UNDO </pre>
 */
public interface Command {
    
    // REQUETES
    
    /**
     * Le texte sur lequel la commande agit.
     */
    Text getText();
    
    /**
     * L'état interne de la commande.
     */
    State getState();
    
    /**
     * Indique que la commande et son environnement sont dans un état
     *  permettant de faire la commande.
     */
    boolean canDo();
    
    /**
     * Indique que la commande et son environnement sont dans un état
     *  permettant de défaire la commande.
     */
    boolean canUndo();

    // COMMANDES
    
    /**
     * Définit l'action qu'effectue la commande sur le texte associé.
     * @pre <pre>
     *     canDo() || canUndo() </pre>
     * @post <pre>
     *     getState() != old getState()
     *     old canDo()
     *         ==> la commande a fait son action sur son texte
     *     old canUndo()
     *         ==> la commande a défait son action sur son texte </pre>
     */
    void act();
}
