package sudoku.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
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
 *         forall int i,j : getGridPlayer().cells[i][j].value() == 0 &&
 *         				    getGridPlayer().cells[i][j].isModifiable()
 *         
 *         forall int i,j : getGridSoluce().cells[i][j].value() == 0 &&
 *         				    getGridSoluce().cells[i][j].isModifiable()
 *    </pre>
 *    
 */
interface ISudoku extends Serializable {
	
	//REQUÊTES
	/**
	 * Retourne la grille de Sudoku du joueur.
	 */
	IGrid getGridPlayer();
	
	/**
	 * Retourne la grille solution du Sudoku.
	 */
	IGrid getGridSoluce();
	
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
	 * Apporte un indice en surbrilant une case (la grille n'est pas modifiée).
	 */
	String help();
	
	//COMMANDES
	/**
	 * Mise à jour des possibilités par méthode triviale de la coordonnée.
	 * Supprime la value de la coordonnée des possibilités sur la région, colonne, ligne de c.
	 *  @pre : <pre>
	 * 		c != null
	 * 		isValidCoord(c)
	 * 		 getGridPlayer().getCell(c).getValue() > 0
	 * </pre>
	 * @post: 
	 * <pre>
	 * 		int n =  getGridPlayer().getCell(c).getValue()
	 * 		forall ICell cell in cells() : getGridPlayer().getUnitCells(c) => !cell.possibilities().[n - 1]
	 * </pre>
	 */
	void updateEasyPossibilities(ICoord c);
	
	/**
	 * Ajout la valeur n dans la cellule de coordonnée c
	 * @pre : <pre>
	 * 		c != null
	 * 		isValidCoord(c)
	 * 		1 <= n <= getGridPlayer().numberPossibility()
	 * 		isModifiableCell(c);
	 * 		getGridPlayer().getCell(c).canTakeValue(n)
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
	 * Ajoute la valeur n dans les possibilités la cellule de coordonnée c.
	 * @pre : <pre>
	 * 		c != null
	 * 		isValidCoord(c)
	 * 		1 <= n <= getGridPlayer().numberPossibility()
	 * 		isModifiableCell(c);
	 * </pre>
	 * @post <pre>
	 * 		getGridPlayer().getCell(c).possibility[n - 1]
	 * </pre>
	 */
	void addPossibility(ICoord c, int n);
	
	/**
	 * Supprime la valeur n dans les possibilités la cellule de c.
	 * @pre : <pre>
	 * 		c != null
	 * 		isValidCoord(c)
	 * 		1 <= n <= getGridPlayer().numberPossibility()
	 * 		isModifiableCell(c);
	 * </pre>
	 * @post <pre>
	 * 		! getGridPlayer().getCell(c).possibility[n - 1]
	 * </pre>
	 */
	void removePossibility(ICoord c, int n);
	
	
	/**
	 * Résous la partie.
	 * @post <pre>
	 * 		getGridPlayer() == getGridSoluce()
	 * </pre>
	 */
	void finish();
	
	/**
	 * Résous pas à pas la grille ajout la valeur dans une case
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
}
