package sudoku.model;

import java.util.List;

import sudoku.util.ICoord;

/**
 * @inv <pre>
 * 		isWin() <==> getGrid().isFull() && check().isEmpty()
 * 		isModifiableCell(c) <==> getGrid().getCell(c).isModifiable()
 * </pre>
 */
interface ISudoku {
	
	//REQUÊTES
	/**
	 * Retourne la grille de Sudoku.
	 */
	IGrid getGrid();
	
	/**
	 * Retourne si on a gagné.
	 */
	boolean isWin();
	
	/**
	 * Retourne si la cellule de coordonnée coord est modifiable.
	 * 
	 * @pre <pre>
	 * 		coord != null
	 * 		0 <= coord.col() < getGrid().numberPossibility()
	 * 		0 <= coord.row() < getGrid().numberPossibility()
	 * </pre>
	 */
	boolean isModifiableCell(ICoord coord);
	
	/**
	 * Retourne la liste des coordonnées des cellules ayant une valeur différente
	 * de la solution.
	 */
	List<ICoord> check(); 
	
	//COMMANDES
	/**
	 * Mise à jour des possibilités par méthode triviale.
	 */
	void updateEasyPossibilities();
	
	/**
	 * Ajout la valeur n dans la cellule de coordonnée c
	 * @pre : <pre>
	 * 		c != null
	 * 		0 <= c.getCol() < getGrid().size()
	 * 		0 <= c.getRow() < getGrid().size()
	 * 		1 <= n <= getGrid().numberPossibility()
	 * 		isModifiableCell(c);
	 * 		getGrid().getCell(c).canTakeValue(n)
	 * </pre>
	 * @post <pre>
	 * 		getGrid().getCell(c).getValue() == n
	 * </pre>
	 */
	void setValue(ICoord c, int n);
	
	/**
	 * Supprime la valeur de la cellule de coordonnée c.
	 * @pre : <pre>
	 * 		c != null
	 * 		0 <= c.getCol() < getGrid().getGridsize()
	 * 		0 <= c.getRow() < getGrid().size()
	 * 		isModifiableCell(c);
	 * </pre>
	 * @post <pre>
	 * 		! getGrid().getCell(c).hasValue()
	 * </pre>
	 */
	void removeValue(ICoord c);
	
	/**
	 * Ajoute la valeur n dans les possibilités la cellule de coordonnée c.
	 * @pre : <pre>
	 * 		c != null
	 * 		0 <= c.getCol() < getGrid().size()
	 * 		0 <= c.getRow() < getGrid().size()
	 * 		1 <= n <= getGrid().numberPossibility()
	 * 		isModifiableCell(c);
	 * </pre>
	 * @post <pre>
	 * 		getGrid().getCell(c).possibility[n]
	 * </pre>
	 */
	void addPossibility(ICoord c, int n);
	
	/**
	 * Supprime la valeur n dans les possibilités la cellule de c.
	 * @pre : <pre>
	 * 		c != null
	 * 		0 <= c.getCol() < getGrid().size()
	 * 		0 <= c.getRow() < getGrid().size()
	 * 		1 <= n <= getGrid().numberPossibility()
	 * 		isModifiableCell(c);
	 * </pre>
	 * @post <pre>
	 * 		! getGrid().getCell(c).possibility[n]
	 * </pre>
	 */
	void removePossibility(ICoord c, int n);
	
	/**
	 * Apporte un indice.
	 */
	void help();
	
	/**
	 * Résous la partie.
	 */
	void finish();
	
	/**
	 * Résous pas à pas la grille
	 */
	void resolve();
	
	/**
	 * Enregistre la grille.
	 */
	void save();
	
	/**
	 * Charge une grille.
	 */
	void load();
	
	
	/**
	 * Réinitialise la grille.
	 * @post <pre>
	 * 		forall ICell cell in getGrid().cells():
	 * 			cell.isModifiable() ==> ! cell.hasValue()
	 * </pre>
	 */
	void reset();
}
