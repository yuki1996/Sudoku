package sudoku.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import sudoku.model.history.cmd.Command;
import sudoku.util.ICoord;

/**
 * @inv <pre>
 * 		isWin() <==> getGridPlayer().isFull() && check().isEmpty()
 * 		isModifiableCell(c) <==> getGridPlayer().getCell(c).isModifiable()
 * 		 isValidCoord(coord) <==> 0 <= coord.getCol() < getGridSoluce().size()
 *				&& 0 <= coord.getRow() < getGridSoluce().size()
 *		getGridPlayer().getWidth() == getGridSoluce().getWidth()
 *		getGridPlayer().getHeight() == getGridSoluce().getHeight() 
 * </pre>
 * 
 * @cons <pre>
 *     $DESC$ Un gestionnaire de sudoku
 *     
 *     $ARGS$ int width, int height
 *     
 *     $PRE$ 
 *         width > 0  && height > 0
 *         
 *     $POST$ 
 *         getGridPlayer().getWidth() == width
 *         getGridPlayer().getHeight() == height
 *         
 *         forall int i,j : ! getGridPlayer().cells[i][j].hasValue() &&
 *         				    getGridPlayer().cells[i][j].isModifiable()
 *         
 *         forall int i,j : ! getGridSoluce().cells[i][j].hasValue() &&
 *         				    getGridSoluce().cells[i][j].isModifiable()
 *    </pre>
 *    
 */
public interface SudokuModel {
	
	//REQUÊTES
	/**
	 * Retourne la grille du joueur.
	 */
	GridModel getGridPlayer();
	
	/**
	 * Retourne la grille solution.
	 */
	GridModel getGridSoluce();
	
	/**
	 * Retourne si on a gagné.
	 */
	boolean isWin();
	
	/**
	 * Retourne si la cellule de coordonnée coord est modifiable.
	 * 
	 * @pre <pre>
	 * 		coord != null
	 * 		isValidCoord(coord)
	 * </pre>
	 */
	boolean isModifiableCell(ICoord coord);
	
	/**
	 * Retourne la liste des coordonnées des cellules ayant une valeur différente
	 * de la getGridSoluce().
	 */
	List<ICoord> check(); 
	
	/**
	 * Retourne si les composantes de la coordonnée coord sont valides entre 0 et getGrid().size().
	 * @pre : <pre>
	 * 		coord != null
	 * </pre>
	 */
	boolean isValidCoord(ICoord coord);
	
	/**
	 * Apporte un indice.
	 */
	String help();
	
	/**
	 * Retourne vrai si on peut annuler la dernière action, faux sinon.
	 */
	boolean canUndo();
	
	/**
	 * Retourne vrai si on peut remettre la dernière action annulée, faux sinon.
	 */
	boolean canRedo();
	
	//COMMANDES
	
	/**
	 * Ajout la valeur n dans la cellule de coordonnée c
	 * @pre : <pre>
	 * 		c != null
	 * 		isValidCoord(c)
	 * 		1 <= n <= getGridPlayer().numberCandidates()
	 * 		isModifiableCell(c);
	 * 		getGridPlayer().getCell(c).isCandidate(n)
	 * </pre>
	 * @post <pre>
	 * 		getGridPlayer().getCell(c).getValue() == n
	 * </pre>
	 */
	void setValue(ICoord c, int n);
	
	/**
	 * Supprime la valeur de la cellule de coordonnée c.
	 * @pre : <pre>
	 * 		c != null
	 * 		isValidCoord(c)
	 * 		isModifiableCell(c);
	 * </pre>
	 * @post <pre>
	 * 		! getGridPlayer().getCell(c).hasValue()
	 * </pre>
	 */
	void removeValue(ICoord c);
	
	/**
	 * Ajoute la valeur n dans les candidates la cellule de coordonnée c.
	 * @pre : <pre>
	 * 		c != null
	 * 		isValidCoord(c)
	 * 		1 <= n <= getGridPlayer().numberCandidates()
	 * 		isModifiableCell(c);
	 * </pre>
	 * @post <pre>
	 * 		getGridPlayer().getCell(c).isCandidate(n)
	 * </pre>
	 */
	void addCandidate(ICoord c, int n);
	
	/**
	 * Supprime la valeur n dans les candidates la cellule de c.
	 * @pre : <pre>
	 * 		c != null
	 * 		isValidCoord(c)
	 * 		1 <= n <= getGridPlayer().numberCandidates()
	 * 		isModifiableCell(c);
	 * </pre>
	 * @post <pre>
	 * 		! getGridPlayer().getCell(c).isCandidate(n)
	 * </pre>
	 */
	void removeCandidate(ICoord c, int n);
	
	
	/**
	 * Résous la partie.
	 * @post <pre>
	 * 		getGridPlayer() == getGridSoluce()
	 * </pre>
	 */
	void finish();
	
	/**
	 * Résous pas à pas la grille joueur
	 */
	void resolve();
	
	/**
	 * Enregistre la grille.
	 * @pre: <pre>
	 * 		name != null && !name.equals("")
	 * </pre> 
	 */
	void save(String name) throws IOException;
	
	/**
	 * Charge une grille.
	 * @pre : <pre>
	 * 		fichier != null
	 * </pre>
	 */
	void load(File fichier) throws IOException, ClassNotFoundException;
	
	
	/**
	 * Réinitialise la grille Player.
	 * @post <pre>
	 * 		forall ICell cell in getPlayerGrid().cells():
	 * 			cell.isModifiable() ==> ! cell.hasValue()
	 * </pre>
	 */
	void reset();

	/**
	 * Réalise la commande cmd et l'ajoute à l'historique.
	 * @pre <pre>
	 * 		cmd != null
	 * </pre>
	 */
	void act(Command cmd);
	
	/**
	 * Annule la dernière action.
	 * @pre <pre>
	 *      canUndo()
	 * </pre>
	 */
	void undo();
	
	/**
	 * Remet la dernière action annulée.
	 * @pre <pre>
	 *      canRedo()
	 * </pre>
	 */
	void redo();
	
}
