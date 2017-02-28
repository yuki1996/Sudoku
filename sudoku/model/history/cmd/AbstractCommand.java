package sudoku.model.history.cmd;

import sudoku.model.IGrid;
import util.Contract;

/**
 * Classe fournissant un mécanisme général pour les commandes.
 */
public abstract class AbstractCommand implements Command {

    // ATTRIBUTS
    
    private State state;
    
    private final IGrid grid;

    // CONSTRUCTEURS

    /**
     * @pre <pre>
     *     grid != null </pre>
     * @post <pre>
     *     getGrid() == grid
     *     getState() == State.DO </pre>
     */
    protected AbstractCommand(IGrid grid) {
        Contract.checkCondition(grid != null, "la grille donné est null");

        this.grid = grid;
        state = State.DO;
    }

    // REQUETES

	@Override
    public boolean canDo() {
        return state == State.DO;
    }

	@Override
    public boolean canUndo() {
        return state == State.UNDO;
    }

	@Override
    public IGrid getGrid() {
        return grid;
    }

	@Override
    public State getState() {
        return state;
    }

    // COMMANDES

	@Override
    public void act() {
        Contract.checkCondition(canDo() || canUndo());

        if (canDo()) {
            doIt();
            state = State.UNDO;
        } else { // nécessairement canUndo() == true
            undoIt();
            state = State.DO;
        }
    }
    
    /**
     * Cette méthode doit être redéfinie dans les sous-classes, de sorte
     *  qu'elle implante l'action à réaliser pour exécuter la commande.
     * Elle est appelée par act() et ne doit pas être appelée directement.
     * @pre
     *     canDo()
     * @post
     *     La commande a été exécutée
     */
    protected abstract void doIt();
    
    /**
     * Cette méthode doit être redéfinie dans les sous-classes, de sorte
     *  qu'elle implante l'action à réaliser pour annuler la commande.
     * Si l'état du texte correspond à celui dans lequel il était après doIt,
     *  alors undoIt rétablit le texte dans l'état où il était avant
     *  l'exécution de doIt.
     * Elle est appelée par act() et ne doit pas être appelée directement.
     * @pre
     *     canUndo()
     * @post
     *     La commande a été annulée
     */
    protected abstract void undoIt();
}
